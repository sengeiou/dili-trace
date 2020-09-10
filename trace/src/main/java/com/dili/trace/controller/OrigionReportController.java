package com.dili.trace.controller;

import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.BillReportQueryDto;
import com.dili.trace.dto.OrigionReportDto;
import com.dili.trace.dto.OrigionReportQueryDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.ReportService;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Api("/origionReport")
@Controller
@RequestMapping("/origionReport")
public class OrigionReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation("跳转到OrigionReport页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {

        OrigionReportQueryDto query=new OrigionReportQueryDto();
        Date now = new Date();
        query.setEndDate(DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        query.setStartDate(DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        modelMap.put("query", query);
        return "origionReport/index";
    }

    @ApiOperation(value = "查询OrigionReport", notes = "查询ROrigionReport，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "OrigionReport", paramType = "form", value = "OrigionReport的form信息", required = false, dataType = "string") })
    @RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    List<OrigionReportDto> list(OrigionReportQueryDto origionReport) {
        String productName = origionReport.getProductName();
        String origionName = origionReport.getOrigionName();
        if(StringUtils.isNotBlank(productName))
        {
            origionReport.setProductName("%"+productName+"%");
        }
        if(StringUtils.isNotBlank(origionName))
        {
            origionReport.setOrigionName("%"+origionName+"%");
        }
        return reportService.origionReportList(origionReport);
    }
}
