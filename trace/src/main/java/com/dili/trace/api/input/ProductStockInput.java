package com.dili.trace.api.input;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存
 */
public class ProductStockInput {

    /**
     * 商品ID
     */
    private Long productStockId;

    /**
     * 交易重量
     */
    private BigDecimal tradeWeight;

    /**
     * 交易详情列表
     */
    private List<TradeDetailInputDto> tradeDetailInputList;
    


    public Long getProductStockId() {
		return productStockId;
	}

	public void setProductStockId(Long productStockId) {
		this.productStockId = productStockId;
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