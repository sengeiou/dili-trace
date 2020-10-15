package com.dili.trace.dto;

import java.math.BigDecimal;

/**
 * @author asa.lee
 */
public class BillSumReportDto {

    private String startDate;
    private String endDate;
    private String userName;
    private Integer billCount;
    private Integer varietyCount;
    private BigDecimal weight;
    private String phone;
    private String varietyName;
    private BigDecimal userWeight;
    private Long marketId;

    public BigDecimal getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(BigDecimal userWeight) {
        this.userWeight = userWeight;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBillCount() {
        return billCount;
    }

    public void setBillCount(Integer billCount) {
        this.billCount = billCount;
    }

    public Integer getVarietyCount() {
        return varietyCount;
    }

    public void setVarietyCount(Integer varietyCount) {
        this.varietyCount = varietyCount;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
}
