package com.dili.trace.api;

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
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
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
import com.dili.trace.glossary.CheckinOutTypeEnum;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import io.swagger.annotations.Api;

@Api(value = "/api/checkinRecordApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/checkinRecordApi")
public class CheckinOutRecordApi {
	private static final Logger logger = LoggerFactory.getLogger(UserApi.class);
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
	@RequestMapping(value = "/doCheckin.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<Long> doCheckin(@RequestBody CheckInApiInput input) {
//		User user = userService.get(sessionContext.getAccountId());
//		if (user == null) {
//			return BaseOutput.failure("未登陆用户");
//		}
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
	@RequestMapping(value = "/doCheckout.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput doCheckin(@RequestBody CheckOutApiInput input) {

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

	@RequestMapping(value = "/listPagedAvailableCheckInData.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<BasePage<RegisterBill>> listPagedAvailableCheckInData(@RequestBody RegisterBillDto query) {
//        User user = userService.get(sessionContext.getAccountId());
//        if (user == null) {
//            return BaseOutput.failure("未登陆用户");
//        }
		logger.info(sessionContext.getSessionId());
		logger.info("{}", sessionContext.getAccountId());
		logger.info("{}", sessionContext.getMap());
		logger.info("{}", sessionContext.getUserType());
		logger.info("{}", sessionContext.getRelationId());
		BasePage<CheckInApiListOutput> page = this.checkinOutRecordService.listCheckInApiListOutputPage(query);

		return BaseOutput.success().setData(page);
	}

	/**
	 * 分页查询需要被进场查询的信息
	 */
	@RequestMapping(value = "/getCheckInDetail.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<CheckInApiDetailOutput> getCheckInDetail(@RequestBody RegisterBillDto query) {
//		User user = userService.get(sessionContext.getAccountId());
//		if (user == null) {
//			return BaseOutput.failure("未登陆用户");
//		}
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
	@RequestMapping(value = "/doManullyCheck.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<Long> doManullyCheck(@RequestBody ManullyCheckInput input) {
//		User user = userService.get(sessionContext.getAccountId());
//		if (user == null) {
//			return BaseOutput.failure("未登陆用户");
//		}
		try {
			SeparateSalesRecord record = this.checkinOutRecordService
					.doManullyCheck(new OperatorUser(sessionContext.getAccountId(), ""), input);
			return BaseOutput.success();
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
	public BaseOutput<BasePage<CheckinOutRecord>> listPagedAvailableCheckOutData(
			@RequestBody CheckoutApiListQuery query) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}

		if (query == null || query.getUserId() == null) {
			return BaseOutput.failure("参数错误");
		}

		SeparateSalesRecord separateSalesRecord = DTOUtils.newDTO(SeparateSalesRecord.class);
		separateSalesRecord.setSalesUserId(query.getUserId());
		separateSalesRecord.mset(IDTO.AND_CONDITION_EXPR,
				" checkin_record_id in(select id from checkinout_record where inout=" + CheckinOutTypeEnum.IN.getCode()
						+ " and status=" + CheckinStatusEnum.ALLOWED.getCode() + ")  and checkout_record_id is null");

		BasePage<SeparateSalesRecord> page = this.separateSalesRecordService.listPageByExample(separateSalesRecord);
		page.getDatas().stream().forEach(sp -> {
			RegisterBill rb = this.registerBillService.get(sp.getBillId());

			sp.mset("state", rb.getState());
			sp.mset("productName", rb.getProductName());

			sp.setMetadata("state", rb.getState());
			sp.setMetadata("productName", rb.getProductName());
		});

		return BaseOutput.success().setData(page);
	}

	/**
	 * 出场详情
	 */
	@RequestMapping(value = "/getCheckoutDataDetail.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput getCheckoutDataDetail(@RequestBody CheckoutApiListQuery input) {
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
	public BaseOutput<BasePage<SeparateSalesRecord>> listPagedData(@RequestBody CheckoutApiListQuery query) {
		if (query == null || query.getUserId() == null) {
			return BaseOutput.failure("参数错误");
		}
		CheckinOutRecord checkinOutRecord = new CheckinOutRecord();
		checkinOutRecord.setOperatorId(sessionContext.getAccountId());

		BasePage<CheckinOutRecord> page = this.checkinOutRecordService.listPageByExample(checkinOutRecord);
		
		page.getDatas().stream().forEach(cr -> {
			RegisterBill billQuery=DTOUtils.newDTO(RegisterBill.class);
			billQuery.mset(IDTO.AND_CONDITION_EXPR,
					" id in (select bill_id from separate_sales_record where checkin_record_id ="+cr.getId()+"or checkout_record_id="+cr.getId()+") ");
			RegisterBill billItem=this.registerBillService.listByExample(billQuery).stream().findFirst().orElse(DTOUtils.newDTO(RegisterBill.class));
			
			SeparateSalesRecord separateSalesRecordQuery=DTOUtils.newDTO(SeparateSalesRecord.class);
			separateSalesRecordQuery.mset(IDTO.AND_CONDITION_EXPR,
					"  ( checkin_record_id ="+cr.getId()+"or checkout_record_id="+cr.getId()+") ");
			SeparateSalesRecord separateSalesRecordItem=	this.separateSalesRecordService.listByExample(separateSalesRecordQuery).stream().findFirst().orElse(DTOUtils.newDTO(SeparateSalesRecord.class));
			

			cr.mset("state", billItem.getState());
			cr.mset("productName", billItem.getProductName());
			cr.mset("storeWeight", separateSalesRecordItem.getStoreWeight());

			cr.setMetadata("state", billItem.getState());
			cr.setMetadata("productName", billItem.getProductName());
			cr.mset("storeWeight", separateSalesRecordItem.getStoreWeight());
		});

		return BaseOutput.success().setData(page);
	}
}