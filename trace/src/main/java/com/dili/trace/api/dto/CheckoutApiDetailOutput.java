package com.dili.trace.api.dto;

import com.dili.trace.domain.SeparateSalesRecord;

public class CheckoutApiDetailOutput {
	private SeparateSalesRecord separateSalesRecord;

	public SeparateSalesRecord getSeparateSalesRecord() {
		return separateSalesRecord;
	}

	public void setSeparateSalesRecord(SeparateSalesRecord separateSalesRecord) {
		this.separateSalesRecord = separateSalesRecord;
	}
   

}