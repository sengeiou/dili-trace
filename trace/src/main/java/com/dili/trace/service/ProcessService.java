package com.dili.trace.service;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.ProcessConfig;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.MarketEnum;
import com.dili.trace.rpc.service.ProductRpcService;
import com.dili.uap.sdk.domain.Firm;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 流程处理
 */
@Service
public class ProcessService {
    private static final Logger logger= LoggerFactory.getLogger(ProcessService.class);

    @Autowired
    ProductRpcService productRpcService;
    @Autowired
    BillService billService;

    @Autowired
    private MarketService marketService;
    @Autowired
    CheckinOutRecordService checkinOutRecordService;

    @Autowired
    TradeDetailService tradeDetailService;

    @Autowired
    ProcessConfigService processConfigService;
    @Autowired
    BillVerifyHistoryService billVerifyHistoryService;

    /**
     * 创建报备之后
     *
     * @param billId
     * @param marketId
     */
    public void afterCreateBill(Long billId, Long marketId,Optional<OperatorUser>operatorUser) {
        ProcessConfig processConfig=this.processConfigService.findByMarketId(marketId);
        if(YesOrNoEnum.NO.getCode().equals(processConfig.getIsAuditAfterRegist())){
            RegisterBill updatableBill=new RegisterBill();
            updatableBill.setId(billId);
            updatableBill.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
            this.billService.updateSelective(updatableBill);
            this.billVerifyHistoryService.createVerifyHistory(Optional.empty(),billId,operatorUser);
        }
        if(YesOrNoEnum.NO.getCode().equals(processConfig.getIsWeightBeforeCheckin())){
            RegisterBill updatableBill=new RegisterBill();
            updatableBill.setId(billId);
            updatableBill.setCheckinStatus(CheckinStatusEnum.ALLOWED.getCode());
            this.billService.updateSelective(updatableBill);
        }
    }

    /**
     * 报备审核通过之后
     *
     * @param billId
     * @param marketId
     */
    public void afterBillPassed(Long billId, Long marketId,Optional<OperatorUser> optUser) {
        logger.debug("afterBillPassed:billId={},marketId={}",billId,marketId);
        RegisterBill registerBill = billService.get(billId);

        Map<String, String> marketCodeMap = marketService.getMarketCodeMap();
        String marketCode = marketService.getMarketById(marketId).map(Firm::getCode).orElseThrow(() -> {
            return new TraceBizException("市场不存在");
        });
        logger.debug("marketCode={}",marketCode);
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
