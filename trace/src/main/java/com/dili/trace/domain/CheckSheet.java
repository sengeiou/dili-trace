package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`check_sheet`")
public class CheckSheet extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;
    
	@Column(name = "`approver_info_id`")
	private Long approverInfoId;

	@Column(name = "`code`")
	private String code;

	@Column(name = "`id_card_no`")
	public String idCardNo;

	@Column(name = "`user_name`")
	public String userName;

	@Column(name = "`valid_period`")
	private Integer validPeriod;

	@Column(name = "`detect_operator_id`")
	private Long detectOperatorId;

	@Column(name = "`detect_operator_name`")
	private String detectOperatorName;

	@Column(name = "`remark`")
	private String remark;

	@Column(name = "`qrcode_url`")
	private String qrcodeUrl;

	@Column(name = "`operator_name`")
	private String operatorName;

	@ApiModelProperty(value = "操作人ID")
	@Column(name = "`operator_id`")
	private Long operatorId;

	@ApiModelProperty(value = "创建时间")
	@Column(name = "`created`")
	private Date created;

	@ApiModelProperty(value = "修改时间")
	@Column(name = "`modified`")
	private Date modified;

	@Transient
	private String approverBase64Sign;

	@Transient
	private String base64Qrcode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getApproverInfoId() {
		return approverInfoId;
	}

	public void setApproverInfoId(Long approverInfoId) {
		this.approverInfoId = approverInfoId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = StringUtils.trimToNull(userName);
	}

	public Integer getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}

	public Long getDetectOperatorId() {
		return detectOperatorId;
	}

	public void setDetectOperatorId(Long detectOperatorId) {
		this.detectOperatorId = detectOperatorId;
	}

	public String getDetectOperatorName() {
		return detectOperatorName;
	}

	public void setDetectOperatorName(String detectOperatorName) {
		this.detectOperatorName = StringUtils.trimToNull(detectOperatorName);;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
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

	public String getApproverBase64Sign() {
		return approverBase64Sign;
	}

	public void setApproverBase64Sign(String approverBase64Sign) {
		this.approverBase64Sign = approverBase64Sign;
	}

	public String getBase64Qrcode() {
		return base64Qrcode;
	}

	public void setBase64Qrcode(String base64Qrcode) {
		this.base64Qrcode = base64Qrcode;
	}

}