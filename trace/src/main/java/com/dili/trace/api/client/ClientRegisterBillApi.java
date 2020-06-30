package com.dili.trace.api.client;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.input.RegisterBillApiInputDto;
import com.dili.trace.api.output.RegisterBillOutput;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;
import com.dili.trace.util.BasePageUtil;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	UserService userService;
	@Autowired
	ImageCertService imageCertService;

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
			RegisterBill registerBill = dto.build(user);
			try {
				registerBillService.createRegisterBill(registerBill, dto.getImageCertList(), operatorUser);
			} catch (TraceBusinessException e) {
				return BaseOutput.failure(e.getMessage());
			}

		}
		return BaseOutput.success();
	}

	@ApiOperation("修改报备单")
	@RequestMapping(value = "/doEditRegisterBill.api", method = RequestMethod.POST)
	public BaseOutput doEditRegisterBill(@RequestBody CreateRegisterBillInputDto dto) {
		logger.info("修改报备单:");
		if (dto == null || dto.getBillId() == null) {
			return BaseOutput.failure("参数错误");
		}
		OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);

		User user = userService.get(operatorUser.getId());
		if (user == null) {
			return BaseOutput.failure("未登陆用户");
		}

		logger.info("保存登记单:" + JSON.toJSONString(dto));
		RegisterBill registerBill = dto.build(user);
		try {
			this.registerBillService.doEdit(registerBill);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
		return BaseOutput.success();
	}

	@ApiOperation(value = "获取登记单列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
	public BaseOutput<BasePage<RegisterBillOutput>> listPage(@RequestBody RegisterBillDto input) {
		logger.info("获取登记单列表:" + JSON.toJSON(input).toString());
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			User user = userService.get(userId);
			if (user == null) {
				return BaseOutput.failure("未登陆用户");
			}
			logger.info("获取登记单列表 操作用户:" + JSON.toJSONString(user));
			input.setUserId(userId);
			if (StringUtils.isBlank(input.getOrder())) {
				input.setOrder("desc");
				input.setSort("id");
			}
			BasePage basePage = BasePageUtil.convert(registerBillService.listPageByExample(input), bill -> {
				return RegisterBillOutput.build(bill);
			});
			return BaseOutput.success().setData(basePage);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

	@ApiOperation(value = "通过登记单ID获取登记单详细信息")
	@RequestMapping(value = "/viewRegisterBill.api", method = RequestMethod.POST)
	public BaseOutput<RegisterBillOutputDto> viewRegisterBill(@RequestBody RegisterBillApiInputDto inputDto) {
		if (inputDto == null || inputDto.getBillId() == null) {
			return BaseOutput.failure("参数错误");
		}

		logger.info("获取登记单:" + inputDto.getBillId());
		try {
			Long billId = inputDto.getBillId();
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			User user = userService.get(userId);
			if (user == null) {
				return BaseOutput.failure("未登陆用户");
			}
			RegisterBill registerBill = registerBillService.get(billId);
			if (registerBill == null) {
				logger.error("获取登记单失败id:" + billId);
				return BaseOutput.failure();
			}
			Map<Object, Object> resultMap = new BeanMap(registerBill);
			resultMap.put("billId", resultMap.remove("id"));
			List<ImageCert> imageCertList = this.imageCertService.findImageCertListByBillId(billId);
			resultMap.put("imageCertList", imageCertList);

			return BaseOutput.success().setData(resultMap);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("查询数据出错");
		}

	}

}
