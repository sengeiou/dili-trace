package com.dili.sg.trace.dao;

import java.util.List;

import com.dili.sg.trace.domain.RegisterBill;
import com.dili.ss.base.MyMapper;
import com.dili.sg.trace.dto.GroupByProductReportDto;
import com.dili.sg.trace.dto.MatchDetectParam;
import com.dili.sg.trace.dto.RegisterBillDto;
import com.dili.sg.trace.dto.RegisterBillReportQueryDto;
import com.dili.sg.trace.dto.RegisterBillStaticsDto;

import org.apache.ibatis.annotations.Param;

public interface RegisterBillMapper extends MyMapper<RegisterBill> {
//	 List<RegisterBill> findByExeMachineNo(String exeMachineNo);

//	Long findMatchDetectBind(MatchDetectParam matchDetectParam);

	List<RegisterBill> findUnMatchedRegisterBill(MatchDetectParam matchDetectParam);

//	int matchDetectBind(@Param("tradeNo") String tradeNo, @Param("weight") Long weight, @Param("id") Long id);

//	int taskByExeMachineNo(@Param("exeMachineNo") String exeMachineNo, @Param("ids") String ids);
	
	int taskByExeMachineNo2(@Param("exeMachineNo") String exeMachineNo, @Param("taskCount") int taskCount);

//  List<Long> findIdsByExeMachineNo(int taskCount);

	RegisterBillStaticsDto groupByState(RegisterBillDto dto);

	int doRemoveReportAndCertifiy(RegisterBill registerBill);

	List<GroupByProductReportDto> listPageGroupByProduct(RegisterBillReportQueryDto dto);
	
	GroupByProductReportDto summaryGroup(RegisterBillReportQueryDto dto);

	Long listPageGroupByProductCount(RegisterBillReportQueryDto dto);

	RegisterBill selectByIdForUpdate(Long id);
}