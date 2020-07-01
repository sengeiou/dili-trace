package com.dili.trace.service;

import java.util.List;
import java.util.stream.Stream;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.api.input.TradeRequestInputDto;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;

import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;

@Service
public class TradeRequestService extends BaseServiceImpl<TradeRequest, Long> {

    /**
     * 创建购买请求
     * 
     * @param request
     * @return
     */
    public Long createBuyRequest(TradeRequest request) {

        this.insertSelective(request);

        return request.getId();

    }

    /**
     * 创建销售请求
     */
    public Long createSellRequest(TradeRequest request, List<TradeRequestInputDto> tradeRequestInputDtoList) {
        if (tradeRequestInputDtoList == null || tradeRequestInputDtoList.isEmpty()) {
             StreamEx.of(tradeRequestInputDtoList).nonNull().map(tr -> {
                TradeDetail detail = new TradeDetail();
                detail.setId(tr.getTradeDetailId());
                //detail.setTotalWeight(tr.getTradeWeight());
                detail.setSellerId(request.getSellerId());
                detail.setBuyerId(request.getBuyerId());
                return detail;
    
            }).toList();
        } else {

        }
        return null;
    }

    /**
     * 处理购买请求
     * 
     * @return
     */
    public Long handleBuyRequest() {

        return null;
    }
}