package com.dili.trace.api.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.TradeRequestInputDto;
import com.dili.trace.api.output.TraceDataDto;
import com.dili.trace.api.output.TraceDetailOutputDto;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.domain.User;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.service.BatchStockService;
import com.dili.trace.service.BillTraceService;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.TradeRequestService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;
import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import one.util.streamex.StreamEx;

@SuppressWarnings("deprecation")
@Api(value = "/api/client/clientBillTraceApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/client/clientBillTraceApi")
public class ClientBillTraceApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientBillTraceApi.class);
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
	@Autowired
	TradeDetailService tradeDetailService;
	@Autowired
	TradeRequestService tradeRequestService;
	@Autowired
	BatchStockService batchStockService;
	@Autowired
	ImageCertService imageCertService;
	@Autowired
	BillTraceService billTraceService;

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listPage.api", method = { RequestMethod.POST })
	public BaseOutput<BasePage<TradeRequest>> listPage(@RequestBody TradeRequestInputDto query) {
		if (query == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			BasePage<TradeRequest> page = this.tradeRequestService.listPageTradeRequestByBuyerIdOrSellerId(query,userId);
			return BaseOutput.success().setData(page);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

	/**
	 * 查询需要被进场查询的信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewBillTrace.api", method = { RequestMethod.POST })
	public BaseOutput<TraceDetailOutputDto> viewBillTrace(@RequestBody TradeRequestInputDto inputDto) {
		if (inputDto == null || inputDto.getTradeRequestId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			TraceDetailOutputDto traceDetailOutputDto = this.billTraceService
					.viewBillTrace(inputDto.getTradeRequestId(), userId);
			return BaseOutput.success().setData(traceDetailOutputDto);

		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

	/**
	 * 查询需要被进场查询的信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewTradeDetailList.api", method = { RequestMethod.POST })
	public BaseOutput<List<TradeDetail>> viewTradeDetailList(@RequestBody TradeRequestInputDto inputDto) {

		if (inputDto == null || inputDto.getTradeRequestId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			List<TradeDetail> list = this.billTraceService.viewTradeDetailList(inputDto.getTradeRequestId(), userId);
			return BaseOutput.success().setData(list);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

}