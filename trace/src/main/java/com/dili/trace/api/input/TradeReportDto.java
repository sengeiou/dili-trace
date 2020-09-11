package com.dili.trace.api.input;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author asa.lee
 */
public class TradeReportDto {
    private String billRatioStr;
    private String reportDateStr;
    private BigDecimal billRatio;
    private Integer billCount;
    private Date reportDate;
    private BigDecimal tradeRatio;
    private Integer tradeCount;
    private String createdStart;
    private String createdEnd;
    private String userIds;

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public Integer getBillCount() {
        return billCount;
    }

    public void setBillCount(Integer billCount) {
        this.billCount = billCount;
    }

    public Integer getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getCreatedStart() {
        return createdStart;
    }

    public void setCreatedStart(String createdStart) {
        this.createdStart = createdStart;
    }

    public String getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(String createdEnd) {
        this.createdEnd = createdEnd;
    }

    public String getBillRatioStr() {
        return billRatioStr;
    }

    public void setBillRatioStr(String billRatioStr) {
        this.billRatioStr = billRatioStr;
    }

    public String getReportDateStr() {
        return reportDateStr;
    }

    public void setReportDateStr(String reportDateStr) {
        this.reportDateStr = reportDateStr;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public BigDecimal getBillRatio() {
        return billRatio;
    }

    public void setBillRatio(BigDecimal billRatio) {
        this.billRatio = billRatio;
    }

    public BigDecimal getTradeRatio() {
        return tradeRatio;
    }

    public void setTradeRatio(BigDecimal tradeRatio) {
        this.tradeRatio = tradeRatio;
    }
}
