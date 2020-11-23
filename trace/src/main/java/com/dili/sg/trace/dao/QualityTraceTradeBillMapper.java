package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.output.QualityTraceTradeBillOutput;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.dto.QualityTraceTradeBillRepeatDto;
import org.apache.ibatis.annotations.Param;

public interface QualityTraceTradeBillMapper extends MyMapper<QualityTraceTradeBill> {

	public List<QualityTraceTradeBillRepeatDto> selectRepeatedOrderId();

	public QualityTraceTradeBill findByOrderId(@Param("orderId") String orderId);

	public List<QualityTraceTradeBillOutput> findQualityTraceTradeBillByTradeBillIdList(@Param("tradeBillIdList")List<Long> tradeBillIdList);
}