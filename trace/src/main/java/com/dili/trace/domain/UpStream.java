package com.dili.trace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

/**
 * 上游信息
 */
@Table(name = "`upstream`")
public class UpStream extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;
    /**
     * 类型 {@link com.dili.trace.glossary.UpStreamTypeEnum}
     */
    @ApiModelProperty(value = "类型")
    @Column(name = "`upstream_type`")
    private Integer upstreamType;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    @Column(name = "`id_card`")
    private String idCard;
    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    @Column(name = "`telphone`")
    private String telphone;
    /**
     * 企业(个人)名称
     */
    @ApiModelProperty(value = "企业(个人)名称")
    @Column(name = "`name`")
    private String name;

    /**
     * 法人
     */
    @ApiModelProperty(value = "法人姓名")
    @Column(name = "`legal_person`")
    private String legalPerson;

    /**
     * 统一信用代码
     */
    @ApiModelProperty(value = " 统一信用代码")
    @Column(name = "`license`")
    private String license;

    /**
     * 企业营业执照
     */
    @ApiModelProperty(value = " 企业营业执照")
    @Column(name = "`business_license_url`")
    private String businessLicenseUrl;

    /**
     * 生產许可证
     */
    @ApiModelProperty(value = "生產许可证")
    @Column(name = "`manufacturing_license_url`")
    private String manufacturingLicenseUrl;

       /**
     * 经营许可证
     */
    @ApiModelProperty(value = "经营许可证")
    @Column(name = "`operation_license_url`")
    private String operationLicenseUrl;



    @ApiModelProperty(value = "身份证照正面URL")
    @Column(name = "`card_no_front_url`")
    private String cardNoFrontUrl;


    @ApiModelProperty(value = "身份证照反面URL")
    @Column(name = "`card_no_back_url`")
    private String cardNoBackUrl;


    @ApiModelProperty(value = "复制来源userid")
    @Column(name = "`source_user_id`")
    private Long sourceUserId;

    
    @ApiModelProperty(value = "操作人姓名")
    @Column(name = "`operator_name`")
    private String operatorName;

    @ApiModelProperty(value = "操作人ID")
    @Column(name = "`operator_id`")
    private Long operatorId;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

    @ApiModelProperty(value = "更新时间")
    @Column(name = "`modified`")
    private Date modified;

    public Long getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(Long sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	/**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String return the telphone
     */
    public String getTelphone() {
        return telphone;
    }

    /**
     * @param telphone the telphone to set
     */
    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the legalPerson
     */
    public String getLegalPerson() {
        return legalPerson;
    }

    /**
     * @param legalPerson the legalPerson to set
     */
    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    /**
     * @return String return the license
     */
    public String getLicense() {
        return license;
    }

    /**
     * @param license the license to set
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * @return String return the idCard
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * @param idCard the idCard to set
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**
     * @return String return the operatorName
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName the operatorName to set
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * @return Long return the operatorId
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId the operatorId to set
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return Date return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return Date return the modified
     */
    public Date getModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }

    /**
     * @return Integer return the upstreamType
     */
    public Integer getUpstreamType() {
        return upstreamType;
    }

    /**
     * @param upstreamType the upstreamType to set
     */
    public void setUpstreamType(Integer upstreamType) {
        this.upstreamType = upstreamType;
    }

    /**
     * @return String return the businessLicenseUrl
     */
    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }

    /**
     * @param businessLicenseUrl the businessLicenseUrl to set
     */
    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl;
    }




    /**
     * @return String return the cardNoFrontUrl
     */
    public String getCardNoFrontUrl() {
        return cardNoFrontUrl;
    }

    /**
     * @param cardNoFrontUrl the cardNoFrontUrl to set
     */
    public void setCardNoFrontUrl(String cardNoFrontUrl) {
        this.cardNoFrontUrl = cardNoFrontUrl;
    }

    /**
     * @return String return the cardNoBackUrl
     */
    public String getCardNoBackUrl() {
        return cardNoBackUrl;
    }

    /**
     * @param cardNoBackUrl the cardNoBackUrl to set
     */
    public void setCardNoBackUrl(String cardNoBackUrl) {
        this.cardNoBackUrl = cardNoBackUrl;
    }


    /**
     * @return String return the manufacturingLicenseUrl
     */
    public String getManufacturingLicenseUrl() {
        return manufacturingLicenseUrl;
    }

    /**
     * @param manufacturingLicenseUrl the manufacturingLicenseUrl to set
     */
    public void setManufacturingLicenseUrl(String manufacturingLicenseUrl) {
        this.manufacturingLicenseUrl = manufacturingLicenseUrl;
    }


    /**
     * @return String return the operationLicenseUrl
     */
    public String getOperationLicenseUrl() {
        return operationLicenseUrl;
    }

    /**
     * @param operationLicenseUrl the operationLicenseUrl to set
     */
    public void setOperationLicenseUrl(String operationLicenseUrl) {
        this.operationLicenseUrl = operationLicenseUrl;
    }

}