package com.dili.trace.domain.sg;

import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import java.util.List;

public interface QualityTraceTradeBillDto extends QualityTraceTradeBill {
	@Column(name = "`match_status`")
	@Operator(Operator.IN)
	List<Integer> getMatchStatusList();

	void setMatchStatusList(List<Integer> matchStatusList);

}
