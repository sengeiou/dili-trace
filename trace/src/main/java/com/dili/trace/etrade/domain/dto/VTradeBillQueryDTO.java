package com.dili.trace.etrade.domain.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.etrade.domain.VTradeBill;

public class VTradeBillQueryDTO extends VTradeBill {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Operator(Operator.GREAT_EQUAL_THAN)
	private Long minBillID;// Bigint

	public Long getMinBillID() {
		return minBillID;
	}

	public void setMinBillID(Long minBillID) {
		this.minBillID = minBillID;
	}

}
