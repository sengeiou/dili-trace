package com.dili.trace.dto;

import com.dili.trace.domain.TradeDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * <p>
 * This file was generated on 2019-07-26 09:20:35.
 */
public class TradeDetailInputWrapperDto {
    private TradeDetail tradeDetail;
    private BigDecimal requestTradeWeight;

    public TradeDetail getTradeDetail() {
        return tradeDetail;
    }

    public void setTradeDetail(TradeDetail tradeDetail) {
        this.tradeDetail = tradeDetail;
    }

    public BigDecimal getRequestTradeWeight() {
        return requestTradeWeight;
    }

    public void setRequestTradeWeight(BigDecimal requestTradeWeight) {
        this.requestTradeWeight = requestTradeWeight;
    }
}