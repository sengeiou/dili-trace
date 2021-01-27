package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.QualityTraceTradeBill;

import javax.persistence.Column;
import java.util.List;

public interface QualityTraceTradeBillDto extends QualityTraceTradeBill {
	@Column(name = "`match_status`")
	@Operator(Operator.IN)
	List<Integer> getMatchStatusList();

	void setMatchStatusList(List<Integer> matchStatusList);

}
