package com.dili.trace.api.input;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class TradeDetailInputDto {

    @ApiModelProperty(value = "购买重量")
    private BigDecimal tradeWeight;
    @ApiModelProperty(value = "订单ID")
    private Long tradeDetailId;

    /**
     * @return BigDecimal return the tradeWeight
     */
    public BigDecimal getTradeWeight() {
        return tradeWeight;
    }

    /**
     * @param tradeWeight the tradeWeight to set
     */
    public void setTradeWeight(BigDecimal tradeWeight) {
        this.tradeWeight = tradeWeight;
    }

    /**
     * @return Long return the tradeDetailId
     */
    public Long getTradeDetailId() {
        return tradeDetailId;
    }

    /**
     * @param tradeDetailId the tradeDetailId to set
     */
    public void setTradeDetailId(Long tradeDetailId) {
        this.tradeDetailId = tradeDetailId;
    }

}