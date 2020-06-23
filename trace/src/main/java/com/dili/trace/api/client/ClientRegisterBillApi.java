package com.dili.trace.api.client;

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
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.input.RegisterBillApiInputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillInputDto;
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
 * Created by wangguofeng
 */
@RestController
@RequestMapping(value = "/api/client/clientRegisterBill")
@Api(value = "/api/client/clientRegisterBill", description = "登记单相关接口")
@InterceptConfiguration
public class ClientRegisterBillApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientRegisterBillApi.class);
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

	@ApiOperation("保存多个登记单")
	@RequestMapping(value = "/createRegisterBillList.api", method = RequestMethod.POST)
	public BaseOutput createRegisterBillList(@RequestBody CreateListBillParam createListBillParam) {
		logger.info("保存多个登记单:");

		OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);

		User user = userService.get(operatorUser.getId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		List<CreateRegisterBillInputDto> registerBills = createListBillParam.getRegisterBills();
		if (registerBills == null) {
			return BaseOutput.failure("没有登记单");
		}
		logger.info("保存多个登记单 操作用户:" + JSON.toJSONString(user));
		for (CreateRegisterBillInputDto dto : registerBills) {
			logger.info("循环保存登记单:" + JSON.toJSONString(dto));
			RegisterBill registerBill = new RegisterBill();
			registerBill.setOperatorName(user.getName());
			registerBill.setOperatorId(user.getId());
			registerBill.setUserId(user.getId());
			registerBill.setName(user.getName());
			registerBill.setAddr(user.getAddr());
			registerBill.setIdCardNo(user.getCardNo());
			registerBill.setWeight(dto.getWeight());
			registerBill.setWeightUnit(dto.getWeightUnit());
			registerBill.setOriginId(dto.getOriginId());
			registerBill.setOriginName(dto.getOriginName());
			registerBill.setProductId(dto.getProductId());
			registerBill.setProductName(dto.getProductName());
//			if (registerBill.getRegisterSource() == null) {
//				// 小程序默认理货区
//				registerBill.setRegisterSource(RegisterSourceEnum.TALLY_AREA.getCode());
//			}
//			if (registerBill.getRegisterSource().equals(RegisterSourceEnum.TALLY_AREA.getCode())) {
//				registerBill.setTallyAreaNo(user.getTallyAreaNos());
//			}
			try {
				registerBillService.createRegisterBill(registerBill, dto.getImageCertList(), operatorUser);
			} catch (TraceBusinessException e) {
				return BaseOutput.failure(e.getMessage());
			}

		}
		return BaseOutput.success();
	}

	@ApiOperation(value = "获取登记单列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
	public BaseOutput<BasePage<RegisterBill>> listPage(@RequestBody RegisterBillDto registerBill) throws Exception {
		logger.info("获取登记单列表:" + JSON.toJSON(registerBill).toString());
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		logger.info("获取登记单列表 操作用户:" + JSON.toJSONString(user));
		registerBill.setUserId(user.getId());
		if (StringUtils.isBlank(registerBill.getOrder())) {
			registerBill.setOrder("desc");
			registerBill.setSort("id");
		}
		BasePage<RegisterBill> basePage = registerBillService.listPageByExample(registerBill);
		return BaseOutput.success().setData(basePage);
	}

	@ApiOperation(value = "通过登记单ID获取登记单详细信息")
	@RequestMapping(value = "/viewRegisterBill.api", method = RequestMethod.POST)
	public BaseOutput<RegisterBillOutputDto> viewRegisterBill(@RequestBody RegisterBillApiInputDto inputDto) {
		if(inputDto==null||inputDto.getBillId()==null) {
			return BaseOutput.failure("参数错误");
		}
		logger.info("获取登记单:" + inputDto.getBillId());
		User user = userService.get(sessionContext.getAccountId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}
		RegisterBill registerBill = registerBillService.get(inputDto.getBillId());
		if (registerBill == null) {
			logger.error("获取登记单失败id:" + inputDto.getBillId());
			return BaseOutput.failure();
		}
		RegisterBillOutputDto bill = registerBillService.conversionDetailOutput(registerBill);

		return BaseOutput.success().setData(bill);
	}


}
