package com.dili.trace.api.client;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.output.VerifyBillInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.*;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 进门登记单相关接口
 *
 * @author Lily
 */
@RestController
@RequestMapping(value = "/api/client/clientRegisterFormBill")
@Api(value = "/api/client/clientRegisterFormBill", description = "进门登记单相关接口")
@InterceptConfiguration
public class ClientRegisterFormBillApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientRegisterFormBillApi.class);
	@Autowired
	private RegisterBillService registerBillService;

	@Autowired
	private LoginSessionContext sessionContext;

	@Autowired
	UserService userService;

	@Autowired
	ImageCertService imageCertService;

	@Autowired
	UpStreamService upStreamService;

	@ApiOperation(value = "获取进门登记单列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
	public BaseOutput<BasePage<RegisterBill>> listPage(@RequestBody RegisterBillDto input) {
		logger.info("获取进门登记单列表:{}", JSON.toJSONString(input));
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();

			logger.info("获取进门登记单列表 操作用户:{}", userId);
			input.setSort("created");
			input.setOrder("desc");
			BasePage<RegisterBill> basePage = this.registerBillService.listPageApi(input);
			return BaseOutput.success().setData(basePage);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询进门登记单数据出错");
		}

	}

	@ApiOperation("保存多个进门登记单")
	@RequestMapping(value = "/createRegisterFormBillList.api", method = RequestMethod.POST)
	public BaseOutput<List<Long>> createRegisterFormBillList(@RequestBody CreateListBillParam createListBillParam) {
		logger.info("保存多个进门登记单:{}", JSON.toJSONString(createListBillParam));
		if (createListBillParam == null || createListBillParam.getRegisterBills() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);

			List<CreateRegisterBillInputDto> registerBills = StreamEx.of(createListBillParam.getRegisterBills())
					.nonNull().toList();
			if (registerBills == null) {
				return BaseOutput.failure("没有进门登记单");
			}
			logger.info("保存多个进门登记单操作用户:{}，{}", operatorUser.getId(), operatorUser.getName());
			List<Long> idList = this.registerBillService.createRegisterFormBillList(registerBills,
					userService.get(createListBillParam.getUserId()), Optional.empty(), createListBillParam.getMarketId());
			return BaseOutput.success().setData(idList);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}

	@ApiOperation("修改进门登记单")
	@RequestMapping(value = "/doEditRegisterBill.api", method = RequestMethod.POST)
	public BaseOutput doEditRegisterBill(@RequestBody CreateRegisterBillInputDto dto) {
		logger.info("修改进门登记单:{}", JSON.toJSONString(dto));
		if (dto == null || dto.getBillId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);
			if (operatorUser == null) {
				return BaseOutput.failure("未登陆用户");
			}
			User user = userService.get(operatorUser.getId());
			RegisterBill registerBill = dto.build(user);
			logger.info("保存进门登记单:{}", JSON.toJSONString(registerBill));
			this.registerBillService.doEditFormBill(registerBill, dto.getImageCertList(), Optional.empty());
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
		return BaseOutput.success();
	}

	@ApiOperation("作废进门登记单")
	@RequestMapping(value = "/doDeleteRegisterBill.api", method = RequestMethod.POST)
	public BaseOutput doDeleteRegisterBill(@RequestBody CreateRegisterBillInputDto dto) {
		logger.info("作废进门登记单:{}", JSON.toJSONString(dto));
		if (dto == null || dto.getBillId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);
			if (operatorUser == null) {
				return BaseOutput.failure("未登陆用户");
			}
			logger.info("作废进门登记单:billId:{},userId:{}", dto.getBillId(), operatorUser.getId());
			this.registerBillService.doDelete(dto, operatorUser.getId(), Optional.empty());
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
		return BaseOutput.success();
	}

	@ApiOperation(value = "进门登记单审核(通过/进门/不通过/退回/进门待检)")
	@RequestMapping(value = "/doVerify.api", method = RequestMethod.POST)
	public BaseOutput<Long> doVerify(@RequestBody VerifyBillInputDto inputDto) {
		logger.info("进门登记单审核(通过/进门/不通过/退回/进门待检):{}", inputDto.getBillId());
		try {
			if (inputDto == null || inputDto.getVerifyStatus() == null || inputDto.getBillId() == null) {
				return BaseOutput.failure("参数错误");
			}
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			if (operatorUser == null) {
				return BaseOutput.failure("未登陆用户");
			}
			RegisterBill input = new RegisterBill();
			input.setId(inputDto.getBillId());
			input.setVerifyStatus(inputDto.getVerifyStatus());
			input.setReason(inputDto.getReason());
			Long id = this.registerBillService.doVerifyFormCheckIn(input,Optional.ofNullable(operatorUser));
			return BaseOutput.success().setData(id);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("操作失败：服务端出错");
		}
	}

	@ApiOperation("查看进门登记单")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewRegisterBill.api", method = {RequestMethod.POST})
	public BaseOutput<RegisterHead> viewRegisterBill(@RequestBody BaseDomain baseDomain) {
		try {
			RegisterBill registerBill = registerBillService.get(baseDomain.getId());

			List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(baseDomain.getId(), BillTypeEnum.REGISTER_FORM_BILL.getCode());
			registerBill.setImageCerts(imageCerts);

			UpStream upStream = upStreamService.get(registerBill.getUpStreamId());
			registerBill.setUpStreamName(upStream.getName());
			return BaseOutput.success().setData(registerBill);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error("查询进门主台账单数据出错", e);
			return BaseOutput.failure("查询进门主台账单数据出错");
		}
	}

	/**
	 * 不同审核状态数据统计
	 */
	@RequestMapping(value = "/countByVerifyStatus.api", method = { RequestMethod.POST })
	public BaseOutput<List<VerifyStatusCountOutputDto>> countByVerifyStatus(@RequestBody RegisterBillDto query) {

		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			if (operatorUser == null) {
				return BaseOutput.failure("未登陆用户");
			}
			List<VerifyStatusCountOutputDto> list = this.registerBillService.countByVerifyStatuseBeforeCheckin(query);
			return BaseOutput.success().setData(list);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("操作失败：服务端出错");
		}
	}
}
