package com.dili.trace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.glossary.UserTypeEnum;

import io.swagger.annotations.ApiModelProperty;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`user`")
public class User extends BaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;

	@ApiModelProperty(value = "名称")
	@Column(name = "`name`")
	private String name;

	@ApiModelProperty(value = "手机号（帐号）")
	@Column(name = "`phone`")
	private String phone;

	@ApiModelProperty(value = "理货区号")
	@Column(name = "`tally_area_nos`")
	private String tallyAreaNos;

	@ApiModelProperty(value = "身份证号")
	@Column(name = "`card_no`")
	private String cardNo;

	@ApiModelProperty(value = "地址")
	@Column(name = "`addr`")
	private String addr;

	@ApiModelProperty(value = "身份证照正面URL")
	@Column(name = "`card_no_front_url`")
	private String cardNoFrontUrl;

	@ApiModelProperty(value = "身份证照反面URL")
	@Column(name = "`card_no_back_url`")
	private String cardNoBackUrl;

	@ApiModelProperty(value = "营业执照URL")
	@Column(name = "`business_license_url`")
	private String businessLicenseUrl;

	@ApiModelProperty(value = "销售城市ID")
	@Column(name = "`sales_city_id`")
	private Long salesCityId;

	@ApiModelProperty(value = "销售城市名称")
	@Column(name = "`sales_city_name`")
	private String salesCityName;

	@ApiModelProperty(value = "状态")
	@Column(name = "`state`")
	private Integer state;

	@ApiModelProperty(value = "密码")
	@Column(name = "`password`")
	private String password;

	@Column(name = "`yn`")
	// 1:正常 -1：删除")
	private Integer yn;

	@Column(name = "`version`")
	private Integer version;

	@ApiModelProperty(value = "创建时间")
	@Column(name = "`created`")
	private Date created;

	@ApiModelProperty(value = "修改时间")
	@Column(name = "`modified`")
	private Date modified;

	/**
	 * 
	 * 0：未删除，非0:已经删除
	 */
	@Column(name = "`is_delete`")
	private Long isDelete;
	
	/**
	 * 用户类型
	 */
	@Column(name = "`user_type`")
	private Integer userType;

	@ApiModelProperty(value = "验证码")
	@Transient
	private String checkCode;

	@ApiModelProperty(value = "确认密码")
	@Transient
	private String ackPassword;

	@ApiModelProperty(value = "旧密码")
	@Transient
	private String oldPassword;

	@Transient
	private String plates;

	@Transient
	public String getUserTypeName() {
		return UserTypeEnum.fromCode(this.getUserType()).map(UserTypeEnum::getDesc).orElse("");
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTallyAreaNos() {
		return tallyAreaNos;
	}

	public void setTallyAreaNos(String tallyAreaNos) {
		this.tallyAreaNos = tallyAreaNos;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCardNoFrontUrl() {
		return cardNoFrontUrl;
	}

	public void setCardNoFrontUrl(String cardNoFrontUrl) {
		this.cardNoFrontUrl = cardNoFrontUrl;
	}

	public String getCardNoBackUrl() {
		return cardNoBackUrl;
	}

	public void setCardNoBackUrl(String cardNoBackUrl) {
		this.cardNoBackUrl = cardNoBackUrl;
	}

	public String getBusinessLicenseUrl() {
		return businessLicenseUrl;
	}

	public void setBusinessLicenseUrl(String businessLicenseUrl) {
		this.businessLicenseUrl = businessLicenseUrl;
	}

	public Long getSalesCityId() {
		return salesCityId;
	}

	public void setSalesCityId(Long salesCityId) {
		this.salesCityId = salesCityId;
	}

	public String getSalesCityName() {
		return salesCityName;
	}

	public void setSalesCityName(String salesCityName) {
		this.salesCityName = salesCityName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getYn() {
		return yn;
	}

	public void setYn(Integer yn) {
		this.yn = yn;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Long getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Long isDelete) {
		this.isDelete = isDelete;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getAckPassword() {
		return ackPassword;
	}

	public void setAckPassword(String ackPassword) {
		this.ackPassword = ackPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPlates() {
		return plates;
	}

	public void setPlates(String plates) {
		this.plates = plates;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

}