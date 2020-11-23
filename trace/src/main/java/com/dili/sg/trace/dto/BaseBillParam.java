package com.dili.trace.dto;

import java.io.Serializable;

/**
 * Created by laikui on 2019/7/26.
 */
public class BaseBillParam implements Serializable{
    private Integer registerSource;
    private String tallyAreaNo;
    private String plate;
    private String name;
    private String addr;
    private String idCardNo;
    private Long userId;

    public Integer getRegisterSource() {
        return registerSource;
    }

    public void setRegisterSource(Integer registerSource) {
        this.registerSource = registerSource;
    }

    public String getTallyAreaNo() {
        return tallyAreaNo;
    }

    public void setTallyAreaNo(String tallyAreaNo) {
        this.tallyAreaNo = tallyAreaNo;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "BaseBillParam{" +
                "registerSource=" + registerSource +
                ", tallyAreaNo='" + tallyAreaNo + '\'' +
                ", plate='" + plate + '\'' +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", userId=" + userId +
                '}';
    }
}
