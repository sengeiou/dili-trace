package com.dili.trace.api.components;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.common.exception.TraceBusinessException;
import com.dili.common.service.SystemPermissionCheckService;
import com.dili.ss.util.RSAUtils;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.dto.ManagerInfoDto;
import com.dili.trace.dto.OperatorUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ManageSystemComponent {
    private static final Logger logger = LoggerFactory.getLogger(ManageSystemComponent.class);
    @Value("${manage.domain}")
    private String manageDomainPath;
    @Value("${uap.publicKey}")
    private String publicKey;
    @Autowired
    private SystemPermissionCheckService systemPermissionCheckService;

    private String hz_admin_authUrl = "user/index.html#list";

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
    public OperatorUser sysManagerLogin(String username, String password, LoginIdentityTypeEnum identityTypeEnum) {
        String loginUrl = (this.manageDomainPath.trim() + "/authenticationApi/login.api");
        try {
            Map<String, Object> loginMap = new HashMap<String, Object>();
            loginMap.put("userName", username);
            loginMap.put("password", password);
            //加密uap登录接口参数
            byte[] encryptByPublic = new byte[0];
            try {
                byte[] publicKeyBytes = Base64.decodeBase64(publicKey);
                encryptByPublic = RSAUtils.encryptByPublicKey(JSON.toJSONString(loginMap).getBytes(), publicKeyBytes);
            } catch (Exception e) {
                logger.error("uap登录接口参数加密错误!}");
                throw new TraceBusinessException("登录失败：服务错误");
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
                throw new TraceBusinessException("登录失败");
            }

            String msg = loginDocumentContext.read("$.message");
            if (!success) {
                throw new TraceBusinessException(msg);
            }

            //获取返回数据中的data
            JSONObject uapLoginInfoData = loginDocumentContext.read("$.data", JSONObject.class);
            //获取uap返回数据中的user数据
            JSONObject uapUserInfo = uapLoginInfoData.getJSONObject("user");

            Long userId = Long.parseLong(uapUserInfo.get("id").toString());
            //真实名称
            String realName = (String) uapUserInfo.get("realName");


            // 调用uap权限认证判断用户有没有主页权限
            boolean checkUrlResult = systemPermissionCheckService.checkUrl(userId, identityTypeEnum.getAuthUrl());
            if (checkUrlResult) {
                return new OperatorUser(userId, realName);
            } else {
                throw new TraceBusinessException("权限不足");
            }

        } catch (Exception e) {
            if (!(e instanceof TraceBusinessException)) {
                logger.error(e.getMessage(), e);
                throw new TraceBusinessException("登录失败:网络错误");
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
    public List<ManagerInfoDto> findUserByUserResource(String authUrl) {
        String requestUrl = (this.manageDomainPath.trim() + "/api/user/findUserByUserResource.do");
        try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("url", authUrl);
            String respBody = HttpUtil.post(requestUrl, dataMap);

            Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
            ParseContext parseContext = JsonPath.using(conf);
            DocumentContext docCtx = parseContext.parse(respBody);

            String msg = docCtx.read("$.msg");

            String code = docCtx.read("$.code");

            if ("200".equals(code)) {
                ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Object objectData = docCtx.read("$.data");
                List<ManagerInfoDto> data = mapper.readValue(mapper.writeValueAsString(objectData), new TypeReference<List<ManagerInfoDto>>() {
                });
                return data;
            } else {
                throw new TraceBusinessException(msg);
            }

        } catch (Exception e) {
            if (!(e instanceof TraceBusinessException)) {
                logger.error(e.getMessage(), e);
                throw new TraceBusinessException("远程请求出错");
            } else {
                throw (TraceBusinessException) e;
            }

        }

    }

    public List<ManagerInfoDto> getAdminUser() {
        return findUserByUserResource(hz_admin_authUrl);
    }

    public boolean isAdminUser(Long userId) {
        Map<Long, ManagerInfoDto> managerMap = new HashMap<>();
        List<ManagerInfoDto> managers = findUserByUserResource(hz_admin_authUrl);
        StreamEx.of(managers).nonNull().forEach(m -> {
            managerMap.put(m.getId(), m);
        });
        return managerMap.containsKey(userId);
    }

    public static void main(String[] args) throws Exception {
        ManageSystemComponent c = new ManageSystemComponent();
        c.manageDomainPath = "http://10.28.10.167";
        c.findUserByUserResource("http://127.0.0.1/menu/preSave.do");
    }

}
