package com.dili.sg.trace.dto;

import javax.persistence.Column;

import com.dili.sg.trace.domain.QualityTraceTradeBillSyncPoint;
import com.dili.ss.domain.annotation.Operator;

public interface QualityTraceTradeBillSyncPointDto extends QualityTraceTradeBillSyncPoint {
	@Column(name = "`bill_id`")
	@Operator(Operator.LITTLE_THAN)
	Long getMinBillId();

	void setMinBillId(Long minBillId);
}
