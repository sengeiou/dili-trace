package com.dili.trace.controller;

import com.dili.ss.util.DateUtils;
import com.dili.trace.dto.OrigionReportQueryDto;
import com.dili.trace.dto.ProductOrigionReportDto;
import com.dili.trace.service.ReportService;
import com.dili.trace.util.MarketUtil;
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

/**
 * 品类报表
 */
@Api("/productOrigionReport")
@Controller
@RequestMapping("/productOrigionReport")
public class ProductOrigionReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 跳转到ProductOrigionReport
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到ProductOrigionReport页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {

        OrigionReportQueryDto query = new OrigionReportQueryDto();
        Date now = new Date();
        query.setEndDate(DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        query.setStartDate(DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        modelMap.put("query", query);
        return "productOrigionReport/index";
    }

    /**
     * 查询ProductOrigionReport
     *
     * @param origionReport
     * @return
     */
    @ApiOperation(value = "查询ProductOrigionReport", notes = "查询ProductOrigionReport，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ProductOrigionReport", paramType = "form", value = "ProductOrigionReport的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<ProductOrigionReportDto> list(OrigionReportQueryDto origionReport) {
        String productName = origionReport.getProductName();
        String origionName = origionReport.getOrigionName();
        if (StringUtils.isNotBlank(productName)) {
            origionReport.setProductName("%" + productName + "%");
        }
        if (StringUtils.isNotBlank(origionName)) {
            origionReport.setOrigionName("%" + origionName + "%");
        }
        origionReport.setMarketId(MarketUtil.returnMarket());
        return reportService.productOrigionReportList(origionReport);
    }
}
