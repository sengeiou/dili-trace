package com.dili.trace.dto;

import java.util.Date;
import java.util.List;

public class TraceReportQueryDto {
    private Integer billType;
    private Integer registType;
    private Date createdStart;
    private Date createdEnd;
    private Boolean readonly;
    private List<Integer> greenBillVerifyStatus;
    private List<Integer> yellowBillVerifyStatus;
    private List<Integer> redBillVerifyStatus;
    private List<Integer> noneVerifyStatus;
    private Integer isUserActive;
    private Long marketId;

    public Integer getRegistType() {
        return registType;
    }

    public void setRegistType(Integer registType) {
        this.registType = registType;
    }

    public Integer getIsUserActive() {
        return isUserActive;
    }

    public void setIsUserActive(Integer isUserActive) {
        this.isUserActive = isUserActive;
    }

    /**
     * @return Date return the createdStart
     */
    public Date getCreatedStart() {
        return createdStart;
    }

    /**
     * @param createdStart the createdStart to set
     */
    public void setCreatedStart(Date createdStart) {
        this.createdStart = createdStart;
    }

    /**
     * @return Date return the createdEnd
     */
    public Date getCreatedEnd() {
        return createdEnd;
    }

    /**
     * @param createdEnd the createdEnd to set
     */
    public void setCreatedEnd(Date createdEnd) {
        this.createdEnd = createdEnd;
    }

    /**
     * @return List<Integer> return the greenBillVerifyStatus
     */
    public List<Integer> getGreenBillVerifyStatus() {
        return greenBillVerifyStatus;
    }

    /**
     * @param greenBillVerifyStatus the greenBillVerifyStatus to set
     */
    public void setGreenBillVerifyStatus(List<Integer> greenBillVerifyStatus) {
        this.greenBillVerifyStatus = greenBillVerifyStatus;
    }

    /**
     * @return List<Integer> return the yellowBillVerifyStatus
     */
    public List<Integer> getYellowBillVerifyStatus() {
        return yellowBillVerifyStatus;
    }

    /**
     * @param yellowBillVerifyStatus the yellowBillVerifyStatus to set
     */
    public void setYellowBillVerifyStatus(List<Integer> yellowBillVerifyStatus) {
        this.yellowBillVerifyStatus = yellowBillVerifyStatus;
    }

    /**
     * @return List<Integer> return the redBillVerifyStatus
     */
    public List<Integer> getRedBillVerifyStatus() {
        return redBillVerifyStatus;
    }

    /**
     * @param redBillVerifyStatus the redBillVerifyStatus to set
     */
    public void setRedBillVerifyStatus(List<Integer> redBillVerifyStatus) {
        this.redBillVerifyStatus = redBillVerifyStatus;
    }


    /**
     * @return List<Integer> return the noneVerifyStatus
     */
    public List<Integer> getNoneVerifyStatus() {
        return noneVerifyStatus;
    }

    /**
     * @param noneVerifyStatus the noneVerifyStatus to set
     */
    public void setNoneVerifyStatus(List<Integer> noneVerifyStatus) {
        this.noneVerifyStatus = noneVerifyStatus;
    }


    /**
     * @return Integer return the billType
     */
    public Integer getBillType() {
        return billType;
    }

    /**
     * @param billType the billType to set
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }


    /**
     * @return Boolean return the readonly
     */
    public Boolean getReadonly() {
        return readonly;
    }

    /**
     * @param readonly the readonly to set
     */
    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    /**
     * @return Long marketId the marketId
     */
    public Long getMarketId() {
        return marketId;
    }

    /**
     * @param marketId the marketId to set
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
}