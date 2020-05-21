package com.dili.trace.dto;

public class CheckinRecordInputDto {
    private Long billId;
    private Integer checkinStatus;
    

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
     * @return Integer return the checkinStatus
     */
    public Integer getCheckinStatus() {
        return checkinStatus;
    }

    /**
     * @param checkinStatus the checkinStatus to set
     */
    public void setCheckinStatus(Integer checkinStatus) {
        this.checkinStatus = checkinStatus;
    }

}