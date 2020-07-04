package com.dili.trace.api.client;

import java.util.List;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.TradeDetailInputDto;
import com.dili.trace.api.output.CheckInApiDetailOutput;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.TradeDetailInputWrapperDto;
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
@Api(value = "/api/client/clientTradeDetail")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/client/clientTradeDetail")
public class ClientTradeDetailApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientTradeDetailApi.class);
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
	public BaseOutput<BasePage<TradeDetail>> listPage(@RequestBody TradeDetailInputDto condition) {

		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			if(!userId.equals(condition.getBuyerId())&&!userId.equals(condition.getSellerId())){
				return BaseOutput.failure("参数错误");
			}
			BasePage<TradeDetail> page = this.tradeDetailService.listPageByExample(condition);
	
			return BaseOutput.success().setData(page);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
		
	}

	/**
	 * 发起退货
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/doReturning.api", method = { RequestMethod.POST })
	public BaseOutput<CheckInApiDetailOutput> doReturning(@RequestBody TradeDetailInputWrapperDto inputDto) {
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			List<Long> idList = this.tradeDetailService.doReturning(inputDto, userId);
			return BaseOutput.success().setData(idList);
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
	public BaseOutput<CheckInApiDetailOutput> handleReturning(@RequestBody TradeDetailInputWrapperDto inputDto) {

		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			List<Long> idList = this.tradeDetailService.handleReturning(inputDto, userId);
			return BaseOutput.success().setData(idList);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}


}