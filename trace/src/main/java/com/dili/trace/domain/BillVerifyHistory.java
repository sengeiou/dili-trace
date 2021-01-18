package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`bill_verify_history`")
public class BillVerifyHistory extends BaseDomain {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;
    /**
     * 登记单ID
     */
    @Column(name = "`bill_id`")
    private Long billId;
    /**
     * 审核时间
     */
    @Column(name = "`verify_date_time`")
    private Date verifyDateTime;
    /*
    *审核人ID
     */
    @Column(name = "`verify_operator_id`")
    private Long verifyOperatorId;
    /**
     * 审核人姓名
     */
    @Column(name = "`verify_operator_name`")
    private String verifyOperatorName;
    /**
     * 审核前状态值
     */
    @Column(name = "`previous_verify_status`")
    private Integer previousVerifyStatus;
    /**
     * 创建时间
     */
    @Column(name = "`created`")
    private Date created;

    /**
     * 修改时间
     */
    @Column(name = "`modified`")
    private Date modified;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public Integer getPreviousVerifyStatus() {
        return previousVerifyStatus;
    }

    public void setPreviousVerifyStatus(Integer previousVerifyStatus) {
        this.previousVerifyStatus = previousVerifyStatus;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Date getVerifyDateTime() {
        return verifyDateTime;
    }

    public void setVerifyDateTime(Date verifyDateTime) {
        this.verifyDateTime = verifyDateTime;
    }

    public Long getVerifyOperatorId() {
        return verifyOperatorId;
    }

    public void setVerifyOperatorId(Long verifyOperatorId) {
        this.verifyOperatorId = verifyOperatorId;
    }

    public String getVerifyOperatorName() {
        return verifyOperatorName;
    }

    public void setVerifyOperatorName(String verifyOperatorName) {
        this.verifyOperatorName = verifyOperatorName;
    }
}
