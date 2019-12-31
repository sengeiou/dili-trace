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
	private Boolean pass;
	
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
	public Boolean getPass() {
		return pass;
	}
	public void setPass(Boolean pass) {
		this.pass = pass;
	}
	public Boolean getPassWithOriginCertifiyUrl() {
		return passWithOriginCertifiyUrl;
	}
	public void setPassWithOriginCertifiyUrl(Boolean passWithOriginCertifiyUrl) {
		this.passWithOriginCertifiyUrl = passWithOriginCertifiyUrl;
	}
	
}
