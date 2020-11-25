package com.dili.trace.service;

import com.dili.trace.api.output.QualityTraceTradeBillOutput;
import com.dili.ss.base.BaseService;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.dto.QualityTraceTradeBillRepeatDto;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface QualityTraceTradeBillService extends BaseService<QualityTraceTradeBill, Long> {
    /**
     * 通过交易号查询交易区SQLServer数据
     *
     * @param tradeNo
     * @return
     */
    public QualityTraceTradeBill findByTradeNo(String tradeNo);

    /**
     * 查询
     *
     * @return
     */
    public List<QualityTraceTradeBillRepeatDto> selectRepeatedOrderId();

    /**
     * 查询
     *
     * @return
     */
    public List<QualityTraceTradeBillOutput> findQualityTraceTradeBillByTradeBillIdList(List<Long> tradeBillIdList);
}