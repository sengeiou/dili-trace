package com.dili.trace.dto.thirdparty.report;

public class ReportDeletedOrderDto {

    private String marketId;// 市场id
    private String thirdOrderIds;// 交易id,多个逗号分隔

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getThirdOrderIds() {
        return thirdOrderIds;
    }

    public void setThirdOrderIds(String thirdOrderIds) {
        this.thirdOrderIds = thirdOrderIds;
    }
}