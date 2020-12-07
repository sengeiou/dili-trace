package com.dili.trace.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Like;
import com.dili.trace.enums.TradeReturnStatusEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "`trade_request`")
public class TradeRequest extends BaseDomain {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JSONField(serialize = false)
    private Long id;

    /**
     * 交易单request编号
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 商品名称
     */
    @Column(name = "`product_name`")
    @Like
    private String productName;

    /**
     * 重量单位
     */
    @Column(name = "`weight_unit`")
    private Integer weightUnit;

    /**
     * 交易单id
     */
    @Column(name = "`trade_order_Id`")
    private Long tradeOrderId;

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
    @Like
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
    @Like
    private String sellerName;

    /**
     * 卖家市场
     */
    @ApiModelProperty(value = "卖家市场")
    @Column(name = "`seller_market_id`")
    private Long sellerMarketId;

    /**
     * 交易重量
     */
    @ApiModelProperty(value = "交易重量")
    @Column(name = "`trade_weight`")
    private BigDecimal tradeWeight;

    /**
     * 批次库存ID
     */
    @ApiModelProperty(value = "批次库存ID")
    @Column(name = "`product_stock_id`")
    private Long productStockId;

    // @ApiModelProperty(value = "类型")
    // @Column(name = "`trade_request_type`")
    // private Integer tradeRequestType;

    // @ApiModelProperty(value = "交易状态")
    // @Column(name = "`trade_status`")
    // private Integer tradeStatus;

    /**
     * 退货状态
     */
    @ApiModelProperty(value = "退货状态")
    @Column(name = "`return_status`")
    private Integer returnStatus;

    /**
     * 原因
     */
    @ApiModelProperty(value = "原因")
    @Column(name = "`reason`")
    private String reason;

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
     * 交易确认时间
     */
    @Column(name = "`handle_time`")
    private Date handleTime;

    @Transient
    public Long getTradeRequestId() {
        return this.id;
    }

    /**
     * 交易单状态
     */
    @Transient
    private Integer orderStatus;

    /**
     * 交易单状态描述
     */
    @Transient
    private String orderStatusName;

    /**
     * 第三方交易编码
     */
    @Column(name = "`third_trade_no`")
    private String thirdTradeNo;

    /**
     * 批次号
     */
    @Column(name = "`batch_no`")
    private String batchNo;

    /**
     * 产地名
     */
    @Column(name = "`origin_name`")
    private String originName;

    /**
     * 仓位码
     */
    @Column(name = "`position_no`")
    private String positionNo;

    /**
     * 仓位名称
     */
    @Column(name = "`position_name`")
    private String positionName;

    /**
     * 成交价格
     */
    @Column(name = "`price`")
    private BigDecimal price;

    /**
     * 件数
     */
    @Column(name = "`package_number`")
    private Integer packageNumber;

    /**
     * 成交数量
     */
    @Column(name = "`number`")
    private Integer number;

    /**
     * 成交金额
     */
    @Column(name = "`amount`")
    private BigDecimal amount;

    /**
     *
     */
    @Column(name = "`pos_no`")
    private String posNo;

    /**
     * POS机号
     */
    @Column(name = "`pay_way`")
    private String payWay;

    /**
     * 总金额
     */
    @Column(name = "`total_amount`")
    private BigDecimal totalAmount;

    /**
     * 营业员
     */
    @Column(name = "`operator`")
    private String operator;

    /**
     * 收款员
     */
    @Column(name = "`payer`")
    @Like
    private String payer;

    /**
     * 支付流水号
     */
    @Column(name = "`pay_no`")
    private String payNo;

    /**
     * 来源类型
     */
    @Column(name = "`source_type`")
    private Integer sourceType;

    /**
     * 上报标志位
     */
    @Column(name = "`report_flag`")
    private Integer reportFlag;

    /**
     * 交易时间
     */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderDate;

    /**
     * 买家第三方卡号
     */
    @Transient
    private String buyerNo;

    /**
     * 卖家第三方卡号
     */
    @Transient
    private String sellerNo;

    /**
     * 商品码
     */
    @Transient
    private String productCode;

    /**
     * 交易时间开始
     */
    @Transient
    private Date orderDateStart;

    /**
     * 交易时间结束
     */
    @Transient
    private Date orderDateEnd;

    /**
     * 上报标志位描述
     */
    @Transient
    private String reportFlagStr;

    public String getReportFlagStr() {
        return reportFlagStr;
    }

    public void setReportFlagStr(String reportFlagStr) {
        this.reportFlagStr = reportFlagStr;
    }

    public Date getOrderDateStart() {
        return orderDateStart;
    }

    public void setOrderDateStart(Date orderDateStart) {
        this.orderDateStart = orderDateStart;
    }

    public Date getOrderDateEnd() {
        return orderDateEnd;
    }

    public void setOrderDateEnd(Date orderDateEnd) {
        this.orderDateEnd = orderDateEnd;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getBuyerNo() {
        return buyerNo;
    }

    public void setBuyerNo(String buyerNo) {
        this.buyerNo = buyerNo;
    }

    public String getSellerNo() {
        return sellerNo;
    }

    public void setSellerNo(String sellerNo) {
        this.sellerNo = sellerNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getReportFlag() {
        return reportFlag;
    }

    public void setReportFlag(Integer reportFlag) {
        this.reportFlag = reportFlag;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(Integer packageNumber) {
        this.packageNumber = packageNumber;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(String positionNo) {
        this.positionNo = positionNo;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPosNo() {
        return posNo;
    }

    public void setPosNo(String posNo) {
        this.posNo = posNo;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getThirdTradeNo() {
        return thirdTradeNo;
    }

    public void setThirdTradeNo(String thirdTradeNo) {
        this.thirdTradeNo = thirdTradeNo;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    @Transient
    public String getWeightUnitName() {
        return WeightUnitEnum.fromCode(this.getWeightUnit()).map(WeightUnitEnum::getName).orElse("");
    }

    @Transient
    public String getReturnStatusName() {
        return TradeReturnStatusEnum.fromCode(this.getReturnStatus()).map(TradeReturnStatusEnum::getName).orElse("");
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

    // /**
    // * @return Integer return the tradeRequestType
    // */
    // public Integer getTradeRequestType() {
    // return tradeRequestType;
    // }

    // /**
    // * @param tradeRequestType the tradeRequestType to set
    // */
    // public void setTradeRequestType(Integer tradeRequestType) {
    // this.tradeRequestType = tradeRequestType;
    // }

    // /**
    // * @return Integer return the tradeStatus
    // */
    // public Integer getTradeStatus() {
    // return tradeStatus;
    // }

    // /**
    // * @param tradeStatus the tradeStatus to set
    // */
    // public void setTradeStatus(Integer tradeStatus) {
    // this.tradeStatus = tradeStatus;
    // }

    public Long getProductStockId() {
        return productStockId;
    }

    public void setProductStockId(Long productStockId) {
        this.productStockId = productStockId;
    }

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

    /**
     * @return Long return the code
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
     * @return Integer return the weightUnit
     */
    public Integer getWeightUnit() {
        return weightUnit;
    }

    /**
     * @param weightUnit the weightUnit to set
     */
    public void setWeightUnit(Integer weightUnit) {
        this.weightUnit = weightUnit;
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