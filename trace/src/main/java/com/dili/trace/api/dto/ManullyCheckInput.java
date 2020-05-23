package com.dili.trace.api.dto;

public class ManullyCheckInput {
	private Long billId;

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}



	/**
	 * 是否合格
	 */
	private Boolean pass;



	public Boolean getPass() {
		return pass;
	}

	public void setPass(Boolean pass) {
		this.pass = pass;
	}
	

}