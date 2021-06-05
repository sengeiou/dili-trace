package com.dili.trace.service;


import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.input.CreateRegisterHeadInputDto;
import com.dili.trace.dao.RegisterHeadMapper;
import com.dili.trace.domain.TraceCustomer;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.RegisterHead;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterHeadDto;
import com.dili.trace.dto.ret.FieldConfigDetailRetDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.FieldConfigModuleTypeEnum;
import com.dili.trace.enums.MeasureTypeEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.rpc.dto.AccountGetListResultDto;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.util.MergeBeanUtils;
import com.dili.trace.util.NumUtils;
import com.dili.trace.util.RegUtils;
import de.cronn.reflection.util.PropertyUtils;
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

/**
 * 进门主台账单接口
 *
 * @author Lily
 */
@Service
@Transactional
public class RegisterHeadService extends TraceBaseService<RegisterHead, Long> {


    private static final Logger logger = LoggerFactory.getLogger(RegisterHeadService.class);

    @Autowired
    com.dili.trace.rpc.service.UidRestfulRpcService uidRestfulRpcService;
    @Autowired
    UserPlateService userPlateService;
    @Autowired
    ImageCertService imageCertService;
    @Autowired
    BrandService brandService;
    @Autowired
    UpStreamService upStreamService;
    @Autowired
    CustomerRpcService clientRpcService;
    @Autowired
    ExtCustomerService extCustomerService;
    @Autowired
    BillService billService;
    @Autowired
    SyncUserInfoService syncUserInfoService;
    @Autowired
    SyncCategoryService syncCategoryService;
    @Autowired
    RegisterTallyAreaNoService registerTallyAreaNoService;
    @Autowired
    RegisterHeadPlateService registerHeadPlateService;

    @Autowired
    FieldConfigDetailService fieldConfigDetailService;


    /**
     * 返回真实mapper
     *
     * @return
     */
    public RegisterHeadMapper getActualDao() {
        return (RegisterHeadMapper) getDao();
    }

    /**
     * 创建多个进门主台账单
     *
     * @param registerHeads
     * @param operatorUser
     * @param marketId
     * @return
     */
    public List<Long> createRegisterHeadList(List<CreateRegisterHeadInputDto> registerHeads,
                                             Optional<OperatorUser> operatorUser, Long marketId) {

        List<Long> customerIdList = StreamEx.of(registerHeads).nonNull().map(CreateRegisterHeadInputDto::getUserId).nonNull().toList();
        Map<Long, AccountGetListResultDto> cardCustomerMap = extCustomerService.findCardInfoByCustomerIdList(marketId, customerIdList);
        return StreamEx.of(registerHeads).nonNull().map(dto -> {
            logger.info("循环保存进门主台账单:" + super.toJSONString(dto));
            CustomerExtendDto customer = this.clientRpcService.findApprovedCustomerByIdOrEx(dto.getUserId(), marketId);
            RegisterHead registerHead = dto.build(customer);

            TraceCustomer cq = new TraceCustomer();
            cq.setCode(customer.getCode());
            registerHead.setCardNo(cardCustomerMap.getOrDefault(dto.getUserId(), new AccountGetListResultDto()).getCardNo());

            registerHead.setMarketId(marketId);
            return this.createRegisterHead(registerHead, dto.getImageCertList(), operatorUser);
        }).toList();
    }

    /**
     * 主台账列表查询
     *
     * @param input
     * @return
     * @throws Exception
     */
    public String listPage(RegisterHeadDto input) throws Exception {
        return this.listEasyuiPageByExample(input, true).toString();
    }

    /**
     * 创建单个进门主台账单
     *
     * @param registerHead
     * @param imageCertList
     * @param operatorUser
     * @return
     */
    @Transactional
    public Long createRegisterHead(RegisterHead registerHead, List<ImageCert> imageCertList,
                                   Optional<OperatorUser> operatorUser) {
        List<FieldConfigDetailRetDto> fieldConfigDetailRetDtoList = this.fieldConfigDetailService.findByMarketIdAndModuleType(registerHead.getMarketId(), FieldConfigModuleTypeEnum.REGISTER);
        Map<String, FieldConfigDetailRetDto> fieldConfigDetailRetDtoMap = StreamEx.of(fieldConfigDetailRetDtoList).nonNull().toMap(item -> item.getDefaultFieldDetail().getFieldName(), Function.identity());
        return this.createRegisterHead(registerHead, imageCertList, fieldConfigDetailRetDtoMap, operatorUser);

    }

    /**
     * 创建台账
     *
     * @param registerHead
     * @param imageCertList
     * @param fieldConfigDetailRetDtoMap
     * @param operatorUser
     * @return
     */
    private Long createRegisterHead(RegisterHead registerHead, List<ImageCert> imageCertList, Map<String, FieldConfigDetailRetDto> fieldConfigDetailRetDtoMap,
                                    Optional<OperatorUser> operatorUser) {
        this.checkRegisterHead(registerHead, fieldConfigDetailRetDtoMap);

        registerHead.setRemainWeight(registerHead.getWeight());
        registerHead.setCode(uidRestfulRpcService.bizNumber(BizNumberType.REGISTER_HEAD));
        operatorUser.ifPresent(op -> {
            registerHead.setCreateUser(op.getName());
            registerHead.setCreated(new Date());
            registerHead.setModifyUser(op.getName());
            registerHead.setModified(new Date());
        });
        registerHead.setIsDeleted(YesOrNoEnum.NO.getCode());
        registerHead.setActive(YesOrNoEnum.YES.getCode());
        registerHead.setVersion(1);

        registerHead.setIdCardNo(StringUtils.trimToEmpty(registerHead.getIdCardNo()).toUpperCase());
        // 车牌转大写
//        String plate = StreamEx.ofNullable(registerHead.getPlate()).nonNull().map(StringUtils::trimToNull).nonNull()
//                .map(String::toUpperCase).findFirst().orElse(null);
//        registerHead.setPlate(plate);
        registerHead.setCreated(new Date());
        registerHead.setModified(new Date());
//        // 保存车牌
//        this.userPlateService.checkAndInsertUserPlate(registerHead.getUserId(), plate);

        // 保存报备单
        int result = super.saveOrUpdate(registerHead);
        if (result == 0) {
            logger.error("新增进门主台账单数据库执行失败" + super.toJSONString(registerHead));
            throw new TraceBizException("创建失败");
        }

        // 保存图片
        imageCertList = StreamEx.ofNullable(imageCertList).nonNull().flatCollection(Function.identity()).nonNull().toList();
//        if (imageCertList.isEmpty()) {
//            throw new TraceBusinessException("请上传凭证");
//        }

        //更新报备单上图片标志位
        this.imageCertService.insertImageCert(imageCertList, registerHead.getId(), BillTypeEnum.MASTER_BILL);
        // 创建/更新品牌信息并更新brandId字段值
        this.brandService.createOrUpdateBrand(registerHead.getBrandName(), registerHead.getUserId(), registerHead.getMarketId())
                .ifPresent(brandId -> {
                    RegisterHead bill = new RegisterHead();
                    bill.setBrandId(brandId);
                    bill.setId(registerHead.getId());
                    this.updateSelective(bill);
                });

        //同步uap商品、经营户
        this.syncCategoryService.saveAndSyncGoodInfo(registerHead.getProductId(), registerHead.getMarketId());
        this.syncUserInfoService.saveAndSyncUserInfo(registerHead.getUserId(), registerHead.getMarketId());
        this.registerTallyAreaNoService.insertTallyAreaNoList(registerHead.getArrivalTallynos(), registerHead.getId(), BillTypeEnum.MASTER_BILL);
        this.registerHeadPlateService.deleteAndInsertPlateList(registerHead.getId(), registerHead.getPlateList());
        return registerHead.getId();
    }

    /**
     * 检查用户输入参数
     *
     * @param registerHead
     * @return
     */
    private BaseOutput checkRegisterHead(RegisterHead registerHead, Map<String, FieldConfigDetailRetDto> fieldConfigDetailRetDtoMap) {
        if (registerHead.getMarketId() == null) {
            throw new TraceBizException("市场不存在");
        }
        BigDecimal weight = registerHead.getWeight();

        if (weight == null) {
            logger.error("商品重量不能为空");
            throw new TraceBizException("商品重量不能为空");
        }

        if (!NumUtils.isIntegerValue(weight)) {
            logger.error("商品重量必须为整数");
            throw new TraceBizException("商品重量必须为整数");
        }

        if (BigDecimal.ZERO.compareTo(weight) >= 0) {
            logger.error("商品重量不能小于0");
            throw new TraceBizException("商品重量不能小于0");
        }

        if (NumUtils.MAX_WEIGHT.compareTo(weight) < 0) {
            logger.error("商品重量不能大于" + NumUtils.MAX_WEIGHT.toString());
            throw new TraceBizException("商品重量不能大于" + NumUtils.MAX_WEIGHT.toString());
        }

        if (registerHead.getWeightUnit() == null) {
            logger.error("重量单位不能为空");
            throw new TraceBizException("重量单位不能为空");
        }

        if (StringUtils.isBlank(registerHead.getProductName()) || registerHead.getProductId() == null) {
            logger.error("商品名称不能为空");
            throw new TraceBizException("商品名称不能为空");
        }

        //客户相关字段
        if (StringUtils.isBlank(registerHead.getName())) {
            logger.error("业户姓名不能为空");
            throw new TraceBizException("业户姓名不能为空");
        }
        if (registerHead.getUserId() == null) {
            logger.error("业户ID不能为空");
            throw new TraceBizException("业户ID不能为空");
        }


        //登记单类型字段
        registerHead.setBillType(BillTypeEnum.MASTER_BILL.getCode());
//        if (!BillTypeEnum.fromCode(registerHead.getBillType()).isPresent()) {
//            String propName = PropertyUtils.getPropertyDescriptor(registerHead, RegisterHead::getBillType).getName();
//            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
//            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
//                throw new TraceBizException("单据类型错误");
//            }
//            registerHead.setBillType(BillTypeEnum.MASTER_BILL.getCode());
//        }

        //产地字段
        if (StringUtils.isBlank(registerHead.getOriginName()) || registerHead.getOriginId() == null) {
            String propName = PropertyUtils.getPropertyDescriptor(registerHead, RegisterHead::getOriginId).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("商品产地不能为空");
            }
        }

        //备注字段
        if (StringUtils.isBlank(registerHead.getRemark())) {
            String propName = PropertyUtils.getPropertyDescriptor(registerHead, RegisterHead::getRemark).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("备注不能为空");
            }
        }
        String remark = registerHead.getRemark();
        if (StringUtils.isNotBlank(remark) && !RegUtils.isValidInput(remark)) {
            throw new TraceBizException("备注包含非法字符");
        }

        //皮重字段
        if (registerHead.getTruckTareWeight() == null) {
            String propName = PropertyUtils.getPropertyDescriptor(registerHead, RegisterHead::getTruckTareWeight).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("皮重不能为空");
            }
        }
        if (registerHead.getTruckTareWeight() != null) {
            if (NumUtils.MAX_WEIGHT.compareTo(registerHead.getTruckTareWeight()) < 0) {
                logger.error("车辆皮重不能大于" + NumUtils.MAX_WEIGHT.toString());
                throw new TraceBizException("车辆皮重不能大于" + NumUtils.MAX_WEIGHT.toString());
            }

            if (!NumUtils.isIntegerValue(registerHead.getTruckTareWeight())) {
                logger.error("车辆皮重必须为整数");
                throw new TraceBizException("车辆皮重必须为整数");
            }
        }

        //商品单价
        if (registerHead.getUnitPrice() == null) {
            String propName = PropertyUtils.getPropertyDescriptor(registerHead, RegisterHead::getUnitPrice).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("商品单价不能为空");
            }
        }

        //商品规格
        if (StringUtils.isBlank(registerHead.getSpecName())) {
            String propName = PropertyUtils.getPropertyDescriptor(registerHead, RegisterHead::getSpecName).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("商品规格不能为空");
            }
        }
        String specName = registerHead.getSpecName();
        if (StringUtils.isNotBlank(specName) && !RegUtils.isValidInput(specName)) {
            throw new TraceBizException("规格名称包含非法字符");
        }


        //品牌
        if (StringUtils.isBlank(registerHead.getBrandName())) {
            String propName = PropertyUtils.getPropertyDescriptor(registerHead, RegisterHead::getBrandName).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("品牌不能为空");
            }
        }


        //上游企业
        if (registerHead.getUpStreamId() == null) {
            String propName = PropertyUtils.getPropertyDescriptor(registerHead, RegisterHead::getUpStreamId).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("上游企业不能为空");
            }
        }

        //预计到场时间
        if (registerHead.getArrivalDatetime() == null) {
            String propName = PropertyUtils.getPropertyDescriptor(registerHead, RegisterHead::getArrivalDatetime).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("预计到场时间不能为空");
            }
        }

        //到货摊位
        List<String> arrivalTallynos = StreamEx.ofNullable(registerHead.getArrivalTallynos()).flatCollection(Function.identity()).map(StringUtils::trimToNull).nonNull().toList();
        if (arrivalTallynos.isEmpty()) {
            String propName = PropertyUtils.getPropertyDescriptor(registerHead, RegisterHead::getArrivalTallynos).getName();
            FieldConfigDetailRetDto retDto = fieldConfigDetailRetDtoMap.getOrDefault(propName, null);
            if (retDto != null && YesOrNoEnum.YES.getCode().equals(retDto.getDisplayed()) && YesOrNoEnum.YES.getCode().equals(retDto.getRequired())) {
                throw new TraceBizException("到货摊位不能为空");
            }
        }
        registerHead.setArrivalTallynos(arrivalTallynos);

        boolean hasValidTallyno = StreamEx.of(arrivalTallynos).anyMatch(no -> {
            return !RegUtils.isValidInput(no);
        });
        if (hasValidTallyno) {
            throw new TraceBizException("到货摊位包含非法字符");
        }

        // 计重类型，把件数和件重置空
        if (MeasureTypeEnum.COUNT_WEIGHT.equalsCode(registerHead.getMeasureType())) {
            registerHead.setPieceNum(null);
            registerHead.setPieceWeight(null);
        }

        // 计件类型，校验件数和件重
        if (MeasureTypeEnum.COUNT_UNIT.equalsCode(registerHead.getMeasureType())) {
            // 件数
            if (registerHead.getPieceNum() == null) {
                logger.error("商品件数不能为空");
                throw new TraceBizException("商品件数不能为空");
            }
            if (BigDecimal.ZERO.compareTo(registerHead.getPieceNum()) >= 0) {
                logger.error("商品件数不能小于0");
                throw new TraceBizException("商品件数不能小于0");
            }
            if (NumUtils.MAX_NUM.compareTo(registerHead.getPieceNum()) < 0) {
                logger.error("商品件数不能大于{}", NumUtils.MAX_NUM.toString());
                throw new TraceBizException("商品件数不能大于" + NumUtils.MAX_NUM.toString());
            }
            if (!NumUtils.isIntegerValue(registerHead.getPieceNum())) {
                logger.error("商品件数必须为整数");
                throw new TraceBizException("商品件数必须为整数");
            }

            // 件重
            if (registerHead.getPieceWeight() == null) {
                logger.error("商品件重不能为空");
                throw new TraceBizException("商品件重不能为空");
            }
            if (BigDecimal.ZERO.compareTo(registerHead.getPieceWeight()) >= 0) {
                logger.error("商品件重不能小于0");
                throw new TraceBizException("商品件重不能小于0");
            }
            if (NumUtils.MAX_WEIGHT.compareTo(registerHead.getPieceWeight()) < 0) {
                logger.error("商品件重不能大于{}", NumUtils.MAX_WEIGHT.toString());
                throw new TraceBizException("商品件重不能大于" + NumUtils.MAX_WEIGHT.toString());
            }
            if (!NumUtils.isIntegerValue(registerHead.getPieceWeight())) {
                logger.error("商品件重必须为整数");
                throw new TraceBizException("商品件重必须为整数");
            }
        }


        // 车牌转大写
        List<String> plateList = registerHead.getPlateList();
        boolean hasValidPlate = StreamEx.of(plateList).anyMatch(plate -> !RegUtils.isPlate(plate));
        if (hasValidPlate) {
            throw new TraceBizException("车牌格式错误");
        }


        // 待进门重量校验（如果有）
        if (Objects.nonNull(registerHead.getRemainWeight()) && BigDecimal.ZERO.compareTo(registerHead.getRemainWeight()) >= 0) {
            logger.error("待进门重量不能小于0");
            throw new TraceBizException("待进门重量不能小于0");
        }
        if (Objects.nonNull(registerHead.getRemainWeight()) && NumUtils.MAX_WEIGHT.compareTo(registerHead.getRemainWeight()) < 0) {
            logger.error("待进门重量不能大于{}", NumUtils.MAX_WEIGHT.toString());
            throw new TraceBizException("待进门重量不能大于" + NumUtils.MAX_WEIGHT.toString());
        }
        if (Objects.nonNull(registerHead.getRemainWeight()) && !NumUtils.isIntegerValue(registerHead.getRemainWeight())) {
            logger.error("待进门重量必须为整数");
            throw new TraceBizException("待进门重量必须为整数");
        }

        // 商品单价校验（如果有）
        if (Objects.nonNull(registerHead.getUnitPrice())
                && NumUtils.MAX_UNIT_PRICE.compareTo(registerHead.getUnitPrice()) < 0) {
            logger.error("商品单价不能大于{}", NumUtils.MAX_UNIT_PRICE.toString());
            throw new TraceBizException("商品单价不能大于" + NumUtils.MAX_UNIT_PRICE.toString());
        }

        WeightUnitEnum.fromCode(registerHead.getWeightUnit()).orElseThrow(() -> {
            logger.error("重量单位错误");
            return new TraceBizException("重量单位错误");
        });
//        if (StringUtils.isNotBlank(registerHead.getPlate())) {
//            if (!RegUtils.isPlate(registerHead.getPlate().trim())) {
//                throw new TraceBizException("车牌格式错误");
//            }
//        }

        return BaseOutput.success();
    }

    /**
     * 修改单个进门主台账单
     *
     * @param input
     * @param imageCertList
     * @param operatorUser
     * @return
     */
    @Transactional
    public Long doEdit(RegisterHead input, List<ImageCert> imageCertList, Optional<OperatorUser> operatorUser) {
        if (input == null || input.getId() == null) {
            throw new TraceBizException("参数错误");
        }
//        String specName=input.getSpecName();
//        if(StringUtils.isNotBlank(specName)&&!RegUtils.isValidInput(specName)) {
//            throw new TraceBizException("规格名称包含非法字符");
//        }
//        String remark=input.getRemark();
//        if(StringUtils.isNotBlank(remark)&&!RegUtils.isValidInput(remark)) {
//            throw new TraceBizException("备注包含非法字符");
//        }
        List<FieldConfigDetailRetDto> fieldConfigDetailRetDtoList = this.fieldConfigDetailService.findByMarketIdAndModuleType(input.getMarketId(), FieldConfigModuleTypeEnum.REGISTER);
        Map<String, FieldConfigDetailRetDto> fieldConfigDetailRetDtoMap = StreamEx.of(fieldConfigDetailRetDtoList).nonNull().toMap(item -> item.getDefaultFieldDetail().getFieldName(), Function.identity());
        this.checkRegisterHead(input, fieldConfigDetailRetDtoMap);

        RegisterHead headItem = this.getAndCheckById(input.getId())
                .orElseThrow(() -> new TraceBizException("数据不存在"));
        RegisterBill rbq = new RegisterBill();
        rbq.setRegisterHeadCode(headItem.getCode());
        rbq.setIsDeleted(YesOrNoEnum.NO.getCode());
        boolean hasBill = this.billService.listByExample(rbq).size() > 0;
        if (hasBill) {
            throw new TraceBizException("已有相关报备单，不能修改");
        }
        List<String> plateList = StreamEx.ofNullable(input.getPlateList()).flatCollection(Function.identity())
                .filter(StringUtils::isNotBlank).map(p -> p.toUpperCase()).toList();
        boolean hasValidPlate = StreamEx.of(plateList).anyMatch(plate -> !RegUtils.isPlate(plate));
        if (hasValidPlate) {
            throw new TraceBizException("车牌格式错误");
        }
//        // 车牌转大写
//        String plate = StreamEx.ofNullable(input.getPlate()).filter(StringUtils::isNotBlank).map(p -> p.toUpperCase())
//                .findFirst().orElse(null);
//        if (plate != null) {
//            if (!RegUtils.isPlate(plate)) {
//                throw new TraceBizException("车牌格式错误");
//            }
//        }
//        input.setPlate(plate);
        // 保存车牌
//        this.userPlateService.checkAndInsertUserPlate(input.getUserId(), plate);

        operatorUser.ifPresent(op -> {
            headItem.setModifyUser(op.getName());
            headItem.setModified(new Date());
        });
        MergeBeanUtils.merge(input, headItem, true);
        headItem.setRemainWeight(input.getWeight());
        headItem.setReason(null);
        headItem.setTruckTareWeight(input.getTruckTareWeight());
        headItem.setUpStreamName(input.getUpStreamName());
        headItem.setUpStreamId(input.getUpStreamId());
        headItem.setUnitPrice(input.getUnitPrice());
        headItem.setBrandId(input.getBrandId());
        headItem.setBrandName(input.getBrandName());
        headItem.setOriginId(input.getOriginId());
        headItem.setOriginName(input.getOriginName());
        headItem.setRemark(input.getRemark());
        headItem.setArrivalDatetime(input.getArrivalDatetime());
        headItem.setSpecName(input.getSpecName());

        this.update(headItem);

        imageCertList = StreamEx.ofNullable(imageCertList).nonNull().flatCollection(Function.identity()).nonNull().toList();
//        if (imageCertList.isEmpty()) {
//            throw new TraceBusinessException("请上传凭证");
//        }
        // 保存图片
        imageCertService.insertImageCert(imageCertList, headItem.getId(), BillTypeEnum.MASTER_BILL);

        this.brandService.createOrUpdateBrand(headItem.getBrandName(), headItem.getUserId(), headItem.getMarketId());
        this.registerTallyAreaNoService.insertTallyAreaNoList(input.getArrivalTallynos(), headItem.getId(), BillTypeEnum.MASTER_BILL);
        this.registerHeadPlateService.deleteAndInsertPlateList(headItem.getId(), plateList);
        return headItem.getId();
    }

    /**
     * 查询并检查报备单子是否删除
     *
     * @param id
     */
    @Transactional
    public Optional<RegisterHead> getAndCheckById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        RegisterHead headItem = this.get(id);
        if (headItem == null) {
            return Optional.empty();
        }
        if (YesOrNoEnum.YES.getCode().equals(headItem.getIsDeleted())) {
            throw new TraceBizException("进门主台账单已经被删除");
        }
        return Optional.of(headItem);
    }

    /**
     * 作废进门主台账单
     *
     * @param dto
     * @param userId
     * @param operatorUser
     * @return
     */
    @Transactional
    public Long doDelete(CreateRegisterHeadInputDto dto, Long userId, Optional<OperatorUser> operatorUser) {
        if (dto == null || userId == null) {
            throw new TraceBizException("参数错误");
        }
        RegisterHead headItem = this.getAndCheckById(dto.getId()).orElseThrow(() -> new TraceBizException("数据不存在"));
        RegisterHead registerHead = new RegisterHead();
        registerHead.setId(headItem.getId());
        registerHead.setIsDeleted(YesOrNoEnum.YES.getCode());
        operatorUser.ifPresent(op -> {
            registerHead.setDeleteUser(op.getName());
            registerHead.setDeleteTime(new Date());
        });
        this.updateSelective(registerHead);
        return dto.getId();
    }


    /**
     * 启用/关闭进门主台账单
     *
     * @param dto
     * @param userId
     * @param operatorUser
     * @return
     */
    @Transactional
    public Long doUpdateActive(CreateRegisterHeadInputDto dto, Long userId, Optional<OperatorUser> operatorUser) {
        if (dto.getId() == null || userId == null) {
            throw new TraceBizException("参数错误");
        }
        RegisterHead headItem = this.getAndCheckById(dto.getId()).orElseThrow(() -> new TraceBizException("数据不存在"));
        RegisterHead registerHead = new RegisterHead();
        registerHead.setId(headItem.getId());
        registerHead.setActive(dto.getActive());

        operatorUser.ifPresent(op -> {
            registerHead.setModifyUser(op.getName());
            registerHead.setModified(new Date());
        });
        this.updateSelective(registerHead);
        return dto.getId();
    }

    /**
     * @return
     * @Author guzman.liu
     * @Description 给小程序提供查询 主台账的接口
     * @Date 2020/10/20 16:04
     */
    public BasePage<RegisterHead> listPageApi(RegisterHeadDto input) {

        StringBuilder sql = new StringBuilder();
        buildLikeKeyword(input).ifPresent(sql::append);


        if (sql.length() > 0) {
            input.setMetadata(IDTO.AND_CONDITION_EXPR, sql.toString());
        }

        BasePage<RegisterHead> registerHeadBasePage = listPageByExample(input);
        if (null != registerHeadBasePage && CollectionUtils.isNotEmpty(registerHeadBasePage.getDatas())) {
            registerHeadBasePage.getDatas().forEach(e -> {
                e.setWeightUnitName(WeightUnitEnum.toName(e.getWeightUnit()));
            });
        }
        return registerHeadBasePage;
    }

    /**
     * 根据code查询
     *
     * @param registerHeadCode
     * @return
     */
    public Optional<RegisterHead> findByCode(String registerHeadCode) {
        if (StringUtils.isBlank(registerHeadCode)) {
            return Optional.empty();
        }
        RegisterHead q = new RegisterHead();
        q.setCode(registerHeadCode);
        return StreamEx.of(this.listByExample(q)).findFirst();
    }

    /**
     * 创建Like sql
     *
     * @param query
     * @return
     */
    private Optional<String> buildLikeKeyword(RegisterHeadDto query) {
        String sql = null;
        if (StringUtils.isNotBlank(query.getKeyword())) {
            String keyword = query.getKeyword().trim();
            sql = "( product_name like '%" + keyword + "%'  OR name like '%"
                    + keyword + "%' OR id_card_no like '%" + keyword + "%' OR card_no like '%" + keyword + "%' OR phone like '%" + keyword + "%' )";
        }
        return Optional.ofNullable(sql);

    }

}