package com.dili.trace.api.output;

import java.util.List;

import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;

public class TradeRequestOutputDto {
    private TradeRequest tradeRequest;
    private List<TradeDetail> tradeDetailList;


    /**
     * @return TradeRequest return the tradeRequest
     */
    public TradeRequest getTradeRequest() {
        return tradeRequest;
    }

    /**
     * @param tradeRequest the tradeRequest to set
     */
    public void setTradeRequest(TradeRequest tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    /**
     * @return List<TradeDetail> return the tradeDetailList
     */
    public List<TradeDetail> getTradeDetailList() {
        return tradeDetailList;
    }

    /**
     * @param tradeDetailList the tradeDetailList to set
     */
    public void setTradeDetailList(List<TradeDetail> tradeDetailList) {
        this.tradeDetailList = tradeDetailList;
    }

}