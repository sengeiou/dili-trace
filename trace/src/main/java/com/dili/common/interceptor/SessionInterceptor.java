package com.dili.common.interceptor;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.ExecutionConstants;
import com.dili.common.entity.SessionContext;
import com.dili.common.service.RedisService;
import com.dili.ss.redis.service.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

public class SessionInterceptor extends HandlerInterceptorAdapter {
    private static final String ATTRIBUTE_CONTEXT_INITIALIZED=SessionInterceptor.class.getName()+".CONTEXT_INITIALIZED";
    //SESSION KEY
    private static final String SESSION_PREFIX="TRACE_SESSION_";
    @Resource
    private SessionContext sessionContext;
    @Resource
    private RedisService redisService;
    @Resource
    private DefaultConfiguration defaultConfiguration;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        Boolean initialized=(Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);
        if(Boolean.TRUE.equals(initialized)){
            return true;
        }
        String sessionId=getSessionId(request);
        if(StringUtils.isBlank(sessionId)){
            return true;
        }
        loadData(request,sessionId);
        //检车禁用用户
        checkDisableUsers(request, response);
        request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        return true;
    }

    /**
     * 检查禁用用户
     * @param request
     * @param response
     */
    private void checkDisableUsers(HttpServletRequest request, HttpServletResponse response) {
        if(redisService.sHasKey(ExecutionConstants.WAITING_DISABLED_USER_PREFIX, sessionContext.getAccountId())){
            deleteSession(response,request);
            sessionContext.setInvalidate(true);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(sessionContext.getChanged()){
            if(sessionContext.getInvalidate()){
                deleteSession(response,request);
            }else{
                saveSession(response);
            }
        }
    }

    private void saveSession(HttpServletResponse response) {
        String sessionId=sessionContext.getSessionId();
        if(!StrUtil.isBlank(sessionId)){
            redisService.set(SESSION_PREFIX+sessionId,sessionContext.getMap(),defaultConfiguration.getSessionExpire());
        }
    }

    private void deleteSession(HttpServletResponse response, HttpServletRequest request) {
        String sessionId=sessionContext.getSessionId();
        if(!StrUtil.isBlank(sessionId)){
            redisService.del(SESSION_PREFIX+sessionId);
        }
    }

    private void loadData(HttpServletRequest request,String sessionId){
        sessionContext.setSessionId(sessionId);
        Map<String,Object> map=(Map<String, Object>) redisService.get(SESSION_PREFIX+sessionId);
        if(MapUtil.isEmpty(map)){
        	String operatorId=request.getHeader("operatorId");
//        	String operatorName=request.getHeader("operatorName");
        	if(StrUtil.isBlank(operatorId)) {//||StrUtil.isBlank(operatorName)) {
        		return;
        	}
        	if(sessionContext.getAccountId()==null) {
        		sessionContext.setAccountId(Long.parseLong(operatorId));	
        	}
        }

        long expire=redisService.getExpire(SESSION_PREFIX+sessionId)*1000;
        sessionContext.setMillis(expire);
        sessionContext.setMap(map);

    }
    private String getSessionId(HttpServletRequest request){
        return StrUtil.isNotBlank(request.getHeader("sessionId"))?request.getHeader("sessionId"):request.getParameter("sessionId");
    }
}
