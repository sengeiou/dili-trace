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

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
	public String productReport(ModelMap modelMap) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);

		return "registerBillReport/product-report";
	}

	@ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPageGroupByProduct.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPage(RegisterBillReportQueryDto dto) throws Exception {
		if(RegisterSourceEnum.TALLY_AREA.getCode().equals(dto.getRegisterSource())) {
			dto.setTradeTypeId(null);
		}
		
		LocalDate start=dto.getCreatedStart();
		LocalDate end=dto.getCreatedEnd();
		if(start!=null&&end!=null) {
			if(start.isEqual(end)) {//整天
				
				dto.setMomStart(start.minusDays(1));
				dto.setMomEnd(end.minusDays(1));
				
				dto.setYoyStart(start.minusYears(1));
				dto.setYoyEnd(end.minusYears(1));
				
			}else if(start.getYear()==end.getYear()&&start.getMonth()==end.getMonth()&&start.getDayOfMonth()==1) {
				
				int lastDayOfMonth=end.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
				if(end.getDayOfMonth()==lastDayOfMonth) {//整月
				
					
					dto.setMomStart(start.minusMonths(1));
					dto.setMomEnd(end.minusMonths(1));
					
					
					dto.setYoyStart(start.minusYears(1));
					dto.setYoyEnd(end.minusYears(1));					
					
				}
				
			}
			
			
		}
		return registerBillReportService.listPageGroupByProduct(dto).toString();
	}
	@ApiOperation("跳转到RegisterBill产品统计页面")
	@RequestMapping(value = "/product-charts.html", method = RequestMethod.GET)
	public String productCharts(ModelMap modelMap) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);

		return "registerBillReport/product-charts";
	}

	@RequestMapping(value = "/getProductChartsJson.action",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Object getProductChartsJson(String type) {
		List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
		
		Map<String, Object>data=new HashMap<String, Object>();
		data.put("id", "1");
		data.put("name", "abc");
		
		list.add(data);
		return list;
	}
}