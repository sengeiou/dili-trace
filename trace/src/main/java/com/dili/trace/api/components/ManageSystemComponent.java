package com.dili.trace.api.components;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.common.service.SystemPermissionCheckService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.RSAUtils;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.Market;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.service.FirmRpcService;
import com.dili.trace.service.MarketService;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.rpc.UserRpc;
import com.jayway.jsonpath.*;
import one.util.streamex.StreamEx;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 权限系统访问接口
 */
@Component
public class ManageSystemComponent {
    private static final Logger logger = LoggerFactory.getLogger(ManageSystemComponent.class);
    @Value("${manage.domain}")
    private String manageDomainPath;
    @Value("${uap.publicKey}")
    private String publicKey;
    @Autowired
    private SystemPermissionCheckService systemPermissionCheckService;
    @Autowired(required = false)
    private UserRpc userRpc;
    @Autowired
    private MarketService marketService;
    @Autowired
    FirmRpcService firmRpcService;

    private String hz_admin_authUrl = "user/index.html#list";

    /**
     * 初始化参数
     */
    @PostConstruct
    public void init() {
        if (StringUtils.isBlank(this.manageDomainPath)) {
            throw new RuntimeException("缺少权限系统的配置信息");
        }
        logger.warn(">>>>>>>>>>>>>>请注意配置host,以确保当前应用所在服务器能访问权限系统: {}", this.manageDomainPath);
        StreamEx.of(LoginIdentityTypeEnum.values()).map(LoginIdentityTypeEnum::getAuthUrl)
                .filter(StringUtils::isNotBlank).forEach(auth -> {

            logger.warn(">>>>>>>>>>>>>>请注意配置登录访问权限: {}", auth);

        });
    }

    /**
     * 管理员登录
     *
     * @param username
     * @param password
     * @param identityTypeEnum
     * @return
     */
    public SessionData sysManagerLogin(String username, String password, LoginIdentityTypeEnum identityTypeEnum) {
        String loginUrl = (this.manageDomainPath.trim() + "/authenticationApi/login.api");
        try {
            Map<String, Object> loginMap = new HashMap<String, Object>(2);
            loginMap.put("userName", username);
            loginMap.put("password", password);
            //加密uap登录接口参数
            byte[] encryptByPublic = new byte[0];
            try {
                byte[] publicKeyBytes = Base64.decodeBase64(publicKey);
                encryptByPublic = RSAUtils.encryptByPublicKey(JSON.toJSONString(loginMap).getBytes(), publicKeyBytes);
            } catch (Exception e) {
                logger.error("uap登录接口参数加密错误!}");
                throw new TraceBizException("登录失败：服务错误");
            }

            //调用uap登录接口
            String encryptStr = Base64.encodeBase64String(encryptByPublic);
            String loginRespBody = new HttpRequest(loginUrl)
                    .setMethod(Method.POST)
                    .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .body(encryptStr)
                    .execute()
                    .body();
            // {"code":"200",
            //  "data":{"loginPath":"/","sessionId":"d588eb00-3523-4baa-a741-4b3dfb2266d2","user":{"cellphone":"15088882222","created":"2020-08-13 09:35:56","departmentId":59,"email":"ceshishouguang@diligrp.com","firmCode":"sg","id":85,"locked":"2020-09-27 18:26:16","metadata":{},"modified":"2020-09-27 18:26:16","realName":"测试寿光","serialNumber":"000","state":1,"userName":"test_sg"}
            // },"message":"登录成功","result":"登录成功","success":true}
            logger.info("loginRespBody={}", loginRespBody);

            Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
            ParseContext parseContext = JsonPath.using(conf);
            DocumentContext loginDocumentContext = parseContext.parse(loginRespBody);

            Boolean success = loginDocumentContext.read("$.success");
            if (success == null) {
                throw new TraceBizException("登录失败");
            }

            String msg = loginDocumentContext.read("$.message");
            if (!success) {
                throw new TraceBizException(msg);
            }

            //获取返回数据中的data
            JSONObject uapLoginInfoData = loginDocumentContext.read("$.data", JSONObject.class);
            //获取uap返回数据中的user数据
            JSONObject uapUserInfo = uapLoginInfoData.getJSONObject("user");

            Long userId = Long.parseLong(uapUserInfo.get("id").toString());
            //真实名称
            String realName = (String) uapUserInfo.get("realName");
            //市场编码
            String marketCode = (String) uapUserInfo.get("firmCode");
            Firm firm = this.firmRpcService.getFirmByCode(marketCode)
                    .orElseThrow(() -> new TraceBizException("当前登录用户所属市场不存在"));

            // 查询管理员用户小程序权限
            Set<String> userWeChatMenus = systemPermissionCheckService.getWeChatUserMenus(userId);
            if (!CollectionUtils.isEmpty(userWeChatMenus)) {
                return SessionData.fromUser(new OperatorUser(userId, realName), identityTypeEnum.getCode(), firm, userWeChatMenus);
            } else {
                throw new TraceBizException("权限不足");
            }
        } catch (Exception e) {
            if (!(e instanceof TraceBizException)) {
                logger.error(e.getMessage(), e);
                throw new TraceBizException("登录失败:网络错误");
            } else {
                throw e;
            }
        }
    }


    /**
     * 根据权限查询用户信息
     *
     * @param authUrl
     * @return
     */
    public List<User> findUserByUserResource(String authUrl, Long marketId) {
        try {
            // 从前端获取 firmCode
            Market market = marketService.get(marketId);
            if (market == null || StringUtils.isBlank(market.getCode())) {
                logger.error("市场用户查询失败。市场【"+marketId+"】不存在或者市场编码未维护！");
                return new ArrayList<>();
            }

            //根据权限code获取有这个权限code的用户列表 // SessionContext.getSessionContext().getUserTicket().getFirmCode()
            BaseOutput<List<User>> usersByResourceCodeList = userRpc.findCurrentFirmUsersByResourceCode(market.getCode()
                    , authUrl);

            return usersByResourceCodeList.getData();
        } catch (Exception e) {
            throw (TraceBizException) e;
        }

    }

    /**
     * 查询管理员信息
     * @param marketId
     * @return
     */
    public List<User> getAdminUser(Long marketId) {
        return findUserByUserResource(hz_admin_authUrl, marketId);
    }

    /**
     * 判断是否是管理员
     * @param userId
     * @param marketId
     * @return
     */
    public boolean isAdminUser(Long userId, Long marketId) {
        Map<Long, User> managerMap = new HashMap<>();
        List<User> managers = findUserByUserResource(hz_admin_authUrl, marketId);
        StreamEx.of(managers).nonNull().forEach(m -> {
            managerMap.put(m.getId(), m);
        });
        return managerMap.containsKey(userId);
    }
/*

    public static void main(String[] args) throws Exception {
        ManageSystemComponent c = new ManageSystemComponent();
        c.manageDomainPath = "http://10.28.10.167";
        c.findUserByUserResource("http://127.0.0.1/menu/preSave.do", 2L);
    }
*/

}
