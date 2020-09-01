package com.dili.trace.dto.thirdparty.report;

/**
 * @author asa.lee
 */
public class ReportUserDeleteDto{
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
