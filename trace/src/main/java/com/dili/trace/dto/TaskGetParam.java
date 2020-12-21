package com.dili.trace.dto;

public class TaskGetParam {
    private String exeMachineNo;
    private Long marketId;
    private int pageSize;

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getExeMachineNo() {
        return exeMachineNo;
    }

    public void setExeMachineNo(String exeMachineNo) {
        this.exeMachineNo = exeMachineNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
