package com.dili.trace.api.input;

public class TradeRequestHandleDto extends TradeRequestListInput {

    private Long tradeRequestId;
    private Integer handleStatus;
    private String reason;

    @Override
    public Long getTradeRequestId() {
        return tradeRequestId;
    }

    public void setTradeRequestId(Long tradeRequestId) {
        this.tradeRequestId = tradeRequestId;
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

}