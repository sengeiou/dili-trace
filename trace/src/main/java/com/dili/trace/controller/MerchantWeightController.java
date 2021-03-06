package com.dili.trace.controller;

import com.dili.trace.dto.BillSumReportDto;
import com.dili.trace.service.TradeStatisticReportService;
import com.dili.trace.util.MarketUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author asa.lee
 */
@Api("/merchantWeightReport")
@Controller
@RequestMapping("/merchantWeightReport")
public class MerchantWeightController {

    private static final Logger logger = LoggerFactory.getLogger(MerchantWeightController.class);

    @Autowired
    TradeStatisticReportService tradeStatisticReportService;

    /**
     * 跳转到页面
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        LocalDateTime now = LocalDateTime.now();
        modelMap.put("startDate", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
        modelMap.put("endDate", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
        return "merchantWeightReport/index";
    }

    /**
     * 查询
     * @param query
     * @return
     */
    @ApiOperation("查询")
    @RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    List<BillSumReportDto> list(BillSumReportDto query) {
        query.setMarketId(MarketUtil.returnMarket());
        List<BillSumReportDto> list = tradeStatisticReportService.getMerchantReportList(query);
        return list;
    }
}
