package com.dili.trace.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`user`")
public interface User extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`user_name`")
    @FieldDef(label="userName", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getUserName();

    void setUserName(String userName);

    @Column(name = "`phone`")
    @FieldDef(label="phone", maxLength = 15)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPhone();

    void setPhone(String phone);

    @Column(name = "`card_no`")
    @FieldDef(label="cardNo", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardNo();

    void setCardNo(String cardNo);

    @Column(name = "`addr`")
    @FieldDef(label="addr", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddr();

    void setAddr(String addr);

    @Column(name = "`card_no_url`")
    @FieldDef(label="cardNoUrl", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardNoUrl();

    void setCardNoUrl(String cardNoUrl);

    @Column(name = "`tailly_area_no`")
    @FieldDef(label="taillyAreaNo", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTaillyAreaNo();

    void setTaillyAreaNo(String taillyAreaNo);

    @Column(name = "`business_license_url`")
    @FieldDef(label="businessLicenseUrl", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getBusinessLicenseUrl();

    void setBusinessLicenseUrl(String businessLicenseUrl);

    @Column(name = "`sales_city_id`")
    @FieldDef(label="salesCityId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSalesCityId();

    void setSalesCityId(Long salesCityId);

    @Column(name = "`sales_city_name`")
    @FieldDef(label="salesCityName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSalesCityName();

    void setSalesCityName(String salesCityName);

    @Column(name = "`state`")
    @FieldDef(label="1:启用 2：禁用")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getState();

    void setState(Boolean state);

    @Column(name = "`pwd`")
    @FieldDef(label="pwd", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPwd();

    void setPwd(String pwd);

    @Column(name = "`created`")
    @FieldDef(label="created")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`modified`")
    @FieldDef(label="modified")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);
}