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
@Table(name = "`usual_address`")
public interface UsualAddress extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @ApiModelProperty(value = "地址")
    @Column(name = "`address`")
    @FieldDef(label="address", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddress();

    void setAddress(String address);

    @ApiModelProperty(value = "地址全名")
    @Column(name = "`merged_address`")
    @FieldDef(label="merged_address", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getMergedAddress();

    void setMergedAddress(String mergedAddress);

    @ApiModelProperty(value = "地址类型")
    @Column(name = "`type`")
    @FieldDef(label="type", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getType();

    void setType(String type);

    @ApiModelProperty(value = "地址id")
    @Column(name = "`address_id`")
    @FieldDef(label="address_id", maxLength = 20)
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAddressId();

    void setAddressId(Long addressId);




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
    
    
    
    @ApiModelProperty(value = "当天使用数量统计")
    @Column(name = "`today_used_count`")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getTodayUsedCount();

    void setTodayUsedCount(Integer todayUsedCount);
    
    @ApiModelProperty(value = "前一天使用数量统计")
    @Column(name = "`preday_used_count`")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getPreDayUsedCount();

    void setPreDayUsedCount(Integer preDayUsedCount);
    
    
    @ApiModelProperty(value = "清理时间")
    @Column(name = "`clear_time`")
    @FieldDef(label="clear_time")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getClearTime();

    void setClearTime(Date clearTime);

}