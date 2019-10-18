package com.dili.trace.controller;

import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.exception.AppException;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.dto.*;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.glossary.SalesTypeEnum;
import com.dili.trace.rpc.BaseInfoRpc;
import com.dili.trace.service.*;
import com.dili.trace.util.MaskUserInfo;
import com.diligrp.manage.sdk.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
	CustomerService customerService;
	@Autowired
	QualityTraceTradeBillService qualityTraceTradeBillService;
    @Autowired
    BaseInfoRpc baseInfoRpc;

	@ApiOperation("跳转到RegisterBill页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
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
	@RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(RegisterBillDto registerBill) throws Exception {
		if (StringUtils.isNotBlank(registerBill.getAttrValue())) {
			switch (registerBill.getAttr()) {
			case "code":
				registerBill.setCode(registerBill.getAttrValue());
				break;
			case "plate":
				registerBill.setPlate(registerBill.getAttrValue());
				break;
			case "tallyAreaNo":
				registerBill.setTallyAreaNo(registerBill.getAttrValue());
				break;
			case "latestDetectOperator":
				registerBill.setLatestDetectOperator(registerBill.getAttrValue());
				break;
			case "name":
				registerBill.setName(registerBill.getAttrValue());
				break;
			case "productName":
				registerBill.setProductName(registerBill.getAttrValue());
				break;
			}
		}
		if (registerBill.getHasReport() != null) {
			if (registerBill.getHasReport()) {
				registerBill.mset(IDTO.AND_CONDITION_EXPR,
						"  (detect_report_url is not null AND detect_report_url<>'')");
			} else {
				registerBill.mset(IDTO.AND_CONDITION_EXPR, "  (detect_report_url is  null or detect_report_url='')");
			}

		}

		return registerBillService.listEasyuiPageByExample(registerBill, true).toString();
	}

	@ApiOperation("新增RegisterBill")
	@RequestMapping(value = "/insert.action", method = RequestMethod.POST)
	public @ResponseBody BaseOutput insert(@RequestBody List<RegisterBill> registerBills) {
		LOGGER.info("保存登记单数据:" + registerBills.size());
		Map<String, String> tradeTypeMap = CollectionUtils.emptyIfNull(tradeTypeService.findAll()).stream()
				.filter(Objects::nonNull).collect(Collectors.toMap(TradeType::getTypeId, TradeType::getTypeName));

		for (RegisterBill registerBill : registerBills) {

			if (registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TALLY_AREA.getCode().intValue()) {
				// 理货区
				User user = userService.findByTallyAreaNo(registerBill.getTallyAreaNo());
				if (user == null) {
					LOGGER.error("新增登记单失败理货区号[" + registerBill.getTallyAreaNo() + "]对应用户不存在");
					return BaseOutput.failure("理货区号[" + registerBill.getTallyAreaNo() + "]对应用户不存在");
				}
				registerBill.setName(user.getName());
				registerBill.setIdCardNo(user.getCardNo());
				registerBill.setAddr(user.getAddr());
				registerBill.setUserId(user.getId());
			} else {
				String tradeTypeId = StringUtils.trimToEmpty(registerBill.getTradeTypeId());
				registerBill.setTradeTypeName(tradeTypeMap.getOrDefault(tradeTypeId, null));

				if (StringUtils.isNotBlank(registerBill.getTradeAccount())
						|| StringUtils.isNotBlank(registerBill.getTradePrintingCard())) {
					Customer condition = DTOUtils.newDTO(Customer.class);
					condition.setCustomerId(StringUtils.trimToNull(registerBill.getTradeAccount()));
					condition.setPrintingCard(StringUtils.trimToNull(registerBill.getTradePrintingCard()));
					Customer customer = this.customerService.findByCustomerIdAndPrintingCard(condition).stream()
							.findFirst().orElse(null);
					if (customer == null) {
						LOGGER.error("新增登记单失败交易账号[" + registerBill.getTradeAccount() + "]对应用户不存在");
						return BaseOutput.failure("交易账号[" + registerBill.getTradeAccount() + "]对应用户不存在");
					}
					registerBill.setName(customer.getName());
					registerBill.setIdCardNo(customer.getIdNo());
					registerBill.setAddr(customer.getAddress());
					registerBill.setTradePrintingCard(customer.getPrintingCard());
					registerBill.setPhone(customer.getPhone());

				}

//				if (StringUtils.isNotBlank(registerBill.getTradeAccount())) {
//					Customer customer = customerService.findByCustomerId(registerBill.getTradeAccount());
//					if (customer == null) {
//						LOGGER.error("新增登记单失败交易账号[" + registerBill.getTradeAccount() + "]对应用户不存在");
//						return BaseOutput.failure("交易账号[" + registerBill.getTradeAccount() + "]对应用户不存在");
//					}
//					registerBill.setName(customer.getName());
//					registerBill.setIdCardNo(customer.getIdNo());
//					registerBill.setAddr(customer.getAddress());
//					registerBill.setTradePrintingCard(customer.getPrintingCard());
//					registerBill.setPhone(customer.getPhone());
//				}
			}
			registerBill.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
			registerBill.setDetectReportUrl(StringUtils.trimToNull(registerBill.getDetectReportUrl()));
			registerBill.setOriginCertifiyUrl(StringUtils.trimToNull(registerBill.getOriginCertifiyUrl()));
			BaseOutput r = registerBillService.createRegisterBill(registerBill);
			if (!r.isSuccess()) {
				return r;
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
		modelMap.put("tradeTypes", tradeTypeService.findAll());
		modelMap.put("citys", this.queryCitys());

		return "registerBill/create";
	}
	private List<City> queryCitys() {
		List<String>prirityCityNames=Arrays.asList("青州市","寿光市","莱西市","平度市","莱芜市","青岛市","博兴县","临朐县","辽宁省","河北省","吉林省","内蒙古自治区");
		
		List<City>cityList=new ArrayList<>();
		for(String name:prirityCityNames) {
			 CityListInput query = new CityListInput();
		        query.setKeyword(name);
		        BaseOutput<List<City>> result = baseInfoRpc.listCityByCondition(query);
		        if(result.isSuccess()){
		        	City city=  result.getData().stream().filter(item->item.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
		        	if(city!=null) {
		        		cityList.add(city);
		        	}
		        }
		      
		}
		return cityList;
       
    }
	/**
	 * 登记单录查看页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(ModelMap modelMap, @PathVariable Long id) {
		RegisterBill registerBill = registerBillService.get(id);
		if (registerBill == null) {
			return "";
		}
		if (RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())) {
			// 分销信息
			if (registerBill.getSalesType() != null
					&& registerBill.getSalesType().intValue() == SalesTypeEnum.SEPARATE_SALES.getCode().intValue()) {
				// 分销
				List<SeparateSalesRecord> records = separateSalesRecordService
						.findByRegisterBillCode(registerBill.getCode());
				modelMap.put("separateSalesRecords", records);
			}
		} else {
			QualityTraceTradeBill condition = DTOUtils.newDTO(QualityTraceTradeBill.class);
			condition.setRegisterBillCode(registerBill.getCode());
			modelMap.put("qualityTraceTradeBills", qualityTraceTradeBillService.listByExample(condition));
		}
		
		
//		DetectRecord conditon=DTOUtils.newDTO(DetectRecord.class);
//		conditon.setRegisterBillCode(registerBill.getCode());
//		conditon.setSort("id");
//		conditon.setOrder("desc");
		List<DetectRecord>detectRecordList=this.detectRecordService.findTop2AndLatest(registerBill.getCode());
		modelMap.put("detectRecordList", detectRecordList);
		modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));
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
		QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService.get(id);
		SeparateSalesRecord condition = DTOUtils.newDTO(SeparateSalesRecord.class);
		condition.setTradeNo(qualityTraceTradeBill.getOrderId());
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
	@RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
	public String modify(ModelMap modelMap, @PathVariable Long id) {
		RegisterBill registerBill = registerBillService.get(id);
		if (registerBill == null) {
			return "";
		}
		if (RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())) {
			// 分销信息
			if (registerBill.getSalesType() != null
					&& registerBill.getSalesType().intValue() == SalesTypeEnum.SEPARATE_SALES.getCode().intValue()) {
				// 分销
				List<SeparateSalesRecord> records = separateSalesRecordService
						.findByRegisterBillCode(registerBill.getCode());
				modelMap.put("separateSalesRecords", records);
			}
		} else {
			QualityTraceTradeBill condition = DTOUtils.newDTO(QualityTraceTradeBill.class);
			condition.setRegisterBillCode(registerBill.getCode());
			modelMap.put("qualityTraceTradeBills", qualityTraceTradeBillService.listByExample(condition));
		}
		modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));
		return "registerBill/modify";
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
	public @ResponseBody BaseOutput doBatchAutoCheck(ModelMap modelMap,@RequestBody List<Long> idList) {
//		modelMap.put("registerBill", registerBillService.get(id));
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
	public @ResponseBody BaseOutput doBatchSamplingCheck(ModelMap modelMap,@RequestBody List<Long> idList) {
//		modelMap.put("registerBill", registerBillService.get(id));
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
	public @ResponseBody BaseOutput doBatchAudit(ModelMap modelMap,@RequestBody  BatchAuditDto batchAuditDto) {
//		modelMap.put("registerBill", registerBillService.get(id));
//		if (batchAuditDto.getPass() == null) {
//			return BaseOutput.failure("参数错误");
//		}
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
		if (StringUtils.isNotBlank(registerBill.getAttrValue())) {
			switch (registerBill.getAttr()) {
			case "code":
				registerBill.setCode(registerBill.getAttrValue());
				break;
			case "plate":
				registerBill.setPlate(registerBill.getAttrValue());
				break;
			case "tallyAreaNo":
				registerBill.setTallyAreaNo(registerBill.getAttrValue());
				break;
			case "latestDetectOperator":
				registerBill.setLatestDetectOperator(registerBill.getAttrValue());
				break;
			case "name":
				registerBill.setName(registerBill.getAttrValue());
				break;
			case "productName":
				registerBill.setProductName(registerBill.getAttrValue());
				break;
			}
		}
		return registerBillService.listEasyuiPageByExample(registerBill, true).toString();
	}

	@RequestMapping(value = "/listStaticsData.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> listStaticsData(RegisterBillDto registerBill) {

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
		RegisterBillOutputDto registerBill = registerBillService.findByTradeNo(tradeNo);
		QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService.findByTradeNo(tradeNo);

		if (null != registerBill) {
			registerBill.setDetectRecord(detectRecordService.findByRegisterBillCode(registerBill.getCode()));
		}
		modelMap.put("tradeBill", registerBill);
		modelMap.put("qualityTraceTradeBill", qualityTraceTradeBill);
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
		QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService.get(id);
		RegisterBill bill = registerBillService.findByCode(qualityTraceTradeBill.getRegisterBillCode());
		modelMap.put("registerBill", bill);
		modelMap.put("qualityTraceTradeBill", qualityTraceTradeBill);
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
		QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService
				.findByTradeNo(separateSalesRecord.getTradeNo());
		RegisterBill bill = registerBillService.findByCode(qualityTraceTradeBill.getRegisterBillCode());
		modelMap.put("registerBill", bill);
		modelMap.put("qualityTraceTradeBill", qualityTraceTradeBill);
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
		UserInfoDto userInfoDto = this.findUserInfoDto(registerBill);
		modelMap.put("userInfo", this.maskUserInfoDto(userInfoDto));
		modelMap.put("tradeTypes", tradeTypeService.findAll());
		modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));
		modelMap.put("citys", this.queryCitys());
		return "registerBill/copy";
	}

	private UserInfoDto findUserInfoDto(RegisterBill registerBill) {
		UserInfoDto userInfoDto=new UserInfoDto();
		if (registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TALLY_AREA.getCode().intValue()) {
			// 理货区
			User user = userService.findByTallyAreaNo(registerBill.getTallyAreaNo());
		
			if(user!=null) {
				userInfoDto.setUserId(String.valueOf(user.getId()));
				userInfoDto.setName(user.getName());
				userInfoDto.setIdCardNo(user.getCardNo());
				userInfoDto.setPhone(user.getPhone());
				userInfoDto.setAddr(user.getAddr());
				
			}
	
		} else {

			Customer condition = DTOUtils.newDTO(Customer.class);
			condition.setCustomerId(StringUtils.trimToNull(registerBill.getTradeAccount()));
			condition.setPrintingCard(StringUtils.trimToNull(registerBill.getTradePrintingCard()));
			Customer customer = this.customerService.findByCustomerIdAndPrintingCard(condition).stream().findFirst()
					.orElse(null);
			if(customer!=null) {
				userInfoDto.setUserId(customer.getCustomerId());
				userInfoDto.setName(customer.getName());
				userInfoDto.setIdCardNo(customer.getIdNo());
				userInfoDto.setPhone(customer.getPhone());
				userInfoDto.setAddr(customer.getAddress());
				userInfoDto.setPrintingCard(customer.getPrintingCard());
			}
			
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

	/**
	 * 保存处理结果
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/doModifyRegisterBill.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<?> doModifyRegisterBill(RegisterBill input) {
		try {
			Long id = this.registerBillService.doModifyRegisterBill(input);
			return BaseOutput.success().setData(id);
		} catch (AppException e) {
			LOGGER.error(e.getMessage(),e);
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			return BaseOutput.failure("服务端出错");
		}

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