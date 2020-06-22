package com.dili.trace.dto;

import java.util.List;

import com.dili.ss.dto.IBaseDomain;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;

public interface QualityTraceTradeBillOutDto extends IBaseDomain {



	public RegisterBill getRegisterBill();

	public void setRegisterBill(RegisterBill registerBill);
	
	public List<SeparateSalesRecord> getSeparateSalesRecords();

	public void setSeparateSalesRecords(List<SeparateSalesRecord> separateSalesRecords);

}
