package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.DetectRequest;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author Alvin.Li
 */
public class DetectRequestDto extends DetectRequest {

    /**
     * 查询检测请求开始时间
     */
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private Date createdStart;

    /**
     * 查询检测请求结束时间
     */
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private Date createdEnd;

    /**
     * 创建人
     */
    @Column(name = "creator_name")
    @Like
    private String likeCreatorName;

    /**
     * 指定检测员
     */
    @Column(name = "designated_name")
    @Like
    private String likeDesignatedName;

    /**
     * 检测员
     */
    @Column(name = "detector_name")
    @Like
    private String likeDetectorName;

    /**
     * 报备单编号查询条件
     */
    @Transient
    private String likeBillCode;

    /**
     * 报备单编号
     */
    @Transient
    private String billCode;

    /**
     * 检测状态
     */
    @Transient
    private Integer detectStatus;

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

    public String getLikeBillCode() {
        return likeBillCode;
    }

    public void setLikeBillCode(String likeBillCode) {
        this.likeBillCode = likeBillCode;
    }

    public String getLikeCreatorName() {
        return likeCreatorName;
    }

    public void setLikeCreatorName(String likeCreatorName) {
        this.likeCreatorName = likeCreatorName;
    }

    public String getLikeDesignatedName() {
        return likeDesignatedName;
    }

    public void setLikeDesignatedName(String likeDesignatedName) {
        this.likeDesignatedName = likeDesignatedName;
    }

    public String getLikeDetectorName() {
        return likeDetectorName;
    }

    public void setLikeDetectorName(String likeDetectorName) {
        this.likeDetectorName = likeDetectorName;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public Integer getDetectStatus() {
        return detectStatus;
    }

    public void setDetectStatus(Integer detectStatus) {
        this.detectStatus = detectStatus;
    }
}
