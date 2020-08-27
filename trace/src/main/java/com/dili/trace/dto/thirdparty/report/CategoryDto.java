package com.dili.trace.dto.thirdparty.report;

import java.util.List;

public class CategoryDto{
    private String market = "1";
    private String thirdBigClassId;
    private String thirdBigClassName;

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
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
