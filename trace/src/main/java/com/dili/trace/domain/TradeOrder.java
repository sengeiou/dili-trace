package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("serial")
@Table(name = "`trade_order`")
public class TradeOrder extends BaseDomain {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

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
     * 买家市场
     */
    @ApiModelProperty(value = "买家市场")
    @Column(name = "`buyer_market_id`")
    private Long buyerMarketId;

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
     * 卖家市场
     */
    @ApiModelProperty(value = "卖家市场")
    @Column(name = "`seller_market_id`")
    private Long sellerMarketId;

    /**
     * 交易状态
     */
    @ApiModelProperty(value = "交易状态")
    @Column(name = "`order_status`")
    private Integer orderStatus;

    /**
     * 交易类型
     */
    @ApiModelProperty(value = "交易类型")
    @Column(name = "`order_type`")
    private Integer orderType;

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
     * 买家类型
     */
    @ApiModelProperty(value = "买家type")
    @Column(name = "`buyer_type`")
    private Integer buyerType;

    /**
     * 第三方流水号
     */
    @ApiModelProperty(value = "第三方流水号")
    @Transient
    private String tradeNo;

    @Transient
    public Long getTradeOrderId() {
        return this.id;
    }

    public Integer getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(Integer buyerType) {
        this.buyerType = buyerType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
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

    public Long getBuyerMarketId() {
        return buyerMarketId;
    }

    public void setBuyerMarketId(Long buyerMarketId) {
        this.buyerMarketId = buyerMarketId;
    }

    public Long getSellerMarketId() {
        return sellerMarketId;
    }

    public void setSellerMarketId(Long sellerMarketId) {
        this.sellerMarketId = sellerMarketId;
    }
}