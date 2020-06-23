package com.dili.trace.api.client;

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
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.output.CheckInApiDetailOutput;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.TradeDetailInputWrapperDto;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import io.swagger.annotations.Api;
import tk.mybatis.mapper.code.IdentityDialect;

@SuppressWarnings("deprecation")
@Api(value = "/api/client/clientCheckinRecord")
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
	@RequestMapping(value = "/listPage.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<BasePage<TradeDetail>> listPage(@RequestBody TradeDetail condition) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		condition.setBuyerId(sessionContext.getAccountId());

		BasePage<TradeDetail> page = this.tradeDetailService.listPageByExample(condition);

		return BaseOutput.success().setData(page);
	}

	/**
	 * 分页查询需要被进场查询的信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/createTradeDetail.api", method = { RequestMethod.POST })
	public BaseOutput<CheckInApiDetailOutput> createTradeDetail(@RequestBody TradeDetailInputWrapperDto inputDto) {
		if (sessionContext.getAccountId() == null) {
			return BaseOutput.failure("未登陆用户");
		}
		try {
			Long sellerId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			List<Long> idList = this.tradeDetailService.createTradeList(inputDto, sellerId);
			return BaseOutput.success().setData(idList);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

}