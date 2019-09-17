package com.dili.trace.dto;

import com.dili.ss.dto.IBaseDomain;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.RegisterBill;

public interface QualityTraceTradeBillOutDto extends IBaseDomain {

	public QualityTraceTradeBill getQualityTraceTradeBill();

	public void setQualityTraceTradeBill(QualityTraceTradeBill qualityTraceTradeBill);

	public RegisterBill getRegisterBill();

	public void setRegisterBill(RegisterBill registerBill);

}
