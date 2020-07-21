package com.dili.trace.dto;

import java.util.Date;
import java.util.List;

public class TraceReportQueryDto {
    private Date createdStart;
    private Date createdEnd;
    private List<Integer> greenBillVerifyStatus;
    private List<Integer> yellowBillVerifyStatus;
    private List<Integer> redBillVerifyStatus;
    private List<Integer> noneVerifyStatus;

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

}