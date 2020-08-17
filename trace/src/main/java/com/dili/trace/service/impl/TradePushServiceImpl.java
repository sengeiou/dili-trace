package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradePushLog;
import com.dili.trace.dto.TradePushInputDto;
import com.dili.trace.service.ProductStockService;
import com.dili.trace.service.TradeDetailService;
import com.dili.trace.service.TradePushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@EnableRetry
public class TradePushServiceImpl extends BaseServiceImpl<TradePushLog, Long> implements TradePushService {

    private static final Logger logger = LoggerFactory.getLogger(TradePushServiceImpl.class);

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private ProductStockService productStockService;

    @Override
    public void tradePush(TradePushLog tradePushLog) {
        logger.info("push, param:{}", JSON.toJSONString(tradePushLog));
        BigDecimal pushAwayWeight = tradePushLog.getOperationWeight();
        TradeDetail tradeDetail = tradeDetailService.get(tradePushLog.getTradeDetailId());
        // 下架
        if(tradePushLog.getLogType() == 0) {
            if(pushAwayWeight.compareTo(BigDecimal.ZERO) <= 0)
            {
                throw new TraceBusinessException("下架数量必须大于0");
            }
            if (pushAwayWeight.compareTo(tradeDetail.getStockWeight()) > 0) {
                throw new TraceBusinessException("下架数量不能大于在售数量");
            }
        }

        // 上架
        if(tradePushLog.getLogType() == 1)
        {
            if(pushAwayWeight.compareTo(BigDecimal.ZERO) <= 0)
            {
                throw new TraceBusinessException("上架数量必须大于0");
            }
            if(pushAwayWeight.compareTo(tradeDetail.getPushawayWeight()) > 0)
            {
                throw new TraceBusinessException("上架数量不能大于下架数量");
            }
            pushAwayWeight = pushAwayWeight.negate();
        }
        BigDecimal tradeDetailStockWeight = tradeDetail.getStockWeight().subtract(pushAwayWeight);
        tradeDetailService.udpateTradePushAway(tradePushLog.getTradeDetailId(), pushAwayWeight);

        ProductStock productStock = new ProductStock();
        productStock = productStockService.get(tradePushLog.getProductStockId());
        BigDecimal stockWeight = productStock.getStockWeight();
        stockWeight = stockWeight.subtract(pushAwayWeight);
        productStock.setStockWeight(stockWeight);
        if(tradeDetailStockWeight.compareTo(BigDecimal.ZERO) <= 0)
        {
            productStock.setTradeDetailNum(productStock.getTradeDetailNum()-1);
        }
        productStockService.update(productStock);

        getDao().insert(tradePushLog);
    }
}
