package com.dili.trace.service.impl;

import com.dili.trace.api.output.QualityTraceTradeBillOutput;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.QualityTraceTradeBillMapper;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.dto.QualityTraceTradeBillRepeatDto;
import com.dili.trace.service.QualityTraceTradeBillService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class QualityTraceTradeBillServiceImpl extends BaseServiceImpl<QualityTraceTradeBill, Long>
        implements QualityTraceTradeBillService {

    public QualityTraceTradeBillMapper getActualDao() {
        return (QualityTraceTradeBillMapper) getDao();
    }

    /**
     * 查询
     *
     * @return
     */
    @Override
    public QualityTraceTradeBill findByTradeNo(String tradeNo) {
        return getActualDao().findByOrderId(tradeNo);
    }

    /**
     * 查询
     *
     * @return
     */
    @Override
    public List<QualityTraceTradeBillRepeatDto> selectRepeatedOrderId() {
        return this.getActualDao().selectRepeatedOrderId();
    }

    /**
     * 查询
     *
     * @return
     */
    @Override
    public List<QualityTraceTradeBillOutput> findQualityTraceTradeBillByTradeBillIdList(List<Long> tradeBillIdList) {
        return this.getActualDao().findQualityTraceTradeBillByTradeBillIdList(tradeBillIdList);
    }
}