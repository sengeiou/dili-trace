package com.dili.trace.controller;

import com.dili.common.service.BaseInfoRpcService;
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
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.service.*;
import com.dili.trace.util.MaskUserInfo;
import com.diligrp.manage.sdk.domain.UserTicket;
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
import java.util.stream.Stream;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Api("/registerBillReport")
@Controller
@RequestMapping("/registerBillReport")
public class RegisterBillReportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillReportController.class);
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	RegisterBillReportService registerBillReportService;
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

	@ApiOperation("跳转到RegisterBill产品统计页面")
	@RequestMapping(value = "/product-report.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
		
		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);
		List<GroupByProductReportDto>list=registerBillReportService.listPageGroupByProduct();
		return "registerBillReport/product-report.html";
	}
	

	@ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPageGroupByProduct.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(RegisterBillDto registerBill) throws Exception {
		if (StringUtils.isNotBlank(registerBill.getAttrValue())) {
			switch (registerBill.getAttr()) {
			case "code":
				registerBill.setCode(registerBill.getAttrValue());
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
			case "likeSampleCode":
				registerBill.setLikeSampleCode(registerBill.getAttrValue());
				break;
			}
		}

		return registerBillService.listEasyuiPageByExample(registerBill, true).toString();
	}
	
}