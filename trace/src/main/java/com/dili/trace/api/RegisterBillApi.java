package com.dili.trace.api;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.User;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/bill")
@Api(value = "/api/bill", description = "登记单相关接口")
@InterceptConfiguration
public class RegisterBillApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillApi.class);
	@Autowired
	private RegisterBillService registerBillService;
	@Autowired
	private SeparateSalesRecordService separateSalesRecordService;
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	UserService userService;


	@Autowired
	UpStreamService upStreamService;

	

	@ApiOperation(value = "获取登记单列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	// @InterceptConfiguration(loginRequired=false)
	public BaseOutput<EasyuiPageOutput> list(@RequestBody RegisterBillDto registerBill) throws Exception {
		LOGGER.info("获取登记单列表:" + JSON.toJSON(registerBill).toString());
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		LOGGER.info("获取登记单列表 操作用户:" + JSON.toJSONString(user));
		registerBill.setUserId(user.getId());
		if (StringUtils.isBlank(registerBill.getOrder())) {
			registerBill.setOrder("desc");
			registerBill.setSort("id");
		}
		EasyuiPageOutput easyuiPageOutput = registerBillService.listEasyuiPageByExample(registerBill, true);
		return BaseOutput.success().setData(easyuiPageOutput);
	}

	
}
