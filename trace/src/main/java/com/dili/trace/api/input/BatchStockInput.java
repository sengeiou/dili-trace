package com.dili.trace.api.input;

import java.math.BigDecimal;
import java.util.List;

public class BatchStockInput {
    private Long batchStockId;

    private BigDecimal tradeWeight;

    private List<TradeDetailInputDto> tradeDetailInputList;
    

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
     * @return List<TradeDetailInputDto> return the tradeDetailInputList
     */
    public List<TradeDetailInputDto> getTradeDetailInputList() {
        return tradeDetailInputList;
    }

    /**
     * @param tradeDetailInputList the tradeDetailInputList to set
     */
    public void setTradeDetailInputList(List<TradeDetailInputDto> tradeDetailInputList) {
        this.tradeDetailInputList = tradeDetailInputList;
    }

}