package com.dili.trace.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CheckinRecordInputDto;
import com.dili.trace.dto.ManullyCheckInputDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillInputDto;
import com.dili.trace.dto.UserInfoDto;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.CodeGenerateService;
import com.dili.trace.service.CustomerService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.QualityTraceTradeBillService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.TradeTypeService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.dili.trace.util.MaskUserInfo;
import com.diligrp.manage.sdk.session.SessionContext;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Api("/registerBillHZ")
@Controller
@RequestMapping("/registerBillHZ")
public class RegisterBillHZController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillController.class);
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	SeparateSalesRecordService separateSalesRecordService;
	@Autowired
	DetectRecordService detectRecordService;
	@Autowired
	TradeTypeService tradeTypeService;
	@Autowired
	UserService userService;
	@Autowired
	UserPlateService userPlateService;
	@Autowired
	CustomerService customerService;
	@Autowired
	QualityTraceTradeBillService qualityTraceTradeBillService;
	@Autowired
	BaseInfoRpcService baseInfoRpcService;
	@Autowired
	UsualAddressService usualAddressService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	CodeGenerateService codeGenerateService;

	@ApiOperation("新增RegisterBill")
	@RequestMapping(value = "/update.action", method = RequestMethod.POST)
	public @ResponseBody BaseOutput insert(@RequestBody RegisterBillInputDto input) {
		List<Long> idList = input.getIdList().stream().filter(Objects::nonNull).collect(Collectors.toList());
		try {
			if (idList.isEmpty()) {
				// insert
				List<RegisterBill> billList = this.buildRegisterBillList(input, new Long[] { 0L },true);
				for (RegisterBill bill : billList) {
					BaseOutput out = this.registerBillService.createRegisterBill(bill);
					if (!out.isSuccess()) {
						return out;
					}
				}

			} else {
				List<RegisterBill> billList = this.buildRegisterBillList(input,
						idList.toArray(new Long[idList.size()]),false);
				try {
					this.registerBillService.batchUpdateSelective(billList);
				} catch (Exception e) {
					return BaseOutput.failure("操作失败");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return BaseOutput.success("操作成功");
	}

	private List<RegisterBill> buildRegisterBillList(RegisterBillInputDto input, Long[] idList,boolean insert) {
		return IntStream.range(0, idList.length).boxed().collect(Collectors.toMap(i -> i, i -> idList[i])).entrySet()
				.stream().map(e -> {
					Integer index = e.getKey();
					Long id = e.getValue();
					Long userId = input.getUserId();
					User user = this.userService.get(userId);
					RegisterBill bill = DTOUtils.newDTO(RegisterBill.class);
					bill.setRegisterSource(RegisterSourceEnum.TRADE_AREA.getCode());
					bill.setId(id);

					Long upStreamId = input.getUpStreamIdList().get(index);
					Long productId = input.getProductIdList().get(index);
					String productName = input.getProductNameList().get(index);
					Integer weight = input.getWeightList().get(index);


				
					bill.setUpStreamId(upStreamId);
					
					bill.setWeight(weight);
					bill.setCreated(new Date());
					bill.setModified(new Date());
					bill.setUserId(input.getUserId());
					bill.setName(user.getName());
					bill.setIdCardNo(user.getCardNo());
					bill.setAddr(user.getAddr());
					bill.setUserId(user.getId());
					bill.setTallyAreaNo(user.getTallyAreaNos());

					return bill;

				}).collect(Collectors.toList());
	}

	/**
	 * 交易单修改
	 *
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit(ModelMap modelMap,@RequestParam("idList")List<Long>idList,@RequestParam("userId")Long userId) {
		List<Long> ids = CollectionUtils.emptyIfNull(idList).stream().filter(Objects::nonNull)
				.collect(Collectors.toList());
		User userItem = this.userService.get(userId);
		modelMap.put("userItem", userItem);
		List<RegisterBill> registerBillList = new ArrayList<>();

		if (!ids.isEmpty()) {
			RegisterBillDto query = DTOUtils.newDTO(RegisterBillDto.class);
			query.setIdList(ids);
			registerBillList.addAll(this.registerBillService.listByExample(query));
		}
		Map<Long, UpStream> idUpStreamMap = registerBillList.stream().map(bill -> {
			return this.upStreamService.get(bill.getUpStreamId());
		}).filter(Objects::nonNull).collect(Collectors.toMap(UpStream::getId, Function.identity()));

		modelMap.put("idUpStreamMap", idUpStreamMap);
		modelMap.put("create", registerBillList.isEmpty());
		if (registerBillList.isEmpty()) {
			registerBillList.add(DTOUtils.newDTO(RegisterBill.class));
		}
		modelMap.put("registerBillList", registerBillList);
		return "registerBill/hz/edit";
	}

	/**
	 * 进门审核
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/doCheckIn.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> doCheckIn(@RequestBody CheckinRecordInputDto input) {
		try {
			if (input == null || input.getBillId() == null || input.getCheckinStatus() == null) {
				return BaseOutput.failure("参数错误");
			}
			RegisterBill bill = this.registerBillService.get(input.getBillId());
			if (bill == null) {
				return BaseOutput.failure("数据错误");
			}
			if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(bill.getState())) {
				return BaseOutput.failure("登记单状态错误");
			}
			RegisterBill updatable = DTOUtils.newDTO(RegisterBill.class);
			updatable.setId(bill.getId());
			updatable.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
			if (CheckinStatusEnum.ALLOWED.equalsCode(input.getCheckinStatus())) {
				//updatable.setDetectState(BillDetectStateEnum.PASS.getCode());
			} else {
				//updatable.setDetectState(BillDetectStateEnum.NO_PASS.getCode());
			}
			
			updatable.setSampleCode(codeGenerateService.nextSampleCode());
			this.registerBillService.updateSelective(updatable);
			return BaseOutput.success();
		} catch (AppException e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 进门审核
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/doManullyCheck.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> doManullyCheck(@RequestBody ManullyCheckInputDto input) {
		try {
			if (input == null || input.getBillId() == null || input.getPass() == null) {
				return BaseOutput.failure("参数错误");
			}
			RegisterBill bill = this.registerBillService.get(input.getBillId());
			if (bill == null) {
				return BaseOutput.failure("数据错误");
			}
			if (!RegisterBillStateEnum.WAIT_CHECK.getCode().equals(bill.getState())) {
				return BaseOutput.failure("登记单状态错误");
			}
			RegisterBill updatable = DTOUtils.newDTO(RegisterBill.class);
			updatable.setId(bill.getId());
			updatable.setState(RegisterBillStateEnum.ALREADY_AUDIT.getCode());
			if (input.getPass()) {
				if (bill.getDetectState() != null) {
					updatable.setDetectState(BillDetectStateEnum.REVIEW_PASS.getCode());
				} else {
					updatable.setDetectState(BillDetectStateEnum.PASS.getCode());
				}
				updatable.setLatestPdResult("合格");
			} else {
				if (bill.getDetectState() != null) {
					updatable.setDetectState(BillDetectStateEnum.REVIEW_NO_PASS.getCode());
				} else {
					updatable.setDetectState(BillDetectStateEnum.NO_PASS.getCode());
				}
				updatable.setLatestPdResult("不合格");
			}
			updatable.setLatestDetectTime(new Date());
			updatable.setLatestDetectOperator(SessionContext.getSessionContext().getUserTicket().getRealName());
			
			this.registerBillService.updateSelective(updatable);
			return BaseOutput.success();
		} catch (AppException e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 所有状态列表
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/listState.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<Map<String, String>> listState() {

		return Stream.of(RegisterBillStateEnum.values()).map(e -> {
			Map<String, String> map = new HashMap<>();
			map.put("id", e.getCode().toString());
			map.put("name", e.getName());
			map.put("parentId", "");
			return map;

		}).collect(Collectors.toList());

	}

	private RegisterBill maskRegisterBillOutputDto(RegisterBill dto) {
		if (dto == null) {
			return dto;
		}
		if (SessionContext.hasAccess("post", "registerBill/create.html#user")) {
			return dto;
		} else {
			dto.setIdCardNo(MaskUserInfo.maskIdNo(dto.getIdCardNo()));
			dto.setAddr(MaskUserInfo.maskAddr(dto.getAddr()));
			return dto;
		}

	}

	private UserInfoDto maskUserInfoDto(UserInfoDto dto) {

		if (dto == null) {
			return dto;
		}
		return dto.mask(!SessionContext.hasAccess("post", "registerBill/create.html#user"));
	}

}