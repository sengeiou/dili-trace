package com.dili.trace.interceptors;

import com.dili.common.config.WebVars;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestInterceptor  implements HandlerInterceptor {
    private WebVars webVars;
    public RequestInterceptor(WebVars webVars){
        this.webVars=webVars;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.getSession().setAttribute("webVars",this.webVars);
        return true;
    }

}
