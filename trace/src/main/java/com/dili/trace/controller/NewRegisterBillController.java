package com.dili.trace.controller;

import com.dili.common.annotation.RegisterBillMessageEvent;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.sg.trace.glossary.SalesTypeEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.dto.*;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.service.*;
import com.dili.trace.util.MaskUserInfo;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Api("/newRegisterBill")
@Controller
@RequestMapping("/newRegisterBill")
public class NewRegisterBillController {
    private static final Logger logger = LoggerFactory.getLogger(NewRegisterBillController.class);

    @Autowired
    BillService billService;

    @Autowired
    SgRegisterBillService registerBillService;
    @Autowired
    TradeTypeService tradeTypeService;

    @Autowired
    CustomerRpcService customerService;
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


    /**
     * 跳转到RegisterBill页面
     *
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到RegisterBill页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap, HttpServletRequest req) {
        BillReportQueryDto query = new BillReportQueryDto();
        Date now = new Date();
        query.setBillCreatedEnd(DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        query.setBillCreatedStart(DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        modelMap.put("query", query);
        UserTicket user = this.uapRpcService.getCurrentUserTicket().orElse(DTOUtils.newDTO(UserTicket.class));
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
     * 分页查询RegisterBill
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/findHighLightBill.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    Object findHighLightBill(RegisterBillDto dto) throws Exception {

        RegisterBill registerBill = registerBillService.findHighLightBill(dto);
        return BaseOutput.success().setData(registerBill);
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

        return "new-registerBill/create";
    }

    /**
     * 交易单复制
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/copy.html", method = RequestMethod.GET)
    public String copy(Long id, ModelMap modelMap) {
        RegisterBill registerBill = billService.get(id);
        String firstTallyAreaNo = Stream.of(StringUtils.trimToEmpty(registerBill.getTallyAreaNo()).split(","))
                .filter(StringUtils::isNotBlank).findFirst().orElse("");
        registerBill.setTallyAreaNo(firstTallyAreaNo);

        UserInfoDto userInfoDto = this.findUserInfoDto(registerBill, firstTallyAreaNo);
        modelMap.put("userInfo", this.maskUserInfoDto(userInfoDto));
        modelMap.put("tradeTypes", tradeTypeService.findAll());
        modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

        modelMap.put("citys", this.queryCitys());

        if (registerBill.getRegisterSource().equals(RegisterSourceEnum.TALLY_AREA.getCode())) {
            List<UserPlate> userPlateList = this.userPlateService.findUserPlateByUserId(registerBill.getUserId());
            modelMap.put("userPlateList", userPlateList);
        } else {
            modelMap.put("userPlateList", new ArrayList<>(0));
        }

        return "new-registerBill/copy";
    }

    /**
     * 交易单修改
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.GET)
    public String edit(Long id, ModelMap modelMap) {
        RegisterBill registerBill = billService.get(id);
        String firstTallyAreaNo = Stream.of(StringUtils.trimToEmpty(registerBill.getTallyAreaNo()).split(","))
                .filter(StringUtils::isNotBlank).findFirst().orElse("");
        registerBill.setTallyAreaNo(firstTallyAreaNo);

        UserInfoDto userInfoDto = this.findUserInfoDto(registerBill, firstTallyAreaNo);
        modelMap.put("userInfo", this.maskUserInfoDto(userInfoDto));
        modelMap.put("tradeTypes", tradeTypeService.findAll());
        modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

        modelMap.put("citys", this.queryCitys());

        UserTicket user = SessionContext.getSessionContext().getUserTicket();
        modelMap.put("user", user);

        if (registerBill.getRegisterSource().equals(RegisterSourceEnum.TALLY_AREA.getCode())) {
            List<UserPlate> userPlateList = this.userPlateService.findUserPlateByUserId(registerBill.getUserId());
            modelMap.put("userPlateList", userPlateList);
        } else {
            modelMap.put("userPlateList", new ArrayList<>(0));
        }
        return "new-registerBill/edit";
    }

    /**
     * 登记单录修改页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/uploadDetectReport.html", method = RequestMethod.GET)
    public String uploadDetectReport(ModelMap modelMap, @RequestParam(name = "id", required = true) Long id) {
        RegisterBill registerBill = billService.get(id);
        if (registerBill == null) {
            return "";
        }
        if (RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())) {
            // 分销信息
            if (registerBill.getSalesType() != null
                    && registerBill.getSalesType().intValue() == SalesTypeEnum.SEPARATE_SALES.getCode().intValue()) {
                // 分销
                List<SeparateSalesRecord> records = separateSalesRecordService
                        .findByRegisterBillCode(registerBill.getCode());
                modelMap.put("separateSalesRecords", records);
            }
        } else {
            QualityTraceTradeBill condition = DTOUtils.newDTO(QualityTraceTradeBill.class);
            condition.setRegisterBillCode(registerBill.getCode());
            modelMap.put("qualityTraceTradeBills", qualityTraceTradeBillService.listByExample(condition));
        }
        modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

        UserTicket user = SessionContext.getSessionContext().getUserTicket();
        modelMap.put("user", user);

        return "new-registerBill/upload-detectReport";
    }

    /**
     * 上传产地证明
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/uploadOrigincertifiy.html", method = RequestMethod.GET)
    public String uploadOrigincertifiy(ModelMap modelMap, @RequestParam(name = "id", required = true) Long id) {
        RegisterBill registerBill = billService.get(id);
        if (registerBill == null) {
            return "";
        }
        if (RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())) {
            // 分销信息
            if (registerBill.getSalesType() != null
                    && registerBill.getSalesType().intValue() == SalesTypeEnum.SEPARATE_SALES.getCode().intValue()) {
                // 分销
                List<SeparateSalesRecord> records = separateSalesRecordService
                        .findByRegisterBillCode(registerBill.getCode());
                modelMap.put("separateSalesRecords", records);
            }
        } else {
            QualityTraceTradeBill condition = DTOUtils.newDTO(QualityTraceTradeBill.class);
            condition.setRegisterBillCode(registerBill.getCode());
            modelMap.put("qualityTraceTradeBills", qualityTraceTradeBillService.listByExample(condition));
        }
        modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

        UserTicket user = SessionContext.getSessionContext().getUserTicket();
        modelMap.put("user", user);

        return "new-registerBill/upload-origincertifiy";
    }

    /**
     * 上传产地证明
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/uploadHandleResult.html", method = RequestMethod.GET)
    public String uploadHandleResult(ModelMap modelMap, @RequestParam(name = "id", required = true) Long id) {
        RegisterBill registerBill = billService.get(id);
        if (registerBill == null) {
            return "";
        }
        if (RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())) {
            // 分销信息
            if (registerBill.getSalesType() != null
                    && registerBill.getSalesType().intValue() == SalesTypeEnum.SEPARATE_SALES.getCode().intValue()) {
                // 分销
                List<SeparateSalesRecord> records = separateSalesRecordService
                        .findByRegisterBillCode(registerBill.getCode());
                modelMap.put("separateSalesRecords", records);
            }
        } else {
            QualityTraceTradeBill condition = DTOUtils.newDTO(QualityTraceTradeBill.class);
            condition.setRegisterBillCode(registerBill.getCode());
            modelMap.put("qualityTraceTradeBills", qualityTraceTradeBillService.listByExample(condition));
        }
        modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

        UserTicket user = SessionContext.getSessionContext().getUserTicket();
        modelMap.put("user", user);

        return "new-registerBill/upload-handleresult";
    }

    /**
     * 审核页面
     *
     * @param modelMap
     * @param id
     * @return
     */
    @RequestMapping(value = "/audit.html", method = RequestMethod.GET)
    public String audit(ModelMap modelMap, @RequestParam(name = "id", required = true) Long id) {
        modelMap.put("registerBill", billService.get(id));
        return "new-registerBill/audit";
    }

    /**
     * 审核
     *
     * @param id
     * @param pass
     * @return
     */
    @RequestMapping(value = "/doAudit.action", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput doAudit(@RequestParam(name = "id", required = true) Long id, @RequestParam(name = "pass", required = true) Boolean pass) {
        try {
            registerBillService.auditRegisterBill(id, pass);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 批量主动送检
     *
     * @param modelMap
     * @param idList
     * @return
     */
    @RequestMapping(value = "/doBatchAutoCheck.action", method = RequestMethod.POST)
    public @ResponseBody
    BaseOutput doBatchAutoCheck(ModelMap modelMap, @RequestBody List<Long> idList) {
//		modelMap.put("registerBill", registerBillService.get(id));
        idList = CollectionUtils.emptyIfNull(idList).stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return BaseOutput.failure("参数错误");
        }
        return this.registerBillService.doBatchAutoCheck(idList);
    }

    /**
     * 批量撤销
     *
     * @param modelMap
     * @param idList
     * @return
     */
    @RequestMapping(value = "/doBatchUndo.action", method = RequestMethod.POST)
    public @ResponseBody
    BaseOutput doBatchUndo(ModelMap modelMap, @RequestBody List<Long> idList) {
        idList = CollectionUtils.emptyIfNull(idList).stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return BaseOutput.failure("参数错误");
        }
        return this.registerBillService.doBatchUndo(idList);
    }

    /**
     * 批量采样检测
     *
     * @param modelMap
     * @param idList
     * @return
     */
    @RequestMapping(value = "/doBatchSamplingCheck.action", method = RequestMethod.POST)
    public @ResponseBody
    BaseOutput doBatchSamplingCheck(ModelMap modelMap, @RequestBody List<Long> idList) {
//		modelMap.put("registerBill", registerBillService.get(id));
        idList = CollectionUtils.emptyIfNull(idList).stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return BaseOutput.failure("参数错误");
        }
        return this.registerBillService.doBatchSamplingCheck(idList);
    }

    /**
     * 批量审核
     *
     * @param modelMap
     * @param batchAuditDto
     * @return
     */
    @RequestMapping(value = "/doBatchAudit.action", method = RequestMethod.POST)
    public @ResponseBody
    BaseOutput doBatchAudit(ModelMap modelMap, @RequestBody BatchAuditDto batchAuditDto) {
//		modelMap.put("registerBill", registerBillService.get(id));
//		if (batchAuditDto.getPass() == null) {
//			return BaseOutput.failure("参数错误");
//		}
        List<Long> idList = CollectionUtils.emptyIfNull(batchAuditDto.getRegisterBillIdList()).stream()
                .filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return BaseOutput.failure("参数错误");
        }
        batchAuditDto.setPass(true);
        batchAuditDto.setRegisterBillIdList(idList);
        return this.registerBillService.doBatchAudit(batchAuditDto);
    }

    /**
     * 撤销
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doUndo.action", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput doUndo(@RequestParam(name = "id", required = true) Long id) {
        try {
            registerBillService.undoRegisterBill(id);
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
    @RequestMapping(value = "/view.html", method = RequestMethod.GET)
    public String view(ModelMap modelMap, @RequestParam(required = true, name = "id") Long id
            , @RequestParam(required = false, name = "displayWeight") Boolean displayWeight) {
        RegisterBill item = billService.get(id);
        if (item == null) {
            return "";
        }
        if (displayWeight == null) {
            displayWeight = false;
        }
        if (RegisterSourceEnum.TALLY_AREA.getCode().equals(item.getRegisterSource())) {
            // 分销信息
            if (item.getSalesType() != null
                    && item.getSalesType().intValue() == SalesTypeEnum.SEPARATE_SALES.getCode().intValue()) {
                // 分销
                List<SeparateSalesRecord> records = separateSalesRecordService
                        .findByRegisterBillCode(item.getCode());
                modelMap.put("separateSalesRecords", records);
            }
        } else {
            QualityTraceTradeBill condition = DTOUtils.newDTO(QualityTraceTradeBill.class);
            condition.setRegisterBillCode(item.getCode());
            modelMap.put("qualityTraceTradeBills", qualityTraceTradeBillService.listByExample(condition));
        }

//		DetectRecord conditon=DTOUtils.newDTO(DetectRecord.class);
//		conditon.setRegisterBillCode(registerBill.getCode());
//		conditon.setSort("id");
//		conditon.setOrder("desc");
        List<DetectRecord> detectRecordList = this.detectRecordService.findTop2AndLatest(item.getCode());
        modelMap.put("detectRecordList", detectRecordList);
        modelMap.put("displayWeight", displayWeight);

        RegisterBillOutputDto registerBill = new RegisterBillOutputDto();
        BeanUtils.copyProperties(this.maskRegisterBillOutputDto(item), registerBill);

        List<ImageCert> imageCerts = this.registerBillService.findImageCertListByBillId(item.getBillId());
        registerBill.setImageCerts(imageCerts);

        modelMap.put("registerBill", registerBill);

        return "new-registerBill/view";
    }

    /**
     * 自动送检
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doAutoCheck.action", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput doAutoCheck(@RequestParam(name = "id", required = true) Long id) {
        try {
            registerBillService.autoCheckRegisterBill(id);
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
    @RequestMapping(value = "/doSamplingCheck.action", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput doSamplingCheck(@RequestParam(name = "id", required = true) Long id) {
        try {
            registerBillService.samplingCheckRegisterBill(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 复检
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doReviewCheck.action", method = RequestMethod.GET)
    public @ResponseBody
    BaseOutput doReviewCheck(@RequestParam(name = "id", required = true) Long id) {
        try {
            registerBillService.reviewCheckRegisterBill(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.success("操作成功");
    }

    /**
     * 保存处理结果
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "/doUploadHandleResult.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<?> saveHandleResult(RegisterBill input) {
        try {
            Long id = this.registerBillService.saveHandleResult(input);
            return BaseOutput.success().setData(id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        }

    }


    /**
     * 上传产地报告
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "/doUploadOrigincertifiy.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<?> doUploadOrigincertifiy(@RequestBody RegisterBill input) {
        try {
            Long id = this.registerBillService.doUploadOrigincertifiy(input);
            return BaseOutput.success().setData(id);
        } catch (TraceBizException e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }

    /**
     * 上传检测报告
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "/doUploadDetectReport.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<?> doUploadDetectReport(@RequestBody RegisterBill input) {
        try {
            Long id = this.registerBillService.doUploadDetectReport(input);
            return BaseOutput.success().setData(id);
        } catch (TraceBizException e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }

    /**
     * 删除检测报告及产地证明
     *
     * @param id
     * @param deleteType
     * @return
     */
    @RequestMapping(value = "/doRemoveReportAndCertifiy.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<?> doRemoveReportAndCertifiy(ModelMap modelMap, Long id, String deleteType) {
        try {
//			Long id = this.registerBillService.doUploadDetectReport(input);
            return this.registerBillService.doRemoveReportAndCertifiy(id, deleteType);
        } catch (TraceBizException e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }

    /**
     * 保存处理结果
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "/doAuditWithoutDetect.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<?> doAuditWithoutDetect(RegisterBill input) {
        try {
            Long id = this.registerBillService.doAuditWithoutDetect(input);
            return BaseOutput.success().setData(id);
        } catch (TraceBizException e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }

    /**
     * 保存处理结果
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "/doEdit.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<?> doEdit(RegisterBill input) {
        try {

            Long id = this.registerBillService.doEdit(input);
            return BaseOutput.success().setData(id);
        } catch (TraceBizException e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

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
                    CustomerExtendDto customer=new CustomerExtendDto();
                    RegisterBill rb = rbInputDto.build(customer,this.uapRpcService.getCurrentFirm().orElse(null).getId());
                    rb.setIdCardNo(input.getIdCardNo());
                    rb.setName(input.getName());
                    rb.setPhone(input.getPhone());
                    rb.setPlate(input.getPlate());
                    rb.setAddr(input.getAddr());
                    rb.setUserId(input.getUserId());
                    List<ImageCert> imageList = this.registerBillService.buildImageCertList(input.getDetectReportUrl()
                            , rbInputDto.getHandleResultUrl(), rbInputDto.getOriginCertifiyUrl());
                    rb.setImageCerts(imageList);
                    rb.setWeightUnit(WeightUnitEnum.KILO.getCode());
                    rb.setCreationSource(RegisterBilCreationSourceEnum.PC.getCode());
                    rb.setRegisterSource(RegisterSourceEnum.getRegisterSourceEnum(input.getRegisterSource()).orElse(RegisterSourceEnum.OTHERS).getCode());
                    rb.setTallyAreaNo(input.getTallyAreaNo());
                    rb.setVerifyStatus(BillVerifyStatusEnum.WAIT_AUDIT.getCode());
                    rb.setPreserveType(PreserveTypeEnum.NONE.getCode());
                    rb.setVerifyType(VerifyTypeEnum.NONE.getCode());
                    rb.setTruckType(TruckTypeEnum.FULL.getCode());
                    rb.setIsCheckin(YesOrNoEnum.NO.getCode());
                    rb.setIsDeleted(YesOrNoEnum.NO.getCode());
                    rb.setMeasureType(MeasureTypeEnum.COUNT_WEIGHT.getCode());
                    // 理货类型为交易区时才保存交易区号和id
                    if (RegisterSourceEnum.TRADE_AREA.getCode().equals(input.getRegisterSource())) {
                        rb.setTradeTypeId(input.getTradeTypeId());
                        rb.setTradeTypeName(input.getTradeTypeName());
                    }
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


    /**
     * 查询城市
     *
     * @return
     */
    private List<UsualAddress> queryCitys() {
        return usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.REGISTER);
    }

    /**
     * 查找用户信息
     *
     * @param registerBill
     * @param firstTallyAreaNo
     * @return
     */
    private UserInfoDto findUserInfoDto(RegisterBill registerBill, String firstTallyAreaNo) {
        UserInfoDto userInfoDto = new UserInfoDto();
        if (registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TALLY_AREA.getCode().intValue()) {
            // 理货区
            User user = userService.findByTallyAreaNo(firstTallyAreaNo);

            if (user != null) {
                userInfoDto.setUserId(String.valueOf(user.getId()));
                userInfoDto.setName(user.getName());
                userInfoDto.setIdCardNo(user.getCardNo());
                userInfoDto.setPhone(user.getPhone());
                userInfoDto.setAddr(user.getAddr());

            }

        } else {

            Customer condition = new Customer();
            condition.setCustomerId(StringUtils.trimToNull(registerBill.getTradeAccount()));
            condition.setPrintingCard(StringUtils.trimToNull(registerBill.getTradePrintingCard()));
            Customer customer = this.customerService.findCustomer(condition).orElse(null);
            if (customer != null) {
                userInfoDto.setUserId(customer.getCustomerId());
                userInfoDto.setName(customer.getName());
                userInfoDto.setIdCardNo(customer.getIdNo());
                userInfoDto.setPhone(customer.getPhone());
                userInfoDto.setAddr(customer.getAddress());
                userInfoDto.setPrintingCard(customer.getPrintingCard());
            }

        }
        return userInfoDto;
    }

    /**
     * 保护敏感信息
     *
     * @param dto
     * @return
     */
    private UserInfoDto maskUserInfoDto(UserInfoDto dto) {

        if (dto == null) {
            return dto;
        }
        return dto.mask(!SessionContext.hasAccess("post", "registerBill/create.html#user"));
    }

    /**
     * 权限判断并保护敏感信息
     *
     * @param dto
     * @return
     */
    private RegisterBill maskRegisterBillOutputDto(RegisterBill dto) {
        if (dto == null) {
            return dto;
        }
        if (SessionContext.hasAccess("post", "registerBill/create.html#user")) {
            return dto;
        } else {
            dto.setIdCardNo(MaskUserInfo.maskIdNo(dto.getIdCardNo()));
            dto.setAddr(MaskUserInfo.maskAddr(dto.getAddr()));
            return dto;
        }

    }

    /**
     * 查询当前controller可用事件
     *
     * @param billIdList
     * @return
     */
    @RequestMapping("/queryEvents.action")
    @ResponseBody
    public List<String> queryEvents(@RequestBody List<Long> billIdList) {
        List<RegisterBillMessageEvent>list= Lists.newArrayList(RegisterBillMessageEvent.add);
        if (billIdList == null || billIdList.size()==0) {
            return StreamEx.of(list).map(msg -> {
                return msg.getCode();
            }).nonNull().toList();
        }
        list.add(RegisterBillMessageEvent.export);
        if(billIdList.size()==1){
           return StreamEx.of(this.registerBillService.queryEvents(billIdList.get(0))).append(list).map(msg -> {
                return msg.getCode();
            }).nonNull().toList();
        }else{
            return StreamEx.of(this.queryBatchEvents(billIdList)).append(list).map(msg -> {
                return msg.getCode();
            }).nonNull().toList();
        }

    }

    /**
     * SB
     * @param idList
     * @return
     */
    private List<RegisterBillMessageEvent> queryBatchEvents(List<Long> idList) {
        Map<RegisterBillMessageEvent, Boolean> eventCount = StreamEx.ofNullable(idList).flatCollection(Function.identity()).nonNull().flatMap(bid -> {
            return StreamEx.of(this.registerBillService.queryEvents(bid));
        }).distinct().mapToEntry(v -> v, v -> v).collapseKeys().mapValues(v -> v.size() > 0).toMap();

        List<RegisterBillMessageEvent> batchEventList = Lists.newLinkedList();
        if (eventCount.getOrDefault(RegisterBillMessageEvent.audit, false)) {
            batchEventList.add(RegisterBillMessageEvent.batch_audit);
        }

        if (eventCount.getOrDefault(RegisterBillMessageEvent.audit, false)) {
            batchEventList.add(RegisterBillMessageEvent.batch_audit);
        }

        if (eventCount.getOrDefault(RegisterBillMessageEvent.auto, false)) {
            batchEventList.add(RegisterBillMessageEvent.batch_auto);
        }

        if (eventCount.getOrDefault(RegisterBillMessageEvent.sampling, false)) {
            batchEventList.add(RegisterBillMessageEvent.batch_sampling);
        }

        if (eventCount.getOrDefault(RegisterBillMessageEvent.undo, false)) {
            batchEventList.add(RegisterBillMessageEvent.batch_undo);
        }
        return batchEventList;
    }

}