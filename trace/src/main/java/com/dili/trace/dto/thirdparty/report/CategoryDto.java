package com.dili.trace.dto.thirdparty.report;

import java.util.List;

public class CategoryDto{
    private String marketId = "330110800";
    private String thirdBigClassId;
    private String thirdBigClassName;

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

    public String getThirdBigClassName() {
        return thirdBigClassName;
    }

    public void setThirdBigClassName(String thirdBigClassName) {
        this.thirdBigClassName = thirdBigClassName;
    }
}
