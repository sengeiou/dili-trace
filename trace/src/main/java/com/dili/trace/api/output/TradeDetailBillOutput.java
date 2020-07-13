package com.dili.trace.api.output;

import java.math.BigDecimal;
import java.util.Date;

import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.TruckTypeEnum;

public class TradeDetailBillOutput {

    private Long billId;
    private Long tradeDetailId;
    private Integer verifyStatus;
    private BigDecimal stockWeight;
    private BigDecimal totalWeight;

    private Integer weightUnit;
    private Integer tradeType;
    private Date created;
    private String productName;
    private Integer isCheckin;
    private Integer truckType;
    private Integer billType;

    public String getBillTypeName() {
        return BillTypeEnum.fromCode(this.getBillType()).map(BillTypeEnum::getName).orElse("");
    }

    public String getTruckTypeName() {
        return TruckTypeEnum.fromCode(this.getTruckType()).map(TruckTypeEnum::getName).orElse("");
    }

    public Integer getIsCheckin() {
        return isCheckin;
    }

    public void setIsCheckin(Integer isCheckin) {
        this.isCheckin = isCheckin;
    }

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

    /**
     * @return BigDecimal return the totalWeight
     */
    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    /**
     * @param totalWeight the totalWeight to set
     */
    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }




    /**
     * @param truckType the truckType to set
     */
    public void setTruckType(Integer truckType) {
        this.truckType = truckType;
    }

    /**
     * @param billType the billType to set
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    /**
     * @return the truckType
     */
    public Integer getTruckType() {
        return truckType;
    }

    /**
     * @return the billType
     */
    public Integer getBillType() {
        return billType;
    }

}