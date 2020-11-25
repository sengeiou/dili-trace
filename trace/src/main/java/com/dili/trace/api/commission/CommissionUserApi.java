package com.dili.trace.api.commission;

import com.dili.trace.domain.User;
import com.dili.trace.service.SMSService;
import com.dili.trace.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ss.domain.BaseOutput;
import com.dili.common.exception.TraceBizException;

/**
 * 委托单接口
 */
@RestController
@RequestMapping(value = "/api/commission/commissionUserApi")
public class CommissionUserApi {
	private static final Logger logger = LoggerFactory.getLogger(CommissionUserApi.class);
	@Autowired
	UserServiceImpl userService;
	@Autowired
	SMSService sms;

	/**
	 * 创建委托用户
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/createCommissionUser.api")
	public BaseOutput<?> createCommissionUser(@RequestBody User input) {
		try {
			this.sms.checkVerificationCode(input.getPhone(), input.getCheckCode());
			User item = this.userService.createCommissionUser(input);
			return BaseOutput.success().setData(item.getId());
		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

}
