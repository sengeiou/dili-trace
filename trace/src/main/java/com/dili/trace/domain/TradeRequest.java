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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JSONField(serialize = false)
    private Long id;

    @Column(name = "`code`")
    private String code;

    @Column(name = "`product_name`")
    @Like
    private String productName;

    @Column(name = "`weight_unit`")
    private Integer weightUnit;

    @Column(name = "`trade_order_Id`")
    private Long tradeOrderId;

    @ApiModelProperty(value = "买家ID")
    @Column(name = "`buyer_id`")
    private Long buyerId;

    @ApiModelProperty(value = "买家姓名")
    @Column(name = "`buyer_name`")
    @Like
    private String buyerName;

    @ApiModelProperty(value = "卖家ID")
    @Column(name = "`seller_id`")
    private Long sellerId;

    @ApiModelProperty(value = "卖家姓名")
    @Column(name = "`seller_name`")
    @Like
    private String sellerName;

    @ApiModelProperty(value = "交易重量")
    @Column(name = "`trade_weight`")
    private BigDecimal tradeWeight;

    @ApiModelProperty(value = "批次库存ID")
    @Column(name = "`product_stock_id`")
    private Long productStockId;

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

    @Column(name = "`handle_time`")
    private Date handleTime;

    @Transient
    public Long getTradeRequestId() {
        return this.id;
    }

    @Transient
    private Integer orderStatus;

    @Transient
    private String orderStatusName;

    @Column(name = "`third_trade_no`")
    private String thirdTradeNo;

    @Column(name = "`batch_no`")
    private String batchNo;

    @Column(name = "`origin_name`")
    private String originName;

    @Column(name = "`position_no`")
    private String positionNo;

    @Column(name = "`position_name`")
    private String positionName;

    @Column(name = "`price`")
    private BigDecimal price;

    @Column(name = "`package_number`")
    private Integer packageNumber;

    @Column(name = "`number`")
    private Integer number;

    @Column(name = "`amount`")
    private BigDecimal amount;

    @Column(name = "`pos_no`")
    private String posNo;

    @Column(name = "`pay_way`")
    private String payWay;

    @Column(name = "`total_amount`")
    private BigDecimal totalAmount;

    @Column(name = "`operator`")
    private String operator;

    @Column(name = "`payer`")
    @Like
    private String payer;

    @Column(name = "`pay_no`")
    private String payNo;

    @Column(name = "`source_type`")
    private Integer sourceType;

    @Column(name = "`report_flag`")
    private Integer reportFlag;

    @Column(name = "`order_date`")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderDate;

    @Column(name = "`buyer_no`")
    @Like
    private String buyerNo;

    @Column(name = "`seller_no`")
    @Like
    private String sellerNo;

    @Column(name = "`product_code`")
    @Like
    private String productCode;

    @Transient
    private Date orderDateStart;

    @Transient
    private Date orderDateEnd;

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

}