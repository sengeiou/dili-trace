package com.dili.trace.dto;

public class OrigionReportQueryDto {
    private String startDate;
    private String endDate;
    private String origionName;
    private String productName;
    private Long marketId;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrigionName() {
        return origionName;
    }

    public void setOrigionName(String origionName) {
        this.origionName = origionName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
}
