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
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@SuppressWarnings("serial")
@Table(name = "`verify_history`")
public class VerifyHistory extends BaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;

	@Column(name = "`bill_id`")
	private Long billId;

	@ApiModelProperty(value = "初始审核状态")
	@Column(name = "`from_verify_status`")
	private Integer fromVerifyStatus;

	@ApiModelProperty(value = "审核状态")
	@Column(name = "`to_verify_status`")
	private Integer toVerifyStatus;

	@ApiModelProperty(value = "操作人")
	@Column(name = "`verify_user_name`")
	private String verifyUserName;

	@ApiModelProperty(value = "操作人ID")
	@Column(name = "`verify_user_id`")
	private Long verifyUserId;

	@ApiModelProperty(value = "当前有效")
	@Column(name = "`valid`")
	private Integer valid;

	@Column(name = "`created`")
	private Date created;

	@Column(name = "`modified`")
	private Date modified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Integer getFromVerifyStatus() {
		return fromVerifyStatus;
	}

	public void setFromVerifyStatus(Integer fromVerifyStatus) {
		this.fromVerifyStatus = fromVerifyStatus;
	}

	public Integer getToVerifyStatus() {
		return toVerifyStatus;
	}

	public void setToVerifyStatus(Integer toVerifyStatus) {
		this.toVerifyStatus = toVerifyStatus;
	}



	public String getVerifyUserName() {
		return verifyUserName;
	}

	public void setVerifyUserName(String verifyUserName) {
		this.verifyUserName = verifyUserName;
	}

	public Long getVerifyUserId() {
		return verifyUserId;
	}

	public void setVerifyUserId(Long verifyUserId) {
		this.verifyUserId = verifyUserId;
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

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

}