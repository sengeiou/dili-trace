package com.dili.trace.api.input;

/**
 * 处理购买请求实体
 */
public class TradeRequestHandleDto extends TradeRequestListInput {

    /**
     * 交易请求ID
     */
    private Long tradeRequestId;

    /**
     * 处理结果
     */
    private Integer handleStatus;

    /**
     * 原因
     */
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