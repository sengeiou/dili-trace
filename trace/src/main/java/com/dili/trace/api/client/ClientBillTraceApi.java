package com.dili.trace.api.client;

import java.util.List;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.output.TraceDetailOutputDto;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.TradeDetailInputDto;
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

	@SuppressWarnings({ "unchecked" })
	// @RequestMapping(value = "/listPage.api", method = { RequestMethod.POST})
	public BaseOutput<BasePage<TradeDetail>> listPage(@RequestBody TradeDetail query) {

		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();

			TradeDetail condition = new TradeDetail();
			condition.setBuyerId(userId);
			condition.setPage(query.getPage());
			condition.setRows(query.getRows());

			BasePage<TradeDetail> page = this.tradeDetailService.listPageByExample(condition);

			return BaseOutput.success().setData(page);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("查询数据出错");
		}

	}

	/**
	 * 分页查询需要被进场查询的信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewBillTrace.api", method = { RequestMethod.POST })
	public BaseOutput<TraceDetailOutputDto> viewBillTrace(@RequestBody TradeDetailInputDto query) {
		if (query == null || query.getTradeDetailId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();

			TradeDetail tradeDetailItem = this.tradeDetailService.get(query.getTradeDetailId());
			if (tradeDetailItem == null) {
				return BaseOutput.failure("没有查找到详情");
			}
			if(!tradeDetailItem.getBuyerId().equals(userId)){
				return BaseOutput.failure("没有权限查看数据详情");
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
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("查询数据出错");
		}

	}
}