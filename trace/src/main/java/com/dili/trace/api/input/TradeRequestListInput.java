package com.dili.trace.api.input;

import java.util.List;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Like;
import com.dili.trace.domain.TradeRequest;

public class TradeRequestListInput extends TradeRequest {
    private Long buyerId;
    private List<BatchStockInput> batchStockList;

    @Column(name = "`product_name`")
    @Like()
    private String likeProductName;

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
     * @return List<BatchStockInput> return the batchStockList
     */
    public List<BatchStockInput> getBatchStockList() {
        return batchStockList;
    }

    /**
     * @param batchStockList the batchStockList to set
     */
    public void setBatchStockList(List<BatchStockInput> batchStockList) {
        this.batchStockList = batchStockList;
    }


    /**
     * @return String return the likeProductName
     */
    public String getLikeProductName() {
        return likeProductName;
    }

    /**
     * @param likeProductName the likeProductName to set
     */
    public void setLikeProductName(String likeProductName) {
        this.likeProductName = likeProductName;
    }

}