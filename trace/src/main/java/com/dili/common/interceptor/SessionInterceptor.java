package com.dili.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;

import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.trace.api.components.SessionRedisService;
import com.dili.trace.service.CustomerRpcService;
import com.dili.trace.service.UapRpcService;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.redis.UserRedis;
import com.dili.uap.sdk.redis.UserUrlRedis;
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


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        AppAccess access = findAnnotation((HandlerMethod) handler,
                AppAccess.class);
        if (access == null) {
            SessionData sessionData = this.loginAsClient(request).orElseGet(() -> {
               return this.loginAsManager(request).orElse(null);
            });
            this.sessionContext.setSessionData(sessionData);
        }
        if (access.role() == Role.Client) {
            if (!this.customerRpcService.hasAccess(access)) {
                throw new TraceBizException("没有权限访问");
            }
            this.sessionContext.setSessionData(this.loginAsClient(request).get());
        } else if (access.role() == Role.Manager) {
            if (!this.uapRpcService.hasAccess(access)) {
                throw new TraceBizException("没有权限访问");
            }
            this.sessionContext.setSessionData(this.loginAsManager(request).get());
        } else {
            throw new TraceBizException("参数错误");
        }
        this.sessionContext.getSessionData().setRole(access.role());

        return true;
    }

    private Optional<SessionData> loginAsManager(HttpServletRequest req) {

        String sessionId=req.getHeader("UAP_SessionId");
        if(StringUtils.isBlank(sessionId)){
            return Optional.empty();
        }
        UserTicket ut=this.userRedis.getUser(sessionId);
        if(ut==null){
            return Optional.empty();
        }
        String url="app_auth";
        if(!this.userUrlRedis.checkUserMenuUrlRight(ut.getId(),url)){
            return Optional.empty();
        }
        SessionData sessionData = SessionData.fromUserTicket(ut);
        return Optional.ofNullable(sessionData);
    }

    private Optional<SessionData> loginAsClient(HttpServletRequest req) {
        return this.customerRpcService.getCurrentCustomer();
    }

    private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
        T annotation = handler.getMethodAnnotation(annotationType);
        if (annotation != null)
            return annotation;
        return handler.getBeanType().getAnnotation(annotationType);
    }

}
