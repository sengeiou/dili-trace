package com.dili.trace.dto;

import java.util.List;

import com.dili.ss.dto.IBaseDomain;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;

public interface QualityTraceTradeBillOutDto extends IBaseDomain {

	public QualityTraceTradeBill getQualityTraceTradeBill();

	public void setQualityTraceTradeBill(QualityTraceTradeBill qualityTraceTradeBill);

	public RegisterBill getRegisterBill();

	public void setRegisterBill(RegisterBill registerBill);
	
	public List<SeparateSalesRecord> getSeparateSalesRecords();

	public void setSeparateSalesRecords(List<SeparateSalesRecord> separateSalesRecords);

}
