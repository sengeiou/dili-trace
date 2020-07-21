package com.dili.trace.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TraceReportDto {
    private String groupKey;
    private Integer userCount;
    private Integer billCount;
    private Integer tradeDetailBuyerCount;
    private BigDecimal percentage;

    private Integer greenBillCount;
    private Integer yellowBillCount;
    private Integer redBillCount;
    private Integer noVerifyedBillCount;
    public BigDecimal calculatePercentage(){
        if(this.userCount==null){
            return this.percentage;
        }
        BigDecimal uc=  new BigDecimal(this.userCount);
        if(BigDecimal.ZERO.compareTo(uc)!=0){
          this.percentage=  new BigDecimal(this.billCount).add( new BigDecimal(this.tradeDetailBuyerCount)).divide(uc,2,RoundingMode.HALF_UP);
        }
        return this.percentage;
    }
    public TraceReportDto sum(TraceReportDto dto){
        if(this.billCount==null){
            this.billCount=0;
        }
        if(this.tradeDetailBuyerCount==null){
            this.tradeDetailBuyerCount=0;
        }
        if(this.greenBillCount==null){
            this.greenBillCount=0;
        }

        if(this.yellowBillCount==null){
            this.yellowBillCount=0;
        }
        if(this.redBillCount==null){
            this.redBillCount=0;
        }
        if(this.noVerifyedBillCount==null){
            this.noVerifyedBillCount=0;
        }


        if(dto.getBillCount()!=null){
            this.setBillCount(this.getBillCount()+dto.getBillCount());
        }
        if(dto.getTradeDetailBuyerCount()!=null){
            this.setTradeDetailBuyerCount(this.getTradeDetailBuyerCount()+  dto.getTradeDetailBuyerCount());
        }
        if(dto.getGreenBillCount()!=null){
            this.setGreenBillCount(this.getGreenBillCount()+dto.getGreenBillCount());
        }

        if(dto.getYellowBillCount()!=null){
            this.setYellowBillCount(this.getYellowBillCount()+dto.getYellowBillCount());
        }
        if(dto.getRedBillCount()!=null){
            this.setRedBillCount(this.getRedBillCount()+ dto.getRedBillCount());
        }
        if(dto.getNoVerifyedBillCount()!=null){
            this.setNoVerifyedBillCount(this.getNoVerifyedBillCount()+  dto.getNoVerifyedBillCount());
        }

        return this;
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


    /**
     * @return String return the groupKey
     */
    public String getGroupKey() {
        return groupKey;
    }

    /**
     * @param groupKey the groupKey to set
     */
    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

}