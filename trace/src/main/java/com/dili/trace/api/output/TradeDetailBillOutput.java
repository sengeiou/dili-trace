package com.dili.trace.api.output;

import java.math.BigDecimal;
import java.util.Date;


public class TradeDetailBillOutput {

    private Long billId;
    private Long tradeDetailId;
    private Integer verifyStatus;
    private BigDecimal stockWeight;
    private Integer weightUnit;
    private Integer tradeType;
    private Date created;
    private String productName;


    /**
     * @return Long return the billId
     */
    public Long getBillId() {
        return billId;
    }

    /**
     * @param billId the billId to set
     */
    public void setBillId(Long billId) {
        this.billId = billId;
    }

    /**
     * @return Long return the tradeDetailId
     */
    public Long getTradeDetailId() {
        return tradeDetailId;
    }

    /**
     * @param tradeDetailId the tradeDetailId to set
     */
    public void setTradeDetailId(Long tradeDetailId) {
        this.tradeDetailId = tradeDetailId;
    }

    /**
     * @return Integer return the verifyStatus
     */
    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    /**
     * @param verifyStatus the verifyStatus to set
     */
    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }


    /**
     * @return BigDecimal return the stockWeight
     */
    public BigDecimal getStockWeight() {
        return stockWeight;
    }

    /**
     * @param stockWeight the stockWeight to set
     */
    public void setStockWeight(BigDecimal stockWeight) {
        this.stockWeight = stockWeight;
    }

    /**
     * @return Integer return the weightUnit
     */
    public Integer getWeightUnit() {
        return weightUnit;
    }

    /**
     * @param weightUnit the weightUnit to set
     */
    public void setWeightUnit(Integer weightUnit) {
        this.weightUnit = weightUnit;
    }


    /**
     * @return Integer return the tradeType
     */
    public Integer getTradeType() {
        return tradeType;
    }

    /**
     * @param tradeType the tradeType to set
     */
    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }


    /**
     * @return Date return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }


    /**
     * @return String return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

}