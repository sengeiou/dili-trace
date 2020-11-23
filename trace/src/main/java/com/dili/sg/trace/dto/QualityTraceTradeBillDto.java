package com.dili.sg.trace.dto;

import java.util.List;

import javax.persistence.Column;

import com.dili.sg.trace.domain.QualityTraceTradeBill;
import com.dili.ss.domain.annotation.Operator;

public interface QualityTraceTradeBillDto extends QualityTraceTradeBill {
	@Column(name = "`match_status`")
	@Operator(Operator.IN)
	List<Integer> getMatchStatusList();

	void setMatchStatusList(List<Integer> matchStatusList);

}
