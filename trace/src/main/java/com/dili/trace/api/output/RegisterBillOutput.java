package com.dili.trace.api.output;

public class RegisterBillOutput {
	private Long billId;
	private Integer verifyStatus;
	private String verifyStatusDesc;
	private String productName;
	private Integer color;

	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getVerifyStatusDesc() {
		return verifyStatusDesc;
	}

	public void setVerifyStatusDesc(String verifyStatusDesc) {
		this.verifyStatusDesc = verifyStatusDesc;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}
	
	
	

}
