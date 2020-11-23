package com.dili.sg.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.sg.trace.domain.SeparateSalesRecord;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
public interface SeparateSalesRecordService extends BaseService<SeparateSalesRecord, Long> {
    List<SeparateSalesRecord> findByRegisterBillCode(String registerBillCode);
    Integer alreadySeparateSalesWeight(String registerBillCode);
    
    Integer getAlreadySeparateSalesWeightByTradeNo(String tradeNo);
}