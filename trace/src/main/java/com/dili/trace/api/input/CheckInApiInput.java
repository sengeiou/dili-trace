package com.dili.trace.api.input;

import java.util.List;

public class CheckInApiInput {
    /**
     * 状态 {@link com.dili.trace.glossary.CheckinStatusEnum}
     */
    private Integer checkinStatus;
    /**
     * 登记单ID集合
     */
    private List<Long> billIdList;

    /**
     * 备注
     */
    private String remark;

    /**
     * @return List<Long> return the billIdList
     */
    public List<Long> getBillIdList() {
        return billIdList;
    }

    /**
     * @param billIdList the billIdList to set
     */
    public void setBillIdList(List<Long> billIdList) {
        this.billIdList = billIdList;
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


    /**
     * @return String return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

}