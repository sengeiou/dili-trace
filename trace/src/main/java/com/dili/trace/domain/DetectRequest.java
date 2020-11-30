package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;

/**
 * 检测请求创建时间
 */
@Table(name = "`detect_request`")
public class DetectRequest extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    /**
     * 报备单ID
     */
    @Column(name = "`bill_id`")
    private Long billId;

    /**
     * 创建人ID
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    /**
     * 创建人姓名
     */
    @Column(name = "`creator_name`")
    private String creatorName;

    /**
     * 被指定检测员ID
     */
    @Column(name = "`designated_id`")
    private Long designatedId;

    /**
     * 被指定检测员姓名
     */
    @Column(name = "`designated_name`")
    private String designatedName;


    /**
     * 检测员ID
     */
    @Column(name = "`detector_id`")
    private Long detectorId;

    /**
     * 检测员姓名
     */
    @Column(name = "`detector_name`")
    private String detectorName;

    /**
     * 检测类型
     * {@link com.dili.trace.enums.DetectTypeEnum}
     */
    @Column(name = "`detect_type`")
    private Integer detectType;


    /**
     * 检测状态
     * {@link com.dili.trace.enums.DetectRequestStatusEnum}
     */
    @Column(name = "`detect_request_status`")
    private Integer detectRequestStatus;


    @Column(name = "`created`")
    private Date created;

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

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getDesignatedId() {
        return designatedId;
    }

    public void setDesignatedId(Long designatedId) {
        this.designatedId = designatedId;
    }

    public String getDesignatedName() {
        return designatedName;
    }

    public void setDesignatedName(String designatedName) {
        this.designatedName = designatedName;
    }

    public Integer getDetectType() {
        return detectType;
    }

    public void setDetectType(Integer detectType) {
        this.detectType = detectType;
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

    public Integer getDetectRequestStatus() {
        return detectRequestStatus;
    }

    public void setDetectRequestStatus(Integer detectRequestStatus) {
        this.detectRequestStatus = detectRequestStatus;
    }

    public Long getDetectorId() {
        return detectorId;
    }

    public void setDetectorId(Long detectorId) {
        this.detectorId = detectorId;
    }

    public String getDetectorName() {
        return detectorName;
    }

    public void setDetectorName(String detectorName) {
        this.detectorName = detectorName;
    }
}
