package com.dili.trace.api.client;

import java.util.ArrayList;
import java.util.List;
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
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.domain.User;
import com.dili.trace.service.BatchStockService;
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

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listPage.api", method = { RequestMethod.POST })
	public BaseOutput<BasePage<TradeRequest>> listPage(@RequestBody TradeRequestInputDto query) {
		if (query == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			query.setBuyerId(userId);
			query.setSellerId(userId);
			BasePage<TradeRequest> page = this.tradeRequestService.listPageTradeRequestByBuyerIdOrSellerId(query);
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
			TradeRequest tradeRequestItem = this.tradeRequestService.get(inputDto.getTradeRequestId());
			if (tradeRequestItem == null) {
				return BaseOutput.failure("没有查找到详情");
			}

			TraceDetailOutputDto traceDetailOutputDto = new TraceDetailOutputDto();
			traceDetailOutputDto.setTradeRequestId(tradeRequestItem.getTradeRequestId());
			traceDetailOutputDto.setCreated(tradeRequestItem.getCreated());
			if (tradeRequestItem.getBuyerId().equals(userId)) {

				User seller = this.userService.get(tradeRequestItem.getSellerId());
				// BatchStock
				// batchStock=this.batchStockService.get(tradeRequestItem.getBatchStockId());
				TraceDataDto upTrace = new TraceDataDto();
				upTrace.setCreated(tradeRequestItem.getCreated());
				upTrace.setBuyerName(tradeRequestItem.getBuyerName());
				upTrace.setSellerName(tradeRequestItem.getSellerName());
				upTrace.setMarketName(seller.getMarketName());
				upTrace.setTallyAreaNo(seller.getTallyAreaNos());

				TradeDetail tradeDetailQuery = new TradeDetail();
				tradeDetailQuery.setBuyerId(userId);
				tradeDetailQuery.setTradeRequestId(tradeRequestItem.getId());
				List<Long> parentIdList = StreamEx.of(this.tradeDetailService.listByExample(tradeDetailQuery))
						.map(TradeDetail::getId).toList();
				List<TradeDetail> buyerTradeDetailList = this.tradeDetailService
						.findTradeDetailByParentIdList(parentIdList);

				List<TraceDataDto> downTraceList = StreamEx.of(buyerTradeDetailList).map(TradeDetail::getTradeRequestId)
						.distinct().map(requestId -> {

							TradeRequest tr = this.tradeRequestService.get(requestId);
							User buyer = this.userService.get(tr.getBuyerId());
							TraceDataDto downTrace = new TraceDataDto();
							downTrace.setCreated(tr.getCreated());
							downTrace.setBuyerName(tr.getBuyerName());
							downTrace.setSellerName(tr.getSellerName());
							downTrace.setMarketName(buyer.getMarketName());
							downTrace.setTallyAreaNo(buyer.getTallyAreaNos());
							return downTrace;
						}).toList();
				traceDetailOutputDto.setUpTraceList(Lists.newArrayList(upTrace));
				traceDetailOutputDto.setDownTraceList(downTraceList);
			} else if (tradeRequestItem.getSellerId().equals(userId)) {

				TradeDetail condition = new TradeDetail();
				condition.setSellerId(userId);
				condition.setTradeRequestId(tradeRequestItem.getId());
				List<Long> upTradeDetailIdList = StreamEx.of(this.tradeDetailService.listByExample(condition))
						.map(TradeDetail::getParentId).nonNull().distinct().toList();
				List<TraceDataDto> upTraceList = StreamEx
						.of(this.tradeDetailService.findTradeDetailByIdList(upTradeDetailIdList))
						.map(TradeDetail::getTradeRequestId).distinct().map(requestId -> {

							TradeRequest tr = this.tradeRequestService.get(requestId);
							User buyer = this.userService.get(tr.getBuyerId());
							TraceDataDto downTrace = new TraceDataDto();
							downTrace.setCreated(tr.getCreated());
							downTrace.setBuyerName(tr.getBuyerName());
							downTrace.setSellerName(tr.getSellerName());
							downTrace.setMarketName(buyer.getMarketName());
							downTrace.setTallyAreaNo(buyer.getTallyAreaNos());
							return downTrace;
						}).toList();

				User seller = this.userService.get(tradeRequestItem.getSellerId());
				// BatchStock
				// batchStock=this.batchStockService.get(tradeRequestItem.getBatchStockId());
				TraceDataDto upTrace = new TraceDataDto();
				upTrace.setCreated(tradeRequestItem.getCreated());
				upTrace.setBuyerName(tradeRequestItem.getBuyerName());
				upTrace.setSellerName(tradeRequestItem.getSellerName());
				upTrace.setMarketName(seller.getMarketName());
				upTrace.setTallyAreaNo(seller.getTallyAreaNos());

				TradeDetail tradeDetailQuery = new TradeDetail();
				tradeDetailQuery.setBuyerId(userId);
				tradeDetailQuery.setTradeRequestId(tradeRequestItem.getId());
				List<Long> parentIdList = StreamEx.of(this.tradeDetailService.listByExample(tradeDetailQuery))
						.map(TradeDetail::getId).toList();
				List<TradeDetail> buyerTradeDetailList = this.tradeDetailService
						.findTradeDetailByParentIdList(parentIdList);

				List<TraceDataDto> downTraceList = StreamEx.of(buyerTradeDetailList).map(TradeDetail::getTradeRequestId)
						.distinct().map(requestId -> {

							TradeRequest tr = this.tradeRequestService.get(requestId);
							User buyer = this.userService.get(tr.getBuyerId());
							TraceDataDto downTrace = new TraceDataDto();
							downTrace.setCreated(tr.getCreated());
							downTrace.setBuyerName(tr.getBuyerName());
							downTrace.setSellerName(tr.getSellerName());
							downTrace.setMarketName(buyer.getMarketName());
							downTrace.setTallyAreaNo(buyer.getTallyAreaNos());
							return downTrace;
						}).toList();
				traceDetailOutputDto.setUpTraceList(upTraceList);
				traceDetailOutputDto.setDownTraceList(downTraceList);
			} else {
				return BaseOutput.failure("没有查询到数据");
			}
			BatchStock batchStockItem = this.batchStockService.get(tradeRequestItem.getBatchStockId());
			traceDetailOutputDto.setBrandName(batchStockItem.getBrandName());
			traceDetailOutputDto.setSpecName(batchStockItem.getSpecName());
			traceDetailOutputDto.setProductName(batchStockItem.getProductName());
			TradeDetail query = new TradeDetail();
			query.setTradeRequestId(tradeRequestItem.getId());
			List<TradeDetail> traceList = this.tradeDetailService.listByExample(query);
			List<Long> billIdList = StreamEx.of(traceList).map(TradeDetail::getBillId).distinct().toList();
			List<ImageCert> imageCertList = this.imageCertService.findImageCertListByBillIdList(billIdList);
			traceDetailOutputDto.setImageCertList(imageCertList);

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
			TradeRequest tradeRequestItem = this.tradeRequestService.get(inputDto.getTradeRequestId());
			if (tradeRequestItem == null) {
				return BaseOutput.failure("没有查找到详情");
			}
			TradeDetail tradeDetail = new TradeDetail();
			tradeDetail.setTradeRequestId(tradeRequestItem.getTradeRequestId());
			List<TradeDetail> list = this.tradeDetailService.listByExample(tradeDetail);
			return BaseOutput.success().setData(list);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

}