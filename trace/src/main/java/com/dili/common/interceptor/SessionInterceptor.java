package com.dili.common.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.service.RedisService;
import com.dili.common.service.SessionRedisService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

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
	private RedisService redisService;
	@Autowired
	private DefaultConfiguration defaultConfiguration;
	@Autowired
	SessionRedisService sessionRedisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		Boolean initialized = (Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);
		if (Boolean.TRUE.equals(initialized)) {
			return true;
		}
		String sessionId = getSessionId(request);
		if (StringUtils.isBlank(sessionId)) {
			return true;
		}
		loadData(request, response, sessionId);
		// 检车禁用用户
		checkDisableUsers(request, response);
		request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
		return true;
	}

	/**
	 * 检查禁用用户
	 * 
	 * @param request
	 * @param response
	 */
	private void checkDisableUsers(HttpServletRequest request, HttpServletResponse response) {
		// if (redisService.sHasKey(ExecutionConstants.WAITING_DISABLED_USER_PREFIX,
		// sessionContext.getAccountId())) {
		// deleteSession(response, request);
		// sessionContext.setInvalidate(true);
		// }
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		this.sessionRedisService.refresh(this.sessionContext.getSessionData());
		
	}

	private void loadData(HttpServletRequest request, HttpServletResponse response, String sessionId) {
		this.sessionRedisService.loadFromRedis(sessionId).ifPresent(sd -> {
			// sd.setToLoginSessionContext(this.sessionContext);
			this.sessionContext.setSessionData(sd);
		});

	}

	private String getSessionId(HttpServletRequest request) {
		return StrUtil.isNotBlank(request.getHeader("sessionId")) ? request.getHeader("sessionId")
				: request.getParameter("sessionId");
	}
}
