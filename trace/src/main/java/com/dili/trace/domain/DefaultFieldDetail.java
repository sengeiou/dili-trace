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
     * field label
     */
    @Column(name = "field_label")
    private String fieldLabel;
    /**
     * field name
     */
    @Column(name = "field_name")
    private String fieldName;
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
     * 可选值范围
     */
    @Column(name = "available_values")
    private String availableValues;


    /**
     * jsonpath
     */
    @Column(name = "json_path")
    private String jsonPath;

    /**
     * jsonpath类型
     */
    @Column(name = "json_path_type")
    private Integer jsonPathType;
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

    public String getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public Integer getJsonPathType() {
        return jsonPathType;
    }

    public void setJsonPathType(Integer jsonPathType) {
        this.jsonPathType = jsonPathType;
    }

    public String getAvailableValues() {
        return availableValues;
    }

    public void setAvailableValues(String availableValues) {
        this.availableValues = availableValues;
    }
}
