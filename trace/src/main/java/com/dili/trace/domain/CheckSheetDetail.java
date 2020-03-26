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
@Table(name = "`check_sheet_detail`")
public interface CheckSheetDetail extends IBaseDomain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	@FieldDef(label = "id")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getId();

	void setId(Long id);

	@Column(name = "`check_sheet_id`")
	@FieldDef(label = "check_sheet_id")
	@EditMode(editor = FieldEditor.Number, required = true)

	public Long getCheckSheetId();

	public void setCheckSheetId(Long checkSheetId);

	@Column(name = "`register_bill_id`")
	@FieldDef(label = "register_bill_id")
	@EditMode(editor = FieldEditor.Number, required = true)
	public Long getRegisterBillId();

	public void setRegisterBillId(Long registerBillId);

	@ApiModelProperty(value = "商品ID")
	@Column(name = "`product_id`")
	@FieldDef(label = "productId")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getProductId();

	void setProductId(Long productId);

	@ApiModelProperty(value = "商品名称")
	@Column(name = "`product_name`")
	@FieldDef(label = "productName", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getProductName();

	void setProductName(String productName);

	@ApiModelProperty(value = "产地ID")
	@Column(name = "`origin_id`")
	@FieldDef(label = "originId")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getOriginId();

	void setOriginId(Long originId);

	@ApiModelProperty(value = "产地名")
	@Column(name = "`origin_name`")
	@FieldDef(label = "originName", maxLength = 20)
	@EditMode(editor = FieldEditor.Text, required = false)
	String getOriginName();

	@ApiModelProperty(value = "1.合格 2.不合格 3.复检合格 4.复检不合格")
	@Column(name = "`detect_state`")
	@FieldDef(label = "1.合格 2.不合格 3.复检合格 4.复检不合格")
	@EditMode(editor = FieldEditor.Number, required = false)
	Integer getDetectState();

	void setDetectState(Integer detectState);

	@Column(name = "`product_alias_name`")
	@FieldDef(label = "product_alias_name")
	@EditMode(editor = FieldEditor.Text, required = false)
	public String getProductAliasName();

	public void setProductAliasName(String productAliasName);

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

}