package com.dili.trace.api.dto;

public class TradeDetailBillOutputDto {
	private Long billId;
	private Long tradeDetailId;
	private String productName;
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Long getTradeDetailId() {
		return tradeDetailId;
	}

	public void setTradeDetailId(Long tradeDetailId) {
		this.tradeDetailId = tradeDetailId;
	}

}
