package com.dili.trace.api.client;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.output.TradeDetailBillOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.TradeRequestService;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;

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
	@Autowired
	TradeDetailService tradeDetailService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	TradeRequestService tradeRequestService;

	@ApiOperation("保存多个登记单")
	@RequestMapping(value = "/createRegisterBillList.api", method = RequestMethod.POST)
	public BaseOutput<List<Long>> createRegisterBillList(@RequestBody CreateListBillParam createListBillParam) {
		logger.info("保存多个登记单:{}",JSON.toJSONString(createListBillParam));
		if (createListBillParam == null || createListBillParam.getRegisterBills() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);

			List<CreateRegisterBillInputDto> registerBills = StreamEx.of(createListBillParam.getRegisterBills())
					.nonNull().toList();
			if (registerBills == null) {
				return BaseOutput.failure("没有登记单");
			}
			logger.info("保存多个登记单操作用户:{}，{}" ,operatorUser.getId(),operatorUser.getName());
			List<Long> idList = this.registerBillService.createBillList(registerBills, userService.get(operatorUser.getId()), Optional.empty());
			return BaseOutput.success().setData(idList);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}

	@ApiOperation("修改报备单")
	@RequestMapping(value = "/doEditRegisterBill.api", method = RequestMethod.POST)
	public BaseOutput doEditRegisterBill(@RequestBody CreateRegisterBillInputDto dto) {
		logger.info("修改报备单:{}",JSON.toJSONString(dto));
		if (dto == null || dto.getBillId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);

			User user = userService.get(operatorUser.getId());
			if (user == null) {
				return BaseOutput.failure("未登陆用户");
			}

			
			RegisterBill registerBill = dto.build(user);
			logger.info("保存登记单:{}", JSON.toJSONString(registerBill));
			this.registerBillService.doEdit(registerBill,dto.getImageCertList(),Optional.empty());
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
	public BaseOutput<BasePage<TradeDetailBillOutput>> listPage(@RequestBody RegisterBillDto input) {
		logger.info("获取登记单列表:{}", JSON.toJSONString(input));
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();

			logger.info("获取登记单列表 操作用户:{}", userId);
			input.setUserId(userId);
			BasePage basePage = this.tradeDetailService.selectTradeDetailAndBill(input);
			return BaseOutput.success().setData(basePage);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

}
