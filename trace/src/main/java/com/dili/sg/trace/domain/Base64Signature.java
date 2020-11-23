package com.dili.sg.trace.domain;

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
@Table(name = "`base64_signature`")
public interface Base64Signature extends IBaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);


	
    @Column(name = "`approver_info_id`")
    @FieldDef(label="approver_info_id")
    @EditMode(editor = FieldEditor.Number, required = true)
	
	public Long getApproverInfoId();
	public void setApproverInfoId(Long approverInfoId);
	
	
    @Column(name = "`order_num`")
    @FieldDef(label="order_num")
    @EditMode(editor = FieldEditor.Number, required = true)
	
	public Integer getOrderNum();
	public void setOrderNum(Integer orderNum);
	
	
	
	
    @Column(name = "`base64`")
    @FieldDef(label="base64")
    @EditMode(editor = FieldEditor.Text, required = false)
	public String getBase64();
	public void setBase64(String base64);

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    @FieldDef(label="created")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @ApiModelProperty(value = "修改时间")
    @Column(name = "`modified`")
    @FieldDef(label="modified")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);

    
 

}