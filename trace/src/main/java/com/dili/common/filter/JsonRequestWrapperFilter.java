package com.dili.common.filter;

import com.dili.common.entity.BodyRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JsonRequestWrapperFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String contentType=servletRequest.getContentType();
        if("application/json".equals(contentType) || "application/json;charset=UTF-8".equals(contentType)){
            BodyRequestWrapper bodyRequestWrapper=new BodyRequestWrapper((HttpServletRequest) servletRequest);
            filterChain.doFilter(bodyRequestWrapper,servletResponse);
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
