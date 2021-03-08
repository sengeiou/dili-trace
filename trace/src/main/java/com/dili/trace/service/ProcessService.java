package com.dili.trace.service;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.ProcessConfig;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.*;
import com.dili.trace.rpc.service.ProductRpcService;
import com.dili.uap.sdk.domain.Firm;
import com.google.common.collect.Lists;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 流程处理
 */
@Service
public class ProcessService {
    private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

    @Autowired
    ProductRpcService productRpcService;
    @Autowired
    BillService billService;
    @Autowired
    RegisterBillService registerBillService;

    @Autowired
    private MarketService marketService;
    @Autowired
    CheckinOutRecordService checkinOutRecordService;

    @Autowired
    ProcessConfigService processConfigService;
    @Autowired
    BillVerifyHistoryService billVerifyHistoryService;
    @Autowired
    UserQrHistoryService userQrHistoryService;
    @Autowired
    TradeService tradeService;


    /**
     * 创建报备之后
     *
     * @param billId
     * @param marketId
     */
    public void afterCreateBill(Long billId, Long marketId, Optional<OperatorUser> operatorUser) {
        ProcessConfig processConfig = this.processConfigService.findByMarketId(marketId);
        if (YesOrNoEnum.NO.getCode().equals(processConfig.getIsAuditAfterRegist())) {
            this.updateVerifyStatus(billId, BillVerifyStatusEnum.PASSED, Optional.empty(), operatorUser);
        }
        if (YesOrNoEnum.NO.getCode().equals(processConfig.getIsWeightBeforeCheckin())) {
            this.updateCheckinstatus(billId, CheckinStatusEnum.ALLOWED, operatorUser);
        }
    }

    /**
     * 进门
     * @param operateUser
     * @param billIdList
     * @param checkinStatusEnum
     * @return
     */
    public List<CheckinOutRecord>  doCheckIn(Optional<OperatorUser> operateUser, List<Long> billIdList,
                         CheckinStatusEnum checkinStatusEnum){
        if (billIdList == null) {
            throw new TraceBizException("参数错误");
        }

        if (checkinStatusEnum == null) {
            throw new TraceBizException("参数错误");
        }
        return StreamEx.of(billIdList).nonNull().map(billId -> {
            return this.billService.get(billId);
        }).nonNull().map(bill -> {
            return this.updateCheckinstatus(bill.getId(), checkinStatusEnum, operateUser);

        }).nonNull().toList();
    }
    /**
     * 更新状态到进门
     *
     * @param billId
     * @return
     */
    private CheckinOutRecord updateCheckinstatus(Long billId, CheckinStatusEnum checkinStatusEnum, Optional<OperatorUser> operatorUser) {

        if (CheckinStatusEnum.NONE == checkinStatusEnum) {
            throw new TraceBizException("参数错误");
        }
        if (CheckinStatusEnum.NOTALLOWED == checkinStatusEnum) {
            return null;
        }

        RegisterBill billItem =this.registerBillService.getAndCheckById(billId).orElseThrow(()->new TraceBizException("数据不存在"));

        RegisterBill updatableBill = new RegisterBill();
        updatableBill.setId(billId);
        if (CheckinStatusEnum.ALLOWED == checkinStatusEnum) {
            updatableBill.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
        } else {
            updatableBill.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
        }
        if (BillVerifyStatusEnum.PASSED.equalsToCode(billItem.getVerifyStatus())) {
            if (CheckinStatusEnum.ALLOWED==checkinStatusEnum) {
                updatableBill.setVerifyType(VerifyTypeEnum.PASSED_AFTER_CHECKIN.getCode());
            } else {
                updatableBill.setVerifyType(VerifyTypeEnum.PASSED_BEFORE_CHECKIN.getCode());
            }
        }
        this.billService.updateSelective(updatableBill);
        if (CheckinStatusEnum.ALLOWED == checkinStatusEnum) {
            CheckinOutRecord item=new CheckinOutRecord();
//				item.setId(cin.getId());
            item.setStatus(checkinStatusEnum.getCode());
            operatorUser.ifPresent(op -> {
                // item.setOperatorId(op.getId());
                // item.setOperatorName(op.getName());
            });
            item.setModified(new Date());
            item.setProductName(billItem.getProductName());
            item.setInoutWeight(billItem.getWeight());
            item.setWeightUnit(billItem.getWeightUnit());
            item.setUserName(billItem.getName());
            item.setUserId(billItem.getUserId());
            item.setBillType(billItem.getBillType());
            // item.setVerifyStatus(billItem.getVerifyStatus());
            item.setBillId(billItem.getBillId());
            item.setInout(CheckinOutTypeEnum.IN.getCode());
//				item.setTradeDetailId(tradeDetailItem.getId());
//				this.updateSelective(item);
//				return this.get(item.getId());
            this.checkinOutRecordService.insertSelective(item);
//			}).orElseGet(()->{
//				 return this.createRecordForCheckin(billItem,tradeDetailItem.getTradeDetailId(), checkinStatusEnum, operateUser);
//			});




            this.tradeService.createBatchStockAfterVerifiedAndCheckin(billItem.getId(),
                    operatorUser);
            return item;
        }
        return null;

    }

    /**
     * 更新审核状态
     *
     * @param billId
     * @param operatorUser
     * @return
     */
    private int updateVerifyStatus(Long billId, BillVerifyStatusEnum toVerifyStatusEnum, Optional<String> reason, Optional<OperatorUser> operatorUser) {



        RegisterBill updatableBill = new RegisterBill();
        updatableBill.setId(billId);
        updatableBill.setVerifyStatus(toVerifyStatusEnum.getCode());
        // 更新当前报务单数据
        operatorUser.ifPresent(op -> {
            updatableBill.setOperatorId(op.getId());
            updatableBill.setOperatorName(op.getName());
            updatableBill.setOperationTime(new Date());
        });
        reason.ifPresent(rs -> {
            updatableBill.setReason(StringUtils.trimToEmpty(rs));
        });
        RegisterBill billItem = this.billService.get(billId);
        if (BillVerifyStatusEnum.PASSED == toVerifyStatusEnum) {
            if (CheckinStatusEnum.ALLOWED.equalsToCode(billItem.getCheckinStatus())) {
                updatableBill.setVerifyType(VerifyTypeEnum.PASSED_AFTER_CHECKIN.getCode());
            } else {
                updatableBill.setVerifyType(VerifyTypeEnum.PASSED_BEFORE_CHECKIN.getCode());
            }
        }
        updatableBill.setModified(new Date());
        this.billService.updateSelective(updatableBill);

        this.billVerifyHistoryService.createVerifyHistory(Optional.of(toVerifyStatusEnum), billId, operatorUser);

        // 创建相关的tradeDetail及batchStock数据
        this.tradeService.createBatchStockAfterVerifiedAndCheckin(billId,
                operatorUser);

        // 更新用户颜色码
        this.userQrHistoryService.createUserQrHistoryForVerifyBill(billId);
        return 1;
    }

    /**
     * 报备审核通过之后
     *
     * @param billId
     * @param marketId
     */
    public void afterBillPassed(Long billId, Long marketId, Optional<OperatorUser> optUser) {
        logger.debug("afterBillPassed:billId={},marketId={}", billId, marketId);
        RegisterBill registerBill = billService.get(billId);

        Map<String, String> marketCodeMap = marketService.getMarketCodeMap();
        String marketCode = marketService.getMarketById(marketId).map(Firm::getCode).orElseThrow(() -> {
            return new TraceBizException("市场不存在");
        });
        logger.debug("marketCode={}", marketCode);
        // 杭果和寿光市场，审核通过后系统自动进门
//        if (marketCode.equals(marketCodeMap.get(MarketEnum.HZSG.getCode()))
//                || marketCode.equals(marketCodeMap.get(MarketEnum.SDSG.getCode()))||marketId.equals(8L)) {
//            logger.debug("杭果和寿光自动进门");
//            List<CheckinOutRecord> checkinRecordList = this.checkinOutRecordService
//                    .doCheckin(optUser, Lists.newArrayList(billId), CheckinStatusEnum.ALLOWED);
//        }else{
//            logger.debug("其他市场状态不变");
//        }
    }


    /**
     * 进门之后
     *
     * @param billId
     * @param marketId
     */
//    public void afterCheckIn(Long billId, Long marketId,Optional<OperatorUser> optUser ) {
//        RegisterBill registerBill = billService.get(billId);
//
//        // 进门之后向 UAP 同步库存
//        productRpcService.create(registerBill, optUser);
//    }

    /**
     * 交易之后
     * @param buyerDetailList
     * @param sellerDetailList
     * @param marketId
     */
//    public void afterTrade(List<TradeDetail> buyerDetailList, List<TradeDetail> sellerDetailList, Long marketId, Optional<OperatorUser> optUser) {
//        // 交易之后向 UAP 同步库存
//        productRpcService.handleTradeStocks(buyerDetailList, sellerDetailList, optUser, marketId);
//    }
}
