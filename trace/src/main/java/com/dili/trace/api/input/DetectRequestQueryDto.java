package com.dili.trace.api.input;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.DetectRequest;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 检测请求查询实体
 */
public class DetectRequestQueryDto extends DetectRequest {

    /**
     * 开始时间
     */
    private Date createdStart;

    /**
     * 查询登记结束时间
     */
    private Date createdEnd;

    /**
     * 商品名称/商户名称LIKE
     */
    private String likeProductNameOrUserName;

    /**
     * 市场过滤
     */
    private Long marketId;

    /**
     * 报备单是否删除过滤
     */
    private Integer isDeleted;

    /**
     * 业户名称/商品名称（查询条件 OR Like）
     */
    @Transient
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * 采样来源过滤条件 {@link com.dili.trace.glossary.SampleSourceEnum}
     */
    private Integer sampleSource;

    private List<Integer>sampleSourceList;
    @Transient
    private List<Integer>detectStatusList;
    @Transient
    private List<Integer>detectTypeList;
    @Transient
    private List<Integer>detectResultList;

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
}