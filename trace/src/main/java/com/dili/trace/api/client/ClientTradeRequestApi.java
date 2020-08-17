package com.dili.trace.api.client;

import java.util.List;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.TradeHistoryOutPutDto;
import com.dili.trace.api.input.TradeRequestHandleDto;
import com.dili.trace.api.input.TradeRequestInputDto;
import com.dili.trace.api.input.TradeRequestListInput;
import com.dili.trace.api.output.CheckInApiDetailOutput;
import com.dili.trace.api.output.TradeRequestOutputDto;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.enums.TradeOrderStatusEnum;
import com.dili.trace.enums.TradeReturnStatusEnum;
import com.dili.trace.service.*;

import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Example;

@SuppressWarnings("deprecation")
@Api(value = "/api/client/clientTradeRequestApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/client/clientTradeRequestApi")
public class ClientTradeRequestApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientTradeDetailApi.class);
	@Autowired
	private UserService userService;
	@Autowired
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
	TradeOrderService tradeOrderService;

	@Autowired
	ProductStockService productStockService;


	/**
	 * 查询交易请求列表
	 * @param condition
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listPage.api", method = { RequestMethod.POST })
	public BaseOutput<BasePage<TradeRequest>> listPage(@RequestBody TradeRequestListInput condition) {

		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			if (condition.getBuyerId() == null && condition.getSellerId() == null) {
				return BaseOutput.failure("参数错误");
			}
			if (!sessionContext.getAccountId().equals(condition.getBuyerId())
					&& !sessionContext.getAccountId().equals(condition.getSellerId())) {
				return BaseOutput.failure("参数错误");
			}
			condition.setSort("created");
			condition.setOrder("desc");
			BasePage<TradeRequest> page = this.tradeRequestService.listPageByExample(condition);
			List<TradeRequest> data = page.getDatas();
			StreamEx.of(data).nonNull().forEach(td -> {
				TradeOrder tradeOrder = this.tradeOrderService.get(td.getTradeOrderId());
				td.setOrderStatusName(TradeOrderStatusEnum.fromCode(tradeOrder.getOrderStatus()).get().getName());
			});
			return BaseOutput.success().setData(page);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 查询交易详情(包括交易批次)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewTradeDetail.api", method = { RequestMethod.POST })
	public BaseOutput<CheckInApiDetailOutput> viewTradeDetail(@RequestBody TradeRequestInputDto inputDto) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		if (inputDto == null || inputDto.getTradeRequestId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();

			TradeRequest tradeRequestItem = this.tradeRequestService.get(inputDto.getTradeRequestId());
			if (tradeRequestItem == null) {
				return BaseOutput.failure("数据不存在");
			}
			if (!tradeRequestItem.getBuyerId().equals(userId) && !tradeRequestItem.getSellerId().equals(userId)) {
				return BaseOutput.failure("没有权限查看数据");
			}
			TradeRequestOutputDto out = new TradeRequestOutputDto();
			TradeDetail tradeDetailQuery = new TradeDetail();
			tradeDetailQuery.setTradeRequestId(tradeRequestItem.getId());
			List<TradeDetail> tradeDetailList = this.tradeDetailService.listByExample(tradeDetailQuery);
			out.setTradeRequest(tradeRequestItem);
			out.setTradeDetailList(tradeDetailList);
			return BaseOutput.success().setData(out);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	@ApiOperation(value = "创建购买请求")
	@RequestMapping(value = "/createBuyProductRequest.api", method = RequestMethod.POST)
	public BaseOutput<?> createBuyProductRequest(@RequestBody TradeRequestInputDto inputDto) {
		if (inputDto == null || inputDto.getBatchStockList() == null || inputDto.getBatchStockList().isEmpty()) {
			return BaseOutput.failure("参数错误");
		}

		try {
			Long buyerId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			inputDto.setBuyerId(buyerId);
			List<TradeRequest> list = this.tradeRequestService.createBuyRequest(buyerId, inputDto.getBatchStockList());
			return BaseOutput.success();
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

	@ApiOperation(value = "创建销售请求")
	@RequestMapping(value = "/createSellProductRequest.api", method = RequestMethod.POST)
	public BaseOutput<?> createSellProductRequest(@RequestBody TradeRequestListInput inputDto) {
		if (inputDto == null || inputDto.getBuyerId() == null || inputDto.getBatchStockList() == null
				|| inputDto.getBatchStockList().isEmpty()) {
			return BaseOutput.failure("参数错误");
		}

		try {
			Long sellerId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			logger.info("seller id in session:{}",sellerId);
			List<TradeRequest> list = this.tradeRequestService.createSellRequest(sellerId, inputDto.getBuyerId(),
					inputDto.getBatchStockList());
			return BaseOutput.success();
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

	@ApiOperation(value = "通过订单ID查询交易请求详情")
	@RequestMapping(value = "/listPageTradeRequestByTraderOrderId.api", method = RequestMethod.POST)
	public BaseOutput<?> listPageTradeRequestByTraderOrderId(@RequestBody TradeRequestInputDto inputDto) {
		if (inputDto == null || inputDto.getTraderOrderId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			Long sellerId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			TradeRequest request=new TradeRequest();
			request.setTradeOrderId(inputDto.getTraderOrderId());
			return BaseOutput.success().setData(this.tradeRequestService.listPageByExample(request));
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

	/**
	 * 发起退货
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/createReturning.api", method = { RequestMethod.POST })
	public BaseOutput<Long> createReturning(@RequestBody TradeRequestInputDto inputDto) {
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			Long id = this.tradeRequestService.createReturning(inputDto.getTradeRequestId(), userId);
			return BaseOutput.success().setData(id);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 确认退货
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/handleReturning.api", method = { RequestMethod.POST })
	public BaseOutput<Long> handleReturning(@RequestBody TradeRequestInputDto inputDto) {

		try {
			TradeReturnStatusEnum returnStatus = TradeReturnStatusEnum.fromCode(inputDto.getReturnStatus())
					.orElse(null);
			if (returnStatus == null || inputDto.getTradeRequestId() == null) {
				return BaseOutput.failure("参数错误");
			}
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			Long id = this.tradeRequestService.handleReturning(inputDto.getTradeRequestId(), userId, returnStatus,
					inputDto.getReason());
			return BaseOutput.success().setData(id);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	@RequestMapping(value = "/handleBuyerRquest.api", method = { RequestMethod.POST })
	public BaseOutput handleBuyRequest(@RequestBody TradeRequestHandleDto inputDto) {
		try{
			this.tradeRequestService.handleBuyerRequest(inputDto);
			return BaseOutput.success();
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}

	@RequestMapping(value = "/listBuyHistory.api", method = { RequestMethod.GET })
	public BaseOutput<List<TradeHistoryOutPutDto>> listBuyHistory(@RequestParam Long buyerId,
					@RequestParam String queryCondition) {
		try {
			List<TradeHistoryOutPutDto> list = this.tradeRequestService.queryTradeSellerHistoryList(buyerId, queryCondition);
			return BaseOutput.success().setData(list);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}

	@RequestMapping(value = "/listSaleableProduct.api", method = { RequestMethod.GET })
	public BaseOutput<List<ProductStock>> listSaleableProduct(@RequestParam Long userId) {
		try {
			ProductStock productStock = new ProductStock();
			productStock.setUserId(userId);
			productStock.setMetadata(IDTO.AND_CONDITION_EXPR, "and stock_weight > 0");
			List<ProductStock> list = this.productStockService.list(productStock);
			return BaseOutput.success().setData(list);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}


}