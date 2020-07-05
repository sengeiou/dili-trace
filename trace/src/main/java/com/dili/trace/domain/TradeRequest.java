package com.dili.trace.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@SuppressWarnings("serial")
@Table(name = "`trade_request`")
public class TradeRequest extends BaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JSONField(serialize = false)
    private Long id;
    
    @Column(name = "`trade_order_Id`")
	private Long tradeOrderId;
	
	
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

	@ApiModelProperty(value = "交易重量")
	@Column(name = "`trade_weight`")
	private BigDecimal tradeWeight;
	
	@ApiModelProperty(value = "批次库存ID")
	@Column(name = "`batch_stock_id`")
    private Long batchStockId;
    
    // @ApiModelProperty(value = "类型")
	// @Column(name = "`trade_request_type`")
    // private Integer tradeRequestType;

    // @ApiModelProperty(value = "交易状态")
	// @Column(name = "`trade_status`")
    // private Integer tradeStatus;

    
    @ApiModelProperty(value = "退货状态")
	@Column(name = "`return_status`")
    private Integer returnStatus;

    @ApiModelProperty(value = "原因")
	@Column(name = "`reason`")
    private String reason;

	@Column(name = "`created`")
	private Date created;

	@Column(name = "`modified`")
    private Date modified;
    
    @Transient
    public Long getTradeRequestId(){
        return this.id;
    }
    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Long return the buyerId
     */
    public Long getBuyerId() {
        return buyerId;
    }

    /**
     * @param buyerId the buyerId to set
     */
    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    /**
     * @return String return the buyerName
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * @param buyerName the buyerName to set
     */
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    /**
     * @return Long return the sellerId
     */
    public Long getSellerId() {
        return sellerId;
    }

    /**
     * @param sellerId the sellerId to set
     */
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * @return String return the sellerName
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * @param sellerName the sellerName to set
     */
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    /**
     * @return BigDecimal return the tradeWeight
     */
    public BigDecimal getTradeWeight() {
        return tradeWeight;
    }

    /**
     * @param tradeWeight the tradeWeight to set
     */
    public void setTradeWeight(BigDecimal tradeWeight) {
        this.tradeWeight = tradeWeight;
    }

    /**
     * @return Date return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return Date return the modified
     */
    public Date getModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(Date modified) {
        this.modified = modified;
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


   


    // /**
    //  * @return Integer return the tradeRequestType
    //  */
    // public Integer getTradeRequestType() {
    //     return tradeRequestType;
    // }

    // /**
    //  * @param tradeRequestType the tradeRequestType to set
    //  */
    // public void setTradeRequestType(Integer tradeRequestType) {
    //     this.tradeRequestType = tradeRequestType;
    // }




    // /**
    //  * @return Integer return the tradeStatus
    //  */
    // public Integer getTradeStatus() {
    //     return tradeStatus;
    // }

    // /**
    //  * @param tradeStatus the tradeStatus to set
    //  */
    // public void setTradeStatus(Integer tradeStatus) {
    //     this.tradeStatus = tradeStatus;
    // }

    /**
     * @return Integer return the returnStatus
     */
    public Integer getReturnStatus() {
        return returnStatus;
    }

    /**
     * @param returnStatus the returnStatus to set
     */
    public void setReturnStatus(Integer returnStatus) {
        this.returnStatus = returnStatus;
    }


    /**
     * @return String return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }


    /**
     * @return Long return the tradeOrderId
     */
    public Long getTradeOrderId() {
        return tradeOrderId;
    }

    /**
     * @param tradeOrderId the tradeOrderId to set
     */
    public void setTradeOrderId(Long tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }

}