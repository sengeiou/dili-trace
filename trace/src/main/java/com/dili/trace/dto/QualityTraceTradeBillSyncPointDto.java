package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.sg.QualityTraceTradeBillSyncPoint;

import javax.persistence.Column;

public interface QualityTraceTradeBillSyncPointDto extends QualityTraceTradeBillSyncPoint {
	@Column(name = "`bill_id`")
	@Operator(Operator.LITTLE_THAN)
	Long getMinBillId();

	void setMinBillId(Long minBillId);
}
