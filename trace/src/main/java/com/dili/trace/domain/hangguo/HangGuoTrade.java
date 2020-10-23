package com.dili.trace.domain.hangguo;

import javax.persistence.*;

/**
 * 杭果交易单
 *
 * @author asa.lee
 */
@Table(name = "third_hangguo_trade_data")
public class HangGuoTrade {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "order_date")
    private String orderDate;
    /**
     * 供应商
     */
    @Column(name = "supplier_no")
    private String supplierNo;
    @Column(name = "supplier_name")
    private String supplierName;
    /**
     * 报备单id
     */
    @Column(name = "register_no")
    private String registerNo;
    @Transient
    private String registerId;
    @Column(name = "batch_no")
    private String batchNo;
    @Column(name = "item_number")
    private String itemNumber;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "unit")
    private String unit;
    @Column(name = "origin_no")
    private String originNo;
    @Column(name = "origin_name")
    private String originName;
    @Column(name = "position_no")
    private String positionNo;
    @Column(name = "position_name")
    private String positionName;
    @Column(name = "price")
    private String price;
    @Column(name = "package_number")
    private String packageNumber;
    @Column(name = "number")
    private String number;
    @Column(name = "amount")
    private String amount;
    @Column(name = "weight")
    private String weight;
    @Column(name = "trade_no")
    private String tradeNo;
    @Column(name = "pos_no")
    private String posNo;
    @Column(name = "pay_way")
    private String payWay;
    @Column(name = "member_no")
    private String memberNo;
    @Column(name = "member_name")
    private String memberName;
    @Column(name = "total_amount")
    private String totalAmount;
    @Column(name = "operator")
    private String operator;
    @Column(name = "payer")
    private String payer;
    @Column(name = "pay_no")
    private String payNo;
    @Column(name = "handle_flag")
    private Integer handleFlag;
    @Column(name = "report_flag")
    private Integer reportFlag;
    @Column(name = "handle_remark")
    private String handleRemark;

    public Integer getHandleFlag() {
        return handleFlag;
    }

    public void setHandleFlag(Integer handleFlag) {
        this.handleFlag = handleFlag;
    }

    public Integer getReportFlag() {
        return reportFlag;
    }

    public void setReportFlag(Integer reportFlag) {
        this.reportFlag = reportFlag;
    }

    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOriginNo() {
        return originNo;
    }

    public void setOriginNo(String originNo) {
        this.originNo = originNo;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(String packageNumber) {
        this.packageNumber = packageNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
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

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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
}
