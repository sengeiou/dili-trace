package com.dili.trace.dto;

import java.util.List;

import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;

/**
 * Created by laikui on 2019/7/30.
 */
public class RegisterBillOutputDto extends RegisterBill {
	private List<SeparateSalesRecord> separateSalesRecords;

	private DetectRecord detectRecord;


	public List<SeparateSalesRecord> getSeparateSalesRecords() {
		return separateSalesRecords;
	}

	public void setSeparateSalesRecords(List<SeparateSalesRecord> separateSalesRecords) {
		this.separateSalesRecords = separateSalesRecords;
	}

	public DetectRecord getDetectRecord() {
		return detectRecord;
	}

	public void setDetectRecord(DetectRecord detectRecord) {
		this.detectRecord = detectRecord;
	}


	
	
}
