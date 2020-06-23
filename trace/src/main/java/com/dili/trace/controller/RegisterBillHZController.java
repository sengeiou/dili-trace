package com.dili.trace.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

import com.dili.common.exception.TraceBusinessException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.input.CheckInApiInput;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CheckinRecordInputDto;
import com.dili.trace.dto.ManullyCheckInputDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillInputDto;
import com.dili.trace.dto.UserInfoDto;
import com.dili.trace.glossary.CheckinStatusEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.CodeGenerateService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.TradeTypeService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.dili.trace.util.MaskUserInfo;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Api("/registerBillHZ")
@Controller
@RequestMapping("/registerBillHZ")
public class RegisterBillHZController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillHZController.class);
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
	BaseInfoRpcService baseInfoRpcService;
	@Autowired
	UsualAddressService usualAddressService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	CodeGenerateService codeGenerateService;
	@Autowired
	CheckinOutRecordService checkinOutRecordService;

	@ApiOperation("新增RegisterBill")
	@RequestMapping(value = "/update.action", method = RequestMethod.POST)
	public @ResponseBody BaseOutput insert(@RequestBody RegisterBillInputDto input) {
		List<Long> idList = input.getIdList().stream().filter(Objects::nonNull).collect(Collectors.toList());
		try {
			UserTicket userTicket=SessionContext.getSessionContext().getUserTicket();
			if (idList.isEmpty()) {
				// insert
				List<RegisterBill> billList = this.buildRegisterBillList(input, new Long[] { 0L }, true);
				for (RegisterBill bill : billList) {
					BaseOutput out = this.registerBillService.createRegisterBill(bill,new ArrayList<ImageCert>(),new OperatorUser(userTicket.getId(), userTicket.getRealName()));
					if (!out.isSuccess()) {
						return out;
					}
				}

			} else {
				List<RegisterBill> billList = this.buildRegisterBillList(input, idList.toArray(new Long[idList.size()]),
						false);
				try {
					this.registerBillService.batchUpdateSelective(billList);
				} catch (Exception e) {
					return BaseOutput.failure("操作失败");
				}

			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure("服务端失败");
		}

		return BaseOutput.success("操作成功");
	}

	private List<RegisterBill> buildRegisterBillList(RegisterBillInputDto input, Long[] idList, boolean insert) {
		return IntStream.range(0, idList.length).boxed().collect(Collectors.toMap(i -> i, i -> idList[i])).entrySet()
				.stream().map(e -> {
					Integer index = e.getKey();
					Long id = e.getValue();
					Long userId = input.getUserId();
					User user = this.userService.get(userId);
					RegisterBill bill = new RegisterBill();
//					bill.setRegisterSource(RegisterSourceEnum.TRADE_AREA.getCode());
					bill.setId(id);

					Long upStreamId = input.getUpStreamIdList().get(index);
					Long productId = input.getProductIdList().get(index);
					String productName = input.getProductNameList().get(index);
					Integer weight = input.getWeightList().get(index);

					bill.setUpStreamId(upStreamId);

					bill.setWeight(new BigDecimal(weight));
					bill.setCreated(new Date());
					bill.setModified(new Date());
					bill.setUserId(input.getUserId());
					bill.setName(user.getName());
					bill.setIdCardNo(user.getCardNo());
					bill.setAddr(user.getAddr());
					bill.setUserId(user.getId());
//					bill.setTallyAreaNo(user.getTallyAreaNos());

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
	public String edit(ModelMap modelMap, @RequestParam("id") Long id, @RequestParam("userId") Long userId) {
		List<Long> ids = Arrays.asList(id);
		User userItem = this.userService.get(userId);
		modelMap.put("userItem", userItem);
		List<RegisterBill> registerBillList = new ArrayList<>();

		if (!ids.isEmpty()) {
			RegisterBillDto query = new RegisterBillDto();
			query.setIdList(ids);
			registerBillList.addAll(this.registerBillService.listByExample(query));
		}
		Map<Long, UpStream> idUpStreamMap = registerBillList.stream().map(bill -> {
			return this.upStreamService.get(bill.getUpStreamId());
		}).filter(Objects::nonNull).collect(Collectors.toMap(UpStream::getId, Function.identity()));

		modelMap.put("idUpStreamMap", idUpStreamMap);
		modelMap.put("create", registerBillList.isEmpty());
		if (registerBillList.isEmpty()) {
			registerBillList.add(new RegisterBill());
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
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (null == userTicket) {
				return BaseOutput.failure("未登录或登录过期");
			}
			CheckInApiInput checkInApiInput = new CheckInApiInput();
			checkInApiInput.setBillIdList(Arrays.asList(input.getBillId()));
			CheckinStatusEnum checkinStatusEnum = CheckinStatusEnum.fromCode(input.getCheckinStatus());
			if(checkinStatusEnum==null) {
				return BaseOutput.failure("参数错误");
			}
			checkInApiInput.setCheckinStatus(checkinStatusEnum.getCode());

			this.checkinOutRecordService.doCheckin(new OperatorUser(userTicket.getId(), userTicket.getRealName()),
					checkInApiInput);

			return BaseOutput.success();
		} catch (TraceBusinessException e) {
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
	public BaseOutput<?> doManullyCheck(@RequestBody ManullyCheckInputDto inputDto) {
//		try {
//			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
//			if (null == userTicket) {
//				return BaseOutput.failure("未登录或登录过期");
//			}
//			if (inputDto == null || inputDto.getBillId() == null || inputDto.getPass() == null) {
//				return BaseOutput.failure("参数错误");
//			}
//			RegisterBill bill = this.registerBillService.get(inputDto.getBillId());
//			if (bill == null) {
//				return BaseOutput.failure("数据错误");
//			}
//			if (!RegisterBillStateEnum.WAIT_CHECK.getCode().equals(bill.getState())) {
//				return BaseOutput.failure("登记单状态错误");
//			}
//			 ManullyCheckInput input=new ManullyCheckInput();
//			 input.setBillId(inputDto.getBillId());
//			 input.setPass(inputDto.getPass());
//			this.checkinOutRecordService.doManullyCheck(new OperatorUser(userTicket.getId(),userTicket.getRealName()), input);RegisterBill updatable = new RegisterBill();
			
			return BaseOutput.success();
//		} catch (AppException e) {
//			LOGGER.error(e.getMessage(), e);
//			return BaseOutput.failure(e.getMessage());
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage(), e);
//			return BaseOutput.failure("服务端出错");
//		}

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