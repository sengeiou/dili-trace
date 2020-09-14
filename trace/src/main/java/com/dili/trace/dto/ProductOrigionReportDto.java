package com.dili.trace.dto;

import java.math.BigDecimal;

public class ProductOrigionReportDto {
    private String origionName;
    private String productName;
    private BigDecimal weight;
    private BigDecimal totalWeight;

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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }
}
