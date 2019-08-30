package com.dili.trace.controller;

import com.alibaba.fastjson.JSON;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.exception.AppException;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.dto.*;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.glossary.SalesTypeEnum;
import com.dili.trace.service.*;
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
				User user = userService.findByTaillyAreaNo(registerBill.getTallyAreaNo());
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

				if (StringUtils.isNotBlank(registerBill.getTradeAccount())) {
					Customer customer = customerService.findByCustomerId(registerBill.getTradeAccount());
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
		return "registerBill/create";
	}

	/**
	 * 登记单录查看页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(ModelMap modelMap, @PathVariable Long id) {
		RegisterBill rb = registerBillService.get(id);
		if (rb == null) {
			return "";
		}
		RegisterBillOutputDto registerBill = registerBillService.conversionDetailOutput(rb);

		modelMap.put("registerBill", registerBill);
		return "registerBill/view";
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
	 * 登记单溯源（二维码）
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/registerBillQRCode.html", method = RequestMethod.GET)
	public String registerBillQRCcode(Long id, ModelMap modelMap) {
		RegisterBill bill = registerBillService.get(id);
		RegisterBillOutputDto outputDto = registerBillService.conversionDetailOutput(bill);
		modelMap.put("registerBill", outputDto);
		return "registerBill/registerBillQRCode";
	}

	/**
	 * 分销记录溯源（二维码）
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/separateSalesRecordQRCode.html", method = RequestMethod.GET)
	public String separateSalesRecordQRCcode(Long id, ModelMap modelMap) {
		SeparateSalesRecord separateSalesRecord = separateSalesRecordService.get(id);
		RegisterBill bill = registerBillService.findByCode(separateSalesRecord.getRegisterBillCode());
		bill.setSalesType(SalesTypeEnum.ONE_SALES.getCode());
		RegisterBillOutputDto outputDto = registerBillService.conversionDetailOutput(bill);
		outputDto.setSeparateSalesRecords(Arrays.asList(separateSalesRecord));
		modelMap.put("registerBill", outputDto);
		return "registerBill/registerBillQRCode";
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

}