package com.dili.trace.controller;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.AppException;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.dto.*;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.PreserveTypeEnum;
import com.dili.trace.enums.TruckTypeEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.*;
import com.dili.trace.util.MarketUtil;
import com.dili.trace.util.MaskUserInfo;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.security.auth.login.LoginContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Api("/registerBill")
@Controller
@RequestMapping("/registerBill")
public class RegisterBillController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBillController.class);
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
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
    BillReportService billReportService;

    @Autowired
    UapRpcService uapRpcService;
    @Autowired
    CustomerRpcService customerRpcService;
    @Autowired
    private LoginSessionContext loginSessionContext;
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
        UserTicket user = SessionContext.getSessionContext().getUserTicket();
        modelMap.put("user", user);

        return "registerBill/index";

    }

    /**
     * 查询RegisterBill
     *
     * @param registerBill
     * @return
     */
    @ApiOperation(value = "查询RegisterBill", notes = "查询RegisterBill，返回列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    List<RegisterBill> list(RegisterBillDto registerBill) {
        return registerBillService.list(registerBill);
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
    @RequestMapping(value = "/findFirstWaitAuditRegisterBillCreateByCurrentUser.action", method = {RequestMethod.GET,
            RequestMethod.POST})
    public @ResponseBody
    Object findFirstWaitAuditRegisterBillCreateByCurrentUser(RegisterBillDto dto)
            throws Exception {

        RegisterBill registerBill = registerBillService.findFirstWaitAuditRegisterBillCreateByCurrentUser(dto);
        return BaseOutput.success().setData(registerBill);
    }

    /**
     * 分页查询RegisterBill
     *
     * @param query
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string")})
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})

    public @ResponseBody
    String listPage(BillReportQueryDto query) throws Exception {
        // 设置市场查询条件
        query.setMarketId(MarketUtil.returnMarket());
        return billReportService.listEasyuiPage(query).toString();
    }
    //public @ResponseBody String listPage(RegisterBillDto registerBill) throws Exception {



    /**
     * 新增RegisterBill
     *
     * @param registerBills
     * @return
     */
    @ApiOperation("新增RegisterBill")
    @RequestMapping(value = "/insert.action", method = RequestMethod.POST)
    public @ResponseBody
    BaseOutput insert(@RequestBody List<RegisterBill> registerBills) {
        logger.info("保存登记单数据:" + registerBills.size());
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        for (RegisterBill registerBill : registerBills) {

            CustomerExtendDto customer = customerRpcService.findCustomerByIdOrEx(registerBill.getUserId(), loginSessionContext.getSessionData().getMarketId());
            if (customer == null) {
                return BaseOutput.failure("用户不存在");
            }
            registerBill.setName(customer.getName());
            registerBill.setIdCardNo(customer.getCertificateNumber());
            registerBill.setAddr(customer.getCertificateAddr());
            registerBill.setUserId(customer.getId());
            try {
                Long billId = registerBillService.createRegisterBill(registerBill, new ArrayList<ImageCert>(),
                        Optional.ofNullable(new OperatorUser(userTicket.getId(), userTicket.getRealName())));

            } catch (TraceBizException e) {
                return BaseOutput.failure(e.getMessage());

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return BaseOutput.failure("服务端出错");

            }
        }
        return BaseOutput.success("新增成功").setData(registerBills);
    }

    /**
     * 修改RegisterBill
     *
     * @param registerBill
     * @return
     */
    @ApiOperation("修改RegisterBill")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = true, dataType = "string")})
    @RequestMapping(value = "/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput update(RegisterBill registerBill) {
        registerBillService.updateSelective(registerBill);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除RegisterBill
     *
     * @param id
     * @return
     */
    @ApiOperation("删除RegisterBill")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "form", value = "RegisterBill的主键", required = true, dataType = "long")})
    @RequestMapping(value = "/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput delete(Long id) {
        registerBillService.delete(id);
        return BaseOutput.success("删除成功");
    }

    /**
     * 登记单录入页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/create.html")
    public String create(ModelMap modelMap) {

        modelMap.put("citys", this.queryCitys());
        modelMap.put("billTypes",
                StreamEx.of(BillTypeEnum.values()).mapToEntry(BillTypeEnum::getName, BillTypeEnum::getCode).toList());
        modelMap.put("truckTypes", StreamEx.of(TruckTypeEnum.values())
                .mapToEntry(TruckTypeEnum::getName, TruckTypeEnum::getCode).toList());
        modelMap.put("preserveTypes", StreamEx.of(PreserveTypeEnum.values())
                .mapToEntry(PreserveTypeEnum::getName, PreserveTypeEnum::getCode).toList());
        String str = String.valueOf(System.currentTimeMillis());
        modelMap.put("plate", "川A" + str.substring(str.length() - 5));


        return "registerBill/create";
    }

    /**
     * 查询用户常用城市
     *
     * @return
     */
    private List<UsualAddress> queryCitys() {
        return usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.REGISTER);
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
        RegisterBill registerBill = registerBillService.get(id);
        if (registerBill == null) {
            return "";
        }
        if (displayWeight == null) {
            displayWeight = false;
        }
        List<ImageCert> imageList = StreamEx.of(this.imageCertService.findImageCertListByBillId(registerBill.getBillId())).sortedBy(ImageCert::getCertType).toList();

        modelMap.put("imageList", imageList);
        modelMap.put("separateSalesRecords", Collections.emptyList());

        modelMap.put("qualityTraceTradeBills", CollectionUtils.emptyCollection());
        if (registerBill.getUpStreamId() != null) {
            UpStream upStream = this.upStreamService.get(registerBill.getUpStreamId());
            modelMap.put("upStream", upStream);
        } else {
            modelMap.put("upStream", null);
        }
        Map<Integer, String> upStreamTypeMap = Stream.of(UpStreamTypeEnum.values())
                .collect(Collectors.toMap(UpStreamTypeEnum::getCode, UpStreamTypeEnum::getName));
        modelMap.put("upStreamTypeMap", upStreamTypeMap);
        // DetectRecord conditon=DTOUtils.newDTO(DetectRecord.class);
        // conditon.setRegisterBillCode(registerBill.getCode());
        // conditon.setSort("id");
        // conditon.setOrder("desc");
        modelMap.put("detectRecordList", Collections.emptyList());
        modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));
        modelMap.put("displayWeight", displayWeight);
        return "registerBill/view";
    }

    /**
     * 交易区交易单分销记录
     *
     * @param modelMap
     * @param id       交易单ID
     * @return
     */
    @RequestMapping(value = "/tradeBillSsRecord/{id}", method = RequestMethod.GET)
    public String tradeBillSRecord(ModelMap modelMap, @PathVariable Long id) {
        SeparateSalesRecord condition = new SeparateSalesRecord();
        List<SeparateSalesRecord> separateSalesRecords = separateSalesRecordService.listByExample(condition);
        modelMap.put("separateSalesRecords", separateSalesRecords);
        return "registerBill/tradeBillSsRecord";
    }

    /**
     * 登记单录修改页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/uploadDetectReport/{id}", method = RequestMethod.GET)
    public String uploadDetectReport(ModelMap modelMap, @PathVariable Long id) {
        RegisterBill registerBill = registerBillService.get(id);
        if (registerBill == null) {
            return "";
        }

        modelMap.put("separateSalesRecords", Collections.emptyList());

        modelMap.put("qualityTraceTradeBills", CollectionUtils.emptyCollection());
        modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

        UserTicket user = SessionContext.getSessionContext().getUserTicket();
        modelMap.put("user", user);

        return "registerBill/upload-detectReport";
    }

    /**
     * 上传产地证明
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/uploadOrigincertifiy/{id}", method = RequestMethod.GET)
    public String uploadOrigincertifiy(ModelMap modelMap, @PathVariable Long id) {
        RegisterBill registerBill = registerBillService.get(id);
        if (registerBill == null) {
            return "";
        }
        modelMap.put("separateSalesRecords", Collections.emptyList());
        modelMap.put("qualityTraceTradeBills", CollectionUtils.emptyCollection());
        modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

        UserTicket user = SessionContext.getSessionContext().getUserTicket();
        modelMap.put("user", user);

        return "registerBill/upload-origincertifiy";
    }

    /**
     * 交易区订单溯源页面（二维码）
     *
     * @param tradeNo
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/tradeBillDetail.html", method = RequestMethod.GET)
    public String tradeBillDetail(String tradeNo, ModelMap modelMap) {

        modelMap.put("registerBill", new RegisterBillOutputDto());
        modelMap.put("qualityTraceTradeBill", null);
        return "registerBill/tradeBillDetail";
    }

    /**
     * 登记单溯源（二维码） 没有分销记录
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/registerBillQRCode.html", method = RequestMethod.GET)
    public String registerBillQRCcode(Long id, ModelMap modelMap) {
        RegisterBill bill = registerBillService.get(id);
        modelMap.put("registerBill", bill);
        return "registerBill/registerBillQRCode";
    }

    /**
     * 交易单溯源（二维码） 没有分销记录
     *
     * @param id       交易单ID
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/tradeBillQRCcode.html", method = RequestMethod.GET)
    public String tradeBillQRCcode(Long id, ModelMap modelMap) {
        modelMap.put("registerBill", new RegisterBill());
        modelMap.put("qualityTraceTradeBill", null);
        return "registerBill/tradeBillQRCode";
    }

    /**
     * 交易单分销记录溯源（二维码）
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/tradeSsrQRCcode.html", method = RequestMethod.GET)
    public String tradeSsrQRCcode(Long id, ModelMap modelMap) {
        SeparateSalesRecord separateSalesRecord = separateSalesRecordService.get(id);

        RegisterBill bill = new RegisterBill();
        modelMap.put("registerBill", bill);
        modelMap.put("qualityTraceTradeBill", null);
        modelMap.put("separateSalesRecord", separateSalesRecord);
        return "registerBill/tradeBillQRCode";
    }

    /**
     * 查询客户信息
     *
     * @param registerBill
     * @param firstTallyAreaNo
     * @return
     */
    private UserInfoDto findUserInfoDto(RegisterBill registerBill, String firstTallyAreaNo) {
        UserInfoDto userInfoDto = new UserInfoDto();

        // 理货区
        //User user = userService.get(registerBill.getUserId());
        CustomerExtendDto customer = customerRpcService.findCustomerByIdOrEx(registerBill.getUserId(), loginSessionContext.getSessionData().getMarketId());
        if (customer != null) {
            userInfoDto.setUserId(String.valueOf(customer.getId()));
            userInfoDto.setName(customer.getName());
            userInfoDto.setIdCardNo(customer.getCertificateNumber());
            userInfoDto.setPhone(customer.getContactsPhone());
            userInfoDto.setAddr(customer.getCertificateAddr());

        }

        return userInfoDto;
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
        RegisterBill registerBill = registerBillService.get(id);
        String firstTallyAreaNo = Stream.of(StringUtils.trimToEmpty("").split(",")).filter(StringUtils::isNotBlank)
                .findFirst().orElse("");
        // registerBill.setTallyAreaNo(firstTallyAreaNo);

        UserInfoDto userInfoDto = this.findUserInfoDto(registerBill, firstTallyAreaNo);
        modelMap.put("userInfo", this.maskUserInfoDto(userInfoDto));
        modelMap.put("registerBill", this.maskRegisterBillOutputDto(registerBill));

        modelMap.put("citys", this.queryCitys());

        UserTicket user = SessionContext.getSessionContext().getUserTicket();
        modelMap.put("user", user);

        List<UserPlate> userPlateList = this.userPlateService.findUserPlateByUserId(registerBill.getUserId());
        modelMap.put("userPlateList", userPlateList);
        return "registerBill/edit";
    }

    /**
     * 保存处理结果
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "/doEdit.action", method = {RequestMethod.POST})
    @ResponseBody
    public BaseOutput<?> doEdit(@RequestBody RegisterBill input) {
        try {

            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            List<ImageCert> imageCerts = input.getImageCerts();
            Long id = this.registerBillService.doEdit(input, imageCerts, Optional.ofNullable(new OperatorUser(userTicket.getId(), userTicket.getRealName())));
            return BaseOutput.success().setData(id);
        } catch (AppException e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }

    /**
     * 所有状态列表
     *
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
     * 遮蔽用户敏感信息
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
     * 遮蔽用户敏感信息
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
     * 分页查询RegisterBill
     * @param dto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询RegisterBill", notes = "分页查询RegisterBill，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "RegisterBill", paramType = "form", value = "RegisterBill的form信息", required = false, dataType = "string") })
    @RequestMapping(value = "/findHighLightBill.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody Object findHighLightBill(RegisterBillDto dto) throws Exception {

        RegisterBill registerBill= registerBillService.findHighLightBill(dto);
        return BaseOutput.success().setData(registerBill);
    }
}