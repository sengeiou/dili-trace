package com.dili.trace.dto.thirdparty.report;

public class CredentialInfoDto {
	private String credentialName;// 证件名称
	private String picUrl;// 证件url

	public String getCredentialName() {
		return credentialName;
	}

	public void setCredentialName(String credentialName) {
		this.credentialName = credentialName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
