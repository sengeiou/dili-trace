package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.dto.IBaseDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
@Table(name = "`trade_push_log`")
public class TradePushLog extends BaseDomain {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JsonIgnore
    private Long id;

    /**
     * 批次主键
     */
    @ApiModelProperty(value = "批次主键")
    @Column(name = "`trade_detail_id`")
    private Long tradeDetailId;

    /**
     * 上下架类型（0:下架 1:上架）
     */
    @ApiModelProperty(value = "0:下架 1:上架")
    @Column(name = "`log_type`")
    private Integer logType;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    @Column(name = "`product_name`")
    private String productName;

    /**
     * 上下架重量
     */
    @ApiModelProperty(value = "上下架重量")
    @Column(name = "`operation_weight`")
    private BigDecimal operationWeight;

    /**
     * 上下架原因
     */
    @ApiModelProperty(value = "上下架原因")
    @Column(name = "`operation_reason`")
    private String operationReason;

    /**
     * 上下架订单类型（0：报备单 1：交易单）
     */
    @ApiModelProperty(value = "0：报备单 1：交易单")
    @Column(name = "`order_type`")
    private Integer orderType;

    /**
     * 交易单ID
     */
    @ApiModelProperty(value = "单据主键id")
    @Column(name = "`order_id`")
    private Long orderId;

    /**
     * 交易单编号
     */
    @ApiModelProperty(value = "单据单号")
    @Column(name = "`order_code`")
    private String orderCode;

    /**
     * 业户ID
     */
    @ApiModelProperty(value = "商户 id")
    @Column(name = "`user_id`")
    private Long userId;

    /**
     * 库存ID
     */
    @ApiModelProperty(value = "库存 id")
    @Column(name = "`product_stock_id`")
    private Long productStockId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @Column(name = "`modified`")
    private Date modified;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getTradeDetailId() {
        return tradeDetailId;
    }

    public void setTradeDetailId(Long tradeDetailId) {
        this.tradeDetailId = tradeDetailId;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getOperationWeight() {
        return operationWeight;
    }

    public void setOperationWeight(BigDecimal operationWeight) {
        this.operationWeight = operationWeight;
    }

    public String getOperationReason() {
        return operationReason;
    }

    public void setOperationReason(String operationReason) {
        this.operationReason = operationReason;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductStockId() {
        return productStockId;
    }

    public void setProductStockId(Long productStockId) {
        this.productStockId = productStockId;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
