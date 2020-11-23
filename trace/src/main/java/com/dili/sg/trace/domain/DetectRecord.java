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
@Table(name = "`detect_record`")
public interface DetectRecord extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @ApiModelProperty(value = "检测时间")
    @Column(name = "`detect_time`")
    @FieldDef(label="detectTime")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getDetectTime();

    void setDetectTime(Date detectTime);

    @Column(name = "`detect_operator`")
    @FieldDef(label="detectOperator", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getDetectOperator();

    void setDetectOperator(String detectOperator);

    @Column(name = "`detect_type`")
    @FieldDef(label="1.第一次送检 2：复检")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getDetectType();

    void setDetectType(Integer detectType);

    @Column(name = "`detect_state`")
    @FieldDef(label="1.合格 2.不合格")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getDetectState();

    void setDetectState(Integer detectState);

    @Column(name = "`pd_result`")
    @FieldDef(label="pdResult", maxLength = 10)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getPdResult();

    void setPdResult(String pdResult);

    @Column(name = "`register_bill_code`")
    @FieldDef(label="registerBillCode")
    @EditMode(editor = FieldEditor.Text, required = true)
    String getRegisterBillCode();

    void setRegisterBillCode(String registerBillCode);

    @Column(name = "`created`")
    @FieldDef(label="created")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`modified`")
    @FieldDef(label="modified")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);
}