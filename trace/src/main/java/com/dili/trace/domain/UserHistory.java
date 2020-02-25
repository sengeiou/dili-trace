package com.dili.trace.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import io.swagger.annotations.ApiModelProperty;

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
@Table(name = "`user_history`")
public interface UserHistory extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);
    
    @ApiModelProperty(value = "用户ID")
    @Column(name = "`user_id`")
    @FieldDef(label="user_id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getUserId();

    void setUserId(Long userId);

    @ApiModelProperty(value = "名称")
    @Column(name = "`name`")
    @FieldDef(label="name", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @ApiModelProperty(value = "手机号（帐号）")
    @Column(name = "`phone`")
    @FieldDef(label="phone", maxLength = 15)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPhone();

    void setPhone(String phone);

    @ApiModelProperty(value = "理货区号")
    @Column(name = "`tally_area_nos`")
    @FieldDef(label="tallyAreaNos", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTallyAreaNos();

    void setTallyAreaNos(String tallyAreaNos);

    @ApiModelProperty(value = "身份证号")
    @Column(name = "`card_no`")
    @FieldDef(label="cardNo", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardNo();

    void setCardNo(String cardNo);

    @ApiModelProperty(value = "地址")
    @Column(name = "`addr`")
    @FieldDef(label="addr", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddr();

    void setAddr(String addr);

    @ApiModelProperty(value = "身份证照正面URL")
    @Column(name = "`card_no_front_url`")
    @FieldDef(label="cardNoFrontUrl", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardNoFrontUrl();

    void setCardNoFrontUrl(String cardNoFrontUrl);

    @ApiModelProperty(value = "身份证照反面URL")
    @Column(name = "`card_no_back_url`")
    @FieldDef(label="cardNoBackUrl", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardNoBackUrl();

    void setCardNoBackUrl(String cardNoBackUrl);

    @ApiModelProperty(value = "营业执照URL")
    @Column(name = "`business_license_url`")
    @FieldDef(label="businessLicenseUrl", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getBusinessLicenseUrl();

    void setBusinessLicenseUrl(String businessLicenseUrl);

    @ApiModelProperty(value = "销售城市ID")
    @Column(name = "`sales_city_id`")
    @FieldDef(label="salesCityId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSalesCityId();

    void setSalesCityId(Long salesCityId);

    @ApiModelProperty(value = "销售城市名称")
    @Column(name = "`sales_city_name`")
    @FieldDef(label="salesCityName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSalesCityName();

    void setSalesCityName(String salesCityName);

    @ApiModelProperty(value = "状态")
    @Column(name = "`state`")
    @FieldDef(label="1:启用 2：禁用")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getState();

    void setState(Integer state);

    @ApiModelProperty(value = "密码")
    @Column(name = "`password`")
    @FieldDef(label="password", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPassword();

    void setPassword(String password);

    @Column(name = "`yn`")
    @FieldDef(label="1:正常 -1：删除")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getYn();

    void setYn(Integer yn);

    @Column(name = "`version`")
    @FieldDef(label="version")
    @EditMode(editor = FieldEditor.Number)
    Integer getVersion();

    void setVersion(Integer version);

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    @FieldDef(label="created")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @ApiModelProperty(value = "修改时间")
    @Column(name = "`modified`")
    @FieldDef(label="modified")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);


    @ApiModelProperty(value = "车牌号")
    @Column(name = "`user_plates`")
    @FieldDef(label="user_plates", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getUserPlates();

    void setUserPlates(String userPlates);
    
    @ApiModelProperty(value = "车牌数量")
    @Column(name = "`plate_amount`")
    @FieldDef(label="plate_amount", maxLength = 50)
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getPlateAmount();

    void setPlateAmount(Integer plateAmount);
    
    

}