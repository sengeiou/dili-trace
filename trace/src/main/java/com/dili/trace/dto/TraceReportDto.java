package com.dili.trace.dto;

import java.math.BigDecimal;

public class TraceReportDto {
    private String area;
    private Integer userCount;
    private Integer billCount;
    private Integer tradeDetailBuyerCount;
    private BigDecimal percentage;

    private Integer greenBillCount;
    private Integer yellowBillCount;
    private Integer redBillCount;
    private Integer noVerifyedBillCount;

    /**
     * @return String return the area
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * @return Integer return the userCount
     */
    public Integer getUserCount() {
        return userCount;
    }

    /**
     * @param userCount the userCount to set
     */
    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    /**
     * @return Integer return the billCount
     */
    public Integer getBillCount() {
        return billCount;
    }

    /**
     * @param billCount the billCount to set
     */
    public void setBillCount(Integer billCount) {
        this.billCount = billCount;
    }

    /**
     * @return Integer return the tradeDetailBuyerCount
     */
    public Integer getTradeDetailBuyerCount() {
        return tradeDetailBuyerCount;
    }

    /**
     * @param tradeDetailBuyerCount the tradeDetailBuyerCount to set
     */
    public void setTradeDetailBuyerCount(Integer tradeDetailBuyerCount) {
        this.tradeDetailBuyerCount = tradeDetailBuyerCount;
    }

    /**
     * @return BigDecimal return the percentage
     */
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    /**
     * @return Integer return the greenBillCount
     */
    public Integer getGreenBillCount() {
        return greenBillCount;
    }

    /**
     * @param greenBillCount the greenBillCount to set
     */
    public void setGreenBillCount(Integer greenBillCount) {
        this.greenBillCount = greenBillCount;
    }

    /**
     * @return Integer return the yellowBillCount
     */
    public Integer getYellowBillCount() {
        return yellowBillCount;
    }

    /**
     * @param yellowBillCount the yellowBillCount to set
     */
    public void setYellowBillCount(Integer yellowBillCount) {
        this.yellowBillCount = yellowBillCount;
    }

    /**
     * @return Integer return the redBillCount
     */
    public Integer getRedBillCount() {
        return redBillCount;
    }

    /**
     * @param redBillCount the redBillCount to set
     */
    public void setRedBillCount(Integer redBillCount) {
        this.redBillCount = redBillCount;
    }

    /**
     * @return Integer return the noVerifyedBillCount
     */
    public Integer getNoVerifyedBillCount() {
        return noVerifyedBillCount;
    }

    /**
     * @param noVerifyedBillCount the noVerifyedBillCount to set
     */
    public void setNoVerifyedBillCount(Integer noVerifyedBillCount) {
        this.noVerifyedBillCount = noVerifyedBillCount;
    }

}