package com.dili.sg.trace.api.commission;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.sg.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.sg.trace.api.input.CommissionBillInputDto;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dili.sg.common.entity.TraceSessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.sg.trace.domain.RegisterBill;
import com.dili.sg.trace.domain.User;
import com.dili.sg.trace.dto.CreateListBillParam;
import com.dili.sg.trace.dto.RegisterBillDto;
import com.dili.sg.trace.dto.RegisterBillOutputDto;
import com.dili.common.exception.TraceBizException;
import com.dili.sg.trace.glossary.BillTypeEnum;
import com.dili.sg.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.sg.trace.glossary.UserTypeEnum;
import com.dili.sg.trace.service.BillService;
import com.dili.sg.trace.service.CommissionBillService;
import com.dili.sg.trace.service.UserService;
import com.dili.sg.trace.util.BeanMapUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;

/**
 * 委托单查询接口
 */
@RestController
@RequestMapping(value = "/api/commission/commissionBillApi")
@InterceptConfiguration
public class CommissionBillApi {
	private static final Logger logger = LoggerFactory.getLogger(CommissionBillApi.class);
	@Autowired
	CommissionBillService commissionBillService;
	@Autowired
	BillService billService;
	@Autowired
	UserService userService;
	@Autowired
	TraceSessionContext sessionContext;

	/**
	 * 通过小程序接口，用户创建委托单
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/createCommissionBill.api", method = RequestMethod.POST)
	public BaseOutput<?> createCommissionBill(@RequestBody CreateListBillParam createListBillParam) {
		
		try {
			Long userId=sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			if(!UserTypeEnum.COMMISSION_USER.equalsToCode(this.userService.get(userId).getUserType())) {
				return BaseOutput.failure("您不是场外用户");
			}
			
			List<RegisterBill> inputList= StreamEx.ofNullable(createListBillParam).filter(Objects::nonNull).map(CreateListBillParam::getRegisterBills).nonNull().flatCollection(Function.identity()).map(bill -> {
				bill.setCreationSource(RegisterBilCreationSourceEnum.WX.getCode());
				bill.setUserId(userId);
				return bill;
			}).toList();
			if(inputList.isEmpty()) {
				return BaseOutput.failure("参数错误");
			}
			List<RegisterBill> list = this.commissionBillService.createCommissionBillByUser(inputList);
			return BaseOutput.success();
		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 获取登记单列表
	 * @param input
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "获取登记单列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/list.api", method = RequestMethod.POST)
	public BaseOutput<EasyuiPageOutput> list(@RequestBody RegisterBillDto input) throws Exception {
		
		try {

			RegisterBillDto registerBill=BeanMapUtil.trimBean(input);
			logger.info("获取登记单列表:{}", JSON.toJSON(registerBill).toString());
			
			Long userId=sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			if(!UserTypeEnum.COMMISSION_USER.equalsToCode(this.userService.get(userId).getUserType())) {
				return BaseOutput.failure("您不是场外用户");
			}
			
			registerBill.setUserId(userId);
			registerBill.setBillType(BillTypeEnum.COMMISSION_BILL.getCode());
			if (StringUtils.isBlank(registerBill.getOrder())) {
				registerBill.setOrder("desc");
				registerBill.setSort("id");
			}
			EasyuiPageOutput easyuiPageOutput = billService.listEasyuiPageByExample(registerBill, true);
			return BaseOutput.success().setData(easyuiPageOutput);
		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
		
	}

	/**
	 * 通过登记单ID获取登记单详细信息
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "通过登记单ID获取登记单详细信息")
	@RequestMapping(value = "/detail.api", method = RequestMethod.POST)
	public BaseOutput<RegisterBillOutputDto> detail(@RequestBody CommissionBillInputDto input) {
		
		try {

			Long userId=sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			User user = userService.get(userId);
			if(!UserTypeEnum.COMMISSION_USER.equalsToCode(user.getUserType())) {
				return BaseOutput.failure("您不是场外用户");
			}

			logger.info("获取登记单详情:{}", input.getBillId());
			RegisterBill registerBill = billService.get(input.getBillId());
			if (registerBill == null) {
				return BaseOutput.failure("数据不存在");
			}
			RegisterBillOutputDto outdto = new RegisterBillOutputDto();
			try {
				BeanUtils.copyProperties(outdto, registerBill);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.error(e.getMessage(), e);
				return BaseOutput.failure("服务端出错");
			}

			return BaseOutput.success().setData(outdto);
		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
		
		
	}

}
