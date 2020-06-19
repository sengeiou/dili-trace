package com.dili.trace.dto;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
public class SeparateSalesInputDTO {

	private Long salesUserId;
	
	private List<SeparateSalesRecordInput> separateList;

	

	public List<SeparateSalesRecordInput> getSeparateList() {
		return separateList;
	}

	public void setSeparateList(List<SeparateSalesRecordInput> separateList) {
		this.separateList = separateList;
	}

	public Long getSalesUserId() {
		return salesUserId;
	}

	public void setSalesUserId(Long salesUserId) {
		this.salesUserId = salesUserId;
	}

}