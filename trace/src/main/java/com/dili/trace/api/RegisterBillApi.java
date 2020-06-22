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
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UserTallyAreaService;

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


	@Autowired
	DetectRecordService detectRecordService;
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	UserService userService;
	@Autowired
	UserTallyAreaService userTallyAreaService;

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

	

	@ApiOperation(value = "通过登记单ID获取登记单详细信息")
	@RequestMapping(value = "id/{id}", method = RequestMethod.GET)
	public BaseOutput<RegisterBillOutputDto> getRegisterBill(@PathVariable Long id) {
		LOGGER.info("获取登记单:" + id);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		RegisterBill registerBill = registerBillService.get(id);
		if (registerBill == null) {
			LOGGER.error("获取登记单失败id:" + id);
			return BaseOutput.failure();
		}
		RegisterBillOutputDto bill = registerBillService.conversionDetailOutput(registerBill);

		return BaseOutput.success().setData(bill);
	}

	@ApiOperation(value = "通过登记单编号获取登记单详细信息")
	@RequestMapping(value = "/code/{code}", method = RequestMethod.GET)
	public BaseOutput<RegisterBillOutputDto> getRegisterBillByCode(@PathVariable String code) {
		LOGGER.info("获取登记单:" + code);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		RegisterBill registerBill = registerBillService.findByCode(code);
		if (registerBill == null) {
			LOGGER.error("获取登记单失败code:" + code);
			return BaseOutput.failure();
		}
		RegisterBillOutputDto bill = registerBillService.conversionDetailOutput(registerBill);
		return BaseOutput.success().setData(bill);
	}

	
	@ApiOperation(value = "通过分销记录ID获取分销单")
	@RequestMapping(value = "/salesRecordId/{salesRecordId}", method = RequestMethod.GET)
	public BaseOutput<SeparateSalesRecord> getSeparateSalesRecord(@PathVariable Long salesRecordId) {
		LOGGER.info("获取分销记录:" + salesRecordId);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		SeparateSalesRecord record = separateSalesRecordService.get(salesRecordId);
		return BaseOutput.success().setData(record);
	}

	@ApiOperation(value = "通过登记单商品名获取登记单", httpMethod = "GET", notes = "productName=?")
	@RequestMapping(value = "/productName/{productName}", method = RequestMethod.GET)
	public BaseOutput<List<RegisterBill>> getBillByProductName(@PathVariable String productName) {
		LOGGER.info("获取登记单&分销记录:" + productName);
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		List<RegisterBill> bills = registerBillService.findByProductName(productName);
		return BaseOutput.success().setData(bills);
	}

	@ApiOperation(value = "通过code删除登记单", httpMethod = "GET", notes = "productName=?")
	@RequestMapping(value = "/deleteRegisterBillByCode/{registerBillCode}", method = RequestMethod.GET)
	public BaseOutput<Object> deleteRegisterBillByCode(@PathVariable String registerBillCode) {
		LOGGER.info("通过code删除登记单:{}", registerBillCode);
		User user = userService.get(sessionContext.getAccountId());
		if (StringUtils.isBlank(registerBillCode)) {
			return BaseOutput.failure("参数错误");
		}
		RegisterBill registerBill = this.registerBillService.findByCode(registerBillCode);
		if (registerBill == null) {
			return BaseOutput.failure("登记单不存在");
		}
		if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(registerBill.getState())) {
			return BaseOutput.failure("不能删除当前状态登记单");
		}
		this.registerBillService.delete(registerBill.getId());
		return BaseOutput.success().setData(true);

	}

	

}
