package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.dto.GroupByProductReportDto;
import com.dili.trace.dto.MatchDetectParam;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillReportQueryDto;
import com.dili.trace.dto.RegisterBillStaticsDto;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RegisterBillMapper extends MyMapper<RegisterBill> {
	List<RegisterBill> findByExeMachineNo(String exeMachineNo);

	Long findMatchDetectBind(MatchDetectParam matchDetectParam);

	List<RegisterBill> findUnMatchedRegisterBill(MatchDetectParam matchDetectParam);

	int matchDetectBind(@Param("tradeNo") String tradeNo, @Param("weight") Long weight, @Param("id") Long id);

	int taskByExeMachineNo(@Param("exeMachineNo") String exeMachineNo, @Param("ids") String ids);

	List<Long> findIdsByExeMachineNo(int taskCount);

	RegisterBillStaticsDto groupByState(RegisterBillDto dto);

	int doRemoveReportAndCertifiy(RegisterBill registerBill);

	List<GroupByProductReportDto> listPageGroupByProduct(RegisterBillReportQueryDto dto);
	
	GroupByProductReportDto summaryGroup(RegisterBillReportQueryDto dto);

	Long listPageGroupByProductCount(RegisterBillReportQueryDto dto);
}