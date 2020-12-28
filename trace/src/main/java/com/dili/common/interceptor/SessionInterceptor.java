package com.dili.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;

import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.components.SessionRedisService;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.UapRpcService;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.redis.UserRedis;
import com.dili.uap.sdk.redis.UserUrlRedis;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// import cn.hutool.core.map.MapUtil;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class SessionInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    private static final String ATTRIBUTE_CONTEXT_INITIALIZED = SessionInterceptor.class.getName()
            + ".CONTEXT_INITIALIZED";
    // SESSION KEY
    // private static final String SESSION_PREFIX = "TRACE_SESSION_";
    // private static final String SESSION_PREFIX_ACCOUNT =
    // "TRACE_SESSION_ACCOUNT_";
    // private static final String PREFIX_GAP = "_";
    @Autowired
    private LoginSessionContext sessionContext;

    @Autowired
    SessionRedisService sessionRedisService;
    @Autowired
    UapRpcService uapRpcService;
    @Autowired
    CustomerRpcService customerRpcService;
    @Autowired
    UserRedis userRedis;
    @Autowired
    UserUrlRedis userUrlRedis;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        AppAccess access = findAnnotation((HandlerMethod) handler,
                AppAccess.class);

        try {
            if (access == null) {
                return this.write401(response, "没有权限访问");
            }
            Optional<SessionData> currentSessionData=Optional.empty();
            if (access.role() == Role.ANY) {
                currentSessionData=this.loginAsAny(request);
            } else if (access.role() == Role.Client) {
                currentSessionData=this.loginAsClient(request);
            } else if (access.role() == Role.Manager) {
                currentSessionData = this.loginAsManager(request);
            } else if (access.role() == Role.NONE) {
                logger.info("无需权限即可访问。比如检测机器");
                return true;
            } else {
                return this.writeError(response, "权限配置错误");
            }
            if (!currentSessionData.isPresent()) {
                return this.write401(response, "没有权限访问");
            }
            this.sessionContext.setSessionData(currentSessionData.get(),access);

        } catch (TraceBizException e) {
            return this.writeError(response, e.getMessage());
        } catch (Exception e) {
            return this.writeError(response, "服务端出错");
        }

        this.sessionContext.getSessionData().setRole(access.role());

        return true;
    }

    private boolean writeError(HttpServletResponse resp, String msg) {
        try {
            BaseOutput out = BaseOutput.failure(msg);
            byte[] bytes = mapper.writeValueAsBytes(out);
            resp.getOutputStream().write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    private boolean write401(HttpServletResponse resp, String msg) {
        try {
            BaseOutput out = BaseOutput.failure("401", msg);
            byte[] bytes = mapper.writeValueAsBytes(out);
            resp.getOutputStream().write(bytes);
            resp.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    private Optional<SessionData> loginAsManager(HttpServletRequest req) {
//        System.out.println(req.getHeaderNames());
        String userToken = req.getHeader("UAP_Token");
        if (StringUtils.isBlank(userToken)) {
            return Optional.empty();
        }

        UserTicket ut = this.userRedis.getTokenUser(userToken);
        if (ut == null) {
            return Optional.empty();
        }
//        String url = "app_auth";
//        if (!this.userUrlRedis.checkUserMenuUrlRight(ut.getId(), url)) {
//            return Optional.empty();
//        }
        SessionData sessionData = SessionData.fromUserTicket(ut);
        return Optional.ofNullable(sessionData);
    }

    private Optional<SessionData> loginAsClient(HttpServletRequest req) {
        return this.customerRpcService.getCurrentCustomer();
    }

    private Optional<SessionData> loginAsAny(HttpServletRequest req) {
        String userToken = req.getHeader("UAP_Token");
        String userId = req.getHeader("userId");
        if(StringUtils.isNotBlank(userToken)){
            return this.loginAsManager(req);
        }else if(StringUtils.isNotBlank(userId)){
            return this.loginAsClient(req);
        }else{
            return Optional.empty();
        }
    }

    private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
        T annotation = handler.getMethodAnnotation(annotationType);
        if (annotation != null)
            return annotation;
        return handler.getBeanType().getAnnotation(annotationType);
    }

}
