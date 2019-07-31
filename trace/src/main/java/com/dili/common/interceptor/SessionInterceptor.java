package com.dili.common.interceptor;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.SessionContext;
import com.dili.ss.redis.service.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class SessionInterceptor extends HandlerInterceptorAdapter {
    private static final String ATTRIBUTE_CONTEXT_INITIALIZED=SessionInterceptor.class.getName()+".CONTEXT_INITIALIZED";

    private static final String prefix="TRACE_SESSION_";
    @Resource
    private SessionContext sessionContext;
    @Resource
    private RedisUtil redisUtil;
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
        loadData(sessionId);
        request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        return true;
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
            redisUtil.set(prefix+sessionId,sessionContext.getMap(),defaultConfiguration.getSessionExpire());
        }
    }

    private void deleteSession(HttpServletResponse response, HttpServletRequest request) {
        String sessionId=sessionContext.getSessionId();
        if(!StrUtil.isBlank(sessionId)){
            redisUtil.remove(prefix+sessionId);
        }
    }

    private void loadData(String sessionId){
        sessionContext.setSessionId(sessionId);
        Map<String,Object> map=(Map<String, Object>) redisUtil.get(prefix+sessionId);
        if(MapUtil.isEmpty(map)){
            return;
        }
        long expire=redisUtil.getRedisTemplate().getExpire(prefix+sessionId)*1000;
        sessionContext.setMillis(expire);
        sessionContext.setMap(map);
    }
    private String getSessionId(HttpServletRequest request){
        return StrUtil.isNotBlank(request.getHeader("sessionId"))?request.getHeader("sessionId"):request.getParameter("sessionId");
    }
}
