package com.dili.trace.api.dto;

import java.util.List;

public class ManullyCheckInput {

	/**
	 * ID
	 */
	private Long separateSalesId;

	/**
	 * 是否合格
	 */
	private Boolean pass;

	public Long getSeparateSalesId() {
		return separateSalesId;
	}

	public void setSeparateSalesId(Long separateSalesId) {
		this.separateSalesId = separateSalesId;
	}

	public Boolean getPass() {
		return pass;
	}

	public void setPass(Boolean pass) {
		this.pass = pass;
	}
	

}