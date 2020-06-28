package com.dili.trace.dto;

import java.math.BigDecimal;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
public class TradeDetailInputDto {
	private Long tradeDetailId;
	private BigDecimal tradeWeight;

	public Long getTradeDetailId() {
		return tradeDetailId;
	}

	public void setTradeDetailId(Long tradeDetailId) {
		this.tradeDetailId = tradeDetailId;
	}


	public BigDecimal getTradeWeight() {
		return tradeWeight;
	}

	public void setTradeWeight(BigDecimal tradeWeight) {
		this.tradeWeight = tradeWeight;
	}

}