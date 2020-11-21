package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.TraceReportDto;
import com.dili.trace.dto.TraceReportQueryDto;
import com.dili.trace.service.TraceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * 溯源报表
 */
@Controller
@RequestMapping("/traceReport")
public class TraceReportController {
    @Autowired
    TraceReportService traceReportService;

    /**
     * 跳转到index
     * @param modelMap
     * @param query
     * @return
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap,TraceReportQueryDto query) {

        // TraceReportQueryDto query=new TraceReportQueryDto();
        // query.setReadonly(readonly);
        if(query.getReadonly()==null){
            query.setReadonly(false);
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime splitTime=now.withHour(20).withMinute(30).withSecond(0);
        if(now.isBefore(splitTime)){
            now=now.minusDays(1);
        }
        now=now.withHour(20).withMinute(30).withSecond(0);

        Date start=Date.from(now.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        Date end=Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        if(query.getCreatedEnd()==null&&query.getCreatedEnd()==null){
            query.setCreatedStart(start);
            query.setCreatedEnd(end);
        }

        // 设置市场查询条件
        // query.setMarketId(MarketUtil.returnMarket());

        // 设置活跃度查询条件
        this.traceReportService.settingUserActive(query);
        

        Map<String, TraceReportDto>data= this.traceReportService.getTraceBillReportData(query);
       
        Map<String, TraceReportDto> commonCheckinData=this.traceReportService.getCommonCheckinReportData(query);
        TraceReportDto supplementCheckinData=this.traceReportService.getSupplementCheckinReportData(query);

        modelMap.put("data", data);
        modelMap.put("commonCheckinData", commonCheckinData);
        modelMap.put("supplementCheckinData", supplementCheckinData);
        modelMap.put("query", query);
        return "traceReport/index";
    }

    /**
     * 查询数据
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/getBillReport.action", method = RequestMethod.POST)
    @ResponseBody
    public BaseOutput getBillReport(ModelMap modelMap) {

        return BaseOutput.success();
    }

}