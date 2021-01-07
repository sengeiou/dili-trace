package com.dili.trace.dto.query;

import com.dili.trace.domain.UserDriverRef;

import javax.persistence.Transient;

public class UserDriverRefQueryDto extends UserDriverRef {

    @Transient
    private Integer isCheckIn;
    @Transient
    private Integer billType;

    @Transient
    private Integer isDelete;

    @Transient
    private Integer verifyStatus;

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Integer getIsCheckIn() {
        return isCheckIn;
    }

    public void setIsCheckIn(Integer isCheckIn) {
        this.isCheckIn = isCheckIn;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
