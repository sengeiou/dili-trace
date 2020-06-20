package com.dili.trace.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.components.LoginComponent;
import com.dili.trace.api.input.LoginInputDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 账号相关api
 */
@Api(value = "/api/loginApi", description = "有关于帐号相关的接口")
@RestController
@RequestMapping(value = "/api/loginApi")
public class LoginApi {
	private static final Logger logger = LoggerFactory.getLogger(LoginApi.class);

	@Autowired
	private LoginComponent loginComponent;

	@ApiOperation(value = "登录", notes = "登录")
	@RequestMapping(value = "/login.api", method = RequestMethod.POST)
	public BaseOutput<Map<String, Object>> login(@RequestBody LoginInputDto loginInput) {
		try {
			Map<String, Object> data = this.loginComponent.login(loginInput);
			return BaseOutput.success().setData(data);
		} catch (BusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure();
		}
	}

}
