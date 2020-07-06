package com.dili.trace.service;

import java.util.Date;

import com.dili.common.exception.TraceBusinessException;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.CheckoutStatusEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.glossary.TFEnum;
import com.dili.trace.glossary.YnEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.StreamEx;

@Service
@Transactional
public class TradeService {
    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);
    @Autowired
    BatchStockService batchStockService;
    @Autowired
    RegisterBillService billService;
    @Autowired
    TradeDetailService tradeDetailService;

    public Long createBatchStockAfterVerifiedAndCheckin(Long billId, Long tradeDetailId,
            OperatorUser operateUser) {
        RegisterBill billItem = this.billService.get(billId);
        if (!YnEnum.YES.equalsToCode(billItem.getIsCheckin())) {
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

            BatchStock batchStock = this.batchStockService.findOrCreateBatchStock(billItem.getUserId(), billItem);
            batchStock.setStockWeight(batchStock.getStockWeight().add(billItem.getWeight()));
            batchStock.setTotalWeight(batchStock.getTotalWeight().add(billItem.getWeight()));
            this.batchStockService.updateSelective(batchStock);

            TradeDetail updatableRecord = new TradeDetail();
            updatableRecord.setId(tradeDetailItem.getId());
            updatableRecord.setModified(new Date());
            updatableRecord.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
            updatableRecord.setIsBatched(TFEnum.TRUE.getCode());
            updatableRecord.setBatchStockId(batchStock.getId());
            this.tradeDetailService.updateSelective(updatableRecord);

        }
        return billId;

    }

}