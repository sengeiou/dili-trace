package com.dili.trace.controller;

import java.util.Optional;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.ThirdPartyReportData;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.ThirdPartyReportDataQueryDto;
import com.dili.trace.dto.TraceReportQueryDto;
import com.dili.trace.dto.thirdparty.report.CodeCountDto;
import com.dili.trace.dto.thirdparty.report.MarketCountDto;
import com.dili.trace.dto.thirdparty.report.RegionCountDto;
import com.dili.trace.dto.thirdparty.report.ReportCountDto;
import com.dili.trace.enums.ReportDtoTypeEnum;
import com.dili.trace.jobs.ThirdPartyReportJob;
import com.dili.trace.service.ThirdPartyReportDataService;
import com.dili.trace.service.ThirdPartyReportService;
import com.dili.trace.service.TraceReportService;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/thirdPartyReport")
public class ThirdPartyReportController {
    @Autowired
    TraceReportService traceReportService;
    @Autowired
    ThirdPartyReportJob thirdPartyReportJob;

    @Autowired
    ThirdPartyReportService thirdPartyReportService;

    @Autowired
    ThirdPartyReportDataService thirdPartyReportDataService;

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, TraceReportQueryDto query) {

        return "thirdPartyReport/index";
    }

    @RequestMapping(value = "/listPage.action", method = RequestMethod.POST)
    @ResponseBody
    public String listPage(ModelMap modelMap, ThirdPartyReportDataQueryDto input) throws Exception {
        input.setOperatorName(StringUtils.trimToNull(input.getOperatorName()));
        input.setName(StringUtils.trimToNull(input.getName()));
        return thirdPartyReportDataService.listEasyuiPage(input, true).toString();
    }

    private static Optional<OperatorUser> fromSessionContext() {
        if (SessionContext.getSessionContext() != null) {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (userTicket != null) {
                return Optional.of(new OperatorUser(userTicket.getId(), userTicket.getRealName()));
            }
        }
        return Optional.empty();
    }

    @RequestMapping(value = "/countAll.action", method = RequestMethod.POST)
    @ResponseBody
    public BaseOutput countAll(ModelMap modelMap, @RequestBody ReportCountDto input) {
        if(input==null||input.getCheckBatch()==null||input.getCheckBatch()<0){
            return BaseOutput.failure("参数错误");
        }
        Optional<OperatorUser> opt = this.fromSessionContext();

        this.thirdPartyReportJob.codeCount(opt);
        this.thirdPartyReportJob.marketCount(opt);
        this.thirdPartyReportJob.regionCount(opt);
        this.thirdPartyReportJob.reportCount(opt,input.getCheckBatch());
        return BaseOutput.success();
    }

    @RequestMapping(value = "/reportAgain.action", method = RequestMethod.POST)
    @ResponseBody
    public BaseOutput reportAgain(ModelMap modelMap, @RequestBody ThirdPartyReportData input) {
        if (input == null || input.getId() == null) {
            return BaseOutput.failure("参数错误");
        }

        try {

            ThirdPartyReportData reportData = this.thirdPartyReportDataService.get(input.getId());
            if(reportData.getSuccess()!=null&&reportData.getSuccess()==1){
                return BaseOutput.failure("请不要上传已经成功的数据");
            }
            String json = reportData.getData();
            ObjectMapper mapper = new ObjectMapper();
            if (ReportDtoTypeEnum.codeCount.equalsToCode(reportData.getType())) {
                CodeCountDto dto = mapper.readValue(json, CodeCountDto.class);
                return this.thirdPartyReportService.codeCount(dto, this.fromSessionContext());
            } else if (ReportDtoTypeEnum.regionCount.equalsToCode(reportData.getType())) {
                RegionCountDto dto = mapper.readValue(json, RegionCountDto.class);
                return this.thirdPartyReportService.regionCount(dto, this.fromSessionContext());
            } else if (ReportDtoTypeEnum.reportCount.equalsToCode(reportData.getType())) {
                ReportCountDto dto = mapper.readValue(json, ReportCountDto.class);
                return this.thirdPartyReportService.reportCount(dto, this.fromSessionContext());
            } else if (ReportDtoTypeEnum.marketCount.equalsToCode(reportData.getType())) {
                MarketCountDto dto = mapper.readValue(json, MarketCountDto.class);
                return this.thirdPartyReportService.marketCount(dto, this.fromSessionContext());
            } else {
                return BaseOutput.failure("数据错误");
            }
        } catch (Exception e) {
            return BaseOutput.failure("服务端出错");
        }
    }

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(ModelMap modelMap, @PathVariable Long id) {


		return "thirdPartyReport/view";
	}
}