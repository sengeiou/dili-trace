package com.dili.trace.dto.thirdparty.report;

/**
 * @author asa.lee
 */
public class ReportRegisterBillDeleteDto {

    /**
     * 市场id
     */
    private String marketId;
    /**
     * 报备单id
     */
    private String thirdEnterIds;

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getThirdEnterIds() {
        return thirdEnterIds;
    }

    public void setThirdEnterIds(String thirdEnterIds) {
        this.thirdEnterIds = thirdEnterIds;
    }
}