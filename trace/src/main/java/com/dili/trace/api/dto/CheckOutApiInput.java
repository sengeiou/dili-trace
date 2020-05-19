package com.dili.trace.api.dto;

import java.util.List;

public class CheckOutApiInput {
    /**
     * 状态 {@link com.dili.trace.glossary.CheckinStatusEnum}
     */
    private Integer checkoutStatus;
    /**
     * 登记单ID集合
     */
    private List<Long> separateSalesIdList;

    /**
     * 备注
     */
    private String remark;


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


    /**
     * @return List<Long> return the separateSalesIdList
     */
    public List<Long> getSeparateSalesIdList() {
        return separateSalesIdList;
    }

    /**
     * @param separateSalesIdList the separateSalesIdList to set
     */
    public void setSeparateSalesIdList(List<Long> separateSalesIdList) {
        this.separateSalesIdList = separateSalesIdList;
    }


    /**
     * @return Integer return the checkoutStatus
     */
    public Integer getCheckoutStatus() {
        return checkoutStatus;
    }

    /**
     * @param checkoutStatus the checkoutStatus to set
     */
    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

}