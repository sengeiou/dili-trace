package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "default_field_detail")
public class DefaultFieldDetail extends BaseDomain {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 模块
     */
    @Column(name = "module_type")
    private Integer moduleType;
    /**
     * field id
     */
    @Column(name = "field_id")
    private String fieldId;
    /**
     * field name
     */
    @Column(name = "field_name")
    private String fieldName;
    /**
     * 是否显示
     */
    @Column(name = "is_display")
    private Integer isDisplay;
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
     * jsonPath
     */
    @Column(name = "json_path")
    private String jsonPath;
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

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(Integer isDisplay) {
        this.isDisplay = isDisplay;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getModuleType() {
        return moduleType;
    }

    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }
}
