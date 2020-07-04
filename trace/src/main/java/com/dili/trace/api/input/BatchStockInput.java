package com.dili.trace.api.input;

import java.math.BigDecimal;
import java.util.List;

public class BatchStockInput {
    private Long batchStockId;

    private BigDecimal tradeWeight;

    private List<TradeRequestInputDto> tradeRequestList;
    

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

}