package com.dili.trace.api.components;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.BusinessException;
import com.dili.common.util.MD5Util;
import com.dili.common.util.UUIDUtil;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.LoginInputDto;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.service.UserService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import one.util.streamex.StreamEx;

@Component
public class LoginComponent {
	private static final Logger logger = LoggerFactory.getLogger(LoginComponent.class);
	@Resource
	private UserService userService;
	@Resource
	private LoginSessionContext sessionContext;
	@Value("${manage.domain}")
	private String manageDomainPath;

	@PostConstruct
	public void init() {
		if (StringUtils.isBlank(this.manageDomainPath)) {
			throw new RuntimeException("缺少权限系统的配置信息");
		}
		logger.warn(">>>>>>>>>>>>>>请注意配置host,以确保当前应用所在服务器能访问权限系统: {}", this.manageDomainPath);
		StreamEx.of(LoginIdentityTypeEnum.values()).map(LoginIdentityTypeEnum::getAuthUrl)
				.filter(StringUtils::isNotBlank).forEach(auth -> {

					logger.warn(">>>>>>>>>>>>>>请注意配置登录访问权限: {}", auth);

				});
	}

	public Map<String, Object> login(LoginInputDto loginInput) {

		if (loginInput == null) {
			throw new BusinessException("参数错误");
		}
		if (StrUtil.isAllBlank(loginInput.getUsername())) {
			throw new BusinessException("帐号/手机号不能为空");
		}
		if (StrUtil.isBlank(loginInput.getPassword())) {
			throw new BusinessException("密码不能为空");
		}
		if (loginInput.getLoginIdentityType() == null) {
			loginInput.setLoginIdentityType(LoginIdentityTypeEnum.USER.getCode());
		}

		if (LoginIdentityTypeEnum.USER.equalsToCode(loginInput.getLoginIdentityType())) {
			OperatorUser operatorUser = this.userLogin(loginInput.getUsername(), loginInput.getPassword());
			return this.responseData(operatorUser);
		} else if (LoginIdentityTypeEnum.SYS_MANAGER.equalsToCode(loginInput.getLoginIdentityType())) {

			OperatorUser operatorUser = this.sysManagerLogin(loginInput.getUsername(), loginInput.getPassword(),
					LoginIdentityTypeEnum.SYS_MANAGER);
			return this.responseData(operatorUser);

		} else {
			throw new BusinessException("登录参数出错");
		}
	}

	private Map<String, Object> responseData(OperatorUser operatorUser) {
		Map<String, Object> result = new HashMap<>();

		sessionContext.setSessionId(UUIDUtil.get());
		sessionContext.setAccountId(operatorUser.getId());
		sessionContext.setUserName(operatorUser.getName());
		result.put("userId", operatorUser.getId());
		result.put("userName", operatorUser.getName());
		result.put("sessionId", sessionContext.getSessionId());
		return result;

	}

	private OperatorUser userLogin(String phone, String password) {
		User po = userService.login(phone, MD5Util.md5(password));

		OperatorUser operatorUser = new OperatorUser(po.getId(), po.getName());

		return operatorUser;
	}

	private OperatorUser sysManagerLogin(String username, String password, LoginIdentityTypeEnum identityTypeEnum) {
		String loginUrl = (this.manageDomainPath.trim() + "/loginControl/doLoginAPP.do");
		String checkAuthUrl = (this.manageDomainPath.trim() + "/api/user/checkUserResource.do");
		try {
			Map<String, Object> loginMap = new HashMap<String, Object>();
			loginMap.put("username", username);
			loginMap.put("passwd", password);
			String loginRespBody = HttpUtil.post(loginUrl, loginMap);
			// {"success":true, "msg":"登录成功！", "sessionId":
			// "0fff1b6f-51ee-4bdb-9700-6d31854ac2c0", "userId":
			// 260,"realName":"超级用户","depId":29}
			logger.info("loginRespBody={}", loginRespBody);
			Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
			ParseContext parseContext = JsonPath.using(conf);
			DocumentContext loginDocumentContext = parseContext.parse(loginRespBody);

			Boolean success = loginDocumentContext.read("$.success");
			if (success == null) {
				throw new BusinessException("登录失败");
			}

			String msg = loginDocumentContext.read("$.msg");
			if (!success) {
				throw new BusinessException(msg);
			}

			Long depId = loginDocumentContext.read("$.depId", Long.class);

			String sessionId = loginDocumentContext.read("$.sessionId");
			Long userId = loginDocumentContext.read("$.userId", Long.class);
			String realName = loginDocumentContext.read("$.realName");

			Map<String, Object> checkAuthMap = new HashMap<String, Object>();
			checkAuthMap.put("sessionId", sessionId);
			checkAuthMap.put("url", identityTypeEnum.getAuthUrl());

			String checkAuthRespBody = HttpUtil.post(checkAuthUrl, checkAuthMap, 20 * 1000);
			DocumentContext checkAuthDocumentContext = parseContext.parse(checkAuthRespBody);
			logger.info("checkAuthRespBody={}", checkAuthRespBody);
			String code = checkAuthDocumentContext.read("$.code");
			String result = checkAuthDocumentContext.read("$.result");
			Object data = checkAuthDocumentContext.read("$.data");
			Object errorData = checkAuthDocumentContext.read("$.errorData");

			if ("200".equals(code)) {
				return new OperatorUser(userId, realName);
			} else {
				throw new BusinessException("权限不足");
			}

		} catch (Exception e) {
			if (!(e instanceof BusinessException)) {
				logger.error(e.getMessage(), e);
				throw new BusinessException("登录失败:网络错误");
			} else {
				throw e;
			}

		}

	}

	public static void main(String[] args) {
		LoginComponent loginapi = new LoginComponent();
		loginapi.manageDomainPath = "http://mg.nong12.com/";
		loginapi.sysManagerLogin("admin", "wenze123456", LoginIdentityTypeEnum.SYS_MANAGER);
	}
}