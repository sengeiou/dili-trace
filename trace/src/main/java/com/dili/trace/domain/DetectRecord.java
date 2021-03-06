package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
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
 * <p>
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`detect_record`")
public class DetectRecord extends BaseDomain {

    /**
     * 检测记录ID
     *
     * @return
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    /**
     * 检测时间
     *
     * @return
     */
    @ApiModelProperty(value = "检测时间")
    @Column(name = "`detect_time`")
    private Date detectTime;

    /**
     * 检测人员
     *
     * @return
     */
    @Column(name = "`detect_operator`")
    private String detectOperator;
    /**
     * 检测类型-1.第一次送检 2：复检
     *
     * @return
     */
    @Column(name = "`detect_type`")
    private Integer detectType;

    /**
     * 检测状态 1.合格 2.不合格
     *
     * @return
     */
    @Column(name = "`detect_state`")
    private Integer detectState;

    /**
     * 产品结果
     *
     * @return
     */
    @Column(name = "`pd_result`")
    private String pdResult;


    /**
     * 报备单编号
     *
     * @return
     */
    @Column(name = "`register_bill_code`")
    private String registerBillCode;

    /**
     * 创建时间
     *
     * @return
     */
    @Column(name = "`created`")
    private Date created;

    /**
     * 修改时间
     *
     * @return
     */
    @Column(name = "`modified`")
    private Date modified;


    /**
     * 检测批号
     *
     * @return
     */
    @Column(name = "`detect_batch_no`")
    private String detectBatchNo;

    /**
     * 标准值
     *
     * @return
     */
    @Column(name = "`normal_result`")
    private String normalResult;


    /**
     * 检测机构
     *
     * @return
     */
    @Column(name = "`detect_company`")
    private String detectCompany;


    /**
     * 检测请求主键
     *
     * @return
     */
    @Column(name = "`detect_request_id`")
    private Long detectRequestId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Date getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(Date detectTime) {
        this.detectTime = detectTime;
    }

    public String getDetectOperator() {
        return detectOperator;
    }

    public void setDetectOperator(String detectOperator) {
        this.detectOperator = detectOperator;
    }

    public Integer getDetectType() {
        return detectType;
    }

    public void setDetectType(Integer detectType) {
        this.detectType = detectType;
    }

    public Integer getDetectState() {
        return detectState;
    }

    public void setDetectState(Integer detectState) {
        this.detectState = detectState;
    }

    public String getPdResult() {
        return pdResult;
    }

    public void setPdResult(String pdResult) {
        this.pdResult = pdResult;
    }

    public String getRegisterBillCode() {
        return registerBillCode;
    }

    public void setRegisterBillCode(String registerBillCode) {
        this.registerBillCode = registerBillCode;
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

    public String getDetectBatchNo() {
        return detectBatchNo;
    }

    public void setDetectBatchNo(String detectBatchNo) {
        this.detectBatchNo = detectBatchNo;
    }

    public String getNormalResult() {
        return normalResult;
    }

    public void setNormalResult(String normalResult) {
        this.normalResult = normalResult;
    }

    public String getDetectCompany() {
        return detectCompany;
    }

    public void setDetectCompany(String detectCompany) {
        this.detectCompany = detectCompany;
    }

    public Long getDetectRequestId() {
        return detectRequestId;
    }

    public void setDetectRequestId(Long detectRequestId) {
        this.detectRequestId = detectRequestId;
    }
}