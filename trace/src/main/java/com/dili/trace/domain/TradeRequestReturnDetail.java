package com.dili.trace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.dili.ss.domain.BaseDomain;

public class TradeRequestReturnDetail extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;
    @Column(name = "`trade_request_id`")
    private Long tradeRequestId;
    @Column(name = "`return_status`")
    private Integer returnStatus;
    @Column(name = "`reason`")
    private String reason;
    @Column(name = "`created`")
    private Date created;
    @Column(name = "`modified`")
    private Date modified;
    @Column(name = "`operator_name`")
    private String operatorName;
    @Column(name = "`operator_id`")
    private Long operatorId;

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Long return the tradeRequestId
     */
    public Long getTradeRequestId() {
        return tradeRequestId;
    }

    /**
     * @param tradeRequestId the tradeRequestId to set
     */
    public void setTradeRequestId(Long tradeRequestId) {
        this.tradeRequestId = tradeRequestId;
    }

    /**
     * @return Integer return the returnStatus
     */
    public Integer getReturnStatus() {
        return returnStatus;
    }

    /**
     * @param returnStatus the returnStatus to set
     */
    public void setReturnStatus(Integer returnStatus) {
        this.returnStatus = returnStatus;
    }

    /**
     * @return String return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return Date return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return Date return the modified
     */
    public Date getModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }

    /**
     * @return String return the operatorName
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName the operatorName to set
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * @return Long return the operatorId
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId the operatorId to set
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

}