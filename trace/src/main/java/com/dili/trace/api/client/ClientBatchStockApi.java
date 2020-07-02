package com.dili.trace.api.client;

import java.util.List;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.BatchStockQueryDto;
import com.dili.trace.api.input.TradeRequestWrapperDto;
import com.dili.trace.api.output.CheckInApiDetailOutput;
import com.dili.trace.api.output.TradeRequestOutputDto;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.service.BatchStockService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.TradeRequestService;
import com.dili.trace.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@SuppressWarnings("deprecation")
@Api(value = "/api/client/clientBatchStockApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/client/clientBatchStockApi")
public class ClientBatchStockApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientTradeDetailApi.class);
	@Resource
	private UserService userService;
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	BatchStockService batchStockService;
	@Autowired
	TradeDetailService tradeDetailService;
	@Autowired
	TradeRequestService tradeRequestService;

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listMyBatchStock.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<BasePage<BatchStock>> listMyBatchStock(@RequestBody BatchStockQueryDto condition) {
		try {
			Long userId = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			condition.setUserId(userId);
			BasePage<BatchStock> page = this.batchStockService.listPageByExample(condition);
			return BaseOutput.success().setData(page);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("查询出错");
		}

	}

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listSellersBatchStock.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<BasePage<BatchStock>> listSellersBatchStock(@RequestBody BatchStockQueryDto condition) {
		if (condition == null || condition.getUserId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			Long userId = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			BasePage<BatchStock> page = this.batchStockService.listPageByExample(condition);
			return BaseOutput.success().setData(page);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("查询出错");
		}
	}

	/**
	 * 详细列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listTradeDetailByBatchId.api", method = { RequestMethod.POST })
	public BaseOutput<CheckInApiDetailOutput> listTradeDetailByBatchId(@RequestBody TradeRequestWrapperDto inputDto) {
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

}