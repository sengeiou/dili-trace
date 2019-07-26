package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.dto.MatchDetectParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RegisterBillMapper extends MyMapper<RegisterBill> {
    List<RegisterBill> findByExeMachineNo(@Param("exeMachineNo") String exeMachineNo,@Param("pageSize") int pageSize);
    List<RegisterBill> matchDetectBind(MatchDetectParam matchDetectParam);
}