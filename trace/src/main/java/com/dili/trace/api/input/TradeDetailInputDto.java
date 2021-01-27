package com.dili.trace.api.input;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

/**
 * 交易详情
 */
public class TradeDetailInputDto {

    /**
     * 交易重量
     */
    @ApiModelProperty(value = "购买重量")
    private BigDecimal tradeWeight;

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long tradeDetailId;


    /**
     * BillID
     */
    @ApiModelProperty(value = "BillID")
    private Long billId;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

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