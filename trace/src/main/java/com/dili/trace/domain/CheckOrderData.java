package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;

/**
 * @author asa.lee
 */
@Table(name = "check_data")
public class CheckOrderData extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Column(name = "`check_id`")
    private Long checkId;

    @Column(name = "`normal_value`")
    private String normalValue;
    @Column(name = "`project`")
    private String project;
    @Column(name = "`result`")
    private String result;
    @Column(name = "`value`")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCheckId() {
        return checkId;
    }

    public void setCheckId(Long checkId) {
        this.checkId = checkId;
    }

    public String getNormalValue() {
        return normalValue;
    }

    public void setNormalValue(String normalValue) {
        this.normalValue = normalValue;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
