package com.dili.sg.trace.api;

import com.dili.sg.common.entity.SessionData;
import com.dili.sg.trace.api.components.LoginComponent;
import com.dili.sg.trace.api.input.LoginInputDto;
import com.dili.sg.trace.exception.TraceBizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ss.domain.BaseOutput;

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

	/**
	 *登录
	 * @param loginInput
	 * @return
	 */
	@ApiOperation(value = "登录", notes = "登录")
	@RequestMapping(value = "/login.api", method = RequestMethod.POST)
	public BaseOutput<SessionData> login(@RequestBody LoginInputDto loginInput) {
		try {
			SessionData data = this.loginComponent.login(loginInput);
			return BaseOutput.success().setData(data);
		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure();
		}
	}

}
