package com.dili.trace.domain.sg;

import com.dili.sg.trace.domain.RegisterBill;
import com.dili.sg.trace.domain.SeparateSalesRecord;
import com.dili.ss.dto.IBaseDomain;

import java.util.List;

public interface QualityTraceTradeBillOutDto extends IBaseDomain {

	public QualityTraceTradeBill getQualityTraceTradeBill();

	public void setQualityTraceTradeBill(QualityTraceTradeBill qualityTraceTradeBill);

	public RegisterBill getRegisterBill();

	public void setRegisterBill(RegisterBill registerBill);
	
	public List<SeparateSalesRecord> getSeparateSalesRecords();

	public void setSeparateSalesRecords(List<SeparateSalesRecord> separateSalesRecords);

}
