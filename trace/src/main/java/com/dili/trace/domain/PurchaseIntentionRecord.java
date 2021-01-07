package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.enums.WeightUnitEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 买家进货意向记录
 * <p>
 * This file was generated on 2019-07-26 09:20:34.
 */
@Table(name = "`purchase_intention_record`")
public class PurchaseIntentionRecord extends BaseDomain {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 买家姓名
     */
    @ApiModelProperty(value = "买家姓名")
    @Column(name = "`buyer_name`")
    private String buyerName;

    /**
     * 买家电话
     */
    @ApiModelProperty(value = "买家电话")
    @Column(name = "`buyer_phone`")
    private String buyerPhone;


    /**
     * 买家ID
     */
    @ApiModelProperty(value = "买家ID")
    @Column(name = "`buyer_id`")
    private Long buyerId;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    @Column(name = "`product_name`")
    private String productName;


    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    @Column(name = "`product_id`")
    private Long productId;


    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    @Column(name = "`product_weight`")
    private BigDecimal productWeight;


    /**
     * 重量单位
     */
    @ApiModelProperty(value = "重量单位")
    @Column(name = "`weight_unit`")
    private Integer weightUnit;


    /**
     * 车牌
     */
    @ApiModelProperty(value = "车牌")
    @Column(name = "`plate`")
    private String plate;

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

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @Column(name = "`operator_name`")
    private String operatorName;

    /**
     * 操作人ID
     */
    @ApiModelProperty(value = "操作人ID")
    @Column(name = "`operator_id`")
    private Long operatorId;

    /**
     * 买家报备编号
     */
    @ApiModelProperty(value = "买家报备编号")
    @Column(name = "`code`")
    private String code;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    @Column(name = "`corporate_name`")
    private String corporateName;

    /**
     * 品牌ID
     */
    @ApiModelProperty(value = "品牌ID")
    @Column(name = "`brand_id`")
    private Long brandId;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    @Column(name = "`brand_name`")
    private String brandName;

    /**
     * 产地ID
     */
    @ApiModelProperty(value = "产地ID")
    @Column(name = "`origin_id`")
    private Long originId;

    /**
     * 产地
     */
    @ApiModelProperty(value = "产地")
    @Column(name = "`origin_name`")
    private String originName;

    /**
     * 重量单位名称
     */
    @Transient
    public String getWeightUnitName() {
        return WeightUnitEnum.toName(this.weightUnit);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public BigDecimal getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(BigDecimal productWeight) {
        this.productWeight = productWeight;
    }

    public Integer getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(Integer weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }
}