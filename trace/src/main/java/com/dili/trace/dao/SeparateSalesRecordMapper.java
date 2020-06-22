package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.output.SeparateSalesApiListOutput;
import com.dili.trace.api.output.SeparateSalesApiListQueryInput;
import com.dili.trace.domain.SeparateSalesRecord;
import org.apache.ibatis.annotations.Param;

public interface SeparateSalesRecordMapper extends MyMapper<SeparateSalesRecord> {
    Integer alreadySeparateSalesWeight(@Param("registerBillCode") String registerBillCode);

    Integer getAlreadySeparateSalesWeightByTradeNo(@Param("tradeNo") String tradeNo);

    List<SeparateSalesApiListOutput> listSeparateSalesOutput(SeparateSalesApiListQueryInput queryInput);

    Long countSeparateSalesOutput(SeparateSalesApiListQueryInput queryInput);

}