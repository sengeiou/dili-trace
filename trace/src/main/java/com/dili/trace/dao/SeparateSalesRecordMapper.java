package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.SeparateSalesRecord;
import org.apache.ibatis.annotations.Param;

public interface SeparateSalesRecordMapper extends MyMapper<SeparateSalesRecord> {
    Integer alreadySeparateSalesWeight(@Param("registerBillCode") String registerBillCode);
}