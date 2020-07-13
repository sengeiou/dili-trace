package com.dili.trace.api.client;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.dili.trace.api.output.BrandOutputDto;
import com.dili.trace.domain.RegisterBillHistory;
import com.dili.trace.service.RegisterBillHistoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * Created by wangguofeng
 */
@SuppressWarnings("deprecation")
@RestController
@RequestMapping(value = "/api/client/clientRegisterBillHistoryApi")
@Api(value = "/api/client/clientRegisterBillHistoryApi", description = "历史相关接口")
@InterceptConfiguration
public class ClientRegisterBillHistoryApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientRegisterBillHistoryApi.class);

	@Autowired
	private LoginSessionContext sessionContext;
	@Autowired
	private RegisterBillHistoryService billHistoryService;

	@ApiOperation(value = "获取报备单历史列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
	public BaseOutput<List<BrandOutputDto>> listPage(@RequestBody RegisterBillHistory inputDto) {
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			logger.info("获取报备单历史列表操作用户:{}", userId);
			inputDto.setUserId(userId);
			if (StringUtils.isBlank(inputDto.getOrder())) {
				inputDto.setOrder("desc");
				inputDto.setSort("created");
			}
			BasePage<RegisterBillHistory> page = this.billHistoryService.listPageByExample(inputDto);
			return BaseOutput.success().setData(page);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}
}
