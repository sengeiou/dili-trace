package com.dili.trace.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
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
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
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
	@ApiOperation("跳转到RegisterBill产品统计页面")
	@RequestMapping(value = "/product-report-detail.html", method = RequestMethod.GET)
	public String productReportDetail(ModelMap modelMap,RegisterBillReportQueryDto dto) {
		Date now = new Date();
		modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
		modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));

		UserTicket user = SessionContext.getSessionContext().getUserTicket();
		modelMap.put("user", user);

		RegisterBillReportQueryDto queryDto = this.calAndSetDates(dto);
		try {
			List<GroupByProductReportDto> list = this.registerBillReportService.listGroupByProduct(queryDto);
			modelMap.put("items", list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			modelMap.put("item",  new GroupByProductReportDto());
		}
		try {
//			queryDto.setProductName(null);
			queryDto.setProductIdList(Arrays.asList());
			GroupByProductReportDto summaryItem= this.registerBillReportService.summaryGroup(queryDto);
			modelMap.put("summaryItem", summaryItem!=null?summaryItem: new GroupByProductReportDto());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			modelMap.put("summaryItem",  new GroupByProductReportDto());
		}
		return "registerBillReport/product-report-detail";
	}

	@ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string") })
	@RequestMapping(value = "/listPageGroupByProduct.action", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String listPageGroupByProduct(RegisterBillReportQueryDto dto) throws Exception {
		RegisterBillReportQueryDto queryDto = this.calAndSetDates(dto);
		return registerBillReportService.listPageGroupByProduct(queryDto).toString();
	}

	private RegisterBillReportQueryDto calAndSetDates(RegisterBillReportQueryDto dto) {

		if (RegisterSourceEnum.TALLY_AREA.getCode().equals(dto.getRegisterSource())) {
			dto.setTradeTypeId(null);
		}

		LocalDate start = dto.getCreatedStart();
		LocalDate end = dto.getCreatedEnd();
		if(start!=null&&end!=null){
			boolean wholeDay=start.isEqual(end);
			boolean wholeMonth=(start.getYear()==end.getYear()&&start.getMonthValue()==end.getMonthValue()&&(start.getDayOfMonth()==start.with(TemporalAdjusters.firstDayOfMonth()).getDayOfMonth()&&end.getDayOfMonth()==end.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()));
			boolean wholeYear=(start.getYear()==end.getYear()&&start.getMonthValue()==1&&end.getMonthValue()==12&&(start.getDayOfMonth()==start.with(TemporalAdjusters.firstDayOfMonth()).getDayOfMonth()&&end.getDayOfMonth()==end.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()));
			
			if(wholeDay) {
				
				dto.setMomStart(start.minusDays(1));
				dto.setMomEnd(end.minusDays(1));
				
				
				//如果上一年当月没有今天，就不计算同比数据
				if(start.getDayOfMonth()>start.withDayOfMonth(1).minusYears(1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()) {
					dto.setYoyStart(null);
					dto.setYoyEnd(null);
				}else {
					dto.setYoyStart(start.minusYears(1));
					dto.setYoyEnd(end.minusYears(1));
				}
				
				
			}
			if(wholeMonth) {
				//整月的时候，把天先设置为1，防止出错
				dto.setMomStart(start.withDayOfMonth(1).minusMonths(1));
				dto.setMomEnd(end.withDayOfMonth(1).minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));
				
				dto.setYoyStart(start.withDayOfMonth(1).minusYears(1));
				dto.setYoyEnd(end.withDayOfMonth(1).minusYears(1).with(TemporalAdjusters.lastDayOfMonth()));
			}
			if(wholeYear) {
				dto.setMomStart(start.withDayOfYear(1).minusYears(1).with(TemporalAdjusters.firstDayOfYear()));
				dto.setMomEnd(end.withDayOfYear(1).minusYears(1).with(TemporalAdjusters.lastDayOfYear()));
				//整年的时候，没有同比
				dto.setYoyStart(null);
				dto.setYoyEnd(null);
			}
			
			
		}else {
			dto.setMomStart(null);
			dto.setMomEnd(null);
			dto.setYoyStart(null);
			dto.setYoyEnd(null);
		}
		
		return dto;

	}
	public static void main(String[] args) {
		RegisterBillReportQueryDto dto=new RegisterBillReportQueryDto();
		LocalDate now=LocalDate.now();
		dto.setCreatedStart(now.withMonth(3).withDayOfMonth(1));
		dto.setCreatedEnd(now.withMonth(3).withDayOfMonth(31));
		
		RegisterBillReportController c=new RegisterBillReportController();
		c.calAndSetDates(dto);
		System.out.println(dto.getMomStart());
		System.out.println(dto.getMomEnd());
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
			if (Boolean.TRUE.equals(dto.getSumOthers()) && dto.getSumAsOthersMoreThan() != null
					&& dto.getSumAsOthersMoreThan() > 0) {
				list = this.sumOthers(list, dto.getSumAsOthersMoreThan(), () -> {

					GroupByProductReportDto otherSummaryDto = new GroupByProductReportDto();
					otherSummaryDto.setProductName("其他+");
					return otherSummaryDto;

				},t -> {
					StringBuilder label=new StringBuilder();
					
					label.append(t.getProductName());
					 t.setLabel(label.toString());
					 return t;
				});
			}

			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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
		RegisterBillReportQueryDto queryDto = this.calAndSetDates(dto);
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
			if (Boolean.TRUE.equals(dto.getSumOthers()) && dto.getSumAsOthersMoreThan() != null
					&& dto.getSumAsOthersMoreThan() > 0) {
				list = this.sumOthers(list, dto.getSumAsOthersMoreThan(), () -> {

					GroupByProductReportDto otherSummaryDto = new GroupByProductReportDto();
					otherSummaryDto.setPlate("");
					otherSummaryDto.setName("其他+");
					return otherSummaryDto;

				},t -> {
					StringBuilder label=new StringBuilder();
					
					label.append(t.getName());
					if(StringUtils.isNotBlank(t.getPlate())) {
						label.append(" ");
						label.append(t.getPlate());
					}
					 t.setLabel(label.toString());
					 return t;
				});
			}

			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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
		RegisterBillReportQueryDto queryDto = this.calAndSetDates(dto);
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
					
				},t -> {
					StringBuilder label=new StringBuilder();
					
					label.append(t.getOriginName());
					 t.setLabel(label.toString());
					 return t;
				});
			}

			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return BaseOutput.failure(e.getMessage());
		}

	}

	private List<GroupByProductReportDto> sumOthers(List<GroupByProductReportDto> list, Integer sumIndex,
			Supplier<GroupByProductReportDto> otherSummary,
			Function<GroupByProductReportDto, GroupByProductReportDto> labelFun) {
		if (sumIndex != null && sumIndex >= 0 && sumIndex < list.size()) {
			List<GroupByProductReportDto> otherList = list.subList(sumIndex, list.size());

			GroupByProductReportDto otherSummaryDto = otherSummary.get();
			List<String> methodNames = Stream.of(otherSummaryDto.getClass().getDeclaredMethods()).map(Method::getName)
					.collect(Collectors.toList());

			List<Pair<Method, Method>> methodPairs = Stream.of(PropertyUtils.getPropertyDescriptors(otherSummaryDto))
					.filter(p -> p.getReadMethod().getReturnType() == Long.class||p.getReadMethod().getName().equals("getTotalWeight"))
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
							if(tValue!=null||uValue!=null) {
								Class<?>type=(tValue!=null?tValue.getClass():uValue.getClass());
								if(type==Long.class) {
									
									Long summaryValue = (tValue==null?0L:(Long)tValue) + (uValue==null?0L:(Long)uValue);
									pair.getRight().invoke(t, summaryValue);
									
								}else if(type==BigDecimal.class) {
									
									BigDecimal summaryValue = ((tValue==null?BigDecimal.ZERO:(BigDecimal)tValue)).add((uValue==null?BigDecimal.ZERO:(BigDecimal)uValue));
									pair.getRight().invoke(t, summaryValue);
								}
								
							}

						} catch (IllegalAccessException | InvocationTargetException e) {
							logger.error(e.getMessage(), e);
						}

					});
					return t;
				}

			});
			list.removeAll(otherList);
			list.add(otherSummaryDto);
		}
		return list.stream().map(t->{
			
			return labelFun.apply(t);
		}).collect(Collectors.toList());

	}

}