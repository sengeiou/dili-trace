package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.glossary.UsualAddressTypeEnum;
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
public class UsualAddress extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;


    @ApiModelProperty(value = "地址")
    @Column(name = "`address`")
    private String address;

    @ApiModelProperty(value = "地址全名")
    @Column(name = "`merged_address`")
    private String mergedAddress;

    @ApiModelProperty(value = "地址类型")
    @Column(name = "`type`")
    private String type;


    @ApiModelProperty(value = "地址id")
    @Column(name = "`address_id`")
    private Long addressId;


    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;


    @ApiModelProperty(value = "修改时间")
    @Column(name = "`modified`")
    private Date modified;


    
    
    @ApiModelProperty(value = "当天使用数量统计")
    @Column(name = "`today_used_count`")
    private Integer todayUsedCount;


    @ApiModelProperty(value = "前一天使用数量统计")
    @Column(name = "`preday_used_count`")
    private Integer preDayUsedCount;


    
    @ApiModelProperty(value = "清理时间")
    @Column(name = "`clear_time`")
    private Date clearTime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMergedAddress() {
        return mergedAddress;
    }

    public void setMergedAddress(String mergedAddress) {
        this.mergedAddress = mergedAddress;
    }

    public String getType() {
        return type;
    }

    @Transient
    public String getTypeDesc() {
        return UsualAddressTypeEnum.fromType(this.type).map(UsualAddressTypeEnum::getDesc).orElse("");
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
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

    public Integer getTodayUsedCount() {
        return todayUsedCount;
    }

    public void setTodayUsedCount(Integer todayUsedCount) {
        this.todayUsedCount = todayUsedCount;
    }

    public Integer getPreDayUsedCount() {
        return preDayUsedCount;
    }

    public void setPreDayUsedCount(Integer preDayUsedCount) {
        this.preDayUsedCount = preDayUsedCount;
    }

    public Date getClearTime() {
        return clearTime;
    }

    public void setClearTime(Date clearTime) {
        this.clearTime = clearTime;
    }
}