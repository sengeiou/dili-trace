package com.dili.trace.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-10-11 10:05:11.
 */
@Table(name = "`code_generate`")
public interface CodeGenerate extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);
    
    @Column(name = "`type`")
    @FieldDef(label="编号类型", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getType();

    void setType(String type);
    
    @Column(name = "`prefix`")
    @FieldDef(label="编号前缀", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPrefix();

    void setPrefix(String prefix);


    @Column(name = "`segment`")
    @FieldDef(label="当前编号段", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSegment();

    void setSegment(String segment);


    @Column(name = "`seq`")
    @FieldDef(label="当前编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSeq();

    void setSeq(Long seq);
    
    @Column(name = "`pattern`")
    @FieldDef(label="模式", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPattern();

    void setPattern(String pattern);
    
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