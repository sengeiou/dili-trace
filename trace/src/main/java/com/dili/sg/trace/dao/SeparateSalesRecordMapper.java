package com.dili.sg.trace.dao;

import com.dili.sg.trace.domain.SeparateSalesRecord;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface SeparateSalesRecordMapper extends MyMapper<SeparateSalesRecord> {
    Integer alreadySeparateSalesWeight(@Param("registerBillCode") String registerBillCode);
    Integer getAlreadySeparateSalesWeightByTradeNo(@Param("tradeNo") String tradeNo);
}