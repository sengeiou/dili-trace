package com.dili.common.interceptor;

import com.dili.common.annotation.Access;
import com.dili.common.entity.LoginSessionContext;
import com.dili.trace.service.GlobalVarService;
import com.dili.trace.service.UapRpcService;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * @author xuliang
 */
public class AddAttributeInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AddAttributeInterceptor.class);


    @Autowired
    GlobalVarService globalVarService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setAttribute("imageViewPathPrefix", this.globalVarService.getDfsImageViewPathPrefix());
        return true;
    }

}
