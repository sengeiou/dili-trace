package com.dili.trace.dto;

import java.util.List;

import com.dili.ss.dto.IBaseDomain;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.QualityTraceTradeBill;

public interface QualityTraceTradeBillOutDto extends IBaseDomain {

	public RegisterBill getRegisterBill();

	public void setRegisterBill(RegisterBill registerBill);
	
	public List<SeparateSalesRecord> getSeparateSalesRecords();

	public void setSeparateSalesRecords(List<SeparateSalesRecord> separateSalesRecords);

	public QualityTraceTradeBill getQualityTraceTradeBill();

	public void setQualityTraceTradeBill(QualityTraceTradeBill qualityTraceTradeBill);

}
