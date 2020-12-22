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
@Table(name = "`approver_info`")
public interface ApproverInfo extends IBaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`user_name`")
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getUserName();
	public void setUserName(String userName);
	
    @Column(name = "`phone`")
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPhone();
	public void setPhone(String phone);
	
    @Column(name = "`user_id`")
    @FieldDef(label="user_id")
    @EditMode(editor = FieldEditor.Number, required = true)
	
	public Long getUserId();
	public void setUserId(Long userId);
	

    @Transient
    @EditMode(editor = FieldEditor.Text, required = false)
	public String getSignBase64();
	public void setSignBase64(String signBase64);

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


    @Column(name = "`market_id`")
    @FieldDef(label="market_id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getMarketId();
    public void setMarketId(Long marketId);
    
 

}