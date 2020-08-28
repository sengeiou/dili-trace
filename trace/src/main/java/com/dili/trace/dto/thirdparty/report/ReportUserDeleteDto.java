package com.dili.trace.dto.thirdparty.report;

public class ReportUserDeleteDto implements ReportDto {
    private String marketId;
    private String thirdAccIds;

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getThirdAccIds() {
        return thirdAccIds;
    }

    public void setThirdAccIds(String thirdAccIds) {
        this.thirdAccIds = thirdAccIds;
    }
}
