package com.dili.trace.controller;

import com.dili.ss.util.DateUtils;
import com.dili.trace.dto.OrigionReportQueryDto;
import com.dili.trace.dto.PurchaseGoodsReportDto;
import com.dili.trace.dto.PurchaseGoodsReportQueryDto;
import com.dili.trace.dto.UserPurchaseReportQueryDto;
import com.dili.trace.service.ReportService;
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

@Api("/userPurchaseReport")
@Controller
@RequestMapping("/userPurchaseReport")
public class UserPurchaseReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation("跳转到UserPurchaseReport页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {

        OrigionReportQueryDto query=new OrigionReportQueryDto();
        Date now = new Date();
        query.setEndDate(DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        query.setStartDate(DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        modelMap.put("query", query);
        return "userPurchaseReport/index";
    }

    @ApiOperation(value = "查询UserPurchaseReport", notes = "查询UserPurchaseReport，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "UserPurchaseReport", paramType = "form", value = "UserPurchaseReport的form信息", required = false, dataType = "string") })
    @RequestMapping(value = "/list.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    List<PurchaseGoodsReportDto> list(UserPurchaseReportQueryDto goodsReport) {
        String userName = goodsReport.getUserName();
        String phone = goodsReport.getPhone();
        if(StringUtils.isNotBlank(userName))
        {
            goodsReport.setUserName("%"+userName+"%");
        }
        if(StringUtils.isNotBlank(phone))
        {
            goodsReport.setPhone("%"+phone+"%");
        }
        return reportService.purchaseGoodsReportList(goodsReport);
    }
}
