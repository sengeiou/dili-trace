package com.dili.trace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@Table(name = "`trade_order`")
public class TradeOrder extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Column(name = "`code`")
    private String code;

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

    
    @ApiModelProperty(value = "交易状态")
	@Column(name = "`order_status`")
    private Integer orderStatus;

    @ApiModelProperty(value = "交易类型")
	@Column(name = "`order_type`")
    private Integer orderType;

    @Column(name = "`created`")
    private Date created;

    @Column(name = "`modified`")
    private Date modified;

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
     * @return String return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
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
     * @return Integer return the orderStatus
     */
    public Integer getOrderStatus() {
        return orderStatus;
    }

    /**
     * @param orderStatus the orderStatus to set
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }


    /**
     * @return Integer return the orderType
     */
    public Integer getOrderType() {
        return orderType;
    }

    /**
     * @param orderType the orderType to set
     */
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

}