package com.dili.trace.dto.query;

import com.dili.trace.domain.UserDriverRef;

import javax.persistence.Transient;

public class UserDriverRefQueryDto extends UserDriverRef {

    @Transient
    private Integer checkinStatus;
    @Transient
    private Integer billType;

    @Transient
    private Integer isDelete;

    @Transient
    private Integer verifyStatus;


    @Transient
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Integer getCheckinStatus() {
        return checkinStatus;
    }

    public void setCheckinStatus(Integer checkinStatus) {
        this.checkinStatus = checkinStatus;
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
