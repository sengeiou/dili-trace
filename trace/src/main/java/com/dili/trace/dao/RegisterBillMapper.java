package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.dto.MatchDetectParam;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillStaticsDto;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RegisterBillMapper extends MyMapper<RegisterBill> {
	List<RegisterBill> findByExeMachineNo(String exeMachineNo);

	Long findMatchDetectBind(MatchDetectParam matchDetectParam);

	int matchDetectBind(@Param("tradeNo") String tradeNo, @Param("id") Long id);

	int taskByExeMachineNo(@Param("exeMachineNo") String exeMachineNo, @Param("ids") String ids);

	List<Long> findIdsByExeMachineNo(int taskCount);

	RegisterBillStaticsDto groupByState(RegisterBillDto dto);
}