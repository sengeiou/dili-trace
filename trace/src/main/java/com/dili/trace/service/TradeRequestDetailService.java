package com.dili.trace.service;

import com.dili.trace.domain.TradeRequestDetail;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 不知道
 */
@Service
public class TradeRequestDetailService extends TraceBaseService<TradeRequestDetail, Long> {
    /**
     * 根据traderequestid查询
     *
     * @param tradeRequestIdList
     * @return
     */
    public List<TradeRequestDetail> findByTradeRequestIdList(List<Long> tradeRequestIdList) {
        if (tradeRequestIdList == null || tradeRequestIdList.isEmpty()) {
            return Lists.newArrayList();
        }
        Example example = new Example(TradeRequestDetail.class);
        example.and().andIn("tradeRequestId", tradeRequestIdList);
        return this.getDao().selectByExample(example);
    }
}
