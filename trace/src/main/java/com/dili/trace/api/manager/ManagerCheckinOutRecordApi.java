package com.dili.trace.api.manager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.dili.trace.dto.RegisterBillInputDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.enums.TruckTypeEnum;
import com.dili.trace.glossary.TFEnum;
import com.dili.trace.glossary.YnEnum;
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

import cn.hutool.core.date.DateUtil;
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
		if (input == null || input.getBillIdList() == null) {
			return BaseOutput.failure("参数错误");
		}
		List<Long> billIdList = StreamEx.of(input.getBillIdList()).nonNull().toList();
		if (billIdList.isEmpty()) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);

			RegisterBillDto query = new RegisterBillDto();
			query.setIdList(billIdList);
			query.setIsDeleted(TFEnum.FALSE.getCode());
			StreamEx.of(this.registerBillService.listByExample(query)).forEach(billItem -> {
				if (!BillVerifyStatusEnum.PASSED.equalsToCode(billItem.getVerifyStatus())
						&& !BillVerifyStatusEnum.RETURNED.equalsToCode(billItem.getVerifyStatus())) {
					throw new TraceBusinessException("当前状态不能进行进门操作");
				}
			});

			List<CheckinOutRecord> checkinRecordList = this.checkinOutRecordService
					.doCheckin(Optional.ofNullable(operatorUser), billIdList, CheckinStatusEnum.ALLOWED);
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
	public BaseOutput<Map<Integer, Map<String,List<RegisterBill>>>> listPageCheckInData(@RequestBody RegisterBillDto query) {
		try {
			if (query == null || query.getUserId() == null) {
				return BaseOutput.failure("参数错误");
			}
			Long operatorUserId = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER).getId();
			Map<Integer, Map<String,List<RegisterBill>>> resultMap = this.registerBillService.listPageCheckInData(query);
			return BaseOutput.success().setData(resultMap);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}

}