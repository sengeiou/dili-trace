package com.dili.trace.dto;

import java.math.BigDecimal;

public class TradePushInputDto {
    private Integer logType;
    private Integer orderType;
    private Long orderId;
    private BigDecimal operationWeight;
    private String operationReason;

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOperationWeight() {
        return operationWeight;
    }

    public void setOperationWeight(BigDecimal operationWeight) {
        this.operationWeight = operationWeight;
    }

    public String getOperationReason() {
        return operationReason;
    }

    public void setOperationReason(String operationReason) {
        this.operationReason = operationReason;
    }
}
