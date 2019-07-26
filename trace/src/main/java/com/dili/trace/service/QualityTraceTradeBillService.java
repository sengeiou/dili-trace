package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.QualityTraceTradeBill;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
public interface QualityTraceTradeBillService extends BaseService<QualityTraceTradeBill, Long> {
    //通过交易号查询交易区SQLServer数据
    public QualityTraceTradeBill findByTradeNo(String tradeNo);
}