package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.dto.*;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.TradeTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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

	@ApiOperation("跳转到RegisterBill页面")
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
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
		for (RegisterBill registerBill : registerBills) {
			registerBill.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
			if (registerBillService.createRegisterBill(registerBill) == 0) {
				return BaseOutput.success("新增失败");
			}
			;
		}
		return BaseOutput.success("新增成功");
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
	 * 登记单录入页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String create(ModelMap modelMap, @PathVariable Long id) {
		RegisterBillOutputDto registerBill = (RegisterBillOutputDto) registerBillService.get(id);
		List<SeparateSalesRecord> records = separateSalesRecordService.findByRegisterBillCode(registerBill.getCode());
		registerBill.setSeparateSalesRecords(records);
		DetectRecord detectRecord = detectRecordService.findByRegisterBillCode(registerBill.getCode());
		if (detectRecord == null) {
			detectRecord = DTOUtils.newDTO(DetectRecord.class);
			detectRecord.setPdResult("");
			detectRecord.setDetectOperator("");
		}
		registerBill.setDetectRecord(detectRecord);
		modelMap.put("registerBill", registerBill);
		return "registerBill/view";
	}

	@RequestMapping(value = "/audit/{id}", method = RequestMethod.GET)
	public String audit(ModelMap modelMap, @PathVariable Long id) {
		modelMap.put("registerBill", registerBillService.get(id));
		return "registerBill/audit";
	}

	@RequestMapping(value = "/audit/{id}/{pass}", method = RequestMethod.GET)
	public @ResponseBody BaseOutput audit(@PathVariable Long id, @PathVariable Boolean pass) {
		registerBillService.auditRegisterBill(id, pass);
		return BaseOutput.success("操作成功");
	}

	@RequestMapping(value = "/undo/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseOutput undo(@PathVariable Long id) {
		registerBillService.undoRegisterBill(id);
		return BaseOutput.success("操作成功");
	}

	@RequestMapping(value = "/autoCheck/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseOutput autoCheck(@PathVariable Long id) {
		registerBillService.autoCheckRegisterBill(id);
		return BaseOutput.success("操作成功");
	}

	@RequestMapping(value = "/samplingCheck/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseOutput samplingCheck(@PathVariable Long id) {
		registerBillService.samplingCheckRegisterBill(id);
		return BaseOutput.success("操作成功");
	}

	@RequestMapping(value = "/reviewCheck/{id}", method = RequestMethod.GET)
	public @ResponseBody BaseOutput reviewCheck(@PathVariable Long id) {
		registerBillService.reviewCheckRegisterBill(id);
		return BaseOutput.success("操作成功");
	}

	@ApiOperation("跳转到statics页面")
	@RequestMapping(value = "/statics.html", method = RequestMethod.GET)
	public String statics(ModelMap modelMap) {
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

}