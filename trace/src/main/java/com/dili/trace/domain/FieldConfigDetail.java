package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "field_config_detail")
public class FieldConfigDetail extends BaseDomain {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 配置ID
     */
    @Column(name = "field_config_id")
    private Long fieldConfigId;
    /**
     * field id
     */
    @Column(name = "default_field_detail_id")
    private Long defaultFieldDetailId;

    /**
     * 是否显示
     */
    @Column(name = "is_displayed")
    private Integer isDisplayed;
    /**
     * 是否必填
     */
    @Column(name = "is_required")
    private Integer isRequired;
    /**
     * 默认值
     */
    @Column(name = "default_value")
    private String defaultValue;

    /**
     * 是否可用
     */
    @Column(name = "is_valid")
    private Integer isValid;

    /**
     * 创建时间
     */
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * 更新时间
     */
    @Column(name = "modified")
    private LocalDateTime modified;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getFieldConfigId() {
        return fieldConfigId;
    }

    public void setFieldConfigId(Long fieldConfigId) {
        this.fieldConfigId = fieldConfigId;
    }


    public Integer getIsDisplayed() {
        return isDisplayed;
    }

    public void setIsDisplayed(Integer isDisplayed) {
        this.isDisplayed = isDisplayed;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public Long getDefaultFieldDetailId() {
        return defaultFieldDetailId;
    }

    public void setDefaultFieldDetailId(Long defaultFieldDetailId) {
        this.defaultFieldDetailId = defaultFieldDetailId;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
