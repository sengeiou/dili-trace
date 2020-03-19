package com.dili.trace.controller;

import com.alibaba.fastjson.JSON;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		return registerBillReportService.listPageGroupByProduct(dto).toString();
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
				list=this.sumOthers(list, dto.getSumAsOthersMoreThan());
			}

			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return BaseOutput.failure(e.getMessage());
		}

	}

	private List<GroupByProductReportDto> sumOthers(List<GroupByProductReportDto> list, Integer sumIndex) {
		if (sumIndex != null && sumIndex > 0 && sumIndex > list.size()) {
			List<GroupByProductReportDto> otherList = list.subList(sumIndex, list.size());

			GroupByProductReportDto otherSummary = new GroupByProductReportDto("其他+");
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

			otherList.stream().reduce(otherSummary, new BinaryOperator<GroupByProductReportDto>() {

				@Override
				public GroupByProductReportDto apply(GroupByProductReportDto t, GroupByProductReportDto u) {
					methodPairs.stream().forEach(pair -> {
						try {
							Object tValue = PropertyUtils.getProperty(t, pair.getKey().getName());
							Object uValue = PropertyUtils.getProperty(u, pair.getKey().getName());
							if (uValue != null) {
									Long summaryValue = ((Long) tValue) + ((Long) uValue);
									pair.getRight().invoke(t, summaryValue);

							}

						} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
							logger.error(e.getMessage(), e);
						}

					});
					return t;
				}

			});
			list.removeAll(otherList);
			list.add(otherSummary);
		}
		return list;

	}

}