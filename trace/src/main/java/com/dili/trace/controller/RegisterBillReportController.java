package com.dili.trace.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.common.service.BaseInfoRpcService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.DateUtils;
import com.dili.trace.dto.GroupByProductReportDto;
import com.dili.trace.dto.RegisterBillReportQueryDto;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.CustomerService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.QualityTraceTradeBillService;
import com.dili.trace.service.RegisterBillReportService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.TradeTypeService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Api("/registerBillReport")
@Controller
@RequestMapping("/registerBillReport")
public class RegisterBillReportController {
	private static final Logger logger = LoggerFactory.getLogger(RegisterBillReportController.class);
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
	public @ResponseBody String listPageGroupByProduct(RegisterBillReportQueryDto dto) throws Exception {
		RegisterBillReportQueryDto queryDto=this.calAndSetDates(dto);
		return registerBillReportService.listPageGroupByProduct(queryDto).toString();
	}
	private RegisterBillReportQueryDto calAndSetDates(RegisterBillReportQueryDto dto) {
		
		if (RegisterSourceEnum.TALLY_AREA.getCode().equals(dto.getRegisterSource())) {
			dto.setTradeTypeId(null);
		}
	
		LocalDate start = dto.getCreatedStart();
		LocalDate end = dto.getCreatedEnd();
		if (start != null && end != null) {
			if (start.isEqual(end)) {// 整天
	
				dto.setMomStart(start.minusDays(1));
				dto.setMomEnd(end.minusDays(1));
	
				dto.setYoyStart(start.minusYears(1));
				dto.setYoyEnd(end.minusYears(1));
	
			} else if (start.getYear() == end.getYear() && start.getMonth() == end.getMonth()
					&& start.getDayOfMonth() == 1) {
	
				int lastDayOfMonth = end.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
				if (end.getDayOfMonth() == lastDayOfMonth) {// 整月
	
					dto.setMomStart(start.minusMonths(1));
					dto.setMomEnd(end.minusMonths(1));
	
					dto.setYoyStart(start.minusYears(1));
					dto.setYoyEnd(end.minusYears(1));
	
				}
	
			}
	
		}
		return dto;
		
	}
	@ApiOperation("跳转到RegisterBill产品统计页面")
	@RequestMapping(value = "/product-charts.html", method = RequestMethod.GET)
	public String productCharts(ModelMap modelMap, String params) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);
		if (StringUtils.isBlank(params)) {
			modelMap.put("params", "{}");
		} else {
			modelMap.put("params", params);
		}

		return "registerBillReport/product-charts";
	}

	@RequestMapping(value = "/getProductChartsJson.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getProductChartsJson(@RequestBody RegisterBillReportQueryDto dto) throws Exception {
		try {
			List<GroupByProductReportDto> list = this.registerBillReportService.listGroupByProduct(dto);
			if(Boolean.TRUE.equals(dto.getSumOthers())&&dto.getSumAsOthersMoreThan()!=null&&dto.getSumAsOthersMoreThan()>0) {
				list=this.sumOthers(list, dto.getSumAsOthersMoreThan(),()->{
					
					GroupByProductReportDto otherSummaryDto =new GroupByProductReportDto();
					otherSummaryDto.setProductName("其他+");
					return otherSummaryDto;
					
				});
			}

			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return BaseOutput.failure(e.getMessage());
		}

	}

	
	@ApiOperation("跳转到RegisterBill产品统计页面")
	@RequestMapping(value = "/plate-report.html", method = RequestMethod.GET)
	public String plateReport(ModelMap modelMap) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);
		modelMap.put("registerSource", RegisterSourceEnum.TALLY_AREA.getCode());

		return "registerBillReport/plate-report";
	}

	@ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPageGroupByPlate.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPageGroupByPlate(RegisterBillReportQueryDto dto) throws Exception {
		RegisterBillReportQueryDto queryDto=this.calAndSetDates(dto);
		return registerBillReportService.listPageGroupByNameAndPlate(queryDto).toString();
	}

	@ApiOperation("跳转到RegisterBill产品统计页面")
	@RequestMapping(value = "/plate-charts.html", method = RequestMethod.GET)
	public String plateCharts(ModelMap modelMap, String params) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);
		if (StringUtils.isBlank(params)) {
			modelMap.put("params", "{}");
		} else {
			modelMap.put("params", params);
		}

		return "registerBillReport/plate-charts";
	}

	@RequestMapping(value = "/getPlateChartsJson.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getPlteChartsJson(@RequestBody RegisterBillReportQueryDto dto) throws Exception {
		try {
			List<GroupByProductReportDto> list = this.registerBillReportService.listGroupByNameAndPlate(dto);
			if(Boolean.TRUE.equals(dto.getSumOthers())&&dto.getSumAsOthersMoreThan()!=null&&dto.getSumAsOthersMoreThan()>0) {
				list=this.sumOthers(list, dto.getSumAsOthersMoreThan(),()->{
					
					GroupByProductReportDto otherSummaryDto =new GroupByProductReportDto();
					otherSummaryDto.setPlate("");
					otherSummaryDto.setName("其他+");
					return otherSummaryDto;
					
				});
			}

			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return BaseOutput.failure(e.getMessage());
		}

	}
	
	
	
	@ApiOperation("跳转到RegisterBill产品统计页面")
	@RequestMapping(value = "/origin-report.html", method = RequestMethod.GET)
	public String originReport(ModelMap modelMap) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);
		modelMap.put("registerSource", RegisterSourceEnum.TALLY_AREA.getCode());

		return "registerBillReport/origin-report";
	}

	@ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPageGroupByOrigin.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPageGroupByOrigin(RegisterBillReportQueryDto dto) throws Exception {
		RegisterBillReportQueryDto queryDto=this.calAndSetDates(dto);
		return registerBillReportService.listPageGroupByOrigin(queryDto).toString();
	}

	@ApiOperation("跳转到RegisterBill产品统计页面")
	@RequestMapping(value = "/origin-charts.html", method = RequestMethod.GET)
	public String originCharts(ModelMap modelMap, String params) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);
		if (StringUtils.isBlank(params)) {
			modelMap.put("params", "{}");
		} else {
			modelMap.put("params", params);
		}

		return "registerBillReport/origin-charts";
	}

	@RequestMapping(value = "/getOriginChartsJson.action", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object getOriginChartsJson(@RequestBody RegisterBillReportQueryDto dto) throws Exception {
		try {
			List<GroupByProductReportDto> list = this.registerBillReportService.listGroupByOrigin(dto);
			if(Boolean.TRUE.equals(dto.getSumOthers())&&dto.getSumAsOthersMoreThan()!=null&&dto.getSumAsOthersMoreThan()>0) {
				list=this.sumOthers(list, dto.getSumAsOthersMoreThan(),()->{
					
					GroupByProductReportDto otherSummaryDto =new GroupByProductReportDto();
					otherSummaryDto.setOriginName("其他+");
					return otherSummaryDto;
					
				});
			}

			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return BaseOutput.failure(e.getMessage());
		}

	}
	
	
	private List<GroupByProductReportDto> sumOthers(List<GroupByProductReportDto> list, Integer sumIndex,Supplier<GroupByProductReportDto>otherSummary) {
		if (sumIndex != null && sumIndex > 0 && sumIndex < list.size()) {
			List<GroupByProductReportDto> otherList = list.subList(sumIndex, list.size());

			GroupByProductReportDto otherSummaryDto = otherSummary.get();
			List<String> methodNames = Stream.of(otherSummary.getClass().getDeclaredMethods()).map(Method::getName)
					.collect(Collectors.toList());

			List<Pair<Method, Method>> methodPairs = Stream.of(PropertyUtils.getPropertyDescriptors(otherSummary))
					.filter(p -> p.getReadMethod().getReturnType() == Long.class)
					.filter(p -> p.getWriteMethod().getParameterCount() == 1)
					.filter(p -> p.getWriteMethod().getParameterTypes()[0] == p.getReadMethod().getReturnType())
					.filter(p -> methodNames.contains(p.getReadMethod().getName()))
					.filter(p -> methodNames.contains(p.getWriteMethod().getName())).map(p -> {
						Pair<Method, Method> pair = MutablePair.of(p.getReadMethod(), p.getWriteMethod());
						return pair;

					}).collect(Collectors.toList());

			otherList.stream().reduce(otherSummaryDto, new BinaryOperator<GroupByProductReportDto>() {

				@Override
				public GroupByProductReportDto apply(GroupByProductReportDto t, GroupByProductReportDto u) {
					methodPairs.stream().forEach(pair -> {
						try {
							Object tValue = pair.getKey().invoke(t, new Object[0]);
							Object uValue = pair.getKey().invoke(u, new Object[0]);
							if (uValue != null) {
									Long summaryValue = ((Long) tValue) + ((Long) uValue);
									pair.getRight().invoke(t, summaryValue);

							}

						} catch (IllegalAccessException | InvocationTargetException  e) {
							logger.error(e.getMessage(), e);
						}

					});
					return t;
				}

			});
			list.removeAll(otherList);
			list.add(otherSummaryDto);
		}
		for( int i=0;i<list.size();i++) {
			list.get(i).setRownum(i+1);
		}
		return list;

	}

}