package com.dili.trace.controller;

import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.sg.trace.glossary.SalesTypeEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.dto.BillReportQueryDto;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.UserInfoDto;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.service.*;
import com.dili.trace.util.MaskUserInfo;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
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
    CustomerService customerService;

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
    public String uploadDetectReport(ModelMap modelMap, @RequestParam(name = "id",required = true) Long id) {
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

}