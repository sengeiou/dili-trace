package com.dili.trace.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-31 14:56:14.
 */
@Table(name = "`vCustomer`")
public interface Customer extends IBaseDomain {


    void setId(Long id);
    @ApiModelProperty(value = "交易账号")
    @Column(name = "`customerID`")
    @FieldDef(label="customerId")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerId();

    void setCustomerId(String customerId);
    @ApiModelProperty(value = "客户姓名")
    @Column(name = "`name`")
    @FieldDef(label="name", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);
    @ApiModelProperty(value = "客户身份证")
    @Column(name = "`IDNo`")
    @FieldDef(label="idNo", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIdNo();

    void setIdNo(String idNo);
    @ApiModelProperty(value = "客户地址")
    @Column(name = "`address`")
    @FieldDef(label="address", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddress();

    void setAddress(String address);
    @ApiModelProperty(value = "印刷卡号")
    @Column(name = "`printingCode`")
    @FieldDef(label="printingCard", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPrintingCard();

    void setPrintingCard(String printingCard);

    @Column(name = "`active`")
    @FieldDef(label="0:作废，1：活跃")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getActive();

    void setActive(Integer active);
}