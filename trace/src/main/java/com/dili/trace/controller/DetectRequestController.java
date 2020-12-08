package com.dili.trace.controller;

import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.dto.BillReportQueryDto;
import com.dili.trace.dto.DetectRequestDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.*;
import com.dili.trace.util.BeanMapUtil;
import com.dili.uap.sdk.domain.UserTicket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 检测请求业务类
 */
@Api("/detectRequest")
@Controller
@RequestMapping("/detectRequest")
public class DetectRequestController {
    private static final Logger logger = LoggerFactory.getLogger(DetectRequestController.class);

    @Autowired
    BillService billService;

    @Autowired
    SgRegisterBillService registerBillService;
    @Autowired
    TradeTypeService tradeTypeService;

    @Autowired
    CustomerService customerService;
    @Autowired
    DetectRecordService detectRecordService;

    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
    @Autowired
    UserService userService;
    @Autowired
    UserPlateService userPlateService;
    @Autowired
    UsualAddressService usualAddressService;
    @Autowired
    UpStreamService upStreamService;
    @Autowired
    ImageCertService imageCertService;
    @Autowired
    UapRpcService uapRpcService;

    @Autowired
    QualityTraceTradeBillService qualityTraceTradeBillService;

    @Autowired
    DetectRequestService detectRequestService;

    /**
     * 跳转到DetectRequest页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到DetectRequest页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {
        LocalDateTime now = LocalDateTime.now();
        modelMap.put("createdStart", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
        modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
        return "detectRequest/index";
    }

    /**
     * 分页查询DetectRequest
     *
     * @param detectRequestDto 查询条件
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询DetectRequest", notes = "分页查询DetectRequest，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "DetectRequest", paramType = "form", value = "DetectRequest的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(DetectRequestDto detectRequestDto) throws Exception {
        EasyuiPageOutput out=this.detectRequestService.listEasyuiPageByExample(detectRequestDto);
        // EasyuiPageOutput out=this.detectRequestService.listEasyuiPageByExample(detectRequestDto,true);
        return out.toString();
    }

}