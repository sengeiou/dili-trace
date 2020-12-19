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
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;

	/**
	 * 图片URL
	 */
	@ApiModelProperty(value = "图片URL")
	@Column(name = "`url`")
	private String url;

	/**
	 * 图片唯一id
	 */
	@ApiModelProperty(value = "图片唯一id")
	@Column(name = "`uid`")
	private String uid;


	/**
	 * 图片类型 {@link ImageCertTypeEnum}
	 */
	@ApiModelProperty(value = "图片类型")
	@Column(name = "`cert_type`")
	private Integer certType;

	/**
	 * 关联单据ID
	 */
	@Column(name = "`bill_id`")
	private Long billId;

	/**
	 * 创建时间
	 */
	@Column(name = "`created`")
	private Date created;

	/**
	 * 修改时间
	 */
	@Column(name = "`modified`")
	private Date modified;

	/**
	 * 单据类型 {@link com.dili.trace.enums.BillTypeEnum}
	 */
	@ApiModelProperty(value = "单据类型。1-报备单 2-检测单 3-检测不合格处置单 4-进门主台账单。默认为1")
	@Column(name = "`bill_type`")
	private Integer billType;

	/**
	 * 类型名称
	 */
	@Transient
	private String certTypeName;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public String getUrl() {
//		return url;
//	}

//	public void setUrl(String url) {
//		this.url = url;
//	}

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