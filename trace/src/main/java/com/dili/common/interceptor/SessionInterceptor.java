package com.dili.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;

import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.trace.api.components.SessionRedisService;
import com.dili.trace.service.ClientRpcService;
import com.dili.trace.service.UapRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// import cn.hutool.core.map.MapUtil;

import java.lang.annotation.Annotation;

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
    ClientRpcService clientRpcService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        AppAccess access = findAnnotation((HandlerMethod) handler,
                AppAccess.class);
        if (access == null) {
            return true;
        }
        if (access.role() == Role.Client) {
            if (!this.clientRpcService.hasAccess(access)) {
                throw new TraceBizException("没有权限访问");
            }
            this.sessionContext.setSessionData(SessionData.mockClient());
        } else if (access.role() == Role.Manager) {
            if (!this.uapRpcService.hasAccess(access)) {
                throw new TraceBizException("没有权限访问");
            }
            SessionData sessionData= this.uapRpcService.getCurrentUserTicket().map(ut -> {
                return SessionData.fromUserTicket(ut);
            }).orElseGet(() -> {
                return null;
            });
            if(sessionData==null){
                throw new TraceBizException("没有权限访问");
            }
            this.sessionContext.setSessionData(sessionData);
        } else {
            throw new TraceBizException("参数错误");
        }
        this.sessionContext.getSessionData().setRole(access.role());
//        Boolean initialized = (Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);
//        if (Boolean.TRUE.equals(initialized)) {
//            return true;
//        }
//        String sessionId = getSessionId(request);
//        if (StringUtils.isBlank(sessionId)) {
//            return true;
//        }
//        loadData(request, response, sessionId);
        // 检车禁用用户
//        checkDisableUsers(request, response);
//        request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        return true;
    }

    private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
        T annotation = handler.getMethodAnnotation(annotationType);
        if (annotation != null)
            return annotation;
        return handler.getBeanType().getAnnotation(annotationType);
    }
//
//    /**
//     * 检查禁用用户
//     *
//     * @param request
//     * @param response
//     */
//    private void checkDisableUsers(HttpServletRequest request, HttpServletResponse response) {
//        // if (redisService.sHasKey(ExecutionConstants.WAITING_DISABLED_USER_PREFIX,
//        // sessionContext.getAccountId())) {
//        // deleteSession(response, request);
//        // sessionContext.setInvalidate(true);
//        // }
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//                           ModelAndView modelAndView) throws Exception {
//        this.sessionRedisService.refresh(this.sessionContext.getSessionData());
//
//    }

//    private void loadData(HttpServletRequest request, HttpServletResponse response, String sessionId) {
//        this.sessionRedisService.loadFromRedis(sessionId).ifPresent(sd -> {
//            // sd.setToLoginSessionContext(this.sessionContext);
//            this.sessionContext.setSessionData(sd);
//        });
//
//    }
//
//    private String getSessionId(HttpServletRequest request) {
//        return StrUtil.isNotBlank(request.getHeader("sessionId")) ? request.getHeader("sessionId")
//                : request.getParameter("sessionId");
//    }
}
