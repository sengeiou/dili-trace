package com.dili.sg.trace.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dili.sg.trace.service.*;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.UserService;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.common.exception.TraceBizException;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.util.DateUtils;
import com.dili.sg.trace.domain.RegisterBill;
import com.dili.sg.trace.domain.UsualAddress;
import com.dili.sg.trace.dto.RegisterBillDto;
import com.dili.sg.trace.glossary.BillTypeEnum;
import com.dili.sg.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.sg.trace.glossary.RegisterBillStateEnum;
import com.dili.sg.trace.glossary.UsualAddressTypeEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Api("/commissionBill")
@Controller
@RequestMapping("/commissionBill")
public class CommissionBillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillController.class);

    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
    @Autowired
    DetectRecordService detectRecordService;
    @Autowired
    TradeTypeService tradeTypeService;
    @Autowired
    UserService userService;
    @Autowired
    UserPlateService userPlateService;
    @Autowired
    QualityTraceTradeBillService qualityTraceTradeBillService;
    @Autowired
    CityService cityService;
    @Autowired
    UsualAddressService usualAddressService;
    @Autowired
    CommissionBillService commissionBillService;
    @Autowired
    BillService billService;

    @Autowired
    RegisterBillService registerBillService;

    /**
     * 跳转到CommissionBill页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到CommissionBill页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        Date now = new Date();
        modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        // modelMap.put("state", RegisterBillStateEnum.WAIT_CHECK.getCode());
        UserTicket user = SessionContext.getSessionContext().getUserTicket();
        modelMap.put("user", user);

        return "commissionBill/index";
    }

    /**
     * 跳转到CommissionBill页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到CommissionBill页面")
    @RequestMapping(value = "/checksheetIndex.html", method = RequestMethod.GET)
    public String checksheetIndex(ModelMap modelMap) {
        Date now = new Date();
        modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        // modelMap.put("state", RegisterBillStateEnum.WAIT_CHECK.getCode());
        UserTicket user = SessionContext.getSessionContext().getUserTicket();
        modelMap.put("user", user);

        return "forward:/checkSheet/index.html?billType=" + BillTypeEnum.COMMISSION_BILL.getCode();
    }

    /**
     * 分页查询CommissionBill
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询CommissionBill", notes = "分页查询CommissionBill，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CommissionBill", paramType = "form", value = "CommissionBill的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/findHighLightBill.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    Object findHighLightBill(RegisterBillDto dto) throws Exception {
        try {
            OperatorUser operatorUser = OperatorUser.build(SessionContext.getSessionContext());
            RegisterBill bill = this.commissionBillService.findHighLightCommissionBill(dto, operatorUser);
            return BaseOutput.success().setData(bill);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }

    }

    /**
     * 分页查询CommissionBill
     *
     * @param input
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询CommissionBill", notes = "分页查询CommissionBill，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CommissionBill", paramType = "form", value = "CommissionBill的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    String listPage(RegisterBillDto input) throws Exception {
        return this.commissionBillService.listPage(input);

    }

    /**
     * 新增CommissionBill
     *
     * @param inputBillList
     * @return
     */
    @ApiOperation("新增CommissionBill")
    @RequestMapping(value = "/insert.action", method = RequestMethod.POST)
    public @ResponseBody
    BaseOutput insert(@RequestBody List<RegisterBill> inputBillList) {
        List<RegisterBill> billList = ListUtils.emptyIfNull(inputBillList).stream().filter(Objects::nonNull)
                .collect(Collectors.toList());
        LOGGER.info("保存委托单数据:" + billList.size());
        if (billList.isEmpty()) {
            return BaseOutput.failure("没有提交数据");
        }

        for (RegisterBill bill : billList) {
            bill.setCreationSource(RegisterBilCreationSourceEnum.PC.getCode());
        }
        try {
            OperatorUser operatorUser = OperatorUser.build(SessionContext.getSessionContext());
            this.commissionBillService.createCommissionBillByManager(billList, operatorUser);
            return BaseOutput.success("新增成功");
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure("服务器出错，请重试");
        }

    }

    /**
     * 登记单录入页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/create.html")
    public String create(ModelMap modelMap) {
        modelMap.put("tradeTypes", tradeTypeService.findAll());
        modelMap.put("citys", this.queryCitys());

        return "commissionBill/create";
    }

    /**
     * 查询产地
     *
     * @return
     */
    private List<UsualAddress> queryCitys() {
        return usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.REGISTER);
    }


    /**
     * 进入审核页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/audit.html")
    public String audit(ModelMap modelMap, Long billId) {
        RegisterBill item = this.billService.get(billId);
        modelMap.put("item", item);
        return "commissionBill/audit";
    }

    /**
     * 审核委托登记单
     *
     * @param billId
     * @return
     */
    @RequestMapping(value = "/doAuditCommissionBillByManager.action", method = RequestMethod.POST)
    public @ResponseBody
    BaseOutput doAuditCommissionBillByManager(Long billId) {
        try {
            this.commissionBillService.doAuditCommissionBillByManager(billId, OperatorUser.build(SessionContext.getSessionContext()));
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 登记单录查看页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/view/{id}/{displayWeight}", method = RequestMethod.GET)
    public String view(ModelMap modelMap, @PathVariable Long id,
                       @PathVariable(required = false) Boolean displayWeight) {
        RegisterBill bill = this.billService.get(id);
        if (bill == null) {
            return "";
        }
        List<DetectRecord> detectRecordList = this.detectRecordService.findTop2AndLatest(bill.getCode());
        modelMap.put("detectRecordList", detectRecordList);
        if (displayWeight == null) {
            displayWeight = false;
        }

        modelMap.put("item", bill);
        modelMap.put("displayWeight", displayWeight);
        return "commissionBill/view";
    }


    /**
     * 自动送检
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/autoCheck/{id}", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput autoCheck(@PathVariable Long id) {
        try {
            this.registerBillService.autoCheckRegisterBill(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 采样检测
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/samplingCheck/{id}", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput samplingCheck(@PathVariable Long id) {
        try {
            this.registerBillService.samplingCheckRegisterBill(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }


    /**
     * (微信扫码)电商登记单扫码页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/billQRDetail.html", method = RequestMethod.GET)
    public String billQRDetail(ModelMap modelMap, Long id) {
        RegisterBill bill = this.billService.get(id);
        if (bill == null || !BillTypeEnum.COMMISSION_BILL.equalsToCode(bill.getBillType())) {
            modelMap.put("detectRecordList", CollectionUtils.emptyCollection());
            modelMap.put("item", new RegisterBill());
            return "commissionBill/qrDetail";
        }
        List<DetectRecord> detectRecordList = this.detectRecordService.findTop2AndLatest(bill.getCode());
        modelMap.put("detectRecordList", detectRecordList);


        modelMap.put("item", bill);
        return "commissionBill/billQRDetail";
    }


    /**
     * 所有状态列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/listState.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<Map<String, String>> listState() {

        return Stream.of(RegisterBillStateEnum.values()).map(e -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", e.getCode().toString());
            map.put("name", e.getName());
            map.put("parentId", "");
            return map;

        }).collect(Collectors.toList());

    }

    /**
     * 批量复检
     *
     * @param idlist
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "批量复检", notes = "批量复检")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idlist", paramType = "form", value = "ID集合", required = false, dataType = "string")})
    @RequestMapping(value = "/doBatchReviewCheck.action", method = {RequestMethod.POST})
    public @ResponseBody
    BaseOutput doBatchReviewCheck(@RequestBody List<Long> idlist) throws Exception {
        try {
            List<Long> billIdList = StreamEx.ofNullable(idlist).nonNull().flatCollection(Function.identity()).nonNull().toList();
            if (billIdList.isEmpty()) {
                return BaseOutput.failure("参数不能为空");
            }
            OperatorUser operatorUser = OperatorUser.build(SessionContext.getSessionContext());
            List<String>reviewCheckedCodeList= this.commissionBillService.doBatchReviewCheck(billIdList, operatorUser);
            return BaseOutput.success().setData(reviewCheckedCodeList);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }

    }


}
