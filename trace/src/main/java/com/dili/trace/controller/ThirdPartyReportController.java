package com.dili.trace.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.ThirdPartyReportData;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.ThirdPartyReportDataQueryDto;
import com.dili.trace.dto.TraceReportQueryDto;
import com.dili.trace.dto.thirdparty.report.*;
import com.dili.trace.enums.ReportDtoTypeEnum;
import com.dili.trace.jobs.ThirdPartyReportJob;
import com.dili.trace.service.DataReportService;
import com.dili.trace.service.ThirdPartyReportDataService;
import com.dili.trace.service.TraceReportService;
import com.dili.trace.util.BeanMapUtil;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/thirdPartyReport")
public class ThirdPartyReportController {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyReportController.class);
    @Autowired
    TraceReportService traceReportService;
    @Autowired
    ThirdPartyReportJob thirdPartyReportJob;

    @Autowired
    DataReportService dataReportService;

    @Autowired
    ThirdPartyReportDataService thirdPartyReportDataService;

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, TraceReportQueryDto query) {

        return "thirdPartyReport/index";
    }

    @RequestMapping(value = "/listPage.action", method = RequestMethod.POST)
    @ResponseBody
    public String listPage(ModelMap modelMap, ThirdPartyReportDataQueryDto input) throws Exception {
        input = BeanMapUtil.trimBean(input);
        return thirdPartyReportDataService.listEasyuiPageByExample(input, true).toString();
    }

    private Optional<OperatorUser> fromSessionContext() {
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
        if (input == null || input.getCheckBatch() == null || input.getCheckBatch() < 0) {
            return BaseOutput.failure("参数错误");
        }
        Optional<OperatorUser> opt = this.fromSessionContext();

        this.thirdPartyReportJob.codeCount(opt);
        this.thirdPartyReportJob.marketCount(opt);
        this.thirdPartyReportJob.regionCount(opt);
        this.thirdPartyReportJob.reportCount(opt, input.getCheckBatch());
        return BaseOutput.success();
    }

    @RequestMapping(value = "/reportAgain.action", method = RequestMethod.POST)
    @ResponseBody
    public BaseOutput reportAgain(ModelMap modelMap, @RequestBody ThirdPartyReportData input) {
        if (input == null || input.getId() == null) {
            return BaseOutput.failure("参数错误");
        }

        try {
            Optional<OperatorUser> opt = this.fromSessionContext();
            ThirdPartyReportData reportData = this.thirdPartyReportDataService.get(input.getId());
            if (reportData.getSuccess() != null && reportData.getSuccess() == 1) {
                return BaseOutput.failure("请不要上传已经成功的数据");
            }
            String json = reportData.getData();
            ObjectMapper mapper = new ObjectMapper();
            if (ReportDtoTypeEnum.codeCount.equalsToCode(reportData.getType())) {
                CodeCountDto dto = mapper.readValue(json, CodeCountDto.class);
                return this.dataReportService.codeCount(dto, opt);
            } else if (ReportDtoTypeEnum.regionCount.equalsToCode(reportData.getType())) {
                RegionCountDto dto = mapper.readValue(json, RegionCountDto.class);
                return this.dataReportService.regionCount(dto, opt);
            } else if (ReportDtoTypeEnum.reportCount.equalsToCode(reportData.getType())) {
                ReportCountDto dto = mapper.readValue(json, ReportCountDto.class);
                return this.dataReportService.reportCount(dto, opt);
            } else if (ReportDtoTypeEnum.marketCount.equalsToCode(reportData.getType())) {
                MarketCountDto dto = mapper.readValue(json, MarketCountDto.class);
                return this.dataReportService.marketCount(dto, opt);
            } else if (ReportDtoTypeEnum.thirdUserDelete.equalsToCode(reportData.getType())) {
                ReportUserDeleteDto dto = mapper.readValue(json, ReportUserDeleteDto.class);
                return this.dataReportService.reportUserDelete(dto, opt,null);
            } else if (ReportDtoTypeEnum.thirdUserSave.equalsToCode(reportData.getType())) {
                List<ReportUserDto> dto = mapper.readValue(json, new TypeReference<List<ReportUserDto>>() {
                });
                return this.dataReportService.reportUserSaveUpdate(dto, opt,null);
            } else if (ReportDtoTypeEnum.userQrCode.equalsToCode(reportData.getType())) {
                List<ReportQrCodeDto> dto = mapper.readValue(json, new TypeReference<List<ReportQrCodeDto>>() {
                });
                return this.dataReportService.reportUserQrCode(dto, opt,null);
            } else if (ReportDtoTypeEnum.categoryBigLevel.equalsToCode(reportData.getType())) {
                List<CategoryDto> dto = mapper.readValue(json, new TypeReference<List<CategoryDto>>() {
                });
                return this.dataReportService.reportCategory(dto, opt,null);
            } else if (ReportDtoTypeEnum.categorySmallLevel.equalsToCode(reportData.getType())) {
                List<CategorySecondDto> dto = mapper.readValue(json, new TypeReference<List<CategorySecondDto>>() {
                });
                return this.dataReportService.reportSecondCategory(dto, opt,null);
            } else if (ReportDtoTypeEnum.goods.equalsToCode(reportData.getType())) {
                List<GoodsDto> dto = mapper.readValue(json, new TypeReference<List<GoodsDto>>() {
                });
                return this.dataReportService.reportGoods(dto, opt,null);
            } else if (ReportDtoTypeEnum.registerBill.equalsToCode(reportData.getType())) {
                List<ReportRegisterBillDto> dto = mapper.readValue(json, new TypeReference<List<ReportRegisterBillDto>>() {
                });
                return this.dataReportService.reportRegisterBill(dto, opt,null);
            } else if (ReportDtoTypeEnum.upstream.equalsToCode(reportData.getType())) {
                List<UpStreamDto> dto = mapper.readValue(json, new TypeReference<List<UpStreamDto>>() {
                });
                return this.dataReportService.reportUpStream(dto, opt,null);
            } else if (ReportDtoTypeEnum.downstream.equalsToCode(reportData.getType())) {
                List<DownStreamDto> dto = mapper.readValue(json, new TypeReference<List<DownStreamDto>>() {
                });
                return this.dataReportService.reportDownStream(dto, opt,null);
            } else if (ReportDtoTypeEnum.scanCodeOrder.equalsToCode(reportData.getType())) {
                List<ReportScanCodeOrderDto> dto = mapper.readValue(json, new TypeReference<List<ReportScanCodeOrderDto>>() {
                });
                return this.dataReportService.reportScanCodeOrder(dto, opt,null);
            } else if (ReportDtoTypeEnum.deliveryOrder.equalsToCode(reportData.getType())) {
                List<ReportDeliveryOrderDto> dto = mapper.readValue(json, new TypeReference<List<ReportDeliveryOrderDto>>() {
                });
                return this.dataReportService.reportDeliveryOrder(dto, opt,null);
            } else if (ReportDtoTypeEnum.inDoor.equalsToCode(reportData.getType())) {
                List<ReportCheckInDto> dto = mapper.readValue(json, new TypeReference<List<ReportCheckInDto>>() {
                });
                return this.dataReportService.reportCheckIn(dto, opt,null);
            } else if (ReportDtoTypeEnum.deleteScanCodeOrder.equalsToCode(reportData.getType())) {
                ReportDeletedOrderDto dto = mapper.readValue(json, ReportDeletedOrderDto.class);
                return this.dataReportService.reportDeletedScanCodeOrder(dto, opt,null);
            } else if (ReportDtoTypeEnum.deleteDeliveryOrder.equalsToCode(reportData.getType())) {
                ReportDeletedOrderDto dto = mapper.readValue(json, ReportDeletedOrderDto.class);
                return this.dataReportService.reportDeletedDeliveryOrder(dto, opt,null);
            } else if (ReportDtoTypeEnum.registerBillDelete.equalsToCode(reportData.getType())) {
                ReportRegisterBillDeleteDto dto = mapper.readValue(json, ReportRegisterBillDeleteDto.class);
                return this.dataReportService.reportRegisterBillDelete(dto, opt,null);
            }else {
                return BaseOutput.failure("数据错误");
            }
        } catch (Exception e) {
            return BaseOutput.failure("服务端出错");
        }
    }

    @RequestMapping(value = "/dailyReport.action", method = RequestMethod.POST)
    @ResponseBody
    public BaseOutput dailyReport(ModelMap modelMap, @RequestBody ThirdPartyReportDataQueryDto input) {
        if (StringUtils.isBlank(input.getCreatedStart()) || StringUtils.isBlank(input.getCreatedEnd())) {
            return BaseOutput.failure("参数错误");
        }
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate start = null;
        LocalDate end = null;
        try {
            TemporalAccessor startTA = f.parse(input.getCreatedStart());
            start = LocalDate.from(startTA);
        } catch (Exception e) {
            return BaseOutput.failure("开始时间格式错误，请输入yyyy-MM-dd格式");
        }

        try {
            TemporalAccessor endTA = f.parse(input.getCreatedEnd());
            end = LocalDate.from(endTA);
        } catch (Exception e) {
            return BaseOutput.failure("结束时间格式错误，请输入yyyy-MM-dd格式");
        }
        if (start.compareTo(end) >= 0) {
            return BaseOutput.failure("开始时间不能大于等于结束时间");
        }
        if (end.compareTo(LocalDate.now()) >= 0) {
            return BaseOutput.failure("结束时间不能大于或者等于今天");
        }
        List<LocalDate> dateList = Lists.newArrayList();
        while (true) {
            if (start.compareTo(end) > 0) {
                break;
            }
            dateList.add(start);
            start = start.plusDays(1);
        }
        Optional<OperatorUser> opt = this.fromSessionContext();
        List<BaseOutput> list = StreamEx.of(dateList).mapToEntry(ld -> {
            return ld.atTime(0, 0, 0);
        }, ld -> {

            return ld.atTime(23, 59, 59);

        }).mapKeyValue((k, v) -> {
            BaseOutput output = this.dataReportService.reportCount(opt, k, v, 0);
            logger.info("success:{},message:{}", output.isSuccess(), output.getMessage());
            return output;
        }).filter(o -> !o.isSuccess()).toList();
        if (list.size() > 0) {
            return BaseOutput.failure("部分请求失败,请查看后台日志");
        } else {
            return BaseOutput.success();
        }

    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(ModelMap modelMap, @PathVariable Long id) {
        return "thirdPartyReport/view";
    }
}