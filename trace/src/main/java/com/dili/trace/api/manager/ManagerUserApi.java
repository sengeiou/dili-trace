package com.dili.trace.api.manager;

import com.alibaba.fastjson.JSON;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.output.RegisterBillOutput;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.api.output.VerifyBillInputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.glossary.ColorEnum;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;
import com.dili.trace.util.BasePageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/manager/user")
@Api(value = "/api/manager/user", description = "用户管理相关接口")
public class ManagerUserApi {
	private static final Logger logger = LoggerFactory.getLogger(ManagerUserApi.class);
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	UserService userService;

	@ApiOperation(value = "商户审核统计概览")
	@RequestMapping(value = "/userCertCount.api", method = RequestMethod.POST)
	public BaseOutput<Map<String,String>> countGroupByValidateState(@RequestBody User user) {
		try {
			sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			return userService.countGroupByValidateState(user);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("操作失败：服务端出错");
		}
	}

	@ApiOperation(value = "获得用户审核列表")
	@RequestMapping(value = "/listUserCertByQuery.api", method = RequestMethod.POST)
	public BaseOutput<BasePage<UserOutput>> listUserCertByQuery(@RequestBody UserInput user) {
		try {
			sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			BasePage<UserOutput> data = userService.pageUserByQuery(user);
			return BaseOutput.success().setData(data);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("操作失败：服务端出错");
		}
	}

	@ApiOperation(value = "获得用户资料详情")
	@RequestMapping(value = "/userCertDetail.api", method = RequestMethod.POST)
	public BaseOutput<User> userCertDetail(@RequestBody UserInput input) {
		if (input == null || input.getId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
//			sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			User data = userService.get(input.getId());
			return BaseOutput.success().setData(data);

		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("操作失败：服务端出错");
		}
	}

	@ApiOperation(value = "审核用户资料")
	@RequestMapping(value = "/verifyUserCert.api", method = RequestMethod.POST)
	public BaseOutput<Long> verifyUserCert(@RequestBody UserInput input) {
		try {
//			sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			return userService.verifyUserCert(input);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("操作失败：服务端出错");
		}

	}

}
