package com.dili.trace.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.TraceReportDto;
import com.dili.trace.dto.TraceReportQueryDto;
import com.dili.trace.service.TraceReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/traceReport")
public class TraceReportController {
    @Autowired
    TraceReportService traceReportService;

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap,TraceReportQueryDto query) {

        // TraceReportQueryDto query=new TraceReportQueryDto();
        // query.setReadonly(readonly);
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
        
        

        Map<String, TraceReportDto>data= this.traceReportService.getTraceBillReportData(query);
       
        Map<String, TraceReportDto> commonCheckinData=this.traceReportService.getCommonCheckinReportData(query);
        TraceReportDto supplementCheckinData=this.traceReportService.getSupplementCheckinReportData(query);

        modelMap.put("data", data);
        modelMap.put("commonCheckinData", commonCheckinData);
        modelMap.put("supplementCheckinData", supplementCheckinData);
        modelMap.put("query", query);
        return "traceReport/index";
    }

    @RequestMapping(value = "/getBillReport.action", method = RequestMethod.POST)
    @ResponseBody
    public BaseOutput getBillReport(ModelMap modelMap) {

        return BaseOutput.success();
    }

}