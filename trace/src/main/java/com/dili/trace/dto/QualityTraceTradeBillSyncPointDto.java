package com.dili.trace.dto;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.QualityTraceTradeBillSyncPoint;

public interface QualityTraceTradeBillSyncPointDto extends QualityTraceTradeBillSyncPoint {
	@Column(name = "`bill_id`")
	@Operator(Operator.LITTLE_THAN)
	Long getMinBillId();

	void setMinBillId(Long minBillId);
}
