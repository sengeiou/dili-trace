package com.dili.trace.dto;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
public class TradeDetailInputWrapperDto {

	private Long buyerId;

	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	private List<TradeDetailInputDto> tradeDetailInputList;

	public List<TradeDetailInputDto> getTradeDetailInputList() {
		return tradeDetailInputList;
	}

	public void setTradeDetailInputList(List<TradeDetailInputDto> tradeDetailInputList) {
		this.tradeDetailInputList = tradeDetailInputList;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

}