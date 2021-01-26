package com.dili.trace.dto;

import com.dili.trace.domain.TradeRequest;

import java.util.List;

public class TradeRequestWrapperDto {
    private TradeRequest tradeRequest;
    private List<TradeDetailInputWrapperDto> tradeDetailWrapperDtoList;

    public TradeRequest getTradeRequest() {
        return tradeRequest;
    }

    public void setTradeRequest(TradeRequest tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    public List<TradeDetailInputWrapperDto> getTradeDetailWrapperDtoList() {
        return tradeDetailWrapperDtoList;
    }

    public void setTradeDetailWrapperDtoList(List<TradeDetailInputWrapperDto> tradeDetailWrapperDtoList) {
        this.tradeDetailWrapperDtoList = tradeDetailWrapperDtoList;
    }
}
