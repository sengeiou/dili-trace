package com.dili.trace.api.client;

import java.util.List;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.output.TraceDetailOutputDto;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.TradeDetailService;
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

@SuppressWarnings("deprecation")
@Api(value = "/api/client/clientTrace")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/client/clientTrace")
public class ClientTraceApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientTraceApi.class);
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

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/listPage.api", method = { RequestMethod.POST})
	public BaseOutput<BasePage<TradeDetail>> listPage(@RequestBody TradeDetail query) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		TradeDetail condition = new TradeDetail();
		condition.setBuyerId(sessionContext.getAccountId());
		condition.setPage(query.getPage());
		condition.setRows(query.getRows());

		BasePage<TradeDetail> page = this.tradeDetailService.listPageByExample(condition);

		return BaseOutput.success().setData(page);
	}

	/**
	 * 分页查询需要被进场查询的信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewTrace.api", method = { RequestMethod.POST})
	public BaseOutput<TraceDetailOutputDto> viewTrace(@RequestBody TradeDetail query) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		if (query == null || query.getId() == null) {
			return BaseOutput.failure("参数错误");
		}

		TradeDetail tradeDetailItem = this.tradeDetailService.get(query.getId());
		if (tradeDetailItem == null) {
			return BaseOutput.failure("没有查找到详情");
		}
		TraceDetailOutputDto traceDetailOutputDto = new TraceDetailOutputDto();
		traceDetailOutputDto.setTraceItem(tradeDetailItem);
		if (tradeDetailItem.getParentId() != null) {
			TradeDetail parentTradeDetailItem = this.tradeDetailService.get(tradeDetailItem.getParentId());
			traceDetailOutputDto.setTraceUp(parentTradeDetailItem);
		} else {
			traceDetailOutputDto.setTraceUp(null);
		}
		TradeDetail condition = new TradeDetail();
		condition.setParentId(tradeDetailItem.getId());
		List<TradeDetail> childrenTradeDetail = this.tradeDetailService.listByExample(condition);
		traceDetailOutputDto.setTraceDownList(childrenTradeDetail);
		return BaseOutput.success().setData(traceDetailOutputDto);

	}
}