package com.dili.trace.api.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CheckInApiInput;
import com.dili.trace.api.input.CheckOutApiInput;
import com.dili.trace.api.output.CheckoutApiListQuery;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import io.swagger.annotations.Api;

@SuppressWarnings("deprecation")
@Api(value = "/api/manager/managerCheckinRecordApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/manager/managerCheckinRecord")
public class ManagerCheckinOutRecordApi {
	private static final Logger logger = LoggerFactory.getLogger(ManagerCheckinOutRecordApi.class);
	@Resource
	private UserService userService;
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	CheckinOutRecordService checkinOutRecordService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	UpStreamService upStreamService;

	/**
	 * 进场
	 */
	@RequestMapping(value = "/doCheckin.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<Long> doCheckin(@RequestBody CheckInApiInput input) {
		
		try {
			OperatorUser operatorUser=sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			List<CheckinOutRecord> checkinRecordList = this.checkinOutRecordService
					.doCheckin(new OperatorUser(sessionContext.getAccountId(), ""), input);
			return BaseOutput.success();
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}

	/**
	 * 出场
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/doCheckout.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput doCheckout(@RequestBody CheckOutApiInput input) {
		try {
			OperatorUser operatorUser=sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			List<CheckinOutRecord> checkinRecordList = this.checkinOutRecordService
					.doCheckout(new OperatorUser(sessionContext.getAccountId(), ""), input);
			return BaseOutput.success();
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}


	/**
	 * 分页查询需要出场查询的信息
	 */
	@RequestMapping(value = "/listPageCheckInData.api", method = { RequestMethod.POST})
	public BaseOutput<BasePage<Map<String,Object>>> listPageCheckInData(@RequestBody CheckoutApiListQuery query) {
		
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}

		return this.checkinOutRecordService.listPagedData(query, sessionContext.getAccountId());
		
	}
	/**
	 * 分页查询需要出场查询的信息
	 */
	@RequestMapping(value = "/listPageCheckOutData.api", method = { RequestMethod.POST})
	public BaseOutput<BasePage<Map<String,Object>>> listPageCheckOutData(@RequestBody CheckoutApiListQuery query) {
		
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}

		return this.checkinOutRecordService.listPagedData(query, sessionContext.getAccountId());
		
	}
}