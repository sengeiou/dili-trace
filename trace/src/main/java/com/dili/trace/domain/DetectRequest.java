package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.DetectTypeEnum;
import com.dili.trace.glossary.SampleSourceEnum;

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
     * 采样类型
     * {@link com.dili.trace.glossary.SampleSourceEnum}
     */
    @Column(name = "`detect_source`")
    private Integer detectSource;

    /**
     * 检测类型
     * {@link com.dili.trace.enums.DetectResultEnum}
     */
    @Column(name = "`detect_result`")
    private Integer detectResult;

    @Column(name = "`created`")
    private Date created;

    @Column(name = "`modified`")
    private Date modified;

    @Transient
    public String getDetectSourceName() {
        return SampleSourceEnum.name(this.detectSource);
    }

    @Transient
    public String getDetectTypeName() {
        return DetectTypeEnum.name(this.detectType);
    }

    public Integer getDetectSource() {
        return detectSource;
    }

    public void setDetectSource(Integer detectSource) {
        this.detectSource = detectSource;
    }

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
    @Transient
    public String getDetectResultName() {
        return DetectResultEnum.name(this.detectResult);
    }
    public Integer getDetectResult() {
        return detectResult;
    }

    public void setDetectResult(Integer detectResult) {
        this.detectResult = detectResult;
    }
}
