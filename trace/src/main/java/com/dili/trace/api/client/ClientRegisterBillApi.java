package com.dili.trace.api.client;

import java.util.List;

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
import com.dili.trace.api.output.TradeDetailBillOutput;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.TradeRequestService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;
import com.google.common.collect.Lists;

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
			logger.info("保存多个登记单 操作用户:{}" ,operatorUser.getId());
			List<Long> idList = this.registerBillService.createBillList(registerBills, userService.get(operatorUser.getId()), operatorUser);
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

			logger.info("保存登记单:" + JSON.toJSONString(dto));
			RegisterBill registerBill = dto.build(user);

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
	public BaseOutput<BasePage<TradeDetailBillOutput>> listPage(@RequestBody RegisterBillDto input) {
		logger.info("获取登记单列表:{}", JSON.toJSONString(input));
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			User user = userService.get(userId);
			if (user == null) {
				return BaseOutput.failure("未登陆用户");
			}
			logger.info("获取登记单列表 操作用户:{}", JSON.toJSONString(user));
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

	@ApiOperation(value = "通过登记单ID获取登记单详细信息")
	@RequestMapping(value = "/viewTradeDetailBill.api", method = RequestMethod.POST)
	public BaseOutput<RegisterBillOutputDto> viewTradeDetailBill(@RequestBody RegisterBillApiInputDto inputDto) {
		if (inputDto == null || (inputDto.getBillId() == null && inputDto.getTradeDetailId() == null)) {
			return BaseOutput.failure("参数错误");
		}

		logger.info("获取登记单详细信息->billId:{},tradeDetailId:{}",inputDto.getBillId(),inputDto.getTradeDetailId() );
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			User user = userService.get(userId);
			if (user == null) {
				return BaseOutput.failure("未登陆用户");
			}
			TradeDetail tradeDetailItem = StreamEx.ofNullable(inputDto.getTradeDetailId()).nonNull()
					.map(tradeDetailId -> {
						return this.tradeDetailService.get(tradeDetailId);
					}).findFirst().orElse(new TradeDetail());

			RegisterBill registerBill = StreamEx.ofNullable(inputDto.getBillId()).append(tradeDetailItem.getBillId())
					.nonNull().map(billId -> {
						return this.registerBillService.get(billId);
					}).findFirst().orElse(new RegisterBill());

			List<ImageCert> imageCertList = StreamEx.ofNullable(registerBill.getId()).nonNull().flatMap(billId -> {
				return this.imageCertService.findImageCertListByBillId(billId).stream();
			}).toList();

			UpStream upStream = StreamEx.ofNullable(registerBill.getUpStreamId()).nonNull().map(upStreamId -> {
				return this.upStreamService.get(upStreamId);
			}).nonNull().findAny().orElse(null);

			if (tradeDetailItem.getId() != null && registerBill.getId() != null) {
				RegisterBillOutputDto outputdto = RegisterBillOutputDto.build(registerBill, Lists.newArrayList());
				outputdto.setImageCertList(imageCertList);
				outputdto.setUpStream(upStream);
				outputdto.setWeight(tradeDetailItem.getTotalWeight());
				return BaseOutput.success().setData(outputdto);
			} else if (registerBill.getId() != null) {
				RegisterBillOutputDto outputdto = RegisterBillOutputDto.build(registerBill, Lists.newArrayList());
				outputdto.setUpStream(upStream);
				outputdto.setImageCertList(imageCertList);
				outputdto.setWeight(registerBill.getWeight());
				return BaseOutput.success().setData(outputdto);
			} else {
				return BaseOutput.failure("没有数据");
			}
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}
}
