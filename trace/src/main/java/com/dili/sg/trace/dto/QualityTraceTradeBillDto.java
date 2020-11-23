package com.dili.trace.dto;

import java.util.List;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.QualityTraceTradeBill;

public interface QualityTraceTradeBillDto extends QualityTraceTradeBill {
	@Column(name = "`match_status`")
	@Operator(Operator.IN)
	List<Integer> getMatchStatusList();

	void setMatchStatusList(List<Integer> matchStatusList);

}
