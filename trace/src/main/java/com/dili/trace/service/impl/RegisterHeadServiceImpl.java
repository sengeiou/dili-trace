package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.input.CreateRegisterHeadInputDto;
import com.dili.trace.dao.RegisterHeadMapper;
import com.dili.trace.domain.Customer;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterHead;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterHeadDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.MeasureTypeEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.*;
import com.dili.trace.util.NumUtils;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * 进门主台账单接口实现
 *
 * @author Lily
 */
@Service
@Transactional
public class RegisterHeadServiceImpl extends BaseServiceImpl<RegisterHead, Long> implements RegisterHeadService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterHeadServiceImpl.class);

    @Autowired
    com.dili.trace.rpc.service.UidRestfulRpcService uidRestfulRpcService ;

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


    public RegisterHeadMapper getActualDao() {
        return (RegisterHeadMapper) getDao();
    }

    @Override
    public List<Long> createRegisterHeadList(List<CreateRegisterHeadInputDto> registerHeads,
                                             Optional<OperatorUser> operatorUser,Long marketId) {

        return StreamEx.of(registerHeads).nonNull().map(dto -> {
            logger.info("循环保存进门主台账单:" + JSON.toJSONString(dto));
            CustomerExtendDto customer=this.clientRpcService.findApprovedCustomerByIdOrEx(dto.getUserId(),marketId);
            RegisterHead registerHead = dto.build(customer);

            Customer cq=new Customer();
            cq.setCustomerId(customer.getCode());
            this.clientRpcService.findCustomer(cq,marketId).ifPresent(card->{
                registerHead.setThirdPartyCode(card.getPrintingCard());
            });

            registerHead.setMarketId(marketId);
            return this.createRegisterHead(registerHead, dto.getImageCertList(), operatorUser);
        }).toList();
    }

    @Override
    public String listPage(RegisterHeadDto input) throws Exception {
        return this.listEasyuiPageByExample(input, true).toString();
    }

    @Transactional
    @Override
    public Long createRegisterHead(RegisterHead registerHead, List<ImageCert> imageCertList,
                                   Optional<OperatorUser> operatorUser) {
        this.checkRegisterHead(registerHead);

        registerHead.setRemainWeight(registerHead.getWeight());
        registerHead.setCode(uidRestfulRpcService.bizNumber(BizNumberType.REGISTER_HEAD.getType()));
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
        String plate = StreamEx.ofNullable(registerHead.getPlate()).nonNull().map(StringUtils::trimToNull).nonNull()
                .map(String::toUpperCase).findFirst().orElse(null);
        registerHead.setPlate(plate);
        registerHead.setCreated(new Date());
        registerHead.setModified(new Date());
        // 保存车牌
        this.userPlateService.checkAndInsertUserPlate(registerHead.getUserId(), plate);

        // 保存报备单
        int result = super.saveOrUpdate(registerHead);
        if (result == 0) {
            logger.error("新增进门主台账单数据库执行失败" + JSON.toJSONString(registerHead));
            throw new TraceBizException("创建失败");
        }

        // 保存图片
        imageCertList = StreamEx.ofNullable(imageCertList).nonNull().flatCollection(Function.identity()).nonNull().toList();
//        if (imageCertList.isEmpty()) {
//            throw new TraceBusinessException("请上传凭证");
//        }
        imageCertService.insertImageCert(imageCertList, registerHead.getId(), BillTypeEnum.MASTER_BILL.getCode());

        // 创建/更新品牌信息并更新brandId字段值
        this.brandService.createOrUpdateBrand(registerHead.getBrandName(), registerHead.getUserId(), registerHead.getMarketId())
                .ifPresent(brandId -> {
                    RegisterHead bill = new RegisterHead();
                    bill.setBrandId(brandId);
                    bill.setId(registerHead.getId());
                    this.updateSelective(bill);
                });
        return registerHead.getId();
    }

    /**
     * 检查用户输入参数
     *
     * @param registerHead
     * @return
     */
    private BaseOutput checkRegisterHead(RegisterHead registerHead) {
        if (!BillTypeEnum.fromCode(registerHead.getBillType()).isPresent()) {
            throw new TraceBizException("单据类型错误");
        }
        if (registerHead.getUpStreamId() == null) {
            throw new TraceBizException("上游企业不能为空");
        }
        if (StringUtils.isBlank(registerHead.getName())) {
            logger.error("业户姓名不能为空");
            throw new TraceBizException("业户姓名不能为空");
        }
        if (registerHead.getUserId() == null) {
            logger.error("业户ID不能为空");
            throw new TraceBizException("业户ID不能为空");
        }

        if (StringUtils.isBlank(registerHead.getProductName()) || registerHead.getProductId() == null) {
            logger.error("商品名称不能为空");
            throw new TraceBizException("商品名称不能为空");
        }
        if (StringUtils.isBlank(registerHead.getOriginName()) || registerHead.getOriginId() == null) {
            logger.error("商品产地不能为空");
            throw new TraceBizException("商品产地不能为空");
        }

        // 计件类型，件数件重校验
        if (registerHead.getPieceWeight() == null && MeasureTypeEnum.COUNT_UNIT.equalsCode(registerHead.getMeasureType())) {
            logger.error("商品件重不能为空");
            throw new TraceBizException("商品件重不能为空");
        }
        if (registerHead.getPieceNum() == null && MeasureTypeEnum.COUNT_UNIT.equalsCode(registerHead.getMeasureType())) {
            logger.error("商品件数不能为空");
            throw new TraceBizException("商品件数不能为空");
        }
        if (MeasureTypeEnum.COUNT_UNIT.equalsCode(registerHead.getMeasureType()) &&
                BigDecimal.ZERO.compareTo(registerHead.getPieceWeight()) >= 0) {
            logger.error("商品件重不能小于0");
            throw new TraceBizException("商品件重不能小于0");
        }
        if (MeasureTypeEnum.COUNT_UNIT.equalsCode(registerHead.getMeasureType()) &&
                NumUtils.MAX_WEIGHT.compareTo(registerHead.getPieceWeight()) < 0) {
            logger.error("商品件重不能大于" + NumUtils.MAX_WEIGHT.toString());
            throw new TraceBizException("商品件重不能大于" + NumUtils.MAX_WEIGHT.toString());
        }

        // 商品重量校验
        if (registerHead.getWeight() == null) {
            logger.error("商品重量不能为空");
            throw new TraceBizException("商品重量不能为空");
        }
        if (BigDecimal.ZERO.compareTo(registerHead.getWeight()) >= 0) {
            logger.error("商品重量不能小于0");
            throw new TraceBizException("商品重量不能小于0");
        }
        if (NumUtils.MAX_WEIGHT.compareTo(registerHead.getWeight()) < 0) {
            logger.error("商品重量不能大于" + NumUtils.MAX_WEIGHT.toString());
            throw new TraceBizException("商品重量不能大于" + NumUtils.MAX_WEIGHT.toString());
        }

//        if (NumUtils.isIntegerValue(registerBill.getWeight())) {
//            logger.error("商品重量必须为整数");
//            throw new TraceBizException("商品重量必须为整数");
//        }


        // 商品单价校验（如果有）
        if (Objects.nonNull(registerHead.getUnitPrice())
                && NumUtils.MAX_UNIT_PRICE.compareTo(registerHead.getUnitPrice()) < 0) {
            logger.error("商品单价不能大于" + NumUtils.MAX_UNIT_PRICE.toString());
            throw new TraceBizException("商品单价不能大于" + NumUtils.MAX_UNIT_PRICE.toString());
        }

        if (registerHead.getWeightUnit() == null) {
            logger.error("重量单位不能为空");
            throw new TraceBizException("重量单位不能为空");
        }
         return BaseOutput.success();
    }

    @Transactional
    @Override
    public Long doEdit(RegisterHead input, List<ImageCert> imageCertList, Optional<OperatorUser> operatorUser) {
        if (input == null || input.getId() == null) {
            throw new TraceBizException("参数错误");
        }
        RegisterHead headItem = this.getAndCheckById(input.getId())
                .orElseThrow(() -> new TraceBizException("数据不存在"));
        // 车牌转大写
        String plate = StreamEx.ofNullable(input.getPlate()).filter(StringUtils::isNotBlank).map(p -> p.toUpperCase())
                .findFirst().orElse(null);
        input.setPlate(plate);
        // 保存车牌
        this.userPlateService.checkAndInsertUserPlate(input.getUserId(), plate);

        input.setReason("");
        operatorUser.ifPresent(op -> {
            input.setModifyUser(op.getName());
            input.setModified(new Date());
        });
        input.setRemainWeight(input.getWeight());

        this.updateSelective(input);

        imageCertList = StreamEx.ofNullable(imageCertList).nonNull().flatCollection(Function.identity()).nonNull().toList();
//        if (imageCertList.isEmpty()) {
//            throw new TraceBusinessException("请上传凭证");
//        }
        // 保存图片
        imageCertService.insertImageCert(imageCertList, input.getId(), BillTypeEnum.MASTER_BILL.getCode());

        this.brandService.createOrUpdateBrand(input.getBrandName(), headItem.getUserId(), input.getMarketId());
        return input.getId();
    }

    @Transactional
    @Override
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

    @Transactional
    @Override
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

    @Transactional
    @Override
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

    @Override
    public BasePage<RegisterHead> listPageApi(RegisterHeadDto input){

        StringBuilder sql = new StringBuilder();
        buildLikeKeyword(input).ifPresent(sql::append);

        // 校验剩余重量大于 0，登记单选择主台账时使用
        if (Objects.equals(input.getVerifyRemainWeight(), 1)) {
            if (StringUtils.isNotBlank(input.getKeyword())) {
                sql.append(" AND remain_weight > 0 ");
            } else {
                sql.append(" remain_weight > 0 ");
            }
        }

        if(sql.length() > 0){
            input.setMetadata(IDTO.AND_CONDITION_EXPR, sql.toString());
        }

        BasePage<RegisterHead> registerHeadBasePage = listPageByExample(input);
        if(null != registerHeadBasePage && CollectionUtils.isNotEmpty(registerHeadBasePage.getDatas())){
            registerHeadBasePage.getDatas().forEach(e ->{
                e.setWeightUnitName(WeightUnitEnum.fromCode(e.getWeightUnit()).get().getName());
            });
        }
        return registerHeadBasePage;
    }

    private Optional<String> buildLikeKeyword(RegisterHeadDto query) {
        String sql = null;
        if (StringUtils.isNotBlank(query.getKeyword())) {
            String keyword = query.getKeyword().trim();
            sql = "( product_name like '%" + keyword + "%'  OR name like '%"
                    + keyword + "%' OR id_card_no like '%" + keyword + "%' OR third_party_code like '%"+keyword+"%' )";
        }
        return Optional.ofNullable(sql);

    }
}