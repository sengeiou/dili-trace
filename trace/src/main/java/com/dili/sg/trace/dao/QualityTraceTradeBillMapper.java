package com.dili.sg.trace.dao;

import java.util.List;

import com.dili.sg.trace.api.output.QualityTraceTradeBillOutput;
import com.dili.sg.trace.domain.QualityTraceTradeBill;
import com.dili.sg.trace.dto.QualityTraceTradeBillRepeatDto;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface QualityTraceTradeBillMapper extends MyMapper<QualityTraceTradeBill> {

	public List<QualityTraceTradeBillRepeatDto> selectRepeatedOrderId();

	public QualityTraceTradeBill findByOrderId(@Param("orderId") String orderId);

	public List<QualityTraceTradeBillOutput> findQualityTraceTradeBillByTradeBillIdList(@Param("tradeBillIdList")List<Long> tradeBillIdList);
}