package com.dili.trace.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.AppException;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.TradeType;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.domain.UsualAddress;
import com.dili.trace.dto.BatchAuditDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.RegisterBillStaticsDto;
import com.dili.trace.dto.UserInfoDto;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Api("/registerBill")
@Controller
@RequestMapping("/registerBill")
public class RegisterBillController {
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
	BaseInfoRpcService baseInfoRpcService;
	@Autowired
	UsualAddressService usualAddressService;
	@Autowired
	UpStreamService upStreamService;

	@ApiOperation("跳转到RegisterBill页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
		modelMap.put("state", RegisterBillStateEnum.WAIT_AUDIT.getCode());
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);

		return "registerBill/index";
	}

	@ApiOperation(value = "查询RegisterBill", notes = "查询RegisterBill，返回列表信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<RegisterBill> list(RegisterBillDto registerBill) {
		return registerBillService.list(registerBill);
	}

	@ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/findFirstWaitAuditRegisterBillCreateByCurrentUser.action", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody Object findFirstWaitAuditRegisterBillCreateByCurrentUser(RegisterBillDto dto)
			throws Exception {

		RegisterBill registerBill = registerBillService.findFirstWaitAuditRegisterBillCreateByCurrentUser(dto);
		return BaseOutput.success().setData(registerBill);
	}

	@ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(RegisterBillDto registerBill) throws Exception {

		return registerBillService.listPage(registerBill);
	}

	@ApiOperation("新增RegisterBill")
	@RequestMapping(value = "/insert.action", method = RequestMethod.POST)
	public @ResponseBody BaseOutput insert(@RequestBody List<RegisterBill> registerBills) {
		LOGGER.info("保存登记单数据:" + registerBills.size());
		Map<String, String> tradeTypeMap = CollectionUtils.emptyIfNull(tradeTypeService.findAll()).stream()
				.filter(Objects::nonNull).collect(Collectors.toMap(TradeType::getTypeId, TradeType::getTypeName));

		for (RegisterBill registerBill : registerBills) {

			User user = userService.findByTallyAreaNo(registerBill.getTallyAreaNo());
			if (user == null) {
				LOGGER.error("新增登记单失败理货区号[" + registerBill.getTallyAreaNo() + "]对应用户不存在");
				return BaseOutput.failure("理货区号[" + registerBill.getTallyAreaNo() + "]对应用户不存在");
			}
			registerBill.setName(user.getName());
			registerBill.setIdCardNo(user.getCardNo());
			registerBill.setAddr(user.getAddr());
			registerBill.setUserId(user.getId());
			registerBill.setTallyAreaNo(user.getTallyAreaNos());

			registerBill.setDetectReportUrl(StringUtils.trimToNull(registerBill.getDetectReportUrl()));
			registerBill.setOriginCertifiyUrl(StringUtils.trimToNull(registerBill.getOriginCertifiyUrl()));
			registerBill.setCreationSource(RegisterBilCreationSourceEnum.PC.getCode());
			try {
				BaseOutput r = registerBillService.createRegisterBill(registerBill);
				if (!r.isSuccess()) {
					return r;
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return BaseOutput.success("新增成功").setData(registerBills);
	}

	@ApiOperation("修改RegisterBill")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = true, dataType = "string") })
	@RequestMapping(value = "/update.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput update(RegisterBill registerBill) {
		registerBillService.updateSelective(registerBill);
		return BaseOutput.success("修改成功");
	}

	@ApiOperation("删除RegisterBill")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", paramType = "form", value = "RegisterBill的主键", required = true, dataType = "long") })
	@RequestMapping(value = "/delete.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BaseOutput delete(Long id) {
		registerBillService.delete(id);
		return BaseOutput.success("删除成功");
	}

	/**
	 * 登记单录入页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/create.html")
	public String create(ModelMap modelMap) {
		try {
			modelMap.put("tradeTypes", tradeTypeService.findAll());
		} catch (Exception e) {
			// TODO: handle exception
		}
		modelMap.put("citys", this.queryCitys());

		return "registerBill/create";
	}

	private List<UsualAddress> queryCitys() {
		return usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.REGISTER);
	}

	/**
	 * 登记单录查看页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/view/{id}/{displayWeight}", method = RequestMethod.GET)
	public String view(ModelMap modelMap, @PathVariable Long id,
			@PathVariable(required = false) Boolean displayWeight) {
		RegisterBill registerBill = registerBillService.get(id);
		if (registerBill == null) {
			return "";
		}
		if (displayWeight == null) {
			displayWeight = false;
		}
		if (RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())) {
			// 分销信息
			
				modelMap.put("separateSalesRecords", Collections.emptyList());
		
		} else {
			modelMap.put("qualityTraceTradeBills", CollectionUtils.emptyCollection());
		}
		if (registerBill.getUpStreamId() != null) {
			UpStream upStream = this.upStreamService.get(registerBill.getUpStreamId());
			modelMap.put("upStream", upStream);
		} else {
			modelMap.put("upStream", null);
		}
		Map<Integer, String> upStreamTypeMap = Stream.of(UpStreamTypeEnum.values())
				.collect(Collectors.toMap(UpStreamTypeEnum::getCode, UpStreamTypeEnum::getName));
		modelMap.put("upStreamTypeMap", upStreamTypeMap);
		// DetectRecord conditon=DTOUtils.newDTO(DetectRecord.class);
		// conditon.setRegisterBillCode(registerBill.getCode());
		// conditon.setSort("id");
		// conditon.setOrder("desc");
		List<DetectRecord> detectRecordList = this.detectRecordService.findTop2AndLatest(registerBill.getCode());
		modelMap.put("detectRecordList", detectRecordList);
		modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));
		modelMap.put("displayWeight", displayWeight);
		return "registerBill/view";
	}

	/**
	 * 交易区交易单分销记录
	 * 
	 * @param modelMap
	 * @param id       交易单ID
	 * @return
	 */
	@RequestMapping(value = "/tradeBillSsRecord/{id}", method = RequestMethod.GET)
	public String tradeBillSRecord(ModelMap modelMap, @PathVariable Long id) {
		SeparateSalesRecord condition = new SeparateSalesRecord();
		List<SeparateSalesRecord> separateSalesRecords = separateSalesRecordService.listByExample(condition);
		modelMap.put("separateSalesRecords", separateSalesRecords);
		return "registerBill/tradeBillSsRecord";
	}

	/**
	 * 登记单录修改页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/uploadDetectReport/{id}", method = RequestMethod.GET)
	public String uploadDetectReport(ModelMap modelMap, @PathVariable Long id) {
		RegisterBill registerBill = registerBillService.get(id);
		if (registerBill == null) {
			return "";
		}
		if (RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())) {
			// 分销信息
			
			modelMap.put("separateSalesRecords", Collections.emptyList());
		
		} else {
			modelMap.put("qualityTraceTradeBills", CollectionUtils.emptyCollection());
		}
		modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);

		return "registerBill/upload-detectReport";
	}

	/**
	 * 上传产地证明
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/uploadOrigincertifiy/{id}", method = RequestMethod.GET)
	public String uploadOrigincertifiy(ModelMap modelMap, @PathVariable Long id) {
		RegisterBill registerBill = registerBillService.get(id);
		if (registerBill == null) {
			return "";
		}
		if (RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())) {
			// 分销信息
			modelMap.put("separateSalesRecords", Collections.emptyList());
		} else {
			modelMap.put("qualityTraceTradeBills", CollectionUtils.emptyCollection());
		}
		modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);

		return "registerBill/upload-origincertifiy";
	}

	/**
	 * 审核页面
	 * 
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/audit/{id}", method = RequestMethod.GET)
	public String audit(ModelMap modelMap, @PathVariable Long id) {
		modelMap.put("registerBill", registerBillService.get(id));
		return "registerBill/audit";
	}

	/**
	 * 审核
	 * 
	 * @param id
	 * @param pass
	 * @return
	 */
	@RequestMapping(value = "/audit/{id}/{pass}", method = RequestMethod.GET)
	public @ResponseBody BaseOutput audit(@PathVariable Long id, @PathVariable Boolean pass) {
		try {
			registerBillService.auditRegisterBill(id, pass);
		} catch (AppException e) {
			return BaseOutput.failure(e.getMessage());
		}
		return BaseOutput.success("操作成功");
	}

	/**
	 * 批量主动送检
	 * 
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/doBatchAutoCheck", method = RequestMethod.POST)
	public @ResponseBody BaseOutput doBatchAutoCheck(ModelMap modelMap, @RequestBody List<Long> idList) {
		// modelMap.put("registerBill", registerBillService.get(id));
		idList = CollectionUtils.emptyIfNull(idList).stream().filter(Objects::nonNull).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(idList)) {
			return BaseOutput.failure("参数错误");
		}
		return this.registerBillService.doBatchAutoCheck(idList);
	}

	/**
	 * 批量采样检测
	 * 
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/doBatchSamplingCheck", method = RequestMethod.POST)
	public @ResponseBody BaseOutput doBatchSamplingCheck(ModelMap modelMap, @RequestBody List<Long> idList) {
		// modelMap.put("registerBill", registerBillService.get(id));
		idList = CollectionUtils.emptyIfNull(idList).stream().filter(Objects::nonNull).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(idList)) {
			return BaseOutput.failure("参数错误");
		}
		return this.registerBillService.doBatchSamplingCheck(idList);
	}

	/**
	 * 批量审核
	 * 
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/doBatchAudit", method = RequestMethod.POST)
	public @ResponseBody BaseOutput doBatchAudit(ModelMap modelMap, @RequestBody BatchAuditDto batchAuditDto) {
		// modelMap.put("registerBill", registerBillService.get(id));
		// if (batchAuditDto.getPass() == null) {
		// return BaseOutput.failure("参数错误");
		// }
		List<Long> idList = CollectionUtils.emptyIfNull(batchAuditDto.getRegisterBillIdList()).stream()
				.filter(Objects::nonNull).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(idList)) {
			return BaseOutput.failure("参数错误");
		}
		batchAuditDto.setPass(true);
		batchAuditDto.setRegisterBillIdList(idList);
		return this.registerBillService.doBatchAudit(batchAuditDto);
	}

	/**
	 * 撤销
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/undo/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseOutput undo(@PathVariable Long id) {
		try {
			registerBillService.undoRegisterBill(id);
		} catch (AppException e) {
			return BaseOutput.failure(e.getMessage());
		}
		return BaseOutput.success("操作成功");
	}

	/**
	 * 自动送检
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/autoCheck/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseOutput autoCheck(@PathVariable Long id) {
		try {
			registerBillService.autoCheckRegisterBill(id);
		} catch (AppException e) {
			return BaseOutput.failure(e.getMessage());
		}
		return BaseOutput.success("操作成功");
	}

	/**
	 * 采样检测
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/samplingCheck/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseOutput samplingCheck(@PathVariable Long id) {
		try {
			registerBillService.samplingCheckRegisterBill(id);
		} catch (AppException e) {
			return BaseOutput.failure(e.getMessage());
		}
		return BaseOutput.success("操作成功");
	}

	/**
	 * 复检
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/reviewCheck/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseOutput reviewCheck(@PathVariable Long id) {
		try {
			registerBillService.reviewCheckRegisterBill(id);
		} catch (AppException e) {
			return BaseOutput.failure(e.getMessage());
		}
		return BaseOutput.success("操作成功");
	}

	@ApiOperation("跳转到statics页面")
	@RequestMapping(value = "/statics.html", method = RequestMethod.GET)
	public String statics(ModelMap modelMap) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
		return "registerBill/statics";
	}

	@RequestMapping(value = "/listStaticsPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listStaticsPage(RegisterBillDto registerBill) throws Exception {
		return this.registerBillService.listStaticsPage(registerBill);
	}

	@RequestMapping(value = "/listStaticsData.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> listStaticsData(RegisterBillDto registerBill) {
		registerBill.setAttrValue(StringUtils.trimToEmpty(registerBill.getAttrValue()));
		RegisterBillStaticsDto staticsDto = this.registerBillService.groupByState(registerBill);

		return BaseOutput.success().setData(staticsDto);
	}

	/**
	 * 交易区订单溯源页面（二维码）
	 * 
	 * @param tradeNo
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/tradeBillDetail.html", method = RequestMethod.GET)
	public String tradeBillDetail(String tradeNo, ModelMap modelMap) {

		modelMap.put("registerBill", new RegisterBillOutputDto());
		modelMap.put("qualityTraceTradeBill", null);
		return "registerBill/tradeBillDetail";
	}

	/**
	 * 登记单溯源（二维码） 没有分销记录
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/registerBillQRCode.html", method = RequestMethod.GET)
	public String registerBillQRCcode(Long id, ModelMap modelMap) {
		RegisterBill bill = registerBillService.get(id);
		modelMap.put("registerBill", bill);
		return "registerBill/registerBillQRCode";
	}

	/**
	 * 登记单分销记录溯源（二维码）
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/separateSalesRecordQRCode.html", method = RequestMethod.GET)
	public String separateSalesRecordQRCcode(Long id, ModelMap modelMap) {
		SeparateSalesRecord separateSalesRecord = separateSalesRecordService.get(id);
		RegisterBill bill = registerBillService.findByCode(separateSalesRecord.getRegisterBillCode());
		modelMap.put("registerBill", bill);
		modelMap.put("separateSalesRecord", separateSalesRecord);
		return "registerBill/registerBillQRCode";
	}

	/**
	 * 交易单溯源（二维码） 没有分销记录
	 *
	 * @param id       交易单ID
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/tradeBillQRCcode.html", method = RequestMethod.GET)
	public String tradeBillQRCcode(Long id, ModelMap modelMap) {
		modelMap.put("registerBill", new RegisterBill());
		modelMap.put("qualityTraceTradeBill", null);
		return "registerBill/tradeBillQRCode";
	}

	/**
	 * 交易单分销记录溯源（二维码）
	 *
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/tradeSsrQRCcode.html", method = RequestMethod.GET)
	public String tradeSsrQRCcode(Long id, ModelMap modelMap) {
		SeparateSalesRecord separateSalesRecord = separateSalesRecordService.get(id);
		
		RegisterBill bill = new RegisterBill();
		modelMap.put("registerBill", bill);
		modelMap.put("qualityTraceTradeBill", null);
		modelMap.put("separateSalesRecord", separateSalesRecord);
		return "registerBill/tradeBillQRCode";
	}

	/**
	 * 交易单复制
	 *
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/copy.html", method = RequestMethod.GET)
	public String copy(Long id, ModelMap modelMap) {
		RegisterBill registerBill = registerBillService.get(id);
		String firstTallyAreaNo = Stream.of(StringUtils.trimToEmpty(registerBill.getTallyAreaNo()).split(","))
				.filter(StringUtils::isNotBlank).findFirst().orElse("");
		registerBill.setTallyAreaNo(firstTallyAreaNo);

		UserInfoDto userInfoDto = this.findUserInfoDto(registerBill, firstTallyAreaNo);
		modelMap.put("userInfo", this.maskUserInfoDto(userInfoDto));
		modelMap.put("tradeTypes", tradeTypeService.findAll());
		modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

		modelMap.put("citys", this.queryCitys());

		if (registerBill.getRegisterSource().equals(RegisterSourceEnum.TALLY_AREA.getCode())) {
			List<UserPlate> userPlateList = this.userPlateService.findUserPlateByUserId(registerBill.getUserId());
			modelMap.put("userPlateList", userPlateList);
		} else {
			modelMap.put("userPlateList", new ArrayList<>(0));
		}

		return "registerBill/copy";
	}

	private UserInfoDto findUserInfoDto(RegisterBill registerBill, String firstTallyAreaNo) {
		UserInfoDto userInfoDto = new UserInfoDto();

		// 理货区
		User user = userService.findByTallyAreaNo(firstTallyAreaNo);

		if (user != null) {
			userInfoDto.setUserId(String.valueOf(user.getId()));
			userInfoDto.setName(user.getName());
			userInfoDto.setIdCardNo(user.getCardNo());
			userInfoDto.setPhone(user.getPhone());
			userInfoDto.setAddr(user.getAddr());

		}

		return userInfoDto;
	}

	/**
	 * 保存处理结果
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/saveHandleResult.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> saveHandleResult(RegisterBill input) {
		try {
			Long id = this.registerBillService.saveHandleResult(input);
			return BaseOutput.success().setData(id);
		} catch (AppException e) {
			return BaseOutput.failure(e.getMessage());
		}

	}

	// /**
	// * 保存处理结果
	// *
	// * @param input
	// * @return
	// */
	// @RequestMapping(value = "/doModifyRegisterBill.action", method = {
	// RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	// public BaseOutput<?> doModifyRegisterBill(RegisterBill input) {
	// try {
	// Long id = this.registerBillService.doModifyRegisterBill(input);
	// return BaseOutput.success().setData(id);
	// } catch (AppException e) {
	// LOGGER.error(e.getMessage(), e);
	// return BaseOutput.failure(e.getMessage());
	// } catch (Exception e) {
	// LOGGER.error(e.getMessage(), e);
	// return BaseOutput.failure("服务端出错");
	// }
	//
	// }

	/**
	 * 上传产地报告
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/doUploadOrigincertifiy.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> doUploadOrigincertifiy(RegisterBill input) {
		try {
			Long id = this.registerBillService.doUploadOrigincertifiy(input);
			return BaseOutput.success().setData(id);
		} catch (AppException e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 上传检测报告
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/doUploadDetectReport.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> doUploadDetectReport(RegisterBill input) {
		try {
			Long id = this.registerBillService.doUploadDetectReport(input);
			return BaseOutput.success().setData(id);
		} catch (AppException e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 删除检测报告及产地证明
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/doRemoveReportAndCertifiy.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> doRemoveReportAndCertifiy(Long id, String deleteType) {
		try {
			// Long id = this.registerBillService.doUploadDetectReport(input);
			return this.registerBillService.doRemoveReportAndCertifiy(id, deleteType);
		} catch (AppException e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 保存处理结果
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/doAuditWithoutDetect.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> doAuditWithoutDetect(RegisterBill input) {
		try {
			Long id = this.registerBillService.doAuditWithoutDetect(input);
			return BaseOutput.success().setData(id);
		} catch (AppException e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}

	/**
	 * 交易单修改
	 *
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit(Long id, ModelMap modelMap) {
		RegisterBill registerBill = registerBillService.get(id);
		String firstTallyAreaNo = Stream.of(StringUtils.trimToEmpty(registerBill.getTallyAreaNo()).split(","))
				.filter(StringUtils::isNotBlank).findFirst().orElse("");
		registerBill.setTallyAreaNo(firstTallyAreaNo);

		UserInfoDto userInfoDto = this.findUserInfoDto(registerBill, firstTallyAreaNo);
		modelMap.put("userInfo", this.maskUserInfoDto(userInfoDto));
		modelMap.put("tradeTypes", tradeTypeService.findAll());
		modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

		modelMap.put("citys", this.queryCitys());

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);

		if (registerBill.getRegisterSource().equals(RegisterSourceEnum.TALLY_AREA.getCode())) {
			List<UserPlate> userPlateList = this.userPlateService.findUserPlateByUserId(registerBill.getUserId());
			modelMap.put("userPlateList", userPlateList);
		} else {
			modelMap.put("userPlateList", new ArrayList<>(0));
		}
		return "registerBill/edit";
	}

	/**
	 * 保存处理结果
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/doEdit.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> doEdit(RegisterBill input) {
		try {

			Long id = this.registerBillService.doEdit(input);
			return BaseOutput.success().setData(id);
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