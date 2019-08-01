package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.dto.QualityTraceTradeBillRepeatDto;

public interface QualityTraceTradeBillMapper extends MyMapper<QualityTraceTradeBill> {
	
	public List<QualityTraceTradeBillRepeatDto>selectRepeatedOrderId();
}