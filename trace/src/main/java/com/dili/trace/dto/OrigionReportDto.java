package com.dili.trace.dto;

import java.math.BigDecimal;

public class OrigionReportDto {
    private String origionName;
    private Integer billCount;
    private BigDecimal weight;

    public String getOrigionName() {
        return origionName;
    }

    public void setOrigionName(String origionName) {
        this.origionName = origionName;
    }

    public Integer getBillCount() {
        return billCount;
    }

    public void setBillCount(Integer billCount) {
        this.billCount = billCount;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
