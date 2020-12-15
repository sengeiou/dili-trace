package com.dili.trace.dto;

import com.dili.trace.domain.RegisterBill;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Alvin.Li
 */
public class DetectRequestWithBillDto extends RegisterBill {

    private Long id;

    /**
     * 报备单ID
     */
    private Long billId;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 被指定检测员ID
     */
    private Long designatedId;

    /**
     * 被指定检测员姓名
     */
    private String designatedName;

    /**
     * 检测员ID
     */
    private Long detectorId;

    /**
     * 检测员姓名
     */
    private String detectorName;

    /**
     * 检测类型
     * {@link com.dili.trace.enums.DetectTypeEnum}
     */
    private Integer detectType;
    /**
     * 采样类型
     * {@link com.dili.trace.glossary.SampleSourceEnum}
     */
    private Integer detectSource;

    /**
     * 检测类型
     * {@link com.dili.trace.enums.DetectResultEnum}
     */
    private Integer detectResult;

    /**
     * 检测时间
     */
    private Date detectTime;

    /**
     * 检测费用
     */
    private BigDecimal detectFee;

    /**
     * 接单时间
     */
    private Date confirmTime;

    /**
     * 采样时间
     */
    private Date sampleTime;

    /**
     * 车牌号模糊查询条件
     */
    private String likePlate;

    /**
     * 经营户姓名模糊查询条件
     */
    private String likeName;

    /**
     * 经营户手机模糊查询条件
     */
    private String likePhone;

    /**
     * 报备单号模糊查询条件
     */
    private String likeCode;

    /**
     * 报备时间-开始查询条件
     */
    private Date createdStart;

    /**
     * 报备时间-结束查询条件
     */
    private Date createdEnd;

    /**
     * 进门时间-开始查询条件
     */
    private Date checkinCreatedStart;

    /**
     * 进门时间-结束查询条件
     */
    private Date checkinCreatedEnd;

    /**
     * 检测时间-开始查询条件
     */
    private Date detectTimeStart;

    /**
     * 检测时间-结束查询条件
     */
    private Date detectTimeEnd;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
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

    public Integer getDetectType() {
        return detectType;
    }

    public void setDetectType(Integer detectType) {
        this.detectType = detectType;
    }

    public Integer getDetectSource() {
        return detectSource;
    }

    public void setDetectSource(Integer detectSource) {
        this.detectSource = detectSource;
    }

    public Integer getDetectResult() {
        return detectResult;
    }

    public void setDetectResult(Integer detectResult) {
        this.detectResult = detectResult;
    }

    public Date getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(Date detectTime) {
        this.detectTime = detectTime;
    }

    public BigDecimal getDetectFee() {
        return detectFee;
    }

    public void setDetectFee(BigDecimal detectFee) {
        this.detectFee = detectFee;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public String getLikePlate() {
        return likePlate;
    }

    public void setLikePlate(String likePlate) {
        this.likePlate = likePlate;
    }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public String getLikeCode() {
        return likeCode;
    }

    public void setLikeCode(String likeCode) {
        this.likeCode = likeCode;
    }

    public Date getCreatedStart() {
        return createdStart;
    }

    public void setCreatedStart(Date createdStart) {
        this.createdStart = createdStart;
    }

    public Date getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(Date createdEnd) {
        this.createdEnd = createdEnd;
    }

    public Date getCheckinCreatedStart() {
        return checkinCreatedStart;
    }

    public void setCheckinCreatedStart(Date checkinCreatedStart) {
        this.checkinCreatedStart = checkinCreatedStart;
    }

    public Date getCheckinCreatedEnd() {
        return checkinCreatedEnd;
    }

    public void setCheckinCreatedEnd(Date checkinCreatedEnd) {
        this.checkinCreatedEnd = checkinCreatedEnd;
    }

    public Date getDetectTimeStart() {
        return detectTimeStart;
    }

    public void setDetectTimeStart(Date detectTimeStart) {
        this.detectTimeStart = detectTimeStart;
    }

    public Date getDetectTimeEnd() {
        return detectTimeEnd;
    }

    public void setDetectTimeEnd(Date detectTimeEnd) {
        this.detectTimeEnd = detectTimeEnd;
    }

    public String getLikePhone() {
        return likePhone;
    }

    public void setLikePhone(String likePhone) {
        this.likePhone = likePhone;
    }
}
