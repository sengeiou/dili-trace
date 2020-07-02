package com.dili.trace.api.manager;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CheckInApiInput;
import com.dili.trace.api.input.CheckOutApiInput;
import com.dili.trace.api.output.CheckoutApiListQuery;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.enums.TruckTypeEnum;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

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
	 * 进门验货
	 */
	@RequestMapping(value = "/doCheckin.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<Long> doCheckin(@RequestBody CheckInApiInput input) {

		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
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
	 * 出门验货
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/doCheckout.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput doCheckout(@RequestBody CheckOutApiInput input) {
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
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
	@RequestMapping(value = "/listPageCheckInData.api", method = { RequestMethod.POST })
	public BaseOutput<Map<Integer, Object>> listPageCheckInData(@RequestBody RegisterBillDto query) {

		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		if (query == null || query.getUserId() == null) {
			return BaseOutput.failure("参数错误");
		}
		query.setMetadata(IDTO.AND_CONDITION_EXPR," id not in (select bill_id from trade_detail where buyer_id="+query.getUserId()+" and checkin_status="+CheckinStatusEnum.ALLOWED.getCode()+" trade_type="+TradeTypeEnum.NONE.getCode()+")");
		List<RegisterBill>list =  this.registerBillService.listByExample(query);


		List<String>plateList=StreamEx.of(list).filter(bill->{
			return TruckTypeEnum.POOL.equalsToCode(bill.getTruckType());
		}).map(RegisterBill::getPlate).distinct().toList();
		query.setPlateList(plateList);
		query.setUserId(null);
		query.setTruckType(TruckTypeEnum.POOL.getCode());
		query.setMetadata(IDTO.AND_CONDITION_EXPR," id not in (select bill_id from trade_detail where checkin_status="+CheckinStatusEnum.ALLOWED.getCode()+" trade_type="+TradeTypeEnum.NONE.getCode()+")");
		List<RegisterBill>samePlatePoolTruckTypeBillList =  this.registerBillService.listByExample(query);

		Map<Integer, List<RegisterBill>> truckTypeBillMap = StreamEx.of(list).append(samePlatePoolTruckTypeBillList)
				.groupingBy(RegisterBill::getTruckType);
		Map<Integer, Object> resultMap = EntryStream.of(truckTypeBillMap).flatMapToValue((k, v) -> {
			if (TruckTypeEnum.FULL.equalsToCode(k)) {
				return Stream.of(v);
			}
			if (TruckTypeEnum.POOL.equalsToCode(k)) {
				return Stream.of(StreamEx.of(v).groupingBy(RegisterBill::getPlate));
			}
			return StreamEx.empty();
		}).toMap();

		return BaseOutput.success().setData(resultMap);

	}

	// /**
	// * 分页查询需要出场查询的信息
	// */
	// @RequestMapping(value = "/listPageCheckOutData.api", method = {
	// RequestMethod.POST })
	// public BaseOutput<BasePage<Map<String, Object>>>
	// listPageCheckOutData(@RequestBody CheckoutApiListQuery query) {

	// if (sessionContext.getAccountId() == null) {
	// return BaseOutput.failure("未登陆用户");
	// }

	// return this.checkinOutRecordService.listPagedData(query,
	// sessionContext.getAccountId());

	// }
}