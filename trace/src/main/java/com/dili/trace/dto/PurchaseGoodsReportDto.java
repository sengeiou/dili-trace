package com.dili.trace.dto;

import java.math.BigDecimal;

public class PurchaseGoodsReportDto {
    private String productName;
    private BigDecimal weight;
    private String userName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
