package com.dili.common.interceptor;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dili.common.annotation.Access;
import com.dili.common.entity.LoginSessionContext;

import com.dili.trace.service.UapRpcService;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author xuliang
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	@Autowired
	private LoginSessionContext sessionContext;
	@Autowired
	UapRpcService uapRpcService;




	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean value = this.preHandleCheck(request, response, handler);
		return value;
	}

	public boolean preHandleCheck(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		Access access = findAnnotation((HandlerMethod) handler,
				Access.class);
		if (access == null) {
			return true;
		}
		return SessionContext.hasAccess(access.method(),access.url());
	}

	private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
		T annotation = handler.getMethodAnnotation(annotationType);
		if (annotation != null)
			return annotation;
		return handler.getBeanType().getAnnotation(annotationType);
	}
}
