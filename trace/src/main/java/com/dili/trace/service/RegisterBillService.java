package com.dili.trace.service;

import cn.hutool.core.date.DateUtil;

import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.input.RegisterBillApiInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.*;
import com.dili.trace.dto.*;
import com.dili.trace.dto.ret.FieldConfigDetailRetDto;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.glossary.SampleSourceEnum;
import com.dili.trace.rpc.dto.AccountGetListResultDto;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.rpc.service.UidRestfulRpcService;
import com.dili.trace.util.JsonPathUtil;
import com.dili.trace.util.NumUtils;
import com.dili.trace.util.RegUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import de.cronn.reflection.util.PropertyUtils;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ???MyBatis Generator?????????????????? This file was generated on 2019-07-26 09:20:34.
 */
@Service
@Transactional
public class RegisterBillService extends TraceBaseService<RegisterBill, Long> {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBillService.class);
    @Autowired
    UidRestfulRpcService uidRestfulRpcService;
    @Autowired
    ImageCertService imageCertService;
    @Autowired
    UserPlateService userPlateService;
    @Autowired
    UsualAddressService usualAddressService;
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    BrandService brandService;
    @Autowired
    RegisterBillHistoryService registerBillHistoryService;
    @Autowired
    UserService userService;
    @Autowired
    TradeService tradeService;
    @Autowired
    UpStreamService upStreamService;
    @Autowired
    CheckinOutRecordService checkinOutRecordService;
    @Autowired
    UserQrHistoryService userQrHistoryService;
    @Autowired
    TradeRequestService tradeRequestService;

    @Autowired
    RegisterHeadService registerHeadService;
    @Autowired
    CustomerRpcService clientRpcService;
    @Autowired
    ExtCustomerService extCustomerService;
    @Autowired
    BillService billService;
    @Autowired
    ProcessService processService;
    @Autowired
    DetectRequestService detectRequestService;
    @Autowired
    BillVerifyHistoryService billVerifyHistoryService;
    @Autowired
    SyncUserInfoService syncUserInfoService;
    @Autowired
    SyncCategoryService syncCategoryService;

    @Autowired
    FieldConfigDetailService fieldConfigDetailService;

    @Autowired
    RegisterTallyAreaNoService registerTallyAreaNoService;
    @Autowired
    ProcessConfigService processConfigService;
    @Autowired
    CodeGenerateService codeGenerateService;


    /**
     * ??????mapper
     *
     * @return
     */
    public RegisterBillMapper getActualDao() {
        return (RegisterBillMapper) getDao();
    }


    /**
     * ??????ID??????????????????
     */
    @Transactional
    public Optional<RegisterBill> selectByIdForUpdate(Long id) {
        return this.getActualDao().selectByIdForUpdate(id).map(billItem -> {
            if (YesOrNoEnum.YES.getCode().equals(billItem.getIsDeleted())) {
                throw new TraceBizException("????????????????????????");
            }
            return billItem;
        });
    }

    /**
     * ???????????????????????????????????????
     */
    @Transactional
    public Optional<RegisterBill> getAndCheckById(Long billId) {
        if (billId == null) {
            return Optional.empty();
        }
        RegisterBill billItem = this.get(billId);
        if (billItem == null) {
            return Optional.empty();
        }
        if (YesOrNoEnum.YES.getCode().equals(billItem.getIsDeleted())) {
            throw new TraceBizException("????????????????????????");
        }
        return Optional.of(billItem);
    }

    /**
     * ?????????????????????
     *
     * @param inputBillDtoList
     * @param customerId
     * @param operatorUser
     * @return
     */
    public List<Long> createRegisterBillList(Long marketId, List<CreateRegisterBillInputDto> inputBillDtoList, Long customerId,
                                             Optional<OperatorUser> operatorUser, CreatorRoleEnum creatorRoleEnum) {
        List<CreateRegisterBillInputDto> registerBills = StreamEx.ofNullable(inputBillDtoList).flatCollection(Function.identity())
                .nonNull().toList();
        if (registerBills.isEmpty()) {
            throw new TraceBizException("???????????????");
        }
        ProcessConfig processConfig = this.processConfigService.findByMarketId(marketId);
        CustomerExtendDto user = this.clientRpcService.findApprovedCustomerByIdOrEx(customerId, marketId);
        String cardNo = this.extCustomerService.findCardInfoByCustomerIdList(marketId, Lists.newArrayList(customerId)).getOrDefault(customerId, new AccountGetListResultDto()).getCardNo();
        List<FieldConfigDetailRetDto> fieldConfigDetailRetDtoList = this.fieldConfigDetailService.findByMarketIdAndModuleType(marketId, FieldConfigModuleTypeEnum.REGISTER);
        Map<String, FieldConfigDetailRetDto> fieldConfigDetailRetDtoMap = StreamEx.of(fieldConfigDetailRetDtoList).nonNull().toMap(item -> item.getDefaultFieldDetail().getFieldName(), Function.identity());
        return StreamEx.of(registerBills).nonNull().map(dto -> {
            dto.setMarketId(marketId);
            logger.info("?????????????????????:{}" + dto.toString());

            RegisterBill registerBill = dto.build(user, dto.getMarketId());
            if (StringUtils.isNotBlank(registerBill.getPlate())) {
                if (!RegUtils.isPlate(registerBill.getPlate().trim())) {
                    throw new TraceBizException("??????????????????");
                }
            }

            registerBill.setCreatorRole(creatorRoleEnum.getCode());
            registerBill.setCardNo(cardNo);
            registerBill.setImageCertList(dto.getImageCertList());


            Long billId = this.createRegisterBill(registerBill, fieldConfigDetailRetDtoMap, processConfig, operatorUser);

            return billId;
        }).toList();
    }

    /**
     * ??????????????????
     *
     * @param input
     * @return
     */
    public Long doUploadDetectReport(RegisterBill input) {
        if (input == null || input.getId() == null) {
            throw new TraceBizException("????????????");
        }
        List<ImageCert> imageCertList = StreamEx.ofNullable(input.getImageCertList()).nonNull().flatCollection(Function.identity()).nonNull().toList();
        if (!imageCertList.isEmpty()) {
            imageCertList = StreamEx.of(imageCertList).filter(img -> {
                // ??????uid????????????????????????????????????????????????
                return StringUtils.isNotBlank(img.getUid()) && ImageCertTypeEnum.DETECT_REPORT.equalsToCode(img.getCertType());
            }).toList();
        }
        if (imageCertList.isEmpty()) {
            //StringUtils.isBlank(input.getOriginCertifiyUrl()) && StringUtils.isBlank(input.getDetectReportUrl())) {
            throw new TraceBizException("???????????????");
        }

        // TODO:?????????????????????
        // RegisterBill item = this.checkEvent(input.getId(), RegisterBillMessageEvent.upload_detectreport).orElse(null);
        RegisterBill item = this.billService.getAvaiableBill(input.getId()).orElseThrow(() -> {
            return new TraceBizException("???????????????????????????");
        });
        if (!BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(item.getVerifyStatus())) {
            throw new TraceBizException("????????????,????????????????????????");
        }
        List<ImageCert> imageCerts =
                StreamEx.of(this.imageCertService.findImageCertListByBillId(item.getBillId(), BillTypeEnum.REGISTER_BILL)).filter(img -> {
                    Integer cerType = img.getCertType();
                    return !ImageCertTypeEnum.DETECT_REPORT.equalsToCode(cerType);
                }).append(imageCertList).toList();

        this.billService.updateHasImage(item.getBillId(), imageCerts, BillTypeEnum.REGISTER_BILL);
        return item.getBillId();
    }

    /**
     * ????????????
     *
     * @param user
     * @param marketId
     * @return
     */
    private Optional<String> findCardNo(CustomerExtendDto user, Long marketId) {
        TraceCustomer cq = new TraceCustomer();
        cq.setCode(user.getCode());
        return this.clientRpcService.findCustomer(cq, marketId).map(TraceCustomer::getCardNo);
    }

    /**
     * ??????????????????????????????
     *
     * @param registerBill
     * @param operatorUser
     * @return
     */
    public Long createWeightingRegisterBill(RegisterBill registerBill,
                                            Optional<OperatorUser> operatorUser) {
        Long marketId = registerBill.getMarketId();
        List<FieldConfigDetailRetDto> fieldConfigDetailRetDtoList = this.fieldConfigDetailService.findByMarketIdAndModuleType(marketId, FieldConfigModuleTypeEnum.REGISTER);
        Map<String, FieldConfigDetailRetDto> fieldConfigDetailRetDtoMap = StreamEx.of(fieldConfigDetailRetDtoList).nonNull().toMap(item -> item.getDefaultFieldDetail().getFieldName(), Function.identity());

        ProcessConfig processConfig = this.processConfigService.findByMarketId(marketId);
        processConfig.setIsNeedVerify(YesOrNoEnum.NO.getCode());
        Long billId = this.createRegisterBill(registerBill, fieldConfigDetailRetDtoMap, processConfig, operatorUser);

        return billId;
    }

    /**
     * ?????????????????????
     *
     * @param registerBill
     * @return
     */
    private Long createRegisterBill(RegisterBill registerBill
            , Map<String, FieldConfigDetailRetDto> fieldConfigDetailRetDtoMap, ProcessConfig processConfig,
                                    Optional<OperatorUser> operatorUser) {

        Configuration conf = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST, Option.DEFAULT_PATH_LEAF_TO_NULL);

//        String json = JSON.toJSONString(registerBill);
//        StreamEx.of(fieldConfigDetailRetDtoList).forEach(fcd -> {
//            String jsonPath = fcd.getDefaultFieldDetail().getJsonPath();
//            if (StringUtils.isBlank(jsonPath)) {
//                return;
//            }
//            Object jsonValue = JsonPath.using(conf).parse(json).read(jsonPath.trim());
//            logger.debug("jsonpath={},jsonvalue={}", jsonPath, jsonValue);
//        });
        this.checkAndSetDefaultValue(registerBill, fieldConfigDetailRetDtoMap);
        this.checkImageCertList(registerBill);

        registerBill.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
        registerBill.setVerifyStatus(BillVerifyStatusEnum.WAIT_AUDIT.getCode());
        registerBill.setVerifyType(VerifyTypeEnum.NONE.getCode());
//        registerBill.setState(RegisterBillStateEnum.NEW.getCode());
        registerBill.setDetectStatus(DetectStatusEnum.NONE.getCode());
        String code = uidRestfulRpcService.bizNumber(BizNumberType.REGISTER_BILL);
        logger.debug("registerBill.code={}", code);
        registerBill.setCode(code);
        registerBill.setCreated(new Date());
//        registerBill.setIsCheckin(YesOrNoEnum.NO.getCode());
        registerBill.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
        registerBill.setIsDeleted(YesOrNoEnum.NO.getCode());
        registerBill.setIsPrintCheckSheet(YesOrNoEnum.NO.getCode());
        operatorUser.ifPresent(op -> {
            registerBill.setOperatorName(op.getName());
            registerBill.setOperatorId(op.getId());
            registerBill.setOperationTime(new Date());
        });
        registerBill.setIdCardNo(StringUtils.trimToEmpty(registerBill.getIdCardNo()).toUpperCase());
        // ???????????????
        String plate = StreamEx.ofNullable(registerBill.getPlate()).nonNull().map(StringUtils::trimToNull).nonNull()
                .map(String::toUpperCase).findFirst().orElse(null);
        registerBill.setPlate(plate);
        registerBill.setModified(new Date());
//        registerBill.setOrderType(OrderTypeEnum.REGISTER_BILL.getCode());
        registerBill.setRegisterSource(RegisterSourceEnum.TALLY_AREA.getCode());
        registerBill.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
        registerBill.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
        // ????????????
//        this.userPlateService.checkAndInsertUserPlate(registerBill.getUserId(), plate);


        if (registerBill.getPreserveType() == null) {
            registerBill.setPreserveType(PreserveTypeEnum.NONE.getCode());
        }

        logger.debug("???????????????????????????:registType={}", registerBill.getRegistType());
        //??????????????????????????????
        if (RegistTypeEnum.PARTIAL.getCode().equals(registerBill.getRegistType())) {
            RegisterHead registerHead = new RegisterHead();
            registerHead.setCode(registerBill.getRegisterHeadCode());
            List<RegisterHead> registerHeadList = registerHeadService.listByExample(registerHead);
            if (CollectionUtils.isNotEmpty(registerHeadList)) {
                registerHead = registerHeadList.get(0);
            } else {
                throw new TraceBizException("?????????????????????");
            }

            //???????????????????????????????????????????????????????????????????????????
            BigDecimal remianWeight = registerHead.getRemainWeight();
            BigDecimal billWeight = registerBill.getWeight();

            logger.debug("remianWeight={},billWeight={}", remianWeight, billWeight);

            if (remianWeight == null || (remianWeight != null && remianWeight.compareTo(billWeight) == -1)) {
                throw new TraceBizException("???????????????????????????????????????????????????????????????????????????");
            }

            registerHead.setRemainWeight(remianWeight.subtract(billWeight));
            registerHeadService.updateSelective(registerHead);
            List<String> arrivalTallynos = StreamEx.of(registerTallyAreaNoService.findTallyAreaNoByBillIdAndType(registerHead.getId(), BillTypeEnum.MASTER_BILL)).map(RegisterTallyAreaNo::getTallyareaNo).toList();
            registerBill.setArrivalTallynos(arrivalTallynos);
        }

        // ???????????????
        int result = super.saveOrUpdate(registerBill);
        if (result == 0) {
            logger.error("????????????????????????????????????:{}" ,registerBill.toString());
            throw new TraceBizException("????????????");
        }
        this.registerTallyAreaNoService.insertTallyAreaNoList(registerBill.getArrivalTallynos(), registerBill.getBillId(), BillTypeEnum.REGISTER_BILL);


        // ????????????
        List<ImageCert> imageCertList = registerBill.getImageCertList();
        if (!imageCertList.isEmpty()) {
            //?????????????????????????????????
            this.billService.updateHasImage(registerBill.getBillId(), imageCertList, BillTypeEnum.REGISTER_BILL);
        }

        this.processService.afterCreateBill(registerBill.getId(), registerBill.getMarketId(), processConfig, operatorUser);

        // ????????????????????????
        this.registerBillHistoryService.createHistory(registerBill.getBillId());


        // ??????/???????????????????????????brandId?????????
        this.brandService.createOrUpdateBrand(registerBill.getBrandName(), registerBill.getUserId(), registerBill.getMarketId())
                .ifPresent(brandId -> {
                    RegisterBill bill = new RegisterBill();
                    bill.setBrandId(brandId);
                    bill.setId(registerBill.getId());
                    this.updateSelective(bill);
                });


        //?????????????????????
        Integer businessType = MessageStateEnum.BUSINESS_TYPE_BILL.getCode();
        if (RegistTypeEnum.SUPPLEMENT.getCode().equals(registerBill.getRegistType())) {
            businessType = MessageStateEnum.BUSINESS_TYPE_FIELD_BILL.getCode();
        }
        addMessage(registerBill, MessageTypeEnum.BILLSUBMIT.getCode(), businessType, MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_MANAGER.getCode(), registerBill.getMarketId());
        //??????uap??????????????????
        this.syncCategoryService.saveAndSyncGoodInfo(registerBill.getProductId(), registerBill.getMarketId());
        this.syncUserInfoService.saveAndSyncUserInfo(registerBill.getUserId(), registerBill.getMarketId());
        this.updateUserQrStatusByUserId(registerBill.getBillId(), registerBill.getUserId());


        return registerBill.getId();
    }

    /**
     * ????????????????????????
     *
     * @param registerBill
     * @return
     */
    private RegisterBill simpleCheck(RegisterBill registerBill) {
        if (registerBill.getMarketId() == null) {
            throw new TraceBizException("????????????????????????");
        }
        BigDecimal weight = registerBill.getWeight();
        if (weight == null) {
            logger.error("????????????????????????");
            throw new TraceBizException("????????????????????????");
        }

        if (!NumUtils.isIntegerValue(weight)) {
            logger.error("???????????????????????????");
            throw new TraceBizException("???????????????????????????");
        }

        if (BigDecimal.ZERO.compareTo(weight) >= 0) {
            logger.error("????????????????????????0");
            throw new TraceBizException("????????????????????????0");
        }

        if (NumUtils.MAX_WEIGHT.compareTo(weight) < 0) {
            logger.error("????????????????????????" + NumUtils.MAX_WEIGHT.toString());
            throw new TraceBizException("????????????????????????" + NumUtils.MAX_WEIGHT.toString());
        }

        String brandName = StringUtils.trimToNull(registerBill.getBrandName());
        if (Objects.nonNull(brandName)) {
            if (!RegUtils.isValidInput(brandName)) {
                throw new TraceBizException("????????????????????????");
            }
            if (brandName.length() > 20) {
                throw new TraceBizException("??????????????????20??????");
            }
        }

        String remark = StringUtils.trimToNull(registerBill.getRemark());
        if (Objects.nonNull(remark)) {
            if (!RegUtils.isValidInput(remark)) {
                throw new TraceBizException("????????????????????????");
            }
            if (remark.length() > 200) {
                throw new TraceBizException("??????????????????200??????");
            }
        }

        // ????????????????????????????????????
        if (MeasureTypeEnum.COUNT_UNIT.equalsCode(registerBill.getMeasureType())) {
            // ??????
            if (registerBill.getPieceNum() == null) {
                logger.error("????????????????????????");
                throw new TraceBizException("????????????????????????");
            }
            if (BigDecimal.ZERO.compareTo(registerBill.getPieceNum()) >= 0) {
                logger.error("????????????????????????0");
                throw new TraceBizException("????????????????????????0");
            }
            if (NumUtils.MAX_NUM.compareTo(registerBill.getPieceNum()) < 0) {
                logger.error("????????????????????????{}", NumUtils.MAX_NUM.toString());
                throw new TraceBizException("????????????????????????" + NumUtils.MAX_NUM.toString());
            }
            if (!NumUtils.isIntegerValue(registerBill.getPieceNum())) {
                logger.error("???????????????????????????");
                throw new TraceBizException("???????????????????????????");
            }

            // ??????
            if (registerBill.getPieceWeight() == null) {
                logger.error("????????????????????????");
                throw new TraceBizException("????????????????????????");
            }
            if (BigDecimal.ZERO.compareTo(registerBill.getPieceWeight()) >= 0) {
                logger.error("????????????????????????0");
                throw new TraceBizException("????????????????????????0");
            }
            if (NumUtils.MAX_WEIGHT.compareTo(registerBill.getPieceWeight()) < 0) {
                logger.error("????????????????????????{}", NumUtils.MAX_WEIGHT.toString());
                throw new TraceBizException("????????????????????????" + NumUtils.MAX_WEIGHT.toString());
            }
            if (!NumUtils.isIntegerValue(registerBill.getPieceWeight())) {
                logger.error("???????????????????????????");
                throw new TraceBizException("???????????????????????????");
            }
        }
        return registerBill;
    }

    /**
     * ????????????????????????
     *
     * @param registerBill
     * @return
     */
    private RegisterBill checkAndSetDefaultValue(RegisterBill registerBill, Map<String, FieldConfigDetailRetDto> fieldConfigDetailRetDtoMap) {
        if (registerBill.getMarketId() == null) {
            throw new TraceBizException("????????????????????????");
        }
        BigDecimal weight = registerBill.getWeight();
        if (weight == null) {
            logger.error("????????????????????????");
            throw new TraceBizException("????????????????????????");
        }

        if (!NumUtils.isIntegerValue(weight)) {
            logger.error("???????????????????????????");
            throw new TraceBizException("???????????????????????????");
        }

        if (BigDecimal.ZERO.compareTo(weight) >= 0) {
            logger.error("????????????????????????0");
            throw new TraceBizException("????????????????????????0");
        }

        if (NumUtils.MAX_WEIGHT.compareTo(weight) < 0) {
            logger.error("????????????????????????" + NumUtils.MAX_WEIGHT.toString());
            throw new TraceBizException("????????????????????????" + NumUtils.MAX_WEIGHT.toString());
        }

        if (registerBill.getWeightUnit() == null) {
            logger.error("????????????????????????");
            throw new TraceBizException("????????????????????????");
        }

        if (StringUtils.isBlank(registerBill.getProductName()) || registerBill.getProductId() == null) {
            logger.error("????????????????????????");
            throw new TraceBizException("????????????????????????");
        }

        //??????????????????
        if (StringUtils.isBlank(registerBill.getName())) {
            logger.error("????????????????????????");
            throw new TraceBizException("????????????????????????");
        }
        if (registerBill.getUserId() == null) {
            logger.error("??????ID????????????");
            throw new TraceBizException("??????ID????????????");
        }


//        if(StringUtils.isNotBlank(registerBill.getName())){
//            if(!RegUtils.isValidInput(registerBill.getName())){
//                throw new TraceBizException("?????????????????????????????????");
//            }
//            if(StringUtils.trimToEmpty(registerBill.getName()).length()>50){
//                throw new TraceBizException("????????????????????????50??????");
//            }
//        }
        if (StringUtils.isNotBlank(registerBill.getCorporateName())) {
            if (!RegUtils.isValidInput(registerBill.getCorporateName())) {
                throw new TraceBizException("?????????????????????????????????");
            }
            if (StringUtils.trimToEmpty(registerBill.getProductAliasName()).length() > 50) {
                throw new TraceBizException("????????????????????????50??????");
            }
        }

        if (StringUtils.trimToEmpty(registerBill.getProductAliasName()).length() > 40) {
            throw new TraceBizException("????????????????????????40??????");
        }
        //?????????????????????
        if (!BillTypeEnum.fromCode(registerBill.getBillType()).isPresent()) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getBillType).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("???????????????????????????");
            }
            registerBill.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
        }


        //????????????
        if (StringUtils.isBlank(registerBill.getOriginName()) || registerBill.getOriginId() == null) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getOriginId).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("????????????????????????");
            }
        }

        //????????????
        if (StringUtils.isBlank(registerBill.getRemark())) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getRemark).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("??????????????????");
            }
        }
        String remark = StringUtils.trimToNull(registerBill.getRemark());
        if (Objects.nonNull(remark)) {
            if (!RegUtils.isValidInput(remark)) {
                throw new TraceBizException("????????????????????????");
            }
            if (remark.length() > 200) {
                throw new TraceBizException("??????????????????200??????");
            }
        }

        //????????????
        if (registerBill.getTruckTareWeight() == null) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getTruckTareWeight).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("??????????????????");
            }
        }
        if (registerBill.getTruckTareWeight() != null) {
            if (NumUtils.MAX_WEIGHT.compareTo(registerBill.getTruckTareWeight()) < 0) {
                logger.error("????????????????????????" + NumUtils.MAX_WEIGHT.toString());
                throw new TraceBizException("????????????????????????" + NumUtils.MAX_WEIGHT.toString());
            }

            if (!NumUtils.isIntegerValue(registerBill.getTruckTareWeight())) {
                logger.error("???????????????????????????");
                throw new TraceBizException("???????????????????????????");
            }
        }

        //????????????/????????????
        if (!TruckTypeEnum.fromCode(registerBill.getTruckType()).isPresent()) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getTruckType).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("????????????????????????");
            }
            registerBill.setTruckType(TruckTypeEnum.FULL.getCode());
        }

        if (TruckTypeEnum.POOL.equalsToCode(registerBill.getTruckType())) {
            if (StringUtils.isBlank(registerBill.getPlate())) {
                throw new TraceBizException("??????????????????");
            }
        }

        //????????????
        if (registerBill.getUnitPrice() == null) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getUnitPrice).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("????????????????????????");
            }
        }

        //????????????
        if (StringUtils.isBlank(registerBill.getSpecName())) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getSpecName).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("????????????????????????");
            }
        }
        String specName = registerBill.getSpecName();
        if (StringUtils.isNotBlank(specName) && !RegUtils.isValidInput(specName)) {
            throw new TraceBizException("??????????????????????????????");
        }


        //??????
        if (StringUtils.isBlank(registerBill.getBrandName())) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getBrandName).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("??????????????????");
            }
        }

        String brandName = StringUtils.trimToNull(registerBill.getBrandName());
        if (Objects.nonNull(brandName)) {
            if (!RegUtils.isValidInput(brandName)) {
                throw new TraceBizException("????????????????????????");
            }
            if (brandName.length() > 20) {
                throw new TraceBizException("??????????????????20??????");
            }
        }


        //????????????
        if (registerBill.getUpStreamId() == null) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getUpStreamId).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("????????????????????????");
            }
        }

        //??????????????????
        if (registerBill.getArrivalDatetime() == null) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getArrivalDatetime).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("??????????????????????????????");
            }
        }

        //????????????
        List<String> arrivalTallynos = StreamEx.ofNullable(registerBill.getArrivalTallynos()).flatCollection(Function.identity()).map(StringUtils::trimToNull).nonNull().toList();
        if (arrivalTallynos.isEmpty()) {
            String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getArrivalTallynos).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("????????????????????????");
            }

        }
        registerBill.setArrivalTallynos(arrivalTallynos);
        boolean hasValidTallyno = StreamEx.of(arrivalTallynos).anyMatch(no -> {
            return !RegUtils.isValidInput(no);
        });
        if (hasValidTallyno) {
            throw new TraceBizException("??????????????????????????????");
        }
        //????????????
        String propName = PropertyUtils.getPropertyDescriptor(registerBill, RegisterBill::getImageCertList).getName();
        FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
        if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
            if (CollectionUtils.isEmpty(registerBill.getImageCertList())) {
                throw new TraceBizException("??????????????????");
            } else {

                String json = super.toJSONString(registerBill);
                List<Integer> inputCertTypeList = JsonPathUtil.parse(json, retDto.getDefaultFieldDetail().getJsonPath(), Integer.class);
                List<Integer> availableValueList = StreamEx.ofNullable(retDto.getAvailableValueList()).flatCollection(Function.identity()).nonNull().map(String::valueOf).map(v -> {
                    try {
                        return Integer.parseInt(v);
                    } catch (Exception e) {
                        return null;
                    }
                }).nonNull().toList();
                if (!availableValueList.containsAll(inputCertTypeList)) {
                    throw new TraceBizException("??????????????????");
                }
            }
        }


        // ???????????????????????????????????????
//        if (MeasureTypeEnum.COUNT_WEIGHT.equalsCode(registerBill.getMeasureType())) {
//            registerBill.setPieceNum(BigDecimal.ZERO);
//            registerBill.setPieceWeight(BigDecimal.ZERO);
//        }

        // ????????????????????????????????????
        if (MeasureTypeEnum.COUNT_UNIT.equalsCode(registerBill.getMeasureType())) {
            // ??????
            if (registerBill.getPieceNum() == null) {
                logger.error("????????????????????????");
                throw new TraceBizException("????????????????????????");
            }
            if (BigDecimal.ZERO.compareTo(registerBill.getPieceNum()) >= 0) {
                logger.error("????????????????????????0");
                throw new TraceBizException("????????????????????????0");
            }
            if (NumUtils.MAX_NUM.compareTo(registerBill.getPieceNum()) < 0) {
                logger.error("????????????????????????{}", NumUtils.MAX_NUM.toString());
                throw new TraceBizException("????????????????????????" + NumUtils.MAX_NUM.toString());
            }
            if (!NumUtils.isIntegerValue(registerBill.getPieceNum())) {
                logger.error("???????????????????????????");
                throw new TraceBizException("???????????????????????????");
            }

            // ??????
            if (registerBill.getPieceWeight() == null) {
                logger.error("????????????????????????");
                throw new TraceBizException("????????????????????????");
            }
            if (BigDecimal.ZERO.compareTo(registerBill.getPieceWeight()) >= 0) {
                logger.error("????????????????????????0");
                throw new TraceBizException("????????????????????????0");
            }
            if (NumUtils.MAX_WEIGHT.compareTo(registerBill.getPieceWeight()) < 0) {
                logger.error("????????????????????????{}", NumUtils.MAX_WEIGHT.toString());
                throw new TraceBizException("????????????????????????" + NumUtils.MAX_WEIGHT.toString());
            }
            if (!NumUtils.isIntegerValue(registerBill.getPieceWeight())) {
                logger.error("???????????????????????????");
                throw new TraceBizException("???????????????????????????");
            }
        }


        if (Objects.nonNull(registerBill.getUnitPrice())
                && NumUtils.MAX_UNIT_PRICE.compareTo(registerBill.getUnitPrice()) < 0) {
            logger.error("????????????????????????" + NumUtils.MAX_UNIT_PRICE.toString());
            throw new TraceBizException("????????????????????????" + NumUtils.MAX_UNIT_PRICE.toString());
        }


        return registerBill;
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    private UserTicket getOptUser() {
        return SessionContext.getSessionContext().getUserTicket();
    }

    /**
     * ?????????????????????
     *
     * @param
     * @return
     */
    @Transactional
    public Long doEdit(RegisterBill input, List<ImageCert> imageCertList, Optional<OperatorUser> operatorUser) {
        if (input == null || input.getId() == null) {
            throw new TraceBizException("????????????");
        }
        this.simpleCheck(input);
        RegisterBill billItem = this.getAndCheckById(input.getId())
                .orElseThrow(() -> new TraceBizException("???????????????"));

        if (BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(billItem.getVerifyStatus())
                || BillVerifyStatusEnum.RETURNED.equalsToCode(billItem.getVerifyStatus())) {
            // ?????????????????????????????????????????????????????????
        } else {
            throw new TraceBizException("????????????????????????????????????");
        }
        if (!DetectStatusEnum.NONE.equalsToCode(billItem.getDetectStatus())) {
            throw new TraceBizException("????????????????????????????????????");
        }

        TradeDetail tradeDetailItem = this.tradeDetailService.findBilledTradeDetailByBillId(billItem.getBillId())
                .orElse(null);
        // ???????????????
        String plate = StreamEx.ofNullable(input.getPlate()).filter(StringUtils::isNotBlank).map(p -> p.toUpperCase())
                .findFirst().orElse(null);
        input.setPlate(plate);
        // ????????????
//        this.userPlateService.checkAndInsertUserPlate(input.getUserId(), plate);
        input.setVerifyStatus(BillVerifyStatusEnum.WAIT_AUDIT.getCode());
        input.setModified(new Date());

        input.setOperatorName(null);
        input.setOperatorId(null);
        input.setOperationTime(null);
        input.setReason("");
        operatorUser.ifPresent(op -> {
            input.setOperatorName(op.getName());
            input.setOperatorId(op.getId());
            input.setOperationTime(new Date());
        });
        if (tradeDetailItem == null) {
            // ????????????????????????
            if (RegistTypeEnum.SUPPLEMENT.equalsToCode(input.getRegistType())) {
//                input.setIsCheckin(YesOrNoEnum.YES.getCode());
                input.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
            } else {
//                input.setIsCheckin(YesOrNoEnum.NO.getCode());
                input.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
            }
        } else if (CheckinStatusEnum.ALLOWED.equalsToCode(tradeDetailItem.getCheckinStatus())) {
//            input.setIsCheckin(YesOrNoEnum.YES.getCode());
            input.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
        } else {
//            input.setIsCheckin(YesOrNoEnum.NO.getCode());
            input.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
        }
        input.setCreated(new Date());
        this.updateSelective(input);
        this.registerBillHistoryService.createHistory(billItem.getBillId());

        imageCertList = StreamEx.ofNullable(imageCertList).nonNull().flatCollection(Function.identity()).nonNull().toList();
        if (!imageCertList.isEmpty()) {
            // ????????????
            this.billService.updateHasImage(input.getId(), imageCertList, BillTypeEnum.REGISTER_BILL);
        }

        // imageCertService.insertImageCert(imageCertList, input.getId());


        this.tradeDetailService.findBilledTradeDetailByBillId(billItem.getBillId()).ifPresent(td -> {
            TradeDetail updatableRecord = new TradeDetail();
            updatableRecord.setId(td.getId());
            updatableRecord.setModified(new Date());
            updatableRecord.setStockWeight(input.getWeight());
            updatableRecord.setTotalWeight(input.getWeight());
            this.tradeDetailService.updateSelective(updatableRecord);
        });

        this.brandService.createOrUpdateBrand(input.getBrandName(), billItem.getUserId(), input.getMarketId());
        this.updateUserQrStatusByUserId(billItem.getBillId(), billItem.getUserId());
        return input.getId();
    }

    /**
     * ???????????????
     *
     * @param userId
     * @param operatorUser
     * @return
     */
    @Transactional
    public Long doDelete(Long billId, Long userId, Optional<OperatorUser> operatorUser) {
        if (billId == null || userId == null) {
            throw new TraceBizException("????????????");
        }
        RegisterBill billItem = this.getAndCheckById(billId).orElseThrow(() -> new TraceBizException("???????????????"));
        if (!userId.equals(billItem.getUserId())) {
            throw new TraceBizException("????????????????????????");
        }
        if (CheckinStatusEnum.ALLOWED.equalsToCode(billItem.getCheckinStatus())) {//YesOrNoEnum.YES.getCode().equals(billItem.getIsCheckin())) {
            throw new TraceBizException("???????????????????????????");
        }
        if (BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(billItem.getVerifyStatus()) && DetectStatusEnum.NONE.equalsToCode(billItem.getDetectStatus())) {
            //dothing
        } else {
            throw new TraceBizException("??????????????????????????????");
        }
        if (billItem.getDetectRequestId() != null) {
            throw new TraceBizException("???????????????????????????????????????");
        }
        RegisterBill bill = new RegisterBill();
        bill.setId(billItem.getBillId());
        bill.setIsDeleted(YesOrNoEnum.YES.getCode());

        operatorUser.ifPresent(op -> {
            bill.setOperatorName(op.getName());
            bill.setOperatorId(op.getId());
            bill.setOperationTime(new Date());
            bill.setDeleteUser(op.getName());
            bill.setDeleteTime(new Date());
        });
        this.updateSelective(bill);
        this.registerBillHistoryService.createHistory(billItem.getBillId());
        this.userQrHistoryService.rollbackByBill(billItem);
        return billId;
    }

    /**
     * ?????????dto
     *
     * @param dto
     * @return
     */
    private RegisterBillDto preBuildDTO(RegisterBillDto dto) {
        if (StringUtils.isNotBlank(dto.getAttrValue())) {
            switch (dto.getAttr()) {
                case "code":
                    dto.setCode(dto.getAttrValue());
                    break;
                // case "plate":
                // registerBill.setPlate(registerBill.getAttrValue());
                // break;
                // case "tallyAreaNo":
                //// registerBill.setTallyAreaNo(registerBill.getAttrValue());
                // registerBill.setLikeTallyAreaNo(registerBill.getAttrValue());
                // break;
                case "latestDetectOperator":
                    dto.setLatestDetectOperator(dto.getAttrValue());
                    break;
                case "name":
                    dto.setName(dto.getAttrValue());
                    break;
                case "productName":
                    dto.setProductName(dto.getAttrValue());
                    break;
                case "likeSampleCode":
                    dto.setLikeSampleCode(dto.getAttrValue());
                    break;
            }
        }

        StringBuilder sql = this.buildDynamicCondition(dto);
        if (sql.length() > 0) {
            dto.setMetadata(IDTO.AND_CONDITION_EXPR, sql.toString());
        }

        return dto;
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param input
     * @return
     * @throws Exception
     */
    public RegisterBill findFirstWaitAuditRegisterBillCreateByCurrentUser(RegisterBillDto input) throws Exception {
        RegisterBillDto dto = new RegisterBillDto();
        UserTicket userTicket = getOptUser();
        dto.setOperatorId(userTicket.getId());
//        dto.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
        dto.setVerifyStatus(BillVerifyStatusEnum.WAIT_AUDIT.getCode());
        dto.setRows(1);

        dto.setSort("code");
        dto.setOrder("desc");
        return this.listByExample(dto).stream().findFirst().orElse(new RegisterBill());
    }

    /**
     * ????????????
     *
     * @param input
     * @return
     * @throws Exception
     */
    public String listPage(RegisterBillDto input) throws Exception {
        RegisterBillDto dto = this.preBuildDTO(input);
        return this.listEasyuiPageByExample(dto, true).toString();
    }

    /**
     * ????????????
     *
     * @param registerBill
     * @return
     */
    private StringBuilder buildDynamicCondition(RegisterBillDto registerBill) {
        StringBuilder sql = new StringBuilder();
        if (registerBill.getHasCheckSheet() != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            if (registerBill.getHasCheckSheet()) {
                sql.append("  (check_sheet_id is not null) ");
            } else {
                sql.append("  (check_sheet_id is null) ");
            }
        }
        return sql;
    }

    /**
     * ???????????????
     *
     * @param input
     * @param operatorUser
     * @return
     */
    @Transactional
    public Long doVerifyBeforeCheckIn(RegisterBill input, Optional<OperatorUser> operatorUser) {
        if (input == null || input.getId() == null) {
            throw new TraceBizException("????????????");
        }
        RegisterBill billItem = this.getAndCheckById(input.getId())
                .orElseThrow(() -> new TraceBizException("???????????????"));

        if (//YesOrNoEnum.YES.getCode().equals(billItem.getIsCheckin())
                CheckinStatusEnum.ALLOWED.equalsToCode(billItem.getCheckinStatus())
                        || RegistTypeEnum.SUPPLEMENT.equalsToCode(billItem.getRegistType())) {
            throw new TraceBizException("???????????????????????????,??????????????????");
        }
        BillVerifyStatusEnum toVerifyState = BillVerifyStatusEnum.fromCodeOrEx(input.getVerifyStatus());
        this.doVerify(billItem, toVerifyState, input.getReason(), operatorUser);

        if (BillVerifyStatusEnum.PASSED == toVerifyState) {
            processService.afterBillPassed(billItem.getId(), billItem.getMarketId(), operatorUser);
        }

        //????????????
        addMessage(billItem, MessageTypeEnum.BILLPASS.getCode(), MessageStateEnum.BUSINESS_TYPE_BILL.getCode(), MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_NORMAL.getCode(), billItem.getMarketId());
        return billItem.getId();
    }

    /**
     * ???????????????
     *
     * @param billId
     * @param operatorUser
     * @return
     */
    @Transactional
    public Long doVerifyAfterCheckIn(Long billId, Integer verifyStatus, String reason,
                                     Optional<OperatorUser> operatorUser) {
        if (billId == null || verifyStatus == null) {
            throw new TraceBizException("????????????");
        }
        BillVerifyStatusEnum toVerifyState = BillVerifyStatusEnum.fromCodeOrEx(verifyStatus);

        RegisterBill billItem = this.getAndCheckById(billId).orElseThrow(() -> new TraceBizException("???????????????"));

//        if (!YesOrNoEnum.YES.getCode().equals(billItem.getIsCheckin())
        if (!CheckinStatusEnum.ALLOWED.equalsToCode(billItem.getCheckinStatus())
                && !RegistTypeEnum.SUPPLEMENT.equalsToCode(billItem.getRegistType())) {
            throw new TraceBizException("???????????????????????????,??????????????????");
        }
        if (BillVerifyStatusEnum.PASSED.equalsToCode(verifyStatus)) {
            this.processService.doCheckIn(operatorUser, Lists.newArrayList(billItem.getBillId()),
                    CheckinStatusEnum.ALLOWED);
        }
        this.doVerify(billItem, toVerifyState, reason, operatorUser);
        //????????????
        addMessage(billItem, MessageTypeEnum.BILLPASS.getCode(), MessageStateEnum.BUSINESS_TYPE_BILL.getCode(), MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_NORMAL.getCode(), billItem.getMarketId());
        return billItem.getId();

    }


    /**
     * ???????????????
     *
     * @param billItem
     * @param messageType
     * @param businessType
     * @param receiverType
     * @param marketId
     */
    private void addMessage(RegisterBill billItem, Integer messageType, Integer businessType, Integer receiverType, Long marketId) {

    }

    /**
     * ??????????????????
     *
     * @param billItem
     * @return
     */
    private Map<String, Object> getSmsMap(RegisterBill billItem) {
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("userName", billItem.getName());
        smsMap.put("created", DateUtils.format(new Date(), "yyyy???MM???dd??? HH:mm:ss"));
        smsMap.put("billNo", billItem.getCode());
        smsMap.put("productName", "??????:" + billItem.getProductName() + "    ??????:" + billItem.getPlate());
        return smsMap;
    }

    /**
     * ??????
     *
     * @param billItem
     * @param toVerifyState
     * @param reason
     * @param operatorUser
     */
    private void doVerify(RegisterBill billItem, BillVerifyStatusEnum toVerifyState, String reason,
                          Optional<OperatorUser> operatorUser) {
        BillVerifyStatusEnum fromVerifyState = BillVerifyStatusEnum.fromCodeOrEx(billItem.getVerifyStatus());


        logger.info("??????: billId: {} from {} to {}", billItem.getBillId(), fromVerifyState.getName(),
                toVerifyState.getName());
        if (!BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(billItem.getVerifyStatus())) {
            throw new TraceBizException("????????????????????????????????????");
        }
        if (BillVerifyStatusEnum.WAIT_AUDIT == toVerifyState) {
            throw new TraceBizException("??????????????????");
        }
        if (fromVerifyState == toVerifyState) {
            throw new TraceBizException("??????????????????");
        }

        // ???????????????????????????
        RegisterBill bill = new RegisterBill();
        bill.setId(billItem.getId());
        bill.setVerifyStatus(toVerifyState.getCode());
        operatorUser.ifPresent(op -> {
            bill.setOperatorId(op.getId());
            bill.setOperatorName(op.getName());
            bill.setOperationTime(new Date());
        });

        bill.setReason(StringUtils.trimToEmpty(reason));
        if (BillVerifyStatusEnum.PASSED == toVerifyState) {
            if (RegistTypeEnum.SUPPLEMENT.equalsToCode(billItem.getRegistType())) {
                bill.setVerifyType(VerifyTypeEnum.CHECKIN_WITHOUT_VERIFY.getCode());
            } else {
                bill.setVerifyType(VerifyTypeEnum.VERIFY_BEFORE_CHECKIN.getCode());
            }
//            if (CheckinStatusEnum.ALLOWED.equalsToCode(billItem.getCheckinStatus())) {
////            if (YesOrNoEnum.YES.getCode().equals(billItem.getIsCheckin())) {
//                bill.setVerifyType(VerifyTypeEnum.PASSED_AFTER_CHECKIN.getCode());
//            } else {
//                bill.setVerifyType(VerifyTypeEnum.PASSED_BEFORE_CHECKIN.getCode());
//            }
        }
        bill.setModified(new Date());
        this.updateSelective(bill);
        // ????????????????????????
        this.registerBillHistoryService.createHistory(billItem.getId());
        this.billVerifyHistoryService.createVerifyHistory(Optional.of(fromVerifyState), bill.getBillId(), operatorUser);

        // ???????????????tradeDetail???batchStock??????

        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billItem.getId(),
                operatorUser);

        // ?????????????????????
        this.updateUserQrStatusByUserId(billItem.getBillId(), billItem.getUserId());

    }


    /**
     * ??????????????????????????????????????????????????????
     *
     * @param userId
     */
    public void updateUserQrStatusByUserId(Long billId, Long userId) {
        if (billId == null || userId == null) {
            return;
        }
        RegisterBill billItem = this.get(billId);
        this.userQrHistoryService.createUserQrHistoryForVerifyBill(billItem, userId);
    }


    /**
     * ????????????
     *
     * @param query
     * @return
     */
    public BasePage<RegisterBill> listPageBeforeCheckinVerifyBill(RegisterBillDto query) {
        if (query == null || query.getMarketId() == null) {
            throw new TraceBizException("????????????");
        }
        query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLBeforeCheckIn(query));
        query.setIsDeleted(YesOrNoEnum.NO.getCode());
//        query.setOrderType(OrderTypeEnum.REGISTER_BILL.getCode());
        return this.listPageByExample(query);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param query
     * @return
     */
    public List<VerifyStatusCountOutputDto> countByVerifyStatuseBeforeCheckin(RegisterBillDto query) {
        if (query == null || query.getMarketId() == null) {
            throw new TraceBizException("????????????");
        }
        query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLBeforeCheckIn(query));
        query.setIsDeleted(YesOrNoEnum.NO.getCode());
        return this.countByVerifyStatus(query);
    }

    /**
     * ????????????
     *
     * @param query
     * @return
     */
    public BasePage<RegisterBill> listPageAfterCheckinVerifyBill(RegisterBillDto query) {
        if (query == null || query.getMarketId() == null) {
            throw new TraceBizException("????????????");
        }
        query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLAfterCheckIn(query));
        query.setIsDeleted(YesOrNoEnum.NO.getCode());
//        query.setOrderType(OrderTypeEnum.REGISTER_BILL.getCode());
        return this.listPageByExample(query);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param query
     * @return
     */
    public List<VerifyStatusCountOutputDto> countByVerifyStatuseAfterCheckin(RegisterBillDto query) {
        if (query == null || query.getMarketId() == null) {
            throw new TraceBizException("????????????");
        }
        query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLAfterCheckIn(query));
        query.setIsDeleted(YesOrNoEnum.NO.getCode());
        return this.countByVerifyStatus(query);
    }

    /**
     * ??????????????????
     *
     * @param query
     * @return
     */
    public Map<Integer, Map<String, List<RegisterBill>>> listPageCheckInData(RegisterBillDto query) {
        if (query == null || query.getMarketId() == null) {
            throw new TraceBizException("????????????");
        }
        String dynaWhere = " checkin_status=" + CheckinStatusEnum.NONE.getCode() + " and regist_type =" + RegistTypeEnum.NONE.getCode();

        query.setSort("created");
        query.setOrder("desc");
        query.setMetadata(IDTO.AND_CONDITION_EXPR, dynaWhere);
        query.setIsDeleted(YesOrNoEnum.NO.getCode());
        query.setTruckType(TruckTypeEnum.FULL.getCode());
        List<RegisterBill> list = this.listByExample(query);

        query.setTruckType(TruckTypeEnum.POOL.getCode());
        List<RegisterBill> userPoolList = this.listByExample(query);

        List<String> plateList = StreamEx.of(userPoolList).filter(bill -> {
            return TruckTypeEnum.POOL.equalsToCode(bill.getTruckType());
        }).map(RegisterBill::getPlate).distinct().toList();
        List<RegisterBill> samePlatePoolTruckTypeBillList = StreamEx.ofNullable(plateList).filter(l -> !l.isEmpty())
                .flatMap(l -> {
                    query.setPlateList(plateList);
                    query.setTruckType(TruckTypeEnum.POOL.getCode());
                    query.setUserId(null);
                    return StreamEx.of(this.listByExample(query));
                }).toList();

        Map<Integer, List<RegisterBill>> truckTypeBillMap = StreamEx.of(list).append(userPoolList)
                .append(samePlatePoolTruckTypeBillList).distinct(RegisterBill::getBillId)
                .groupingBy(RegisterBill::getTruckType);
        Map<Integer, Map<String, List<RegisterBill>>> resultMap = EntryStream.of(truckTypeBillMap)
                .flatMapToValue((k, v) -> {
                    if (TruckTypeEnum.FULL.equalsToCode(k)) {
                        return Stream.of(StreamEx.of(v).mapToEntry(item -> {
                            return DateUtil.format(item.getCreated(), "yyyy-MM-dd");
                        }, Function.identity())
                                .grouping(() -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER.reversed())));
                    }
                    if (TruckTypeEnum.POOL.equalsToCode(k)) {
                        return Stream.of(StreamEx.of(v).groupingBy(RegisterBill::getPlate));
                    }
                    return StreamEx.empty();
                }).toMap();
        return resultMap;

    }


    /**
     * ????????????
     *
     * @param inputDto
     * @return
     */

    public RegisterBillOutputDto viewTradeDetailBill(RegisterBillApiInputDto inputDto) {
        Long billId = inputDto.getBillId();
        Long tradeDetailId = inputDto.getTradeDetailId();
        Long marketId = inputDto.getMarketId();
        if (billId == null && tradeDetailId == null) {
            throw new TraceBizException("????????????");
        }
//        if(marketId==null){
//            throw new TraceBizException("????????????");
//        }

        TradeDetail tradeDetailItem = StreamEx.ofNullable(tradeDetailId).nonNull().map(tdId -> {
            return this.tradeDetailService.get(tdId);
        }).findFirst().orElse(new TradeDetail());

        RegisterBill registerBill = StreamEx.ofNullable(billId).append(tradeDetailItem.getBillId()).nonNull()
                .map(bId -> {
                    return this.get(bId);
                }).findFirst().orElse(new RegisterBill());

        List<ImageCert> imageCertList = StreamEx.ofNullable(registerBill).flatMap(bill -> {
            return imageCertService.findImageCertListByBillId(bill.getId(), BillTypeEnum.fromCode(bill.getBillType()).orElse(null)).stream();
        }).toList();

        String upStreamName = StreamEx.ofNullable(registerBill.getUpStreamId()).nonNull().map(upStreamId -> {
            return this.upStreamService.get(upStreamId);
        }).nonNull().findAny().map(UpStream::getName).orElse("");
        RegisterBillOutputDto outputdto = RegisterBillOutputDto.build(registerBill, Lists.newArrayList());
        outputdto.setImageCertList(imageCertList);
        outputdto.setUpStreamName(upStreamName);
        outputdto.setTruckTareWeight(registerBill.getTruckTareWeight());

        if (tradeDetailItem.getId() != null && registerBill.getId() != null) {
            outputdto.setWeight(tradeDetailItem.getTotalWeight());
            return outputdto;
        } else if (registerBill.getId() != null) {
            outputdto.setWeight(registerBill.getWeight());
            return outputdto;
        } else {
            throw new TraceBizException("????????????");
        }

    }

    /**
     * ??????like????????????
     *
     * @param query
     * @return
     */
    private Optional<String> buildLikeKeyword(RegisterBillDto query) {
        String sql = null;
        if (StringUtils.isNotBlank(query.getKeyword())) {
            String keyword = query.getKeyword().trim();
            sql = "( product_name like '%" + keyword + "%'  OR name like '%" + keyword + "%'  OR (register_source=" + RegisterSourceEnum.TALLY_AREA.getCode() + " and source_name like '%" + keyword + "%') )";
        }
        return Optional.ofNullable(sql);

    }

    /**
     * ??????????????????
     *
     * @param query
     * @return
     */
    private String dynamicSQLBeforeCheckIn(RegisterBillDto query) {
        List<String> sqlList = new ArrayList<>();
        this.buildLikeKeyword(query).ifPresent(sql -> {
            sqlList.add(sql);
        });
        // ?????????????????????????????? && ?????????????????????????????????
        sqlList.add("( (regist_type=" + RegistTypeEnum.NONE.getCode()
                + " or regist_type=" + RegistTypeEnum.PARTIAL.getCode() + ")  and (checkin_status=" + CheckinStatusEnum.NONE.getCode()
                + " OR (checkin_status=" + CheckinStatusEnum.ALLOWED.getCode() + " and verify_status="
                + BillVerifyStatusEnum.PASSED.getCode() + " and verify_type="
                + VerifyTypeEnum.VERIFY_BEFORE_CHECKIN.getCode() + " ) ) )");

        return StreamEx.of(sqlList).joining(" AND ");
    }

    /**
     * ??????????????????
     *
     * @param query
     * @return
     */
    private String dynamicSQLAfterCheckIn(RegisterBillDto query) {
        List<String> sqlList = new ArrayList<>();
        this.buildLikeKeyword(query).ifPresent(sql -> {
            sqlList.add(sql);
        });
        sqlList.add("( regist_type=" + RegistTypeEnum.SUPPLEMENT.getCode() + " OR  (checkin_status=" + CheckinStatusEnum.ALLOWED.getCode()
                + " AND verify_status<>" + BillVerifyStatusEnum.PASSED.getCode() + ") OR(verify_status="
                + BillVerifyStatusEnum.PASSED.getCode() + " and verify_type="
                + VerifyTypeEnum.CHECKIN_WITHOUT_VERIFY.getCode() + ") )");

        return StreamEx.of(sqlList).joining(" AND ");
    }

    /**
     * ??????????????????
     *
     * @param query
     * @return
     */
    private List<VerifyStatusCountOutputDto> countByVerifyStatus(RegisterBillDto query) {
        List<VerifyStatusCountOutputDto> dtoList = this.getActualDao().countByVerifyStatus(query);
        Map<Integer, Integer> verifyStatusNumMap = StreamEx.of(dtoList)
                .toMap(VerifyStatusCountOutputDto::getVerifyStatus, VerifyStatusCountOutputDto::getNum);
        return StreamEx.of(BillVerifyStatusEnum.values()).map(e -> {
            VerifyStatusCountOutputDto dto = VerifyStatusCountOutputDto.buildDefault(e);
            if (verifyStatusNumMap.containsKey(dto.getVerifyStatus())) {
                dto.setNum(verifyStatusNumMap.get(dto.getVerifyStatus()));
            }
            return dto;
        }).toList();
    }

    /**
     * @return
     * @Description ??????????????????????????????
     * * @Date 2020/10/21 16:04
     */
    public BasePage<RegisterBill> listPageApi(RegisterBillDto input) {

        StringBuilder sql = new StringBuilder();
        buildFormLikeKeyword(input).ifPresent(sql::append);
        if (sql.length() > 0) {
            input.setMetadata(IDTO.AND_CONDITION_EXPR, sql.toString());
        }

        BasePage<RegisterBill> registerBillBasePage = listPageByExample(input);
        return registerBillBasePage;
    }

    /**
     * ??????Like??????
     *
     * @param query
     * @return
     */
    private Optional<String> buildFormLikeKeyword(RegisterBillDto query) {
        String sql = null;
        if (StringUtils.isNotBlank(query.getKeyword())) {
            String keyword = query.getKeyword().trim();
            sql = "( product_name like '%" + keyword + "%'  OR user_id in(select id from `user` u where u.name like '%"
                    + keyword + "%' OR legal_person like '%" + keyword + "%' OR phone like '%"
                    + keyword + "%') OR card_no like '%" + keyword + "%' OR register_head_code LIKE '%" + keyword + "%' )";
        }
        return Optional.ofNullable(sql);
    }

    /**
     * ?????????????????????
     *
     * @param dto
     * @param userId
     * @param operatorUser
     * @return
     */
    @Transactional
    public Long doDelete(CreateRegisterBillInputDto dto, Long userId, Optional<OperatorUser> operatorUser) {
        if (dto == null || userId == null) {
            throw new TraceBizException("????????????");
        }
        RegisterBill billItem = this.getAndCheckById(dto.getBillId()).orElseThrow(() -> new TraceBizException("???????????????"));
//        if (!userId.equals(billItem.getUserId())) {
//            throw new TraceBusinessException("????????????????????????");
//        }
//        if (YesOrNoEnum.YES.equalsToCode(billItem.getIsCheckin())) {
//            throw new TraceBusinessException("???????????????????????????");
//        }
        if (BillVerifyStatusEnum.NO_PASSED.equalsToCode(billItem.getVerifyStatus())) {
            throw new TraceBizException("?????????????????????????????????");
        }
        RegisterBill bill = new RegisterBill();
        bill.setId(billItem.getBillId());
        bill.setIsDeleted(dto.getIsDeleted());

        operatorUser.ifPresent(op -> {
            bill.setOperatorName(op.getName());
            bill.setOperatorId(op.getId());
            bill.setOperationTime(new Date());
            bill.setDeleteUser(op.getName());
            bill.setDeleteTime(new Date());
        });
        this.updateSelective(bill);
        this.registerBillHistoryService.createHistory(billItem.getBillId());
        this.userQrHistoryService.rollbackByBill(bill);
        return dto.getBillId();
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param query
     * @return
     */
    public List<VerifyStatusCountOutputDto> countBillsByVerifyStatus(RegisterBillDto query) {
        if (query == null || query.getMarketId() == null) {
            throw new TraceBizException("????????????");
        }
        query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLFormBill(query));
        query.setIsDeleted(YesOrNoEnum.NO.getCode());
        List<VerifyStatusCountOutputDto> countList = this.countByVerifyStatus(query);

        return countList;
    }

    /**
     * ??????????????????
     *
     * @param query
     * @return
     */
    private String dynamicSQLFormBill(RegisterBillDto query) {
        List<String> sqlList = new ArrayList<>();
        this.buildFormLikeKeyword(query).ifPresent(sql -> {
            sqlList.add(sql);
        });
        sqlList.add("(checkin_status=" + CheckinStatusEnum.NONE.getCode()
                + " OR (checkin_status=" + CheckinStatusEnum.ALLOWED.getCode() + " and verify_status="
                + BillVerifyStatusEnum.PASSED.getCode() + ") OR (checkin_status=" + CheckinStatusEnum.ALLOWED.getCode() + " and verify_status="
                + BillVerifyStatusEnum.RETURNED.getCode() + " ))");
        return StreamEx.of(sqlList).joining(" AND ");
    }

    /**
     * ????????????????????????????????????????????????
     */
    public RegisterBill findHighLightBill(RegisterBillDto input, OperatorUser operatorUser) throws Exception {
        RegisterBillDto dto = new RegisterBillDto();
        dto.setOperatorId(operatorUser.getId());
        dto.setMarketId(operatorUser.getMarketId());
//        dto.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
        dto.setVerifyStatus(BillVerifyStatusEnum.WAIT_AUDIT.getCode());
        dto.setRows(1);
        dto.setSort("code");
        dto.setOrder("desc");
        return this.billService.listByExample(dto).stream().findFirst().orElse(new RegisterBill());
    }

    /**
     * ??????????????????????????????
     *
     * @param query
     * @return
     */
    public EasyuiPageOutput listBasePageByExample(RegisterBillDto query) throws Exception {
        if (query.getPage() == null || query.getPage() < 0) {
            query.setPage(1);
        }
        if (query.getRows() == null || query.getRows() <= 0) {
            query.setRows(10);
        }
        PageHelper.startPage(query.getPage(), query.getRows());
        PageHelper.orderBy(query.getSort() + " " + query.getOrder());
        List<RegisterBillDto> list = this.getActualDao().queryListByExample(query);
        Page<RegisterBillDto> page = (Page) list;

        EasyuiPageOutput out = new EasyuiPageOutput();
//        List results = ValueProviderUtils.buildDataByProvider(query, list);
        out.setRows(list);
        out.setTotal(page.getTotal());

        return out;
    }

    /**
     * ????????????
     *
     * @param registerBill
     */
    public void doUpdateImage(RegisterBill registerBill) {
        this.checkImageCertList(registerBill);
        this.imageCertService.insertImageCert(registerBill.getImageCertList(), registerBill.getId(), BillTypeEnum.E_COMMERCE_BILL);
    }


    /**
     * ??????????????????
     *
     * @param idList
     * @return
     */
    public BaseOutput doBatchAutoCheck(List<Long> idList, OperatorUser operatorUser) {
        BatchResultDto<String> dto = new BatchResultDto<>();
        for (Long id : idList) {
            RegisterBill registerBillItem = this.billService.getAvaiableBill(id).orElse(null);
            if (registerBillItem == null) {
                continue;
            }
            try {
                this.autoCheckRegisterBill(registerBillItem, operatorUser);
                dto.getSuccessList().add(registerBillItem.getCode());
            } catch (Exception e) {
                dto.getFailureList().add(registerBillItem.getCode());
            }
        }

        return BaseOutput.success().setData(dto);

    }

    /**
     * ??????????????????
     *
     * @param id
     * @return
     */
    @Transactional
    public int autoCheckRegisterBill(Long id, OperatorUser operatorUser) {
        RegisterBill registerBillItem = this.billService.getAvaiableBill(id).orElseThrow(() -> {
            return new TraceBizException("???????????????????????????");
        });
        return autoCheckRegisterBill(registerBillItem, operatorUser);
    }





    /**
     * ????????????
     *
     * @param registerBillItem
     * @return
     */
    private int spotCheckRegisterBill(RegisterBill registerBillItem, OperatorUser operatorUser) {
        if (BillVerifyStatusEnum.NO_PASSED.equalsToCode(registerBillItem.getVerifyStatus()) || BillVerifyStatusEnum.DELETED.equalsToCode(registerBillItem.getVerifyStatus())) {
            throw new TraceBizException("?????????????????????????????????");
        }
        if (DetectStatusEnum.WAIT_SAMPLE.equalsToCode(registerBillItem.getDetectStatus())) {
            RegisterBill updatableBill = new RegisterBill();
            updatableBill.setId(registerBillItem.getId());

            updatableBill.setOperatorName(operatorUser.getName());
            updatableBill.setOperatorId(operatorUser.getId());
            updatableBill.setDetectStatus(DetectStatusEnum.WAIT_DETECT.getCode());

            this.spotCheckDetectRequest(registerBillItem.getDetectRequestId());
            return this.updateRegisterBillAsWaitCheck(updatableBill);
        } else {
            throw new TraceBizException("????????????????????????????????????");
        }
    }
    /**
     * ??????????????????
     *
     * @param idList
     * @return
     */
    public BaseOutput doBatchSamplingCheck(List<Long> idList, OperatorUser operatorUser) {
        BatchResultDto<String> dto = new BatchResultDto<>();
        for (Long id : idList) {
            RegisterBill registerBill = this.billService.getAvaiableBill(id).orElse(null);
            if (registerBill == null) {
                continue;
            }
            try {
                this.samplingCheckRegisterBill(registerBill, operatorUser);
                dto.getSuccessList().add(registerBill.getCode());
            } catch (Exception e) {
                dto.getFailureList().add(registerBill.getCode());
            }
        }
        return BaseOutput.success().setData(dto);

    }


    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @Transactional
    public int spotCheckRegisterBill(Long id, OperatorUser operatorUser) {
        RegisterBill registerBill = this.billService.getAvaiableBill(id).orElseThrow(() -> {
            return new TraceBizException("???????????????????????????");
        });
        return spotCheckRegisterBill(registerBill, operatorUser);
    }

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    private int spotCheckDetectRequest(Long id) {
        DetectRequest detectRequest = this.detectRequestService.get(id);
        detectRequest.setDetectSource(SampleSourceEnum.SPOT_CHECK.getCode());
        return this.detectRequestService.updateSelective(detectRequest);
    }

    /**
     * ??????????????????
     *
     * @param id
     * @return
     */
    @Transactional
    public int samplingCheckRegisterBill(Long id, OperatorUser operatorUser) {
        RegisterBill registerBill = this.billService.getAvaiableBill(id).orElseThrow(() -> {
            return new TraceBizException("???????????????????????????");
        });
        return samplingCheckRegisterBill(registerBill, operatorUser);
    }

    /**
     * ??????
     * @param registerBillItem
     * @param operatorUser
     * @return
     */
    private int samplingCheckRegisterBill(RegisterBill registerBillItem, OperatorUser operatorUser) {
        if (BillVerifyStatusEnum.NO_PASSED.equalsToCode(registerBillItem.getVerifyStatus()) || BillVerifyStatusEnum.DELETED.equalsToCode(registerBillItem.getVerifyStatus())) {
            throw new TraceBizException("?????????????????????????????????");
        }
        if (DetectStatusEnum.WAIT_SAMPLE.equalsToCode(registerBillItem.getDetectStatus())) {
            RegisterBill updatableBill = new RegisterBill();
            updatableBill.setId(registerBillItem.getId());
            updatableBill.setOperatorName(operatorUser.getName());
            updatableBill.setOperatorId(operatorUser.getId());
//            registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
            updatableBill.setDetectStatus(DetectStatusEnum.WAIT_DETECT.getCode());

            BillTypeEnum billTypeEnum = BillTypeEnum.fromCode(registerBillItem.getBillType()).orElse(null);
            updatableBill.setSampleCode(this.codeGenerateService.nextSampleCode(billTypeEnum));

//            if (BillTypeEnum.REGISTER_BILL.equalsToCode(registerBillItem.getBillType())) {
//                updatableBill.setSampleCode(this.codeGenerateService.nextRegisterBillSampleCode());
//            } else if (BillTypeEnum.COMMISSION_BILL.equalsToCode(registerBillItem.getBillType())) {
//                updatableBill.setSampleCode(this.codeGenerateService.nextCommissionBillSampleCode());
//            }

            this.samplingCheckDetectRequest(registerBillItem.getDetectRequestId());
            return this.updateRegisterBillAsWaitCheck(updatableBill);
        } else {
            throw new TraceBizException("????????????????????????????????????");
        }
    }
    /**
     * ?????????????????????????????????
     *
     * @param id
     * @param deleteType
     * @return
     */
    public BaseOutput doRemoveReportAndCertifiy(Long id, String deleteType) {
        RegisterBill item = this.billService.getAvaiableBill(id).orElseThrow(() -> {
            return new TraceBizException("???????????????????????????");
        });
        if (!BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(item.getVerifyStatus())) {
            throw new TraceBizException("????????????,???????????????????????????????????????");
        }
        // ??????????????????
        List<ImageCert> imageCertList = this.imageCertService.findImageCertListByBillId(item.getBillId(),BillTypeEnum.REGISTER_BILL);

        if ("all".equalsIgnoreCase(deleteType)) {
            imageCertList = StreamEx.of(imageCertList).filter(imageCert -> {
                return !ImageCertTypeEnum.ORIGIN_CERTIFIY.equalsToCode(imageCert.getCertType())
                        && !ImageCertTypeEnum.DETECT_REPORT.equalsToCode(imageCert.getCertType());
            }).toList();
        } else if ("originCertifiy".equalsIgnoreCase(deleteType)) {
            imageCertList = StreamEx.of(imageCertList).filter(imageCert -> {
                return !ImageCertTypeEnum.ORIGIN_CERTIFIY.equalsToCode(imageCert.getCertType());
            }).toList();
        } else if ("detectReport".equalsIgnoreCase(deleteType)) {
            imageCertList = StreamEx.of(imageCertList).filter(imageCert -> {
                return !ImageCertTypeEnum.DETECT_REPORT.equalsToCode(imageCert.getCertType());
            }).toList();
        } else {
            // do nothing
            return BaseOutput.success();
        }

        // this.billMapper.doRemoveReportAndCertifiy(item);
        this.billService.updateHasImage(item.getBillId(), imageCertList, BillTypeEnum.REGISTER_BILL);

        return BaseOutput.success();
    }
    /**
     * ?????????
     * @param id
     * @return
     */
    private int samplingCheckDetectRequest(Long id) {
        DetectRequest detectRequest = this.detectRequestService.get(id);
        detectRequest.setDetectSource(SampleSourceEnum.SAMPLE_CHECK.getCode());
        // ??????????????????
        detectRequest.setSampleTime(new Date());
        return this.detectRequestService.updateSelective(detectRequest);
    }

    /**
     * ??????
     *
     * @param registerBillItem
     * @param operatorUser
     * @return
     */
    private int autoCheckRegisterBill(RegisterBill registerBillItem, OperatorUser operatorUser) {
        if (BillVerifyStatusEnum.NO_PASSED.equalsToCode(registerBillItem.getVerifyStatus()) || BillVerifyStatusEnum.DELETED.equalsToCode(registerBillItem.getVerifyStatus())) {
            throw new TraceBizException("?????????????????????????????????");
        }
        if (DetectStatusEnum.WAIT_SAMPLE.equalsToCode(registerBillItem.getDetectStatus())) {
            RegisterBill updatableBill = new RegisterBill();
            updatableBill.setId(registerBillItem.getId());

            updatableBill.setOperatorName(operatorUser.getName());
            updatableBill.setOperatorId(operatorUser.getId());
//            registerBill.setSampleSource(SampleSourceEnum.AUTO_CHECK.getCode().intValue());
            updatableBill.setDetectStatus(DetectStatusEnum.WAIT_DETECT.getCode());

            BillTypeEnum billTypeEnum = BillTypeEnum.fromCode(registerBillItem.getBillType()).orElse(null);
            updatableBill.setSampleCode(this.codeGenerateService.nextSampleCode(billTypeEnum));
//            if (BillTypeEnum.REGISTER_BILL.equalsToCode(registerBillItem.getBillType())) {
//                updatableBill.setSampleCode(this.codeGenerateService.nextRegisterBillSampleCode());
//            } else if (BillTypeEnum.COMMISSION_BILL.equalsToCode(registerBillItem.getBillType())) {
//                updatableBill.setSampleCode(this.codeGenerateService.nextCommissionBillSampleCode());
//            }
            // ???????????????????????????????????????AUTO_CHECK ???????????????
            this.autoCheckDetectRequest(registerBillItem.getDetectRequestId());
            return this.updateRegisterBillAsWaitCheck(updatableBill);

        } else {
            throw new TraceBizException("????????????????????????????????????");
        }
    }

    /**
     * ??????
     *
     * @param id
     * @return
     */
    private int autoCheckDetectRequest(Long id) {
        DetectRequest detectRequest = this.detectRequestService.get(id);
        detectRequest.setDetectSource(SampleSourceEnum.AUTO_CHECK.getCode());
        // ??????????????????
        detectRequest.setSampleTime(new Date());
        return this.detectRequestService.updateSelective(detectRequest);
    }

    /**
     * ???????????????
     *
     * @param id
     * @param operatorUser
     * @return
     */
    public int auditRegisterBill(Long id, BillVerifyStatusEnum verifyStatusEnum, OperatorUser operatorUser) {
        RegisterBill registerBill = this.billService.getAvaiableBill(id).orElseThrow(() -> {
            return new TraceBizException("???????????????????????????");
        });
        return auditRegisterBill(verifyStatusEnum, registerBill, operatorUser);
    }

    /**
     * ????????????
     *
     * @param batchAuditDto
     * @param operatorUser
     * @return
     */
    public BaseOutput doBatchAudit(BatchAuditDto batchAuditDto, OperatorUser operatorUser) {
        BillVerifyStatusEnum billVerifyStatusEnum = BillVerifyStatusEnum.fromCodeOrEx(batchAuditDto.getVerifyStatus());
        BatchResultDto<String> dto = new BatchResultDto<>();

        // id?????????RegisterBill,?????????????????????partition(true:????????????????????????????????????????????????,false:??????)
        Map<Boolean, List<RegisterBill>> partitionedMap = CollectionUtils
                .emptyIfNull(batchAuditDto.getRegisterBillIdList()).stream().filter(Objects::nonNull).map(id -> {
                    RegisterBill registerBill = this.billService.getAvaiableBill(id).orElse(null);
                    return registerBill;
                }).filter(Objects::nonNull).filter(registerBill -> {
                    if (Boolean.FALSE.equals(batchAuditDto.getPassWithOriginCertifiyUrl())) {
                        if (YesOrNoEnum.YES.getCode().equals(registerBill.getHasOriginCertifiy())
                                && YesOrNoEnum.YES.getCode().equals(registerBill.getHasDetectReport())) {
                            return false;
                        }
                    }
                    return true;
                }).collect(Collectors.partitioningBy((registerBill) -> {

                    if (Boolean.TRUE.equals(batchAuditDto.getPassWithOriginCertifiyUrl())) {
                        if (YesOrNoEnum.YES.getCode().equals(registerBill.getHasOriginCertifiy())
                                && YesOrNoEnum.YES.getCode().equals(registerBill.getHasDetectReport())) {
                            return true;
                        }
                    }
                    return false;
                }));

        // ????????????????????????????????????????????????
        CollectionUtils.emptyIfNull(partitionedMap.get(Boolean.TRUE)).forEach(registerBill -> {


            if (BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(registerBill.getVerifyStatus())) {
                registerBill.setOperatorName(operatorUser.getName());
                registerBill.setOperatorId(operatorUser.getId());
//                registerBill.setState(RegisterBillStateEnum.ALREADY_AUDIT.getCode());
                registerBill.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
                registerBill.setDetectStatus(DetectStatusEnum.NONE.getCode());
                this.billService.updateSelective(registerBill);
                dto.getSuccessList().add(registerBill.getCode());
            } else {
                dto.getFailureList().add(registerBill.getCode());
            }

        });
        // ???????????????
        CollectionUtils.emptyIfNull(partitionedMap.get(Boolean.FALSE)).forEach(registerBill -> {
            try {
                this.auditRegisterBill(billVerifyStatusEnum, registerBill, operatorUser);
                dto.getSuccessList().add(registerBill.getCode());
            } catch (Exception e) {
                dto.getFailureList().add(registerBill.getCode());
            }

        });

        return BaseOutput.success().setData(dto);
    }

    /**
     * ??????
     *
     * @param verifyStatusEnum
     * @param registerBill
     * @param operatorUser
     * @return
     */
    private int auditRegisterBill(BillVerifyStatusEnum verifyStatusEnum, RegisterBill registerBill, OperatorUser operatorUser) {
        if (BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(registerBill.getVerifyStatus())) {
            registerBill.setOperatorName(operatorUser.getName());
            registerBill.setOperatorId(operatorUser.getId());
            if (BillVerifyStatusEnum.PASSED == verifyStatusEnum) {
                // ?????????
                if (RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())
                        && YesOrNoEnum.YES.getCode().equals(registerBill.getHasDetectReport())) {
                    // ?????????????????????????????????
                    // registerBill.setLatestDetectTime(new Date());
//                    registerBill.setState(RegisterBillStateEnum.ALREADY_AUDIT.getCode());
                    registerBill.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
//                    registerBill.setDetectStatus(DetectStatusEnum.FINISH_DETECT.getCode());
                }
                if (!BillVerifyStatusEnum.PASSED.getCode().equals(registerBill.getVerifyStatus())) {
                    // registerBill.setSampleCode(this.codeGenerateService.nextRegisterBillSampleCode());
                    registerBill.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
//                    registerBill.setDetectStatus(DetectStatusEnum.WAIT_SAMPLE.getCode());
                }

            } else {
                registerBill.setVerifyStatus(verifyStatusEnum.getCode());
            }

            int v = this.billService.update(registerBill);
            this.userQrHistoryService.createUserQrHistoryForVerifyBill(registerBill.getBillId());
            return v;
        } else {
            throw new TraceBizException("????????????????????????????????????");
        }
    }

    /**
     * ??????
     *
     * @param updatableBill
     * @return
     */
    private int updateRegisterBillAsWaitCheck(RegisterBill updatableBill) {

//        registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
        updatableBill.setModified(new Date());
        return this.billService.updateSelective(updatableBill);
//        return this.billService.update(updatableBill);
    }

    /**
     * ??????????????????????????????
     *
     * @param registerBill
     */
    private void checkImageCertList(RegisterBill registerBill) {

        List<ImageCert> imageCertList = StreamEx.ofNullable(registerBill.getImageCertList()).nonNull().flatCollection(Function.identity()).nonNull().toList();
        if (imageCertList.size() > 10) {
            throw new TraceBizException("????????????????????????10???");
        }
        registerBill.setImageCertList(imageCertList);
    }
}