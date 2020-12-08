package com.dili.trace.api.manager;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 管理登记单接口
 */
@RestController
@RequestMapping(value = "/api/manager/managerRegisterBill")
@Api(value = "/api/manager/managerRegisterBill", description = "登记单相关接口")
@AppAccess(role = Role.Manager,url = "dili-trace-app-auth")
public class ManagerRegisterBillApi {
	private static final Logger logger = LoggerFactory.getLogger(ManagerRegisterBillApi.class);
	@Autowired
	private RegisterBillService registerBillService;
	@Autowired
	private LoginSessionContext sessionContext;
	@Autowired
	UserService userService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	ImageCertService imageCertService;

	// @ApiOperation(value = "获得登记单详情")
	// @RequestMapping(value = "/viewRegisterBill.api", method = RequestMethod.POST)
	// public BaseOutput<BasePage<RegisterBill>> viewRegisterBill(@RequestBody RegisterBillDto input) {
	// 	if (input == null || input.getBillId() == null) {
	// 		return BaseOutput.failure("参数错误");
	// 	}
	// 	try {
	// 		OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
	// 		RegisterBill billItem = this.registerBillService.get(input.getBillId());
	// 		if(billItem==null){
	// 			return BaseOutput.failure("数据不存在");
	// 		}

	// 		RegisterBillOutput dto = RegisterBillOutput.build(billItem);
			
	// 		String upStreamName=StreamEx.ofNullable(billItem.getUpStreamId()).nonNull()
	// 		.map(upId->{return this.upStreamService.get(upId);})
	// 		.nonNull().map(UpStream::getName).findFirst().orElse("");
	// 		dto.setUpStreamName(upStreamName);
	// 		dto.setImageCertList(this.imageCertService.findImageCertListByBillId(billItem.getId()));
	// 		return BaseOutput.success().setData(dto);

	// 	} catch (TraceBusinessException e) {
	// 		return BaseOutput.failure(e.getMessage());
	// 	} catch (Exception e) {
	// 		logger.error(e.getMessage(), e);
	// 		return BaseOutput.failure("操作失败：服务端出错");
	// 	}
	// }

}
