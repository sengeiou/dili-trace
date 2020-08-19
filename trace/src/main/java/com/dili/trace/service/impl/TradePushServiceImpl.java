package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.*;
import com.dili.trace.dto.TradePushInputDto;
import com.dili.trace.enums.PushTypeEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeTypeEnum;
import com.dili.trace.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@EnableRetry
public class TradePushServiceImpl extends BaseServiceImpl<TradePushLog, Long> implements TradePushService {

    private static final Logger logger = LoggerFactory.getLogger(TradePushServiceImpl.class);

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private ProductStockService productStockService;

    @Autowired
    private TradeRequestService tradeRequestService;

    @Autowired
    private RegisterBillService registerBillService;

    @Override
    @Transactional
    public void tradePush(TradePushLog tradePushLog) {
        logger.info("push, param:{}", JSON.toJSONString(tradePushLog));
        BigDecimal pushAwayWeight = tradePushLog.getOperationWeight();
        TradeDetail tradeDetail = tradeDetailService.get(tradePushLog.getTradeDetailId());
        ProductStock productStock = productStockService.selectByIdForUpdate(tradeDetail.getProductStockId())
                .orElseThrow(() -> {
                    return new TraceBusinessException("操作库存失败");
                });;
        // 下架
        if(tradePushLog.getLogType().equals(PushTypeEnum.DOWN.getCode())) {
            if(pushAwayWeight.compareTo(BigDecimal.ZERO) <= 0)
            {
                throw new TraceBusinessException("下架数量必须大于0");
            }
            if (pushAwayWeight.compareTo(tradeDetail.getStockWeight()) > 0) {
                throw new TraceBusinessException("下架数量不能大于在售数量");
            }
        }

        // 上架
        if(tradePushLog.getLogType().equals(PushTypeEnum.UP.getCode()))
        {
            if(pushAwayWeight.compareTo(BigDecimal.ZERO) <= 0)
            {
                throw new TraceBusinessException("上架数量必须大于0");
            }
            if(pushAwayWeight.compareTo(tradeDetail.getPushawayWeight()) > 0)
            {
                throw new TraceBusinessException("上架数量不能大于已下架数量");
            }
            pushAwayWeight = pushAwayWeight.negate();
        }
        BigDecimal tradeDetailStockWeight = tradeDetail.getStockWeight().subtract(pushAwayWeight);
        if(tradeDetail.getPushawayWeight() == null)
        {
            tradeDetail.setPushawayWeight(BigDecimal.ZERO);
        }
        BigDecimal tradeDetailPushAwayWeight = tradeDetail.getPushawayWeight().add(pushAwayWeight);
        TradeDetail tradeDetailForUpdate = new TradeDetail();
        tradeDetailForUpdate.setId(tradePushLog.getTradeDetailId());
        tradeDetailForUpdate.setStockWeight(tradeDetailStockWeight);
        tradeDetailForUpdate.setPushawayWeight(tradeDetailPushAwayWeight);

        BigDecimal stockWeight = productStock.getStockWeight();
        stockWeight = stockWeight.subtract(pushAwayWeight);
        productStock.setStockWeight(stockWeight);
        if(tradeDetailStockWeight.compareTo(BigDecimal.ZERO) <= 0)
        {
            productStock.setTradeDetailNum(productStock.getTradeDetailNum()-1);
            tradeDetailForUpdate.setSaleStatus(SaleStatusEnum.NOT_FOR_SALE.getCode());
        }
        else
        {
            // 之前stockWeight<=0,上架后大于0
            if(tradeDetail.getStockWeight().compareTo(BigDecimal.ZERO) <= 0)
            {
                productStock.setTradeDetailNum(productStock.getTradeDetailNum()+1);
                tradeDetailForUpdate.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
            }
        }

        tradeDetailService.updateExact(tradeDetailForUpdate);
        productStockService.update(productStock);
        tradePushLog.setProductName(tradeDetail.getProductName());
        tradePushLog.setProductStockId(tradeDetail.getProductStockId());
        tradePushLog.setOrderType(tradeDetail.getTradeType());
        if(tradeDetail.getTradeType().equals(TradeTypeEnum.NONE.getCode())){
            tradePushLog.setOrderId(tradeDetail.getBillId());
            RegisterBill bill = registerBillService.get(tradeDetail.getBillId());
            tradePushLog.setOrderCode(bill.getCode());
        }
        else
        {
            TradeRequest tradeRequest = tradeRequestService.get(tradeDetail.getTradeRequestId());
            tradePushLog.setOrderId(tradeRequest.getId());
            tradePushLog.setOrderCode(tradeRequest.getCode());
        }
        getDao().insert(tradePushLog);
    }
}
