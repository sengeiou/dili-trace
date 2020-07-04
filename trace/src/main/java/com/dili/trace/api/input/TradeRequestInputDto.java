package com.dili.trace.api.input;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class TradeRequestInputDto {

    private Long tradeRequestId;
    private Integer tradeStatus;
    private Integer returnStatus;
    private String reason;
    
    
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


    /**
     * @return Long return the tradeRequestId
     */
    public Long getTradeRequestId() {
        return tradeRequestId;
    }

    /**
     * @param tradeRequestId the tradeRequestId to set
     */
    public void setTradeRequestId(Long tradeRequestId) {
        this.tradeRequestId = tradeRequestId;
    }

    /**
     * @return Integer return the returnStatus
     */
    public Integer getReturnStatus() {
        return returnStatus;
    }

    /**
     * @param returnStatus the returnStatus to set
     */
    public void setReturnStatus(Integer returnStatus) {
        this.returnStatus = returnStatus;
    }

    /**
     * @return String return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }


    /**
     * @return Integer return the tradeStatus
     */
    public Integer getTradeStatus() {
        return tradeStatus;
    }

    /**
     * @param tradeStatus the tradeStatus to set
     */
    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

}