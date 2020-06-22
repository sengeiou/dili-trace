package com.dili.trace.api.manager;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.RegisterBillApi;
import com.dili.trace.api.output.VerifyBillInputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/manager/manageRegisterBill")
@Api(value = "/api/manager/manageRegisterBill", description = "登记单相关接口")
public class ManagerRegisterBillApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillApi.class);
	@Autowired
	private RegisterBillService registerBillService;
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	UserService userService;

	@ApiOperation(value = "获得登记单", httpMethod = "GET", notes = "productName=?")
	@RequestMapping(value = "/listBill.api", method = RequestMethod.GET)
	public BaseOutput<BasePage<RegisterBill>> listBill(@RequestBody RegisterBill input) {
		try {
			User user = userService.get(sessionContext.getAccountId());
			BasePage<RegisterBill> page = this.registerBillService.listPageByExample(input);
			return BaseOutput.success().setData(page);
		} catch (BusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("操作失败：服务端出错");
		}
	}

	@ApiOperation(value = "查验登记单", httpMethod = "GET", notes = "productName=?")
	@RequestMapping(value = "/doVerify.api", method = RequestMethod.GET)
	public BaseOutput<Long> doVerify(@RequestBody VerifyBillInputDto inputDto) {
		LOGGER.info("通过ID查验登记单:{}", inputDto);
		try {
			if (inputDto == null || inputDto.getVerifyStatus() == null) {
				return BaseOutput.failure("参数错误");
			}
			User user = userService.get(sessionContext.getAccountId());

			RegisterBill input = new RegisterBill();
			input.setId(inputDto.getBillId());
			input.setVerifyStatus(inputDto.getVerifyStatus());
			Long id = this.registerBillService.doVerify(input);
			return BaseOutput.success().setData(id);
		} catch (BusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("操作失败：服务端出错");
		}

	}
}
