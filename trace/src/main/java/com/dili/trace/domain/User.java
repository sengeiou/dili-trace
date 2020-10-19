package com.dili.trace.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.enums.VocationTypeEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

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
    @FieldDef(label = "tallyAreaNos")
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
    @JSONField(serialize = false)
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

    public String getLegalPerson();

    public void setLegalPerson(String legalPerson);

    /**
     * 统一信用代码
     */
    @ApiModelProperty(value = "统一信用代码")
    @Column(name = "`license`")
    public String getLicense();

    public void setLicense(String license);

    /**
     * 用户类型 {@link com.dili.trace.glossary.UserTypeEnum}
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
    public Long getMarketId();

    public void setMarketId(Long marketId);

    @ApiModelProperty(value = "所属市场名称")
    @Column(name = "`market_name`")
    public String getMarketName();

    public void setMarketName(String marketName);

    @ApiModelProperty(value = "生产许可证URL")
    @Column(name = "`manufacturing_license_url`")
    @FieldDef(label = "manufacturing_license_url", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getManufacturingLicenseUrl();

    public void setManufacturingLicenseUrl(String manufacturingLicenseUrl);

    @ApiModelProperty(value = "经营许可证URL")
    @Column(name = "`operation_license_url`")
    @FieldDef(label = "operation_license_url", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOperationLicenseUrl();

    public void setOperationLicenseUrl(String operationLicenseUrl);

    /**
     * {@link com.dili.trace.glossary.UserQrStatusEnum}
     */
    @ApiModelProperty(value = "二维码状态")
    @Column(name = "`qr_status`")
    Integer getQrStatus();

    void setQrStatus(Integer qrStatus);

       /**
     * {@link com.dili.trace.glossary.UserQrStatusEnum}
     */
    @ApiModelProperty(value = "前一次二维码状态")
    @Column(name = "`pre_qr_status`")
    Integer getPreQrStatus();

    void setPreQrStatus(Integer preQrStatus);


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

    @ApiModelProperty(value = "审核状态")
    @Column(name = "`validate_state`")
    Integer getValidateState();

    void setValidateState(Integer state);

    @Column(name = "`vocation_type`")
    Integer getVocationType();

    void setVocationType(Integer type);

    @Column(name = "business_category_ids")
    String getBusinessCategoryIds();

    void setBusinessCategoryIds(String businessCategoryIds);

    @Column(name = "business_categories")
    String getBusinessCategories();

    void setBusinessCategories(String businessCategories);

    @Transient
    default String getVocationTypeName() {
        return VocationTypeEnum.getNameFromCode(this.getVocationType());
    }

    @Column(name = "`source`")
    Integer getSource();

    void setSource(Integer source);

    /**
     * 微信openid
     * @return
     */
    @Column(name = "`open_id`")
    String getOpenId();

    /**
     * 微信openid
     * @param open_id
     */
    void setOpenId(String open_id);

    /**
     * 微信弹窗绑定确认时间
     * @return
     */
    @Column(name = "`confirm_date`")
    Date getConfirmDate();

    /**
     * 微信弹窗绑定确认时间
     * @param confirm_date
     */
    void setConfirmDate(Date confirm_date);

    /**
     * 用户上报标志位
     * @return
     */
    @Column(name = "`is_push`")
    Integer getIsPush();

    /**
     * 用户上报标志位
     * @param is_push
     */
    void setIsPush(Integer is_push);

    /**
     * 用户活跃标志位
     * @return
     */
    @Column(name = "`is_active`")
    Integer getIsActive();

    /**
     * 用户活跃标志位
     * @param is_active
     */
    void setIsActive(Integer is_active);

    /**
     * 经营户卡号
     * * @return
     */
    @ApiModelProperty(value = "经营户卡号")
    @Column(name = "`third_party_code`")
    String getThirdPartyCode();

    /**
     * 经营户卡号
     * @param thirdPartyCode
     */
    void setThirdPartyCode(String thirdPartyCode);
}