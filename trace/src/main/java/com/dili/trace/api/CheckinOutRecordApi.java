package com.dili.trace.api;

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
import com.dili.common.entity.SessionContext;
import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.api.dto.CheckInApiDetailOutput;
import com.dili.trace.api.dto.CheckInApiInput;
import com.dili.trace.api.dto.CheckInApiListOutput;
import com.dili.trace.api.dto.CheckOutApiInput;
import com.dili.trace.api.dto.CheckoutApiDetailOutput;
import com.dili.trace.api.dto.CheckoutApiListQuery;
import com.dili.trace.api.dto.ManullyCheckInput;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import io.swagger.annotations.Api;

@SuppressWarnings("deprecation")
@Api(value = "/api/checkinRecordApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/checkinRecordApi")
public class CheckinOutRecordApi {
	private static final Logger logger = LoggerFactory.getLogger(CheckinOutRecordApi.class);
	@Resource
	private UserService userService;
	@Resource
	private SessionContext sessionContext;
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/doCheckin.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<Long> doCheckin(@RequestBody CheckInApiInput input) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		try {
			CheckinOutRecord checkinRecord = this.checkinOutRecordService
					.doCheckin(new OperatorUser(sessionContext.getAccountId(), ""), input);
			return BaseOutput.success().setData(checkinRecord.getId());
		} catch (BusinessException e) {
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
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		try {
			CheckinOutRecord checkoutRecord = this.checkinOutRecordService
					.doCheckout(new OperatorUser(sessionContext.getAccountId(), ""), input);
			return BaseOutput.success().setData(checkoutRecord.getId());
		} catch (BusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}
	@SuppressWarnings({ "unchecked"})
	
	@RequestMapping(value = "/listPagedAvailableCheckInData.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<BasePage<RegisterBill>> listPagedAvailableCheckInData(@RequestBody RegisterBillDto query) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		RegisterBillDto condition=DTOUtils.newDTO(RegisterBillDto.class);
		condition.setUserId(query.getUserId());
		condition.setLikeProductName(query.getLikeProductName());
		condition.setPage(query.getPage());
		condition.setRows(query.getRows());

		BasePage<CheckInApiListOutput> page = this.checkinOutRecordService.listCheckInApiListOutputPage(condition);

		return BaseOutput.success().setData(page);
	}

	/**
	 * 分页查询需要被进场查询的信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getCheckInDetail.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<CheckInApiDetailOutput> getCheckInDetail(@RequestBody RegisterBillDto query) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		if (query == null || query.getId() == null) {
			return BaseOutput.failure("参数错误");
		}

		CheckInApiDetailOutput detail = this.checkinOutRecordService.getCheckInDetail(query.getId()).orElse(null);
		if (detail != null) {
			return BaseOutput.success().setData(detail);
		}
		return BaseOutput.failure("没有查找到详情");

	}

	/**
	 * 合格/不合格
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/doManullyCheck.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<Long> doManullyCheck(@RequestBody ManullyCheckInput input) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		try {
			SeparateSalesRecord record = this.checkinOutRecordService
					.doManullyCheck(new OperatorUser(sessionContext.getAccountId(), ""), input);
			return BaseOutput.success().setData(record.getId());
		} catch (BusinessException e) {

			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}

	/**
	 * 分页查询需要出场查询的信息
	 */
	@RequestMapping(value = "/listPagedAvailableCheckOutData.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<BasePage<DTO>> listPagedAvailableCheckOutData( @RequestBody CheckoutApiListQuery query) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		return this.checkinOutRecordService.listPagedAvailableCheckOutData(query);
	}

	/**
	 * 出场详情
	 */
	@SuppressWarnings({"rawtypes" })
	@RequestMapping(value = "/getCheckoutDataDetail.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput getCheckoutDataDetail(@RequestBody CheckoutApiListQuery input) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		if (input == null || input.getSeparateSalesId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			CheckoutApiDetailOutput detailOutput = this.checkinOutRecordService
					.getCheckoutDataDetail(input.getSeparateSalesId());
			return BaseOutput.success().setData(detailOutput);
		} catch (BusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 分页查询需要出场查询的信息
	 */
	@RequestMapping(value = "/listPagedData.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<BasePage<Map<String,Object>>> listPagedData(@RequestBody CheckoutApiListQuery query) {
		
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}

		return this.checkinOutRecordService.listPagedData(query, sessionContext.getAccountId());
		
	}
}