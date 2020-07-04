package com.dili.trace.api.input;

import java.math.BigDecimal;
import java.util.List;

import com.dili.trace.domain.TradeRequest;

import io.swagger.annotations.ApiModelProperty;

public class TradeRequestWrapperDto {
    private Long tradeRequestId;
    private Integer returnStatus;
    private String reason;
    
    @ApiModelProperty(value = "购买重量")
    private BigDecimal tradeWeight;
    @ApiModelProperty(value = "卖家ID")
    private Long sellerId;
    @ApiModelProperty(value = "买家ID")
    private Long buyerId;
    @ApiModelProperty(value = "批次库存ID")
    private Long batchStockId;
    private List<TradeRequestInputDto> tradeRequestList;

    public TradeRequest buildTradeRequest() {
        TradeRequest request = new TradeRequest();
        request.setSellerId(this.getSellerId());
        request.setTradeWeight(this.getTradeWeight());
        request.setBatchStockId(this.getBatchStockId());
        request.setBuyerId(this.getBuyerId());
        return request;
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
     * @return Long return the sellerId
     */
    public Long getSellerId() {
        return sellerId;
    }

    /**
     * @param sellerId the sellerId to set
     */
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * @return Long return the batchStockId
     */
    public Long getBatchStockId() {
        return batchStockId;
    }

    /**
     * @param batchStockId the batchStockId to set
     */
    public void setBatchStockId(Long batchStockId) {
        this.batchStockId = batchStockId;
    }

    /**
     * @return Long return the buyerId
     */
    public Long getBuyerId() {
        return buyerId;
    }

    /**
     * @param buyerId the buyerId to set
     */
    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    /**
     * @return List<TradeRequestInputDto> return the tradeRequestList
     */
    public List<TradeRequestInputDto> getTradeRequestList() {
        return tradeRequestList;
    }

    /**
     * @param tradeRequestList the tradeRequestList to set
     */
    public void setTradeRequestList(List<TradeRequestInputDto> tradeRequestList) {
        this.tradeRequestList = tradeRequestList;
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

}