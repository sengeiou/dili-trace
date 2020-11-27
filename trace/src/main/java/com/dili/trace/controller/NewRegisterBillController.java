package com.dili.trace.controller;

import com.dili.common.exception.TraceBizException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.BillReportQueryDto;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.service.*;
import com.dili.uap.sdk.domain.UserTicket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Api("/newRegisterBill")
@Controller
@RequestMapping("/newRegisterBill")
public class NewRegisterBillController {
    private static final Logger logger = LoggerFactory.getLogger(NewRegisterBillController.class);
    @Autowired
    SgRegisterBillService registerBillService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
    @Autowired
    UserService userService;
    @Autowired
    UserPlateService userPlateService;

    @Autowired
    BaseInfoRpcService baseInfoRpcService;
    @Autowired
    UsualAddressService usualAddressService;
    @Autowired
    UpStreamService upStreamService;
    @Autowired
    ImageCertService imageCertService;
    @Autowired
    WebCtxService webCtxService;


    /**
     * 跳转到RegisterBill页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到RegisterBill页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {

        BillReportQueryDto query = new BillReportQueryDto();
        Date now = new Date();
        query.setBillCreatedEnd(DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        query.setBillCreatedStart(DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        modelMap.put("query", query);
        UserTicket user = this.webCtxService.getCurrentUserTicket().orElse(DTOUtils.newDTO(UserTicket.class));
        modelMap.put("user", user);

        return "new-registerBill/index";

    }


    /**
     * 分页查询RegisterBill
     *
     * @param registerBill
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(RegisterBillDto registerBill) throws Exception {

        return registerBillService.listPage(registerBill);
    }


    /**
     * 新增RegisterBill
     *
     * @param input
     * @return
     */
    @ApiOperation("新增RegisterBill")
    @RequestMapping(value = "/insert.action", method = RequestMethod.POST)
    public @ResponseBody
    BaseOutput insert(@RequestBody CreateListBillParam input) {
        if (input == null) {
            return BaseOutput.failure("参数错误");
        }

        List<RegisterBill> billList = StreamEx.ofNullable(input.getRegisterBills()).flatCollection(Function.identity())
                .nonNull()
                .map(rbInputDto -> {
                    User user = DTOUtils.newDTO(User.class);
                    RegisterBill rb = rbInputDto.build(user);
                    List<ImageCert> imageList = this.registerBillService.buildImageCertList(input.getDetectReportUrl()
                            , rbInputDto.getHandleResultUrl(),
                            rbInputDto.getOriginCertifiyUrl());
                    rb.setImageCerts(imageList);
                    rb.setWeightUnit(WeightUnitEnum.KILO.getCode());
                    rb.setCreationSource(RegisterBilCreationSourceEnum.PC.getCode());
                    rb.setRegisterSource(RegisterSourceEnum.getRegisterSourceEnum(input.getRegisterSource()).orElse(RegisterSourceEnum.OTHERS).getCode());
                    rb.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
                    rb.setPreserveType(PreserveTypeEnum.NONE.getCode());
                    rb.setVerifyType(VerifyTypeEnum.NONE.getCode());
                    rb.setTruckType(TruckTypeEnum.FULL.getCode());
                    rb.setIsCheckin(YesOrNoEnum.NO.getCode());
                    rb.setIsDeleted(YesOrNoEnum.NO.getCode());
                    return rb;
                }).toList();
        try {
            this.registerBillService.createRegisterBillList(billList);
            return BaseOutput.success("新增成功").setData(billList);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure("服务器出错,请重试");
        }
    }

}