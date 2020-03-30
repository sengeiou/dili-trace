package com.dili.trace.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`check_sheet`")
public interface CheckSheet extends IBaseDomain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	@FieldDef(label = "id")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getId();

	void setId(Long id);

	@Column(name = "`approver_info_id`")
	@FieldDef(label = "approver_info_id")
	@EditMode(editor = FieldEditor.Number, required = true)

	public Long getApproverInfoId();

	public void setApproverInfoId(Long approverInfoId);

	@Column(name = "`code`")
	@FieldDef(label = "code")
	@EditMode(editor = FieldEditor.Text, required = false)
	public String getCode();

	public void setCode(String code);

	@Column(name = "`id_card_no`")
	@FieldDef(label = "id_card_no")
	@EditMode(editor = FieldEditor.Number, required = true)

	public String getIdCardNo();

	public void setIdCardNo(String submitterId);

	@Column(name = "`user_name`")
	@FieldDef(label = "user_name")
	@EditMode(editor = FieldEditor.Text, required = false)
	public String getUserName();

	public void setUserName(String userName);

	@Column(name = "`valid_period`")
	@FieldDef(label = "valid_period")
	@EditMode(editor = FieldEditor.Number, required = true)

	public Integer getValidPeriod();

	public void setValidPeriod(Integer validPeriod);

	@Column(name = "`detect_operator_id`")
	@FieldDef(label = "detect_operator_id")
	@EditMode(editor = FieldEditor.Number, required = true)

	public Long getDetectOperatorId();

	public void setDetectOperatorId(Long detectOperatorId);

	@Column(name = "`detect_operator_name`")
	@FieldDef(label = "detect_operator_name")
	@EditMode(editor = FieldEditor.Text, required = false)
	public String getDetectOperatorName();

	public void setDetectOperatorName(String detectOperatorName);



	@Column(name = "`remark`")
	@FieldDef(label = "remark")
	@EditMode(editor = FieldEditor.Text, required = false)
	public String getRemark();

	public void setRemark(String remark);
	
	@Column(name = "`qrcode_url`")
	@FieldDef(label = "qrcode_url")
	@EditMode(editor = FieldEditor.Text, required = false)
	public String getQrcodeUrl();

	public void setQrcodeUrl(String qrcodeUrl);
	

	
    @Column(name = "`operator_name`")
    @FieldDef(label="operatorName")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOperatorName();

    void setOperatorName(String operatorName);
    @ApiModelProperty(value = "操作人ID")
    @Column(name = "`operator_id`")
    @FieldDef(label="operatorId")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getOperatorId();

    void setOperatorId(Long operatorId);
    
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
	
    @Transient
    String getApproverBase64Sign();
    void setApproverBase64Sign(String approverBase64Sign);
    
    @Transient
    String getBase64Qrcode();
    void setBase64Qrcode(String base64Qrcode);

}