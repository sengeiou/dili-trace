package com.dili.trace.api.input;

public class TradeRequestInputDto extends TradeRequestListInput {

    private Long tradeRequestId;
    private Long traderOrderId;
    private String reason;

    /**
     * @return Long return the tradeRequestId
     */
    public Long getTradeRequestId() {
        return tradeRequestId;
    }

    /**
     * @param tradeRequestId the tradeRequestId to set
     */
    public void setTradeRequestId(Long tradeRequestId) {
        this.tradeRequestId = tradeRequestId;
    }


    /**
     * @return String return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }


    /**
     * @return Long return the traderOrderId
     */
    public Long getTraderOrderId() {
        return traderOrderId;
    }

    /**
     * @param traderOrderId the traderOrderId to set
     */
    public void setTraderOrderId(Long traderOrderId) {
        this.traderOrderId = traderOrderId;
    }

}