package com.dili.trace.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@SuppressWarnings("serial")
@Table(name = "`trade_detail`")
public class TradeDetail extends BaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;

	@ApiModelProperty(value = "分销自")
	@Column(name = "`parent_id`")
	private Long parentId;

	@ApiModelProperty(value = "最初登记单ID")
	@Column(name = "`bill_id`")
	private Long billId;

	@ApiModelProperty(value = "进场审核ID")
	@Column(name = "`checkin_record_id`")
	private Long checkinRecordId;

	@ApiModelProperty(value = "出场审核ID")
	@Column(name = "`checkout_record_id`")
	private Long checkoutRecordId;

	@Column(name = "`checkin_status`")
	private Integer checkinStatus;

	@Column(name = "`checkout_status`")
	private Integer checkoutStatus;

	@Column(name = "`sale_status`")
	private Integer saleStatus;

	@Column(name = "`trade_type`")
	private Integer tradeType;

	// @Column(name = "`status`")
	// private Integer status;

	@ApiModelProperty(value = "买家ID")
	@Column(name = "`buyer_id`")
	private Long buyerId;

	@ApiModelProperty(value = "买家姓名")
	@Column(name = "`buyer_name`")
	private String buyerName;

	@ApiModelProperty(value = "卖家ID")
	@Column(name = "`seller_id`")
	private Long sellerId;

	@ApiModelProperty(value = "卖家姓名")
	@Column(name = "`seller_name`")
	private String sellerName;

	@ApiModelProperty(value = "商品名称")
	@Column(name = "`product_name`")
	private String productName;

	@ApiModelProperty(value = "库存重量")
	@Column(name = "`stock_weight`")
	private BigDecimal stockWeight;

	@ApiModelProperty(value = "总重量")
	@Column(name = "`total_weight`")
	private BigDecimal totalWeight;

	@ApiModelProperty(value = "重量单位")
	@Column(name = "`weight_unit`")
	private Integer weightUnit;

	@ApiModelProperty(value = "批次库存ID")
	@Column(name = "`batch_stock_id`")
	private Long batchStockId;

	@ApiModelProperty(value = "批次交易ID")
	@Column(name = "`trade_request_id`")
	private Long tradeRequestId;

	@Column(name = "`created`")
	private Date created;

	@Column(name = "`modified`")
	private Date modified;

	// public Integer getStatus() {
	// 	return status;
	// }

	// public void setStatus(Integer status) {
	// 	this.status = status;
	// }

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

	/**
	 * @return Long return the batchStockId
	 */
	public Long getBatchStockId() {
		return batchStockId;
	}

	/**
	 * @param batchStockId the batchStockId to set
	 */
	public void setBatchStockId(Long batchStockId) {
		this.batchStockId = batchStockId;
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

}