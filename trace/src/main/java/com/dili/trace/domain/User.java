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
@Table(name = "`user`")
public interface User extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label = "id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @ApiModelProperty(value = "名称")
    @Column(name = "`name`")
    @FieldDef(label = "name", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @ApiModelProperty(value = "手机号（帐号）")
    @Column(name = "`phone`")
    @FieldDef(label = "phone", maxLength = 15)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPhone();

    void setPhone(String phone);

    @ApiModelProperty(value = "理货区号")
    @Column(name = "`tally_area_nos`")
    @FieldDef(label = "tallyAreaNos", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTallyAreaNos();

    void setTallyAreaNos(String tallyAreaNos);

    @ApiModelProperty(value = "身份证号")
    @Column(name = "`card_no`")
    @FieldDef(label = "cardNo", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardNo();

    void setCardNo(String cardNo);

    @ApiModelProperty(value = "地址")
    @Column(name = "`addr`")
    @FieldDef(label = "addr", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddr();

    void setAddr(String addr);

    @ApiModelProperty(value = "身份证照正面URL")
    @Column(name = "`card_no_front_url`")
    @FieldDef(label = "cardNoFrontUrl", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardNoFrontUrl();

    void setCardNoFrontUrl(String cardNoFrontUrl);

    @ApiModelProperty(value = "身份证照反面URL")
    @Column(name = "`card_no_back_url`")
    @FieldDef(label = "cardNoBackUrl", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCardNoBackUrl();

    void setCardNoBackUrl(String cardNoBackUrl);

    @ApiModelProperty(value = "营业执照URL")
    @Column(name = "`business_license_url`")
    @FieldDef(label = "businessLicenseUrl", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getBusinessLicenseUrl();

    void setBusinessLicenseUrl(String businessLicenseUrl);

    @ApiModelProperty(value = "销售城市ID")
    @Column(name = "`sales_city_id`")
    @FieldDef(label = "salesCityId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSalesCityId();

    void setSalesCityId(Long salesCityId);

    @ApiModelProperty(value = "销售城市名称")
    @Column(name = "`sales_city_name`")
    @FieldDef(label = "salesCityName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSalesCityName();

    void setSalesCityName(String salesCityName);

    @ApiModelProperty(value = "状态")
    @Column(name = "`state`")
    @FieldDef(label = "1:启用 2：禁用")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getState();

    void setState(Integer state);

    @ApiModelProperty(value = "密码")
    @Column(name = "`password`")
    @FieldDef(label = "password", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPassword();

    void setPassword(String password);

    @Column(name = "`yn`")
    @FieldDef(label = "1:正常 -1：删除")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getYn();

    void setYn(Integer yn);

    @Column(name = "`version`")
    @FieldDef(label = "version")
    @EditMode(editor = FieldEditor.Number)
    Integer getVersion();

    void setVersion(Integer version);

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    @FieldDef(label = "created")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @ApiModelProperty(value = "修改时间")
    @Column(name = "`modified`")
    @FieldDef(label = "modified")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);

    /**
     * 
     * 0：未删除，非0:已经删除
     */
    @Column(name = "`is_delete`")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getIsDelete();

    void setIsDelete(Long isDelete);

    /**
     * 法人
     */
    @ApiModelProperty(value = "法人姓名")
    @Column(name = "`legal_person`")
   
    public String getLegalPerson() ;
    public void setLegalPerson(String legalPerson);

    /**
     * 统一信用代码
     */
    @ApiModelProperty(value = "统一信用代码")
    @Column(name = "`license`")
    public String getLicense() ;
    public void setLicense(String license);

    /**
     * 用户类型
     * {@link com.dili.trace.glossary.UserTypeEnum} 
     */
    @Column(name = "`user_type`")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getUserType();
    void setUserType(Integer userType);

    /**
     * 所属市场
     */
    @ApiModelProperty(value = "所属市场")
    @Column(name = "`market_id`")
    public Long getMarketId() ;
    public void setMarketId(Long marketId);


    
    @ApiModelProperty(value = "许可证URL")
    @Column(name = "`license_url`")
    @FieldDef(label = "LicenseUrl", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)

    public String getLicenseUrl();
    public void setLicenseUrl(String licenseUrl) ;


    @ApiModelProperty(value = "验证码")
    @Transient
    String getCheckCode();

    void setCheckCode(String checkCode);

    @ApiModelProperty(value = "确认密码")
    @Transient
    String getAckPassword();

    void setAckPassword(String ackPassword);

    @ApiModelProperty(value = "旧密码")
    @Transient
    String getOldPassword();

    void setOldPassword(String oldPassword);

    @Transient
    String getPlates();

    void setPlates(String plates);



}