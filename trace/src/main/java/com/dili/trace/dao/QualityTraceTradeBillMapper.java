package com.dili.trace.dao;

import com.dili.sg.trace.api.output.QualityTraceTradeBillOutput;
import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.dto.QualityTraceTradeBillRepeatDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityTraceTradeBillMapper extends MyMapper<QualityTraceTradeBill> {

	public List<QualityTraceTradeBillRepeatDto> selectRepeatedOrderId();

	public QualityTraceTradeBill findByOrderId(@Param("orderId") String orderId);

	public List<QualityTraceTradeBillOutput> findQualityTraceTradeBillByTradeBillIdList(@Param("tradeBillIdList") List<Long> tradeBillIdList);
}