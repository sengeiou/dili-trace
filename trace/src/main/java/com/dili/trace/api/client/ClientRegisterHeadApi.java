package com.dili.trace.api.client;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CreateRegisterHeadInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.*;
import com.dili.trace.dto.CreateListRegisterHeadParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterHeadDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.RegisgterHeadStatusEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 进门主台账单相关接口
 *
 * @author Lily
 */
@RestController
@RequestMapping(value = "/api/client/clientRegisterHead")
@Api(value = "/api/client/clientRegisterHead", description = "进门主台账单相关接口")
@InterceptConfiguration
public class ClientRegisterHeadApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientRegisterHeadApi.class);

	@Autowired
	RegisterHeadService registerHeadService;

	@Autowired
	private LoginSessionContext sessionContext;

	@Autowired
	UserService userService;

	@Autowired
	ImageCertService imageCertService;

	@Autowired
	RegisterBillService registerBillService;

	@Autowired
	UpStreamService upStreamService;

	@ApiOperation(value = "获取进门主台账单列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterHead", dataType = "RegisterHead", value = "获取进门主台账单列表")
	@RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
	public BaseOutput<BasePage<CheckinOutRecord>> listPage(@RequestBody RegisterHeadDto input) {
		logger.info("获取进门主台账单列表:{}", JSON.toJSONString(input));
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			logger.info("获取进门主台账单列表 操作用户:{}", userId);
			input.setSort("created");
			input.setOrder("desc");
			BasePage<RegisterHead> registerHeadBasePage = registerHeadService.listPageApi(input);

			if(null != registerHeadBasePage && CollectionUtils.isNotEmpty(registerHeadBasePage.getDatas())){
				registerHeadBasePage.getDatas().forEach(e ->{
					e.setWeightUnitName(WeightUnitEnum.fromCode(e.getWeightUnit()).get().getName());
				});
			}

			return BaseOutput.success().setData(registerHeadBasePage);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

	/**
	 * 不同审核状态数据统计
	 */
	@RequestMapping(value = "/countByStatus.api", method = { RequestMethod.POST })
	public BaseOutput<List<VerifyStatusCountOutputDto>> countByStatus(@RequestBody RegisterHeadDto query) {
		try {
			// 10: 已启用 20：已关闭 30：废弃
			List<VerifyStatusCountOutputDto> list = new ArrayList<>(3);
			List<RegisterHead> registerHeads = registerHeadService.listByExample(query);
			int activeCount = 0;
			int unactiveCount = 0;
			int deleteCount = 0;
			if(CollectionUtils.isNotEmpty(registerHeads)){
				//Stream<RegisterHead> registerHeadStream = registerHeads.stream().filter(e -> (e.getActive() == null || e.getIsDeleted() == null));
				// 已启用
				activeCount= (int)registerHeads.stream().filter(e -> ( e.getActive() == 1 && e.getIsDeleted()== 0)).count();
				unactiveCount = (int)registerHeads.stream().filter(e -> (e.getActive() == 0 && e.getIsDeleted() == 0)).count();
				deleteCount= (int)registerHeads.stream().filter(e -> e.getIsDeleted() == 1).count();
			}
			list.add(new VerifyStatusCountOutputDto(RegisgterHeadStatusEnum.ACTIVE.getCode(),activeCount));
			list.add(new VerifyStatusCountOutputDto(RegisgterHeadStatusEnum.UNACTIVE.getCode(),unactiveCount));
			list.add(new VerifyStatusCountOutputDto(RegisgterHeadStatusEnum.DELETED.getCode(),deleteCount));
			return BaseOutput.success().setData(list);

		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("操作失败：服务端出错");
		}
	}

	@ApiOperation("保存多个进门主台账单")
	@RequestMapping(value = "/createRegisterHeadList.api", method = RequestMethod.POST)
	public BaseOutput<List<Long>> createRegisterHeadList(@RequestBody CreateListRegisterHeadParam createListRegisterHeadParam) {
		logger.info("保存多个进门主台账单:{}", JSON.toJSONString(createListRegisterHeadParam));
		if (createListRegisterHeadParam == null || createListRegisterHeadParam.getRegisterBills() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);

			List<CreateRegisterHeadInputDto> registerHeads = StreamEx.of(createListRegisterHeadParam.getRegisterBills())
					.nonNull().toList();
			if (registerHeads == null) {
				return BaseOutput.failure("没有进门主台账单");
			}
			logger.info("保存多个进门主台账单操作用户:{}，{}", operatorUser.getId(), operatorUser.getName());
			List<Long> idList = this.registerHeadService.createRegisterHeadList(registerHeads,
					userService.get(createListRegisterHeadParam.getUserId()), Optional.of(operatorUser), createListRegisterHeadParam.getMarketId());
			return BaseOutput.success().setData(idList);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}

	@ApiOperation("修改进门主台账单")
	@RequestMapping(value = "/doEditRegisterHead.api", method = RequestMethod.POST)
	public BaseOutput doEditRegisterBill(@RequestBody CreateRegisterHeadInputDto dto) {
		logger.info("修改进门主台账单:{}", JSON.toJSONString(dto));
		if (dto == null || dto.getId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);
			if (operatorUser == null) {
				return BaseOutput.failure("未登陆用户");
			}
			User user = userService.get(dto.getUserId());
			RegisterHead registerHead = dto.build(user);
			logger.info("修改进门主台账单:{}", JSON.toJSONString(registerHead));
			this.registerHeadService.doEdit(registerHead, dto.getImageCertList(), Optional.empty());
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
		return BaseOutput.success();
	}

	@ApiOperation("作废进门主台账单")
	@RequestMapping(value = "/doDeleteRegisterHead.api", method = RequestMethod.POST)
	public BaseOutput doDeleteRegisterHead(@RequestBody CreateRegisterHeadInputDto dto) {
		logger.info("作废进门主台账单:{}", JSON.toJSONString(dto));
		if (dto == null || dto.getId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);
			if (operatorUser == null) {
				return BaseOutput.failure("未登陆用户");
			}
			logger.info("作废进门主台账单:billId:{},userId:{}", dto.getId(), operatorUser.getId());
			this.registerHeadService.doDelete(dto, operatorUser.getId(), Optional.empty());
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
		return BaseOutput.success();
	}

	@ApiOperation("启用/关闭进门主台账单")
	@RequestMapping(value = "/doUpdateActiveRegisterHead.api", method = RequestMethod.POST)
	public BaseOutput doUpdateActiveRegisterHead(@RequestBody CreateRegisterHeadInputDto dto) {
		logger.info("启用/关闭进门主台账单:{}", JSON.toJSONString(dto));
		if (dto == null || dto.getId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER);
			if (operatorUser == null) {
				return BaseOutput.failure("未登陆用户");
			}
			logger.info("启用/关闭进门主台账单:billId:{},userId:{}", dto.getId(), operatorUser.getId());
			this.registerHeadService.doUpdateActive(dto, operatorUser.getId(), Optional.empty());
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
		return BaseOutput.success();
	}

	@ApiOperation("查看进门主台账单")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewRegisterHead.api", method = {RequestMethod.POST})
	public BaseOutput<RegisterHead> viewRegisterHead(@RequestBody BaseDomain baseDomain) {
		try {
			RegisterHead registerHead = registerHeadService.get(baseDomain.getId());

			List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(baseDomain.getId(), BillTypeEnum.MASTER_BILL.getCode());
			registerHead.setImageCertList(imageCerts);

			UpStream upStream = upStreamService.get(registerHead.getUpStreamId());
			registerHead.setUpStreamName(upStream.getName());
			return BaseOutput.success().setData(registerHead);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error("查询进门主台账单数据出错", e);
			return BaseOutput.failure("查询进门主台账单数据出错");
		}
	}

	@ApiOperation("分批详情")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewPartialRegisterHead.api", method = {RequestMethod.GET})
	public BaseOutput<RegisterHead> viewPartialRegisterHead(@RequestParam String code) {
		try {
			RegisterHead registerHead = new RegisterHead();
			registerHead.setCode(code);
			List<RegisterHead> registerHeadList =  registerHeadService.listByExample(registerHead);
			if(CollectionUtils.isNotEmpty(registerHeadList)){
				registerHead = registerHeadList.get(0);
			} else {
				return BaseOutput.failure("没有进门主台账单");
			}
			UpStream upStream = upStreamService.get(registerHead.getUpStreamId());
			registerHead.setUpStreamName(upStream.getName());
			RegisterBill registerBill = new RegisterBill();
			registerBill.setRegisterHeadCode(code);
			List<RegisterBill> registerBills = registerBillService.listByExample(registerBill);
			if(null != registerBills && CollectionUtils.isNotEmpty(registerBills)){
				registerBills.forEach(e ->{
					List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(e.getBillId(), BillTypeEnum.REGISTER_FORM_BILL.getCode());
					e.setImageCerts(imageCerts);
				});
			}
			registerHead.setRegisterBills(registerBills);
			return BaseOutput.success().setData(registerHead);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error("查询分批详情出错", e);
			return BaseOutput.failure("查询分批详情数据出错");
		}
	}
}
