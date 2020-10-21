package com.dili.trace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.enums.ImageCertTypeEnum;

import io.swagger.annotations.ApiModelProperty;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@SuppressWarnings("serial")
@Table(name = "`image_cert`")
public class ImageCert extends BaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;

	@ApiModelProperty(value = "图片URL")
	@Column(name = "`url`")
	private String url;

	/**
	 * {@link ImageCertTypeEnum}
	 */
	@ApiModelProperty(value = "图片类型")
	@Column(name = "`cert_type`")
	private Integer certType;

	@Column(name = "`bill_id`")
	private Long billId;

	@Column(name = "`created`")
	private Date created;

	@Column(name = "`modified`")
	private Date modified;

	@ApiModelProperty(value = "单据类型。1-报备单 2-检测单 3-检测不合格处置单 4-进门主台账单。默认为1")
	@Column(name = "`bill_type`")
	private Integer billType;

	@Transient
	private String certTypeName;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Integer getCertType() {
		return certType;
	}

	public void setCertType(Integer certType) {
		this.certType = certType;
	}

    /**
     * @return Long return the billId
     */
    public Long getBillId() {
        return billId;
    }

    /**
     * @param billId the billId to set
     */
    public void setBillId(Long billId) {
        this.billId = billId;
    }


    /**
     * @return String return the certTypeName
     */
    public String getCertTypeName() {
        return ImageCertTypeEnum.fromCode(this.getCertType()).map(ImageCertTypeEnum::getName).orElse("");
    }

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}
}