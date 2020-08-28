package com.dili.trace.dto.thirdparty.report;

public class CategorySecondDto {
    private String marketId = "330110800";
    private String smallClassName;
    private String thirdBigClassId;
    private String thirdSmallClassId;

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getThirdBigClassId() {
        return thirdBigClassId;
    }

    public void setThirdBigClassId(String thirdBigClassId) {
        this.thirdBigClassId = thirdBigClassId;
    }

    public String getSmallClassName() {
        return smallClassName;
    }

    public void setSmallClassName(String smallClassName) {
        this.smallClassName = smallClassName;
    }

    public String getThirdSmallClassId() {
        return thirdSmallClassId;
    }

    public void setThirdSmallClassId(String thirdSmallClassId) {
        this.thirdSmallClassId = thirdSmallClassId;
    }
}
