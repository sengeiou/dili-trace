package com.dili.trace.dto;

import java.util.List;

import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;

public class ECommerceBillInputDto {
	/**
	 * 电商登记单信息
	 */
	private RegisterBill bill;
	/**
	 * 分销信息
	 */
	private List<SeparateSalesRecord> separateSalesRecordList;

	public RegisterBill getBill() {
		return bill;
	}

	public void setBill(RegisterBill bill) {
		this.bill = bill;
	}

	public List<SeparateSalesRecord> getSeparateSalesRecordList() {
		return separateSalesRecordList;
	}

	public void setSeparateSalesRecordList(List<SeparateSalesRecord> separateSalesRecordList) {
		this.separateSalesRecordList = separateSalesRecordList;
	}

	

}
