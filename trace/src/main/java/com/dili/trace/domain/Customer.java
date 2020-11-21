package com.dili.trace.domain;

import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.util.MaskUserInfo;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * <p>
 * This file was generated on 2019-07-31 14:56:14.
 */
public class Customer {

    private Long id;

    @ApiModelProperty(value = "交易账号")
    private String customerId;

    @ApiModelProperty(value = "客户姓名")
    private String name;

    @ApiModelProperty(value = "客户身份证")
    private String idNo;
    @ApiModelProperty(value = "客户地址")
    private String address;

    @ApiModelProperty(value = "业户手机号")
    private String phone;

    @ApiModelProperty(value = "印刷卡号(客户账号)")
    private String printingCard;


    @ApiModelProperty(value = "作废状态")
//    @FieldDef(label="0:作废，1：活跃")
    private Integer active;
    public static Customer convert(CustomerExtendDto dto){
        Customer c = new Customer();
        c.setAddress(dto.getCertificateAddr());
        c.setCustomerId(String.valueOf(dto.getId()));
        c.setIdNo(dto.getCertificateNumber());
        c.setName(dto.getName());
        c.setPhone(dto.getContactsPhone());
        return c;

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrintingCard() {
        return printingCard;
    }

    public void setPrintingCard(String printingCard) {
        this.printingCard = printingCard;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}