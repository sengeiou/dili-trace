package com.dili.trace.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.dto.IBaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
@Table(name = "`trade_push_log`")
public class TradePushLog extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JSONField(serialize =false)
    private Long id;

    @ApiModelProperty(value = "批次主键")
    @Column(name = "`trade_detail_id`")
    private Long tradeDetailId;

    @ApiModelProperty(value = "0:下架 1:上架")
    @Column(name = "`log_type`")
    private Integer logType;

    @ApiModelProperty(value = "商品名称")
    @Column(name = "`product_name`")
    private String productName;

    @ApiModelProperty(value = "上下架重量")
    @Column(name = "`operation_weight`")
    private BigDecimal operationWeight;

    @ApiModelProperty(value = "上下架原因")
    @Column(name = "`operation_reason`")
    private BigDecimal operationReason;

    @ApiModelProperty(value = "0：报备单 1：交易单")
    @Column(name = "`order_type`")
    private Integer orderType;

    @ApiModelProperty(value = "单据主键id")
    @Column(name = "`order_id`")
    private Long orderId;

    @ApiModelProperty(value = "商户 id")
    @Column(name = "`user_id`")
    private Long userId;

    @ApiModelProperty(value = "库存 id")
    @Column(name = "`product_stock_id`")
    private Long productStockId;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

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

    public BigDecimal getOperationReason() {
        return operationReason;
    }

    public void setOperationReason(BigDecimal operationReason) {
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
}
