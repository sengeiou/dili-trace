package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.CheckoutStatusEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.glossary.TFEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交易服务
 */
@Service
@Transactional
public class TradeService {
    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);
    @Autowired
    ProductStockService batchStockService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    TradeDetailService tradeDetailService;

    /**
     * 创建库存
     * @param billId
     * @param tradeDetailId
     * @param operatorUser
     * @return
     */
    public Long createBatchStockAfterVerifiedAndCheckin(Long billId, Long tradeDetailId,
    Optional<OperatorUser> operatorUser) {
        RegisterBill billItem = this.registerBillService.get(billId);
        BigDecimal weight=billItem.getWeight();
//      if (!YesOrNoEnum.YES.getCode().equals(billItem.getIsCheckin())) {
        if (!CheckinStatusEnum.ALLOWED.equalsToCode(billItem.getCheckinStatus())) {
            // 还没有进门，不对TradeDetaile及BatchStock进行任何操作
            return billId;
        }
        TradeDetail tradeDetailItem=this.tradeDetailService.get(tradeDetailId);
        logger.info("billid:{},billI.verifyStatus:{}", billId, billItem.getVerifyStatusName());
        logger.info("TradeDetail.checkinStatus:{},TradeDetail.saleStatus:{}", tradeDetailItem.getCheckinStatus(),
                tradeDetailItem.getSaleStatus());
        // 通过审核状态及进门状态判断是否可以进行销售
        if (CheckinStatusEnum.ALLOWED.equalsToCode(tradeDetailItem.getCheckinStatus())
                && CheckoutStatusEnum.NONE.equalsToCode(tradeDetailItem.getCheckoutStatus())
                && BillVerifyStatusEnum.PASSED.equalsToCode(billItem.getVerifyStatus())
                && SaleStatusEnum.NONE.equalsToCode(tradeDetailItem.getSaleStatus())) {

            ProductStock batchStock = this.batchStockService.findOrCreateBatchStock(billItem.getUserId(), billItem);
            batchStock.setStockWeight(batchStock.getStockWeight().add(weight));
            batchStock.setTotalWeight(batchStock.getTotalWeight().add(weight));
            batchStock.setTradeDetailNum(batchStock.getTradeDetailNum()+1);
            this.batchStockService.updateSelective(batchStock);

            TradeDetail updatableRecord = new TradeDetail();
            updatableRecord.setId(tradeDetailItem.getId());
            updatableRecord.setModified(new Date());
            updatableRecord.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
            updatableRecord.setIsBatched(YesOrNoEnum.YES.getCode());
            updatableRecord.setProductStockId(batchStock.getId());
            updatableRecord.setStockWeight(weight);
            updatableRecord.setTotalWeight(weight);
            this.tradeDetailService.updateSelective(updatableRecord);

        }
        return billId;

    }

}