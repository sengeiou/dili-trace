package com.dili.trace.dto;

import java.util.List;

public class BatchAuditDto {
	/**
	 * 登记单ID集合
	 */
	private List<Long>registerBillIdList;
	/**
	 * 审核是否通过状态值(true,false)
	 */
	private Integer verifyStatus;
	
	/**
	 * 审核是否通过状态值(true,false)
	 */
	private Boolean passWithOriginCertifiyUrl;
	
	public List<Long> getRegisterBillIdList() {
		return registerBillIdList;
	}
	public void setRegisterBillIdList(List<Long> registerBillIdList) {
		this.registerBillIdList = registerBillIdList;
	}
	public Boolean getPassWithOriginCertifiyUrl() {
		return passWithOriginCertifiyUrl;
	}
	public void setPassWithOriginCertifiyUrl(Boolean passWithOriginCertifiyUrl) {
		this.passWithOriginCertifiyUrl = passWithOriginCertifiyUrl;
	}

	public Integer getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
}
