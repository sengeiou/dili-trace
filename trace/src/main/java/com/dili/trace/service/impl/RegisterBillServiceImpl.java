package com.dili.trace.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBusinessException;
import com.dili.common.service.BizNumberFunction;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.util.DateUtils;
import com.dili.trace.api.components.ManageSystemComponent;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.*;
import com.dili.trace.dto.*;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.*;
import com.dili.trace.service.*;
import com.diligrp.manage.sdk.domain.UserTicket;
import com.diligrp.manage.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Service
@Transactional
public class RegisterBillServiceImpl extends BaseServiceImpl<RegisterBill, Long> implements RegisterBillService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBillServiceImpl.class);
    @Autowired
    BizNumberFunction bizNumberFunction;
    @Autowired
    ImageCertService imageCertService;
    @Autowired
    UserPlateService userPlateService;
    @Autowired
    CodeGenerateService codeGenerateService;
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
    MessageService messageService;
    @Autowired
    ManageSystemComponent manageSystemComponent;
    @Autowired
    TradeRequestService tradeRequestService;


    public RegisterBillMapper getActualDao() {
        return (RegisterBillMapper) getDao();
    }

    @Transactional
    @Override
    public Optional<RegisterBill> selectByIdForUpdate(Long id) {
        return this.getActualDao().selectByIdForUpdate(id).map(billItem -> {
            if (TFEnum.TRUE.equalsCode(billItem.getIsDeleted())) {
                throw new TraceBusinessException("报备单已经被删除");
            }
            return billItem;
        });
    }

    @Transactional
    @Override
    public Optional<RegisterBill> getAndCheckById(Long billId) {
        if (billId == null) {
            return Optional.empty();
        }
        RegisterBill billItem = this.get(billId);
        if (billItem == null) {
            return Optional.empty();
        }
        if (TFEnum.TRUE.equalsCode(billItem.getIsDeleted())) {
            throw new TraceBusinessException("报备单已经被删除");
        }
        return Optional.of(billItem);
    }

    @Override
    public List<Long> createBillList(List<CreateRegisterBillInputDto> registerBills, User user,
                                     Optional<OperatorUser> operatorUser) {
        if (!ValidateStateEnum.PASSED.equalsToCode(user.getValidateState())) {
            throw new TraceBusinessException("用户未审核通过不能创建报备单");
        }

        return StreamEx.of(registerBills).nonNull().map(dto -> {
            logger.info("循环保存登记单:" + JSON.toJSONString(dto));
            RegisterBill registerBill = dto.build(user);
            return this.createRegisterBill(registerBill, dto.getImageCertList(), operatorUser);
        }).toList();
    }

    @Transactional
    @Override
    public Long createRegisterBill(RegisterBill registerBill, List<ImageCert> imageCertList,
                                   Optional<OperatorUser> operatorUser) {
        this.checkBill(registerBill);

        registerBill.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
        registerBill.setVerifyType(VerifyTypeEnum.NONE.getCode());
        registerBill.setState(RegisterBillStateEnum.NEW.getCode());
        registerBill.setCode(bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL));
        registerBill.setVersion(1);
        registerBill.setCreated(new Date());
        registerBill.setIsCheckin(YnEnum.NO.getCode());
        registerBill.setIsDeleted(TFEnum.FALSE.getCode());
        operatorUser.ifPresent(op -> {
            registerBill.setOperatorName(op.getName());
            registerBill.setOperatorId(op.getId());
            registerBill.setOperationTime(new Date());
        });
        registerBill.setIdCardNo(StringUtils.trimToEmpty(registerBill.getIdCardNo()).toUpperCase());
        // 车牌转大写
        String plate = StreamEx.ofNullable(registerBill.getPlate()).nonNull().map(StringUtils::trimToNull).nonNull()
                .map(String::toUpperCase).findFirst().orElse(null);
        registerBill.setPlate(plate);
        registerBill.setModified(new Date());
        registerBill.setOrderType(OrderTypeEnum.REGISTER_BILL.getCode());

        // 补单直接进门状态
        if (BillTypeEnum.SUPPLEMENT.equalsToCode(registerBill.getBillType())) {
            registerBill.setIsCheckin(YnEnum.YES.getCode());
        } else {
            registerBill.setIsCheckin(YnEnum.NO.getCode());
        }

        // 保存车牌
        this.userPlateService.checkAndInsertUserPlate(registerBill.getUserId(), plate);

        // 保存报备单
        int result = super.saveOrUpdate(registerBill);
        if (result == 0) {
            logger.error("新增登记单数据库执行失败" + JSON.toJSONString(registerBill));
            throw new TraceBusinessException("创建失败");
        }
        // 创建审核历史数据
        this.registerBillHistoryService.createHistory(registerBill.getBillId());
        // 保存图片
        imageCertList = StreamEx.ofNullable(imageCertList).nonNull().flatCollection(Function.identity()).nonNull().toList();
        if (imageCertList.isEmpty()) {
            throw new TraceBusinessException("请上传凭证");
        }
        imageCertService.insertImageCert(imageCertList, registerBill.getBillId());

        // 创建/更新品牌信息并更新brandId字段值
        this.brandService.createOrUpdateBrand(registerBill.getBrandName(), registerBill.getUserId())
                .ifPresent(brandId -> {
                    RegisterBill bill = new RegisterBill();
                    bill.setBrandId(brandId);
                    bill.setId(registerBill.getId());
                    this.updateSelective(bill);
                });
        this.updateUserQrStatusByUserId(registerBill.getBillId(), registerBill.getUserId());

        //报备单新增消息
        Integer businessType=MessageStateEnum.BUSINESS_TYPE_BILL.getCode();
        if(BillTypeEnum.SUPPLEMENT.getCode().equals(registerBill.getBillType())){
            businessType=MessageStateEnum.BUSINESS_TYPE_FIELD_BILL.getCode();
        }
        addMessage(registerBill, MessageTypeEnum.BILLSUBMIT.getCode(), businessType, MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_MANAGER.getCode());
        return registerBill.getId();
    }

    /**
     * 检查用户输入参数
     *
     * @param registerBill
     * @return
     */
    private BaseOutput checkBill(RegisterBill registerBill) {

        if (!BillTypeEnum.fromCode(registerBill.getBillType()).isPresent()) {
            throw new TraceBusinessException("单据类型错误");
        }
        if (!TruckTypeEnum.fromCode(registerBill.getTruckType()).isPresent()) {
            throw new TraceBusinessException("装车类型错误");
        }
        if (registerBill.getUpStreamId() == null) {
            throw new TraceBusinessException("上游企业不能为空");
        }
        if (TruckTypeEnum.POOL.equalsToCode(registerBill.getTruckType())) {
            if (StringUtils.isBlank(registerBill.getPlate())) {
                throw new TraceBusinessException("车牌不能为空");
            }
        }
        if (!PreserveTypeEnum.fromCode(registerBill.getPreserveType()).isPresent()) {
            throw new TraceBusinessException("商品类型错误");
        }
        if (StringUtils.isBlank(registerBill.getName())) {
            logger.error("业户姓名不能为空");
            throw new TraceBusinessException("业户姓名不能为空");
        }
        if (registerBill.getUserId() == null) {
            logger.error("业户ID不能为空");
            throw new TraceBusinessException("业户ID不能为空");
        }

        if (StringUtils.isBlank(registerBill.getProductName()) || registerBill.getProductId() == null) {
            logger.error("商品名称不能为空");
            throw new TraceBusinessException("商品名称不能为空");
        }
        if (StringUtils.isBlank(registerBill.getOriginName()) || registerBill.getOriginId() == null) {
            logger.error("商品产地不能为空");
            throw new TraceBusinessException("商品产地不能为空");
        }

        if (registerBill.getWeight() == null) {
            logger.error("商品重量不能为空");
            throw new TraceBusinessException("商品重量不能为空");
        }

        if (BigDecimal.ZERO.compareTo(registerBill.getWeight()) >= 0) {
            logger.error("商品重量不能小于0");
            throw new TraceBusinessException("商品重量不能小于0");
        }
        if (registerBill.getWeightUnit() == null) {
            logger.error("重量单位不能为空");
            throw new TraceBusinessException("重量单位不能为空");
        }
        return BaseOutput.success();
    }

    @Override
    public RegisterBill findByCode(String code) {
        RegisterBill registerBill = new RegisterBill();
        registerBill.setCode(code);
        List<RegisterBill> list = list(registerBill);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private UserTicket getOptUser() {
        return SessionContext.getSessionContext().getUserTicket();
    }

    @Transactional
    @Override
    public Long doEdit(RegisterBill input, List<ImageCert> imageCertList, Optional<OperatorUser> operatorUser) {
        if (input == null || input.getId() == null) {
            throw new TraceBusinessException("参数错误");
        }
        RegisterBill billItem = this.getAndCheckById(input.getId())
                .orElseThrow(() -> new TraceBusinessException("数据不存在"));

        if (BillVerifyStatusEnum.NONE.equalsToCode(billItem.getVerifyStatus())
                || BillVerifyStatusEnum.RETURNED.equalsToCode(billItem.getVerifyStatus())) {
            // 待审核，或者已退回状态可以进行数据修改
        } else {
            throw new TraceBusinessException("当前状态不能修改数据");
        }

        TradeDetail tradeDetailItem = this.tradeDetailService.findBilledTradeDetailByBillId(billItem.getBillId())
                .orElse(null);
        // 车牌转大写
        String plate = StreamEx.ofNullable(input.getPlate()).filter(StringUtils::isNotBlank).map(p -> p.toUpperCase())
                .findFirst().orElse(null);
        input.setPlate(plate);
        // 保存车牌
        this.userPlateService.checkAndInsertUserPlate(input.getUserId(), plate);
        input.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
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
            // 补单直接进门状态
            if (BillTypeEnum.SUPPLEMENT.equalsToCode(input.getBillType())) {
                input.setIsCheckin(YnEnum.YES.getCode());
            } else {
                input.setIsCheckin(YnEnum.NO.getCode());
            }
        } else if (CheckinStatusEnum.ALLOWED.equalsToCode(tradeDetailItem.getCheckinStatus())) {
            input.setIsCheckin(YnEnum.YES.getCode());
        } else {
            input.setIsCheckin(YnEnum.NO.getCode());
        }

        this.updateSelective(input);
        this.registerBillHistoryService.createHistory(billItem.getBillId());

        imageCertList = StreamEx.ofNullable(imageCertList).nonNull().flatCollection(Function.identity()).nonNull().toList();
        if (imageCertList.isEmpty()) {
            throw new TraceBusinessException("请上传凭证");
        }
        // 保存图片
        imageCertService.insertImageCert(imageCertList, input.getId());

        this.tradeDetailService.findBilledTradeDetailByBillId(billItem.getBillId()).ifPresent(td -> {
            TradeDetail updatableRecord = new TradeDetail();
            updatableRecord.setId(td.getId());
            updatableRecord.setModified(new Date());
            updatableRecord.setStockWeight(input.getWeight());
            updatableRecord.setTotalWeight(input.getWeight());
            this.tradeDetailService.updateSelective(updatableRecord);
        });

        this.brandService.createOrUpdateBrand(input.getBrandName(), billItem.getUserId());
        this.updateUserQrStatusByUserId(billItem.getBillId(), billItem.getUserId());
        return input.getId();
    }

    @Transactional
    @Override
    public Long doDelete(Long billId, Long userId, Optional<OperatorUser> operatorUser) {
        if (billId == null || userId == null) {
            throw new TraceBusinessException("参数错误");
        }
        RegisterBill billItem = this.getAndCheckById(billId).orElseThrow(() -> new TraceBusinessException("数据不存在"));
        if (!userId.equals(billItem.getUserId())) {
            throw new TraceBusinessException("没有权限删除数据");
        }
        if (YnEnum.YES.equalsToCode(billItem.getIsCheckin())) {
            throw new TraceBusinessException("不能删除已进门数据");
        }
        if (BillVerifyStatusEnum.NO_PASSED.equalsToCode(billItem.getVerifyStatus())) {
            throw new TraceBusinessException("不能删除审核未通过数据");
        }
        RegisterBill bill = new RegisterBill();
        bill.setId(billItem.getBillId());
        bill.setIsDeleted(TFEnum.TRUE.getCode());

        operatorUser.ifPresent(op -> {
            bill.setOperatorName(op.getName());
            bill.setOperatorId(op.getId());
            bill.setOperationTime(new Date());
            bill.setDeleteUser(op.getName());
            bill.setDeleteTime(new Date());
        });
        this.updateSelective(bill);
        this.registerBillHistoryService.createHistory(billItem.getBillId());
        this.userQrHistoryService.rollbackUserQrStatus(bill.getId(), billItem.getUserId());
        return billId;
    }

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

    @Override
    public RegisterBill findFirstWaitAuditRegisterBillCreateByCurrentUser(RegisterBillDto input) throws Exception {
        RegisterBillDto dto = new RegisterBillDto();
        UserTicket userTicket = getOptUser();
        dto.setOperatorId(userTicket.getId());
        dto.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
        dto.setRows(1);
        dto.setSort("code");
        dto.setOrder("desc");
        return this.listByExample(dto).stream().findFirst().orElse(new RegisterBill());
    }

    @Override
    public String listPage(RegisterBillDto input) throws Exception {
        RegisterBillDto dto = this.preBuildDTO(input);
        return this.listEasyuiPageByExample(dto, true).toString();
    }

    private StringBuilder buildDynamicCondition(RegisterBillDto registerBill) {
        StringBuilder sql = new StringBuilder();

        return sql;
    }

    @Transactional
    @Override
    public Long doVerifyBeforeCheckIn(RegisterBill input, Optional<OperatorUser> operatorUser) {
        if (input == null || input.getId() == null) {
            throw new TraceBusinessException("参数错误");
        }
        RegisterBill billItem = this.getAndCheckById(input.getId())
                .orElseThrow(() -> new TraceBusinessException("数据不存在"));

        if (YnEnum.YES.equalsToCode(billItem.getIsCheckin())
                || BillTypeEnum.SUPPLEMENT.equalsToCode(billItem.getBillType())) {
            throw new TraceBusinessException("补单或已进门报备单,只能场内审核");
        }

        this.doVerify(billItem, input.getVerifyStatus(), input.getReason(), operatorUser);
        //新增消息
        addMessage(billItem, MessageTypeEnum.BILLPASS.getCode(), MessageStateEnum.BUSINESS_TYPE_BILL.getCode(), MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_NORMAL.getCode());
        return billItem.getId();
    }

    @Transactional
    @Override
    public Long doVerifyAfterCheckIn(Long billId, Integer verifyStatus, String reason,
                                     Optional<OperatorUser> operatorUser) {
        if (billId == null || verifyStatus == null) {
            throw new TraceBusinessException("参数错误");
        }

        RegisterBill billItem = this.getAndCheckById(billId).orElseThrow(() -> new TraceBusinessException("数据不存在"));

        if (!YnEnum.YES.equalsToCode(billItem.getIsCheckin())
                && !BillTypeEnum.SUPPLEMENT.equalsToCode(billItem.getBillType())) {
            throw new TraceBusinessException("补单或已进门报备单,才能场内审核");
        }
        if (BillVerifyStatusEnum.PASSED.equalsToCode(verifyStatus)) {
            this.checkinOutRecordService.doCheckin(operatorUser, Lists.newArrayList(billItem.getBillId()),
                    CheckinStatusEnum.ALLOWED);
        }
        this.doVerify(billItem, verifyStatus, reason, operatorUser);
        //新增消息
        addMessage(billItem, MessageTypeEnum.BILLPASS.getCode(), MessageStateEnum.BUSINESS_TYPE_BILL.getCode(), MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_NORMAL.getCode());
        return billItem.getId();

    }


    private void addMessage(RegisterBill billItem, Integer messageType, Integer businessType, Integer receiverType) {

        Integer receiverNormal = 10;
        MessageInputDto messageInputDto = new MessageInputDto();
        messageInputDto.setSourceBusinessType(businessType);
        messageInputDto.setSourceBusinessId(billItem.getId());
        messageInputDto.setMessageType(messageType);
        messageInputDto.setCreatorId(billItem.getUserId());
        messageInputDto.setReceiverType(receiverType);
        messageInputDto.setEventMessageContentParam(new String[]{billItem.getName()});
        //管理员
        if (!receiverType.equals(receiverNormal)) {
            // 审核通过增加消息**已通过
            List<ManagerInfoDto> manageList = manageSystemComponent.findUserByUserResource("user/index.html#list");
            Set<Long> managerIdSet = new HashSet<>();
            StreamEx.of(manageList).nonNull().forEach(s -> {
                managerIdSet.add(s.getId());
            });
            Long[] managerId = managerIdSet.toArray(new Long[managerIdSet.size()]);
            messageInputDto.setReceiverIdArray(managerId);
        } else {
            messageInputDto.setReceiverIdArray(new Long[]{billItem.getUserId()});
        }
        //审核通过/不通过/退回
        //增加卖家短信
        Map<String, Object> smsMap = getSmsMap(billItem);
        messageInputDto.setSmsContentParam(smsMap);
        messageService.addMessage(messageInputDto);
    }

    private Map<String, Object> getSmsMap(RegisterBill billItem) {
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("userName", userService.get(billItem.getUserId()).getName());
        smsMap.put("created", DateUtils.format(new Date(), "yyyy年MM月dd日 HH:mm:ss"));
        smsMap.put("billNo", billItem.getCode());
        smsMap.put("productName", "商品:" + billItem.getProductName() + "    车号:" + billItem.getPlate());
        return smsMap;
    }

    private void doVerify(RegisterBill billItem, Integer verifyStatus, String reason,
                          Optional<OperatorUser> operatorUser) {
        BillVerifyStatusEnum fromVerifyState = BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus())
                .orElseThrow(() -> new TraceBusinessException("数据错误"));

        BillVerifyStatusEnum toVerifyState = BillVerifyStatusEnum.fromCode(verifyStatus)
                .orElseThrow(() -> new TraceBusinessException("参数错误"));

        logger.info("审核: billId: {} from {} to {}", billItem.getBillId(), fromVerifyState.getName(),
                toVerifyState.getName());
        if (!BillVerifyStatusEnum.NONE.equalsToCode(billItem.getVerifyStatus())) {
            throw new TraceBusinessException("当前状态不能进行数据操作");
        }
        if (BillVerifyStatusEnum.NONE == toVerifyState) {
            throw new TraceBusinessException("不支持的操作");
        }
        if (fromVerifyState == toVerifyState) {
            throw new TraceBusinessException("状态不能相同");
        }

        // 更新当前报务单数据
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
            if (YnEnum.YES.equalsToCode(billItem.getIsCheckin())) {
                bill.setVerifyType(VerifyTypeEnum.PASSED_AFTER_CHECKIN.getCode());
            } else {
                bill.setVerifyType(VerifyTypeEnum.PASSED_BEFORE_CHECKIN.getCode());
            }
        }
        bill.setModified(new Date());
        this.updateSelective(bill);
        // 创建审核历史数据
        this.registerBillHistoryService.createHistory(billItem.getId());

        // 创建相关的tradeDetail及batchStock数据
        this.tradeDetailService.findBilledTradeDetailByBillId(billItem.getBillId()).ifPresent(tradeDetailItem -> {
            this.tradeService.createBatchStockAfterVerifiedAndCheckin(billItem.getId(), tradeDetailItem.getId(),
                    operatorUser);
        });

        // 更新用户颜色码
        this.updateUserQrStatusByUserId(billItem.getBillId(), billItem.getUserId());

    }


    /**
     * 根据用户最新报备单审核状态更新颜色码
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
     * 根据报备单数量更新用户状态到黑码
     *
     * @param
     * @return
     */
    public void updateAllUserQrStatusByRegisterBillNum(Date createdStart, Date createdEnd) {
        UserListDto dto = DTOUtils.newDTO(UserListDto.class);
        dto.setQrStatus(UserQrStatusEnum.BLACK.getCode());
        dto.setCreatedStart(createdStart);
        dto.setCreatedEnd(createdEnd);
        List<Long> userIdList = this.getActualDao().selectUserIdWithouBill(dto);
        if(userIdList == null)
        {
            userIdList = new ArrayList<>();
        }
        List<Long> buyerIdList = tradeRequestService.selectBuyerIdWithouTradeRequest(dto);
        if(buyerIdList != null) {
            userIdList.retainAll(buyerIdList);
        }
        StreamEx.of(userIdList).nonNull().forEach(uid -> {
            this.userQrHistoryService.createUserQrHistoryForWithousBills(uid);
        });

        // RegisterBillDto bq = new RegisterBillDto();
        // bq.setCreatedStart(DateUtil.format(createdStart, "yyyy-MM-dd HH:mm:ss"));
        // bq.setCreatedEnd(DateUtil.format(createdEnd, "yyyy-MM-dd HH:mm:ss"));
        // bq.setMetadata(IDTO.AND_CONDITION_EXPR,
        // "user_id in(select id from `user` where qr_status=" +
        // UserQrStatusEnum.BLACK.getCode() + ")");
        // StreamEx.of(this.listByExample(bq)).map(RegisterBill::getUserId).distinct().map(uid
        // -> {
        // return this.userService.get(uid);
        // }).nonNull().forEach(userItem -> {
        // this.updateUserQrStatusByUserId(userItem.getId());
        // });
    }

    @Override
    public BasePage<RegisterBill> listPageBeforeCheckinVerifyBill(RegisterBillDto query) {
        query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLBeforeCheckIn(query));
        query.setIsDeleted(TFEnum.FALSE.getCode());
        return this.listPageByExample(query);
    }

    @Override
    public List<VerifyStatusCountOutputDto> countByVerifyStatuseBeforeCheckin(RegisterBillDto query) {
        if (query == null) {
            throw new TraceBusinessException("参数错误");
        }
        query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLBeforeCheckIn(query));
        query.setIsDeleted(TFEnum.FALSE.getCode());
        return this.countByVerifyStatus(query);
    }

    @Override
    public BasePage<RegisterBill> listPageAfterCheckinVerifyBill(RegisterBillDto query) {
        query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLAfterCheckIn(query));
        query.setIsDeleted(TFEnum.FALSE.getCode());
        return this.listPageByExample(query);
    }

    @Override
    public List<VerifyStatusCountOutputDto> countByVerifyStatuseAfterCheckin(RegisterBillDto query) {
        if (query == null) {
            throw new TraceBusinessException("参数错误");
        }
        query.setMetadata(IDTO.AND_CONDITION_EXPR, this.dynamicSQLAfterCheckIn(query));
        query.setIsDeleted(TFEnum.FALSE.getCode());
        return this.countByVerifyStatus(query);
    }

    @Override
    public Map<Integer, Map<String, List<RegisterBill>>> listPageCheckInData(RegisterBillDto query) {
        String dynaWhere = " is_checkin=" + YnEnum.NO.getCode() + " and bill_type =" + BillTypeEnum.NONE.getCode();

        query.setSort("created");
        query.setOrder("desc");
        query.setMetadata(IDTO.AND_CONDITION_EXPR, dynaWhere);
        query.setIsDeleted(TFEnum.FALSE.getCode());
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

    @Override
    public RegisterBillOutputDto viewTradeDetailBill(Long billId, Long tradeDetailId) {
        if (billId == null && tradeDetailId == null) {
            throw new TraceBusinessException("参数错误");
        }

        TradeDetail tradeDetailItem = StreamEx.ofNullable(tradeDetailId).nonNull().map(tdId -> {
            return this.tradeDetailService.get(tdId);
        }).findFirst().orElse(new TradeDetail());

        RegisterBill registerBill = StreamEx.ofNullable(billId).append(tradeDetailItem.getBillId()).nonNull()
                .map(bId -> {
                    return this.get(bId);
                }).findFirst().orElse(new RegisterBill());

        List<ImageCert> imageCertList = StreamEx.ofNullable(registerBill.getId()).nonNull().flatMap(bid -> {
            return imageCertService.findImageCertListByBillId(bid).stream();
        }).toList();

        String upStreamName = StreamEx.ofNullable(registerBill.getUpStreamId()).nonNull().map(upStreamId -> {
            return this.upStreamService.get(upStreamId);
        }).nonNull().findAny().map(UpStream::getName).orElse("");

        if (tradeDetailItem.getId() != null && registerBill.getId() != null) {
            RegisterBillOutputDto outputdto = RegisterBillOutputDto.build(registerBill, Lists.newArrayList());
            outputdto.setImageCertList(imageCertList);
            outputdto.setUpStreamName(upStreamName);
            outputdto.setWeight(tradeDetailItem.getTotalWeight());
            return outputdto;
        } else if (registerBill.getId() != null) {
            RegisterBillOutputDto outputdto = RegisterBillOutputDto.build(registerBill, Lists.newArrayList());
            outputdto.setUpStreamName(upStreamName);
            outputdto.setImageCertList(imageCertList);
            outputdto.setWeight(registerBill.getWeight());
            return outputdto;
        } else {
            throw new TraceBusinessException("没有数据");
        }

    }

    private Optional<String> buildLikeKeyword(RegisterBillDto query) {
        String sql = null;
        if (StringUtils.isNotBlank(query.getKeyword())) {
            String keyword = query.getKeyword().trim();
            sql = "( product_name like '%" + keyword + "%'  OR user_id in(select id from `user` u where u.name like '%"
                    + keyword + "%' OR legal_person like '%" + keyword + "%' OR upper(tally_area_nos) like '%"
                    + keyword.toUpperCase() + "%'))";
        }
        return Optional.ofNullable(sql);

    }

    private String dynamicSQLBeforeCheckIn(RegisterBillDto query) {
        List<String> sqlList = new ArrayList<>();
        this.buildLikeKeyword(query).ifPresent(sql -> {
            sqlList.add(sql);
        });
        sqlList.add("( bill_type=" + BillTypeEnum.NONE.getCode() + " and (is_checkin=" + YnEnum.NO.getCode()
                + " OR (is_checkin=" + YnEnum.YES.getCode() + " and verify_status="
                + BillVerifyStatusEnum.PASSED.getCode() + " and verify_type="
                + VerifyTypeEnum.PASSED_BEFORE_CHECKIN.getCode() + " ) ) )");

        return StreamEx.of(sqlList).joining(" AND ");
    }

    private String dynamicSQLAfterCheckIn(RegisterBillDto query) {
        List<String> sqlList = new ArrayList<>();
        this.buildLikeKeyword(query).ifPresent(sql -> {
            sqlList.add(sql);
        });
        sqlList.add("( bill_type=" + BillTypeEnum.SUPPLEMENT.getCode() + " OR  (is_checkin=" + YnEnum.YES.getCode()
                + " AND verify_status<>" + BillVerifyStatusEnum.PASSED.getCode() + ") OR(verify_status="
                + BillVerifyStatusEnum.PASSED.getCode() + " and verify_type="
                + VerifyTypeEnum.PASSED_AFTER_CHECKIN.getCode() + ") )");

        return StreamEx.of(sqlList).joining(" AND ");
    }

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

    @Override
    public List<Long> createRegisterFormBillList(List<CreateRegisterBillInputDto> registerBills, User user,
                                     Optional<OperatorUser> operatorUser, Long marketId) {
        return StreamEx.of(registerBills).nonNull().map(dto -> {
            logger.info("循环保存进门登记单:" + JSON.toJSONString(dto));
            RegisterBill registerBill = dto.build(user);
            registerBill.setMarketId(marketId);
            return this.createRegisterFormBill(registerBill, dto.getImageCertList(), operatorUser);
        }).toList();
    }

    @Transactional
    @Override
    public Long createRegisterFormBill(RegisterBill registerBill, List<ImageCert> imageCertList,
                                   Optional<OperatorUser> operatorUser) {
        this.checkBill(registerBill);

        registerBill.setVerifyType(VerifyTypeEnum.NONE.getCode());
        registerBill.setState(RegisterBillStateEnum.NEW.getCode());
        registerBill.setCode(bizNumberFunction.getBizNumberByType(BizNumberType.REGISTER_BILL));
        registerBill.setVersion(1);
        registerBill.setCreated(new Date());
        registerBill.setIsCheckin(YnEnum.NO.getCode());
        registerBill.setIsDeleted(TFEnum.FALSE.getCode());
        operatorUser.ifPresent(op -> {
            registerBill.setOperatorName(op.getName());
            registerBill.setOperatorId(op.getId());
            registerBill.setOperationTime(new Date());

            registerBill.setCreateUser(op.getName());
        });
        registerBill.setIdCardNo(StringUtils.trimToEmpty(registerBill.getIdCardNo()).toUpperCase());
        // 车牌转大写
        String plate = StreamEx.ofNullable(registerBill.getPlate()).nonNull().map(StringUtils::trimToNull).nonNull()
                .map(String::toUpperCase).findFirst().orElse(null);
        registerBill.setPlate(plate);
        registerBill.setModified(new Date());
        registerBill.setOrderType(OrderTypeEnum.REGISTER_FORM_BILL.getCode());

        // 查验状态为不通过，进门状态设置为未进门，其他设置为已进门
        if (BillVerifyStatusEnum.NO_PASSED.equalsToCode(registerBill.getVerifyStatus())) {
            registerBill.setIsCheckin(YnEnum.NO.getCode());
        } else {
            registerBill.setIsCheckin(YnEnum.YES.getCode());
        }
        if (BillVerifyStatusEnum.PASSED.equalsToCode(registerBill.getVerifyStatus())) {
            registerBill.setVerifyType(VerifyTypeEnum.PASSED_BEFORE_CHECKIN.getCode());
        }

        // 保存车牌
        this.userPlateService.checkAndInsertUserPlate(registerBill.getUserId(), plate);

        // 保存进门登记单
        int result = super.saveOrUpdate(registerBill);
        if (result == 0) {
            logger.error("新增登记单数据库执行失败" + JSON.toJSONString(registerBill));
            throw new TraceBusinessException("创建失败");
        }
        // 创建审核历史数据
        this.registerBillHistoryService.createHistory(registerBill.getBillId());
        // 保存图片
        imageCertList = StreamEx.ofNullable(imageCertList).nonNull().flatCollection(Function.identity()).nonNull().toList();
        if (imageCertList.isEmpty()) {
            throw new TraceBusinessException("请上传凭证");
        }
        imageCertService.insertImageCert(imageCertList, registerBill.getBillId(), BillTypeEnum.REGISTER_FORM_BILL.getCode());

        // 创建/更新品牌信息并更新brandId字段值
        this.brandService.createOrUpdateBrand(registerBill.getBrandName(), registerBill.getUserId())
                .ifPresent(brandId -> {
                    RegisterBill bill = new RegisterBill();
                    bill.setBrandId(brandId);
                    bill.setId(registerBill.getId());
                    this.updateSelective(bill);
                });
        this.updateUserQrStatusByUserId(registerBill.getBillId(), registerBill.getUserId());

        //进门登记单新增消息
        Integer businessType=MessageStateEnum.BUSINESS_TYPE_FORM_BILL.getCode();
        if(BillTypeEnum.SUPPLEMENT.getCode().equals(registerBill.getBillType())){
            businessType=MessageStateEnum.BUSINESS_TYPE_FIELD_BILL.getCode();
        }
        addMessage(registerBill, MessageTypeEnum.BILLSUBMIT.getCode(), businessType, MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_MANAGER.getCode());
        return registerBill.getId();
    }

    @Transactional
    @Override
    public Long doEditFormBill(RegisterBill input, List<ImageCert> imageCertList, Optional<OperatorUser> operatorUser) {
        if (input == null || input.getId() == null) {
            throw new TraceBusinessException("参数错误");
        }
        RegisterBill billItem = this.getAndCheckById(input.getId())
                .orElseThrow(() -> new TraceBusinessException("数据不存在"));

        if (BillVerifyStatusEnum.NONE.equalsToCode(billItem.getVerifyStatus())
                || BillVerifyStatusEnum.RETURNED.equalsToCode(billItem.getVerifyStatus())) {
            // 待审核，或者已退回状态可以进行数据修改
        } else {
            throw new TraceBusinessException("当前状态不能修改数据");
        }

        // 车牌转大写
        String plate = StreamEx.ofNullable(input.getPlate()).filter(StringUtils::isNotBlank).map(p -> p.toUpperCase())
                .findFirst().orElse(null);
        input.setPlate(plate);
        // 保存车牌
        this.userPlateService.checkAndInsertUserPlate(input.getUserId(), plate);
        input.setVerifyStatus(BillVerifyStatusEnum.NONE.getCode());
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

        // 查验状态为不通过，进门状态设置为未进门，其他设置为已进门
        if (BillVerifyStatusEnum.NO_PASSED.equalsToCode(input.getVerifyStatus())) {
            input.setIsCheckin(YnEnum.NO.getCode());
        } else {
            input.setIsCheckin(YnEnum.YES.getCode());
        }
        if (BillVerifyStatusEnum.PASSED.equalsToCode(input.getVerifyStatus())) {
            input.setVerifyType(VerifyTypeEnum.PASSED_BEFORE_CHECKIN.getCode());
        }

        this.updateSelective(input);
        this.registerBillHistoryService.createHistory(billItem.getBillId());

        imageCertList = StreamEx.ofNullable(imageCertList).nonNull().flatCollection(Function.identity()).nonNull().toList();
        if (imageCertList.isEmpty()) {
            throw new TraceBusinessException("请上传凭证");
        }
        // 保存图片
        imageCertService.insertImageCert(imageCertList, input.getId(), BillTypeEnum.REGISTER_FORM_BILL.getCode());

        this.brandService.createOrUpdateBrand(input.getBrandName(), billItem.getUserId());
        this.updateUserQrStatusByUserId(billItem.getBillId(), billItem.getUserId());
        return input.getId();
    }

    @Transactional
    @Override
    public Long doVerifyFormCheckIn(RegisterBill input, Optional<OperatorUser> operatorUser) {
        if (input == null || input.getId() == null) {
            throw new TraceBusinessException("参数错误");
        }
        RegisterBill billItem = this.getAndCheckById(input.getId())
                .orElseThrow(() -> new TraceBusinessException("数据不存在"));

        this.doVerifyForm(billItem, input.getVerifyStatus(), input.getReason(), operatorUser);
        //新增消息
        addMessage(billItem, MessageTypeEnum.BILLPASS.getCode(), MessageStateEnum.BUSINESS_TYPE_BILL.getCode(), MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_MANAGER.getCode());
        return billItem.getId();
    }

    private void doVerifyForm(RegisterBill billItem, Integer verifyStatus, String reason,
                          Optional<OperatorUser> operatorUser) {
        BillVerifyStatusEnum fromVerifyState = BillVerifyStatusEnum.fromCode(billItem.getVerifyStatus())
                .orElseThrow(() -> new TraceBusinessException("数据错误"));

        BillVerifyStatusEnum toVerifyState = BillVerifyStatusEnum.fromCode(verifyStatus)
                .orElseThrow(() -> new TraceBusinessException("参数错误"));

        logger.info("审核: billId: {} from {} to {}", billItem.getBillId(), fromVerifyState.getName(),
                toVerifyState.getName());
        if (!BillVerifyStatusEnum.RETURNED.equalsToCode(billItem.getVerifyStatus())) {
            throw new TraceBusinessException("当前状态不能进行数据操作");
        }
        if (BillVerifyStatusEnum.NONE == toVerifyState) {
            throw new TraceBusinessException("不支持的操作");
        }
        if (fromVerifyState == toVerifyState) {
            throw new TraceBusinessException("状态不能相同");
        }

        // 更新当前报务单数据
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
            bill.setVerifyType(VerifyTypeEnum.PASSED_AFTER_CHECKIN.getCode());
        }
        bill.setModified(new Date());
        this.updateSelective(bill);
        // 创建审核历史数据
        this.registerBillHistoryService.createHistory(billItem.getId());

        // 更新用户颜色码
        this.updateUserQrStatusByUserId(billItem.getBillId(), billItem.getUserId());
    }

    @Override
    public BasePage<RegisterBill> listPageApi(RegisterBillDto input){

        StringBuilder sql = new StringBuilder();
        buildFormLikeKeyword(input).ifPresent(sql::append);
        if(sql.length() > 0){
            input.setMetadata(IDTO.AND_CONDITION_EXPR, sql.toString());
        }

        BasePage<RegisterBill> registerBillBasePage = listPageByExample(input);
        return registerBillBasePage;
    }

    private Optional<String> buildFormLikeKeyword(RegisterBillDto query) {
        String sql = null;
        if (StringUtils.isNotBlank(query.getKeyword())) {
            String keyword = query.getKeyword().trim();
            sql = "( product_name like '%" + keyword + "%'  OR user_id in(select id from `user` u where u.name like '%"
                    + keyword + "%' OR legal_person like '%" + keyword + "%' OR phone like '%"
                    + keyword + "%') OR third_party_code like '%"+keyword+"%' )";
        }
        return Optional.ofNullable(sql);
    }

    @Transactional
    @Override
    public Long doDelete(CreateRegisterBillInputDto dto, Long userId, Optional<OperatorUser> operatorUser) {
        if (dto == null || userId == null) {
            throw new TraceBusinessException("参数错误");
        }
        RegisterBill billItem = this.getAndCheckById(dto.getBillId()).orElseThrow(() -> new TraceBusinessException("数据不存在"));
        if (!userId.equals(billItem.getUserId())) {
            throw new TraceBusinessException("没有权限删除数据");
        }
        if (YnEnum.YES.equalsToCode(billItem.getIsCheckin())) {
            throw new TraceBusinessException("不能删除已进门数据");
        }
        if (BillVerifyStatusEnum.NO_PASSED.equalsToCode(billItem.getVerifyStatus())) {
            throw new TraceBusinessException("不能删除审核未通过数据");
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
        this.userQrHistoryService.rollbackUserQrStatus(bill.getId(), billItem.getUserId());
        return dto.getBillId();
    }
}