package com.dili.common.interceptor;

import java.io.Writer;
import java.lang.annotation.Annotation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.ExecutionConstants;
import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.service.UserAccessLogService;

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
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	UserAccessLogService userAccessLogService;

	private void logRequest(HttpServletRequest request) {
		if (this.sessionContext.getSessionData() != null && this.sessionContext.getSessionData().getUserId() != null) {
			String requestUri = request.getRequestURI();
			logger.info("loginType={},accountId={},requestUri={}", this.sessionContext.getSessionData().getIdentityType(),
					this.sessionContext.getAccountId(), requestUri);
			this.userAccessLogService.createUserAccessLog(this.sessionContext.getSessionData(), requestUri);
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean value = this.preHandleCheck(request, response, handler);
		if (value) {
			this.logRequest(request);
		}
		return value;
	}

	public boolean preHandleCheck(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		InterceptConfiguration interceptConfiguration = findAnnotation((HandlerMethod) handler,
				InterceptConfiguration.class);
		if (interceptConfiguration == null || !interceptConfiguration.loginRequired()) {
			return true;
		}
		if (sessionContext.getAccountId() == null) {// 用户id为空表示未登陆
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			BaseOutput baseOutput = new BaseOutput(ExecutionConstants.NO_LOGIN, "未登陆");
			Writer writer = response.getWriter();
			writer.write(JSONObject.toJSONString(baseOutput));
			writer.flush();
			if (writer != null) {
				writer.close();
			}
			return false;
		}
		return true;
	}

	private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
		T annotation = handler.getMethodAnnotation(annotationType);
		if (annotation != null)
			return annotation;
		return handler.getBeanType().getAnnotation(annotationType);
	}
}
