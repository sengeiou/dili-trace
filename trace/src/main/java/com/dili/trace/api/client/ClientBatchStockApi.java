package com.dili.trace.api.client;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.BatchStockInput;
import com.dili.trace.api.input.BatchStockQueryDto;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.Brand;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.service.BatchStockService;
import com.dili.trace.service.BrandService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.TradeRequestService;
import com.dili.trace.service.UserService;
import com.dili.trace.util.BasePageUtil;

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
	@Autowired
	BrandService brandService;

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listMyBatchStock.api", method = { RequestMethod.POST })
	public BaseOutput<BasePage<BatchStock>> listMyBatchStock(@RequestBody BatchStockQueryDto condition) {
		try {
			Long userId = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			condition.setUserId(userId);
			condition.setSort("created");
			condition.setOrder("desc");
			condition.setMinTradeDetailNum(1);
			BasePage<BatchStock> page = this.batchStockService.listPageByExample(condition);
			return BaseOutput.success().setData(page);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询出错");
		}

	}

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listSellersBatchStock.api", method = { RequestMethod.POST })
	public BaseOutput<BasePage<BatchStock>> listSellersBatchStock(@RequestBody BatchStockQueryDto condition) {
		if (condition == null || condition.getUserId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			Long userId = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			condition.setSort("created");
			condition.setOrder("desc");
			condition.setMinTradeDetailNum(1);
			BasePage<BatchStock> page = this.batchStockService.listPageByExample(condition);
			return BaseOutput.success().setData(page);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询出错");
		}
	}

	/**
	 * 获得批次列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listTradeDetailForSaleByBatchId.api", method = { RequestMethod.POST })
	public BaseOutput<List<TradeDetail>> listTradeDetailForSaleByBatchId(@RequestBody BatchStockInput inputDto) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		if (inputDto == null || inputDto.getBatchStockId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			BatchStock batchStockItem = this.batchStockService.get(inputDto.getBatchStockId());
			if (batchStockItem == null) {
				return BaseOutput.failure("数据不存在");
			}
			TradeDetail tradeDetailQuery = new TradeDetail();
			tradeDetailQuery.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
			tradeDetailQuery.setBatchStockId(inputDto.getBatchStockId());
			List<TradeDetail> tradeDetailList = this.tradeDetailService.listByExample(tradeDetailQuery);
			return BaseOutput.success().setData(tradeDetailList);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	

}