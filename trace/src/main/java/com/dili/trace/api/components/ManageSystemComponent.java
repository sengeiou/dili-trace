package com.dili.trace.api.components;

import cn.hutool.http.HttpUtil;
import com.dili.common.exception.TraceBusinessException;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.dto.ManagerInfoDto;
import com.dili.trace.dto.OperatorUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.*;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
        String loginUrl = (this.manageDomainPath.trim() + "/loginControl/doLoginAPP.do");
        String checkAuthUrl = (this.manageDomainPath.trim() + "/api/user/checkUserResource.do");
        try {
            Map<String, Object> loginMap = new HashMap<String, Object>();
            loginMap.put("username", username);
            loginMap.put("passwd", password);
            String loginRespBody = HttpUtil.post(loginUrl, loginMap);
            // {"success":true, "msg":"登录成功！", "sessionId":
            // "0fff1b6f-51ee-4bdb-9700-6d31854ac2c0", "userId":
            // 260,"realName":"超级用户","depId":29}
            logger.info("loginRespBody={}", loginRespBody);
            Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
            ParseContext parseContext = JsonPath.using(conf);
            DocumentContext loginDocumentContext = parseContext.parse(loginRespBody);

            Boolean success = loginDocumentContext.read("$.success");
            if (success == null) {
                throw new TraceBusinessException("登录失败");
            }

            String msg = loginDocumentContext.read("$.msg");
            if (!success) {
                throw new TraceBusinessException(msg);
            }

            Long depId = loginDocumentContext.read("$.depId", Long.class);

            String sessionId = loginDocumentContext.read("$.sessionId");
            Long userId = loginDocumentContext.read("$.userId", Long.class);
            String realName = loginDocumentContext.read("$.realName");

            Map<String, Object> checkAuthMap = new HashMap<String, Object>();
            checkAuthMap.put("sessionId", sessionId);
            checkAuthMap.put("url", identityTypeEnum.getAuthUrl());

            String checkAuthRespBody = HttpUtil.post(checkAuthUrl, checkAuthMap, 20 * 1000);
            DocumentContext checkAuthDocumentContext = parseContext.parse(checkAuthRespBody);
            logger.info("checkAuthRespBody={}", checkAuthRespBody);
            String code = checkAuthDocumentContext.read("$.code");
            String result = checkAuthDocumentContext.read("$.result");
            Object data = checkAuthDocumentContext.read("$.data");
            Object errorData = checkAuthDocumentContext.read("$.errorData");

            if ("200".equals(code)) {
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
