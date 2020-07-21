package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/traceReport")
public class TraceReportController {
    
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
        return "traceReport/index";
    }

    @RequestMapping(value = "/getReport.action", method = RequestMethod.POST)
    @ResponseBody
	public BaseOutput getReport(ModelMap modelMap) {
        return BaseOutput.success();
    }

}