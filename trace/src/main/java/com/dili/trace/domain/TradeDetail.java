package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.enums.WeightUnitEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * <p>
 * This file was generated on 2019-07-26 09:20:35.
 */
@SuppressWarnings("serial")
@Table(name = "`trade_detail`")
public class TradeDetail extends BaseDomain {
    /**
     * 交易明细id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    /**
     * 分销自
     */
    @ApiModelProperty(value = "分销自")
    @Column(name = "`parent_id`")
    private Long parentId;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    @Column(name = "`batch_no`")
    private String batchNo;

    /**
     * 父批次号
     */
    @ApiModelProperty(value = "父批次号")
    @Column(name = "`parent_batch_no`")
    private String parentBatchNo;

    /**
     * 最初登记单ID
     */
    @ApiModelProperty(value = "最初登记单ID")
    @Column(name = "`bill_id`")
    private Long billId;

    /**
     * 进场审核ID
     */
    @ApiModelProperty(value = "进场审核ID")
    @Column(name = "`checkin_record_id`")
    private Long checkinRecordId;

    /**
     * 出场审核ID
     */
    @ApiModelProperty(value = "出场审核ID")
    @Column(name = "`checkout_record_id`")
    private Long checkoutRecordId;

    /**
     * 进门状态
     */
    @Column(name = "`checkin_status`")
    private Integer checkinStatus;

    /**
     * 出门状态
     */
    @Column(name = "`checkout_status`")
    private Integer checkoutStatus;

    /**
     * 销售状态
     */
    @Column(name = "`sale_status`")
    private Integer saleStatus;

    /**
     * 交易类型
     */
    @Column(name = "`trade_type`")
    private Integer tradeType;

    /**
     * 是否批处理
     */
    @Column(name = "`is_batched`")
    private Integer isBatched;

    /**
     * 买家ID
     */
    @ApiModelProperty(value = "买家ID")
    @Column(name = "`buyer_id`")
    private Long buyerId;

    /**
     * 买家姓名
     */
    @ApiModelProperty(value = "买家姓名")
    @Column(name = "`buyer_name`")
    private String buyerName;

    /**
     * 卖家ID
     */
    @ApiModelProperty(value = "卖家ID")
    @Column(name = "`seller_id`")
    private Long sellerId;

    /**
     * 卖家姓名
     */
    @ApiModelProperty(value = "卖家姓名")
    @Column(name = "`seller_name`")
    private String sellerName;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    @Column(name = "`product_name`")
    private String productName;

    /**
     * 库存重量
     */
    @ApiModelProperty(value = "库存重量")
    @Column(name = "`stock_weight`")
    private BigDecimal stockWeight;

    /**
     * 总重量
     */
    @ApiModelProperty(value = "总重量")
    @Column(name = "`total_weight`")
    private BigDecimal totalWeight;

    /**
     * 重量单位
     */
    @ApiModelProperty(value = "重量单位")
    @Column(name = "`weight_unit`")
    private Integer weightUnit;

    /**
     * 批次库存ID
     */
    @ApiModelProperty(value = "批次库存ID")
    @Column(name = "`product_stock_id`")
    private Long productStockId;

    /**
     * 批次交易ID
     */
    @ApiModelProperty(value = "批次交易ID")
    @Column(name = "`trade_request_id`")
    private Long tradeRequestId;

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
     * 上架重量
     */
    @Column(name = "`pushaway_weight`")
    private BigDecimal pushawayWeight;

    /**
     * 库存锁定重量
     */
    @Column(name = "`soft_weight`")
    private BigDecimal softWeight;

    /**
     * 车牌
     */
    @Transient
    private String plate;

    /**
     * 查询检测开始时间
     */
    @ApiModelProperty(value = "查询检测开始时间")
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private String createdStart;

    /**
     * 查询检测结束时间
     */
    @ApiModelProperty(value = "查询检测结束时间")
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private String createdEnd;

    /**
     * 商品名称LIKE
     */
    @ApiModelProperty(value = "商品名称LIKE")
    @Column(name = "`product_name`")
    @Like
    private String likeProductName;

    /**
     * 第三方库存主键（扣减第三方库存需要使用）
     */
    @ApiModelProperty(value = "第三方库存主键")
    @Column(name = "`third_party_stock_id`")
    private Long thirdPartyStockId;

    @Transient
    public String getWeightUnitName() {
        return WeightUnitEnum.fromCode(this.getWeightUnit()).map(WeightUnitEnum::getName).orElse("");
    }
    // public Integer getStatus() {
    // 	return status;
    // }

    // public void setStatus(Integer status) {
    // 	this.status = status;
    // }
    @Transient
    public Long getTradeDetailId() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(Integer weightUnit) {
        this.weightUnit = weightUnit;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getCheckinRecordId() {
        return checkinRecordId;
    }

    public void setCheckinRecordId(Long checkinRecordId) {
        this.checkinRecordId = checkinRecordId;
    }

    public Long getCheckoutRecordId() {
        return checkoutRecordId;
    }

    public void setCheckoutRecordId(Long checkoutRecordId) {
        this.checkoutRecordId = checkoutRecordId;
    }

    public Integer getCheckinStatus() {
        return checkinStatus;
    }

    public void setCheckinStatus(Integer checkinStatus) {
        this.checkinStatus = checkinStatus;
    }

    public Integer getCheckoutStatus() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(Integer checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

    public Integer getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Integer saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public BigDecimal getStockWeight() {
        return stockWeight;
    }

    public void setStockWeight(BigDecimal stockWeight) {
        this.stockWeight = stockWeight;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Long getProductStockId() {
        return productStockId;
    }

    public void setProductStockId(Long productStockId) {
        this.productStockId = productStockId;
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
     * @return String return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }


    /**
     * @return Integer return the isBatched
     */
    public Integer getIsBatched() {
        return isBatched;
    }

    /**
     * @param isBatched the isBatched to set
     */
    public void setIsBatched(Integer isBatched) {
        this.isBatched = isBatched;
    }


    /**
     * @return String return the plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     * @param plate the plate to set
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }


    /**
     * @return String return the batchNo
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * @param batchNo the batchNo to set
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }


    /**
     * @return String return the parentBatchNo
     */
    public String getParentBatchNo() {
        return parentBatchNo;
    }

    /**
     * @param parentBatchNo the parentBatchNo to set
     */
    public void setParentBatchNo(String parentBatchNo) {
        this.parentBatchNo = parentBatchNo;
    }

    public BigDecimal getPushawayWeight() {
        return pushawayWeight;
    }

    public void setPushawayWeight(BigDecimal pushawayWeight) {
        this.pushawayWeight = pushawayWeight;
    }

    public BigDecimal getSoftWeight() {
        return softWeight;
    }

    public void setSoftWeight(BigDecimal softWeight) {
        this.softWeight = softWeight;
    }

    public String getCreatedStart() {
        return createdStart;
    }

    public void setCreatedStart(String createdStart) {
        this.createdStart = createdStart;
    }

    public String getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(String createdEnd) {
        this.createdEnd = createdEnd;
    }

    public String getLikeProductName() {
        return likeProductName;
    }

    public void setLikeProductName(String likeProductName) {
        this.likeProductName = likeProductName;
    }

    public Long getThirdPartyStockId() {
        return thirdPartyStockId;
    }

    public void setThirdPartyStockId(Long thirdPartyStockId) {
        this.thirdPartyStockId = thirdPartyStockId;
    }
}