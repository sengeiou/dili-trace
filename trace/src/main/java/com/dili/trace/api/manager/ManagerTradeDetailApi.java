package com.dili.trace.api.manager;

import java.util.List;

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
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.dto.TradeDetailBillOutputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.User;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.UserService;
import com.dili.trace.util.BasePageUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;

@RestController
@RequestMapping(value = "/api/manager/manageTradeDetail")
@Api(value = "/api/manager/manageTradeDetail", description = "登记单相关接口")
@InterceptConfiguration
public class ManagerTradeDetailApi {
	private static final Logger logger = LoggerFactory.getLogger(ManagerTradeDetailApi.class);
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	UserService userService;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	TradeDetailService tradeDetailService;

	@ApiOperation(value = "获取登记单列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/listAvailableCheckInTradeDetail", method = RequestMethod.POST)
	public BaseOutput<BasePage<TradeDetail>> listAvailableCheckInTradeDetail(@RequestBody TradeDetail input)
			throws Exception {

		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		TradeDetail query = new TradeDetail();
		BasePage<TradeDetail> page = this.tradeDetailService.listPageByExample(query);
		List<TradeDetailBillOutputDto> list = StreamEx.of(page.getDatas()).mapToEntry(tradeDetail -> {
			return this.registerBillService.get(tradeDetail.getBillId());

		}).map(e -> {
			TradeDetail tradeDetailItem = e.getKey();
			RegisterBill registerBillItem = e.getValue();
			TradeDetailBillOutputDto dto = new TradeDetailBillOutputDto();
			dto.setBillId(registerBillItem.getId());
			dto.setTradeDetailId(tradeDetailItem.getId());
			dto.setProductName(registerBillItem.getProductName());
			return dto;

		}).toList();

		return BaseOutput.success().setData(BasePageUtil.convert(list, page));
	}

	@ApiOperation(value = "获取登记单列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/listAvailableCheckOutTradeDetail", method = RequestMethod.POST)
	public BaseOutput<BasePage<TradeDetail>> listAvailableCheckOutTradeDetail(@RequestBody TradeDetail input)
			throws Exception {
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		TradeDetail query = new TradeDetail();
		BasePage<TradeDetail> page = this.tradeDetailService.listPageByExample(query);
		List<TradeDetailBillOutputDto> list = StreamEx.of(page.getDatas()).mapToEntry(tradeDetail -> {
			return this.registerBillService.get(tradeDetail.getBillId());

		}).map(e -> {
			TradeDetail tradeDetailItem = e.getKey();
			RegisterBill registerBillItem = e.getValue();
			TradeDetailBillOutputDto dto = new TradeDetailBillOutputDto();
			dto.setBillId(registerBillItem.getId());
			dto.setTradeDetailId(tradeDetailItem.getId());
			dto.setProductName(registerBillItem.getProductName());
			return dto;

		}).toList();

		return BaseOutput.success().setData(BasePageUtil.convert(list, page));
	}
}