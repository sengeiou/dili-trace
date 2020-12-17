package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.DetectStatusEnum;
import com.dili.trace.enums.WeightUnitEnum;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    /**
     * 商品名称
     */
    @Transient
    private String productName;

    /**
     * 业户名称
     */
    @Transient
    private String name;

    /**
     * 业户名称/商品名称（查询条件 OR Like）
     */
    @Transient
    private String keyword;

    /**
     * 摊位号
     */
    @Transient
    private String tallyAreaNo;

    /**
     * 重量
     */
    @Transient
    private BigDecimal weight;

    /**
     * 重量单位
     */
    @Transient
    private Integer weightUnit;

    /**
     * 审核人
     */
    @Transient
    private String operatorName;

    /**
     * 报备时间
     */
    @Transient
    private Date billCreated;

    /**
     * 审核时间
     */
    @Transient
    private Date billOperationTime;

    /**
     * 报备单修改时间
     */
    @Transient
    private Date billModified;

    /**
     * 产地
     */
    @Transient
    private String originName;

    /**
     * 报备单审核状态
     */
    @Transient
    private Integer verifyStatus;

    /**
     * 车牌号
     */
    @Transient
    private String plate;

    /**
     * 产地证明图片地址
     */
    @Transient
    private String originCertifiyUrl;

    /**
     * 上游企业名称
     */
    @Transient
    private String upStreamName;

    @Transient
    private Long marketId;

    @Transient
    private List<DetectRecord> detectRecordList;

    @Transient
    private String productAliasName;

    @Transient
    private Long productId;

    @Transient
    private Long originId;

    /**
     * 报备单类型
     */
    @Transient
    private Integer billType;
    /**
     * 报备单是否删除过滤
     */
    private Integer isDeleted;

    @Transient
    private List<Integer>detectStatusList;
    @Transient
    private List<Integer>detectTypeList;
    @Transient
    private List<Integer>detectResultList;

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    /**
     * 所有图片列表
     */
    @Transient
    private List<ImageCert> imageCertList;

    public List<ImageCert> getImageCertList() {
        return imageCertList;
    }

    public void setImageCertList(List<ImageCert> imageCertList) {
        this.imageCertList = imageCertList;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductAliasName() {
        return productAliasName;
    }

    public void setProductAliasName(String productAliasName) {
        this.productAliasName = productAliasName;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public List<DetectRecord> getDetectRecordList() {
        return detectRecordList;
    }

    public void setDetectRecordList(List<DetectRecord> detectRecordList) {
        this.detectRecordList = detectRecordList;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Date getBillCreated() {
        return billCreated;
    }

    public void setBillCreated(Date billCreated) {
        this.billCreated = billCreated;
    }

    public Date getBillOperationTime() {
        return billOperationTime;
    }

    public void setBillOperationTime(Date billOperationTime) {
        this.billOperationTime = billOperationTime;
    }

    public Date getBillModified() {
        return billModified;
    }

    public void setBillModified(Date billModified) {
        this.billModified = billModified;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getOriginCertifiyUrl() {
        return originCertifiyUrl;
    }

    public void setOriginCertifiyUrl(String originCertifiyUrl) {
        this.originCertifiyUrl = originCertifiyUrl;
    }

    public String getUpStreamName() {
        return upStreamName;
    }

    public void setUpStreamName(String upStreamName) {
        this.upStreamName = upStreamName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTallyAreaNo() {
        return tallyAreaNo;
    }

    public void setTallyAreaNo(String tallyAreaNo) {
        this.tallyAreaNo = tallyAreaNo;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(Integer weightUnit) {
        this.weightUnit = weightUnit;
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


    @Transient
    public String getWeightUnitName() {
        return WeightUnitEnum.fromCode(this.weightUnit).map(WeightUnitEnum::getName).orElse("");
    }

    @Transient
    public String getBillTypeName() {
        return BillTypeEnum.fromCode(this.billType).map(BillTypeEnum::getName).orElse("");
    }
    @Transient
    public String getDetectStatusName() {
        return DetectStatusEnum.name(this.getDetectStatus());
    }
    @Transient
    public String getVerifyStatusName() {
        return BillVerifyStatusEnum.name(this.getVerifyStatus());
    }
}

