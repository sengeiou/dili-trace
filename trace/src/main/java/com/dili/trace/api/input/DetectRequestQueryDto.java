package com.dili.trace.api.input;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.DetectRequest;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 检测请求查询实体
 */
public class DetectRequestQueryDto extends DetectRequest {


    /**
     * 商品名称/商户名称LIKE
     */
    @Transient
    private String likeProductNameOrUserName;
    @Transient
    private Date created;
    /**
     * 报备单是否删除过滤
     */
    @Transient
    private Integer isDeleted;

    /**
     * 市场过滤
     */
    @Transient
    private Long marketId;

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
     * 报备单ID
     */
    @Column(name = "`bill_id`")
    @Operator(Operator.IN)
    private List<Long> billIdList;

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

    @Transient
    private Long userId;

    @Transient
    /**
     * 采样来源过滤条件 {@link com.dili.trace.glossary.SampleSourceEnum}
     */
    private Integer sampleSource;


    @Transient
    private Integer billType;


    /**
     * 业户名称/商品名称（查询条件 OR Like）
     */
    @Transient
    private String keyword;
    @Transient
    private Integer detectStatus;
    @Transient
    private List<Integer>sampleSourceList;
    @Transient
    private List<Integer> detectStatusList;
    @Transient
    private List<Integer> detectTypeList;
    @Transient
    private List<Integer> detectResultList;

//=============================



    /**
     * 报备单编号查询条件
     */
    @Transient
    private String likeBillCode;


    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }





    public List<Integer> getDetectStatusList() {
        return detectStatusList;
    }

    public void setDetectStatusList(List<Integer> detectStatusList) {
        this.detectStatusList = detectStatusList;
    }

    public List<Integer> getDetectTypeList() {
        return detectTypeList;
    }

    public void setDetectTypeList(List<Integer> detectTypeList) {
        this.detectTypeList = detectTypeList;
    }

    public List<Integer> getDetectResultList() {
        return detectResultList;
    }

    public void setDetectResultList(List<Integer> detectResultList) {
        this.detectResultList = detectResultList;
    }

    public List<Integer> getSampleSourceList() {
        return sampleSourceList;
    }

    public void setSampleSourceList(List<Integer> sampleSourceList) {
        this.sampleSourceList = sampleSourceList;
    }

    public String getLikeProductNameOrUserName() {
        return likeProductNameOrUserName;
    }

    public void setLikeProductNameOrUserName(String likeProductNameOrUserName) {
        this.likeProductNameOrUserName = likeProductNameOrUserName;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getSampleSource() {
        return sampleSource;
    }

    public void setSampleSource(Integer sampleSource) {
        this.sampleSource = sampleSource;
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

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
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

    public Integer getDetectStatus() {
        return detectStatus;
    }

    public void setDetectStatus(Integer detectStatus) {
        this.detectStatus = detectStatus;
    }

    public String getLikeBillCode() {
        return likeBillCode;
    }

    public void setLikeBillCode(String likeBillCode) {
        this.likeBillCode = likeBillCode;
    }

    public List<Long> getBillIdList() {
        return billIdList;
    }

    public void setBillIdList(List<Long> billIdList) {
        this.billIdList = billIdList;
    }
}