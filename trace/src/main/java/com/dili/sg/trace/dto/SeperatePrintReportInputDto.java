package com.dili.sg.trace.dto;

import java.util.List;

import com.dili.sg.trace.domain.SeperatePrintReport;

public class SeperatePrintReportInputDto {
	private Long approverInfoId;
	
	private Long billId;
	
	private Integer validPeriod;
	
	private List<SeperatePrintReport>seperatePrintReportList;

	public List<SeperatePrintReport> getSeperatePrintReportList() {
		return seperatePrintReportList;
	}

	public void setSeperatePrintReportList(List<SeperatePrintReport> seperatePrintReportList) {
		this.seperatePrintReportList = seperatePrintReportList;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Integer getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}

	public Long getApproverInfoId() {
		return approverInfoId;
	}

	public void setApproverInfoId(Long approverInfoId) {
		this.approverInfoId = approverInfoId;
	}

}
