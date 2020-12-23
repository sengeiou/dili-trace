package com.dili.trace.rpc.dto;

import java.math.BigDecimal;

public class RegDetailDto {

    /**
     * 库存ID
     */
    private Long stockId;

    /**
     * 等级
     */
    private String level;
    /**
     * 分类名称
     */
    private String cname;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 入库单位1斤2公斤3件
     */
    private Integer inUnit;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 规格型号
     */
    private String spec;
    /**
     * 货主卡账户
     */
    private Long accountId;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 分类ID
     */
    private Long cateId;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 产地
     */
    private String place;

    /**
     * 品牌
     */
    private String brand;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getInUnit() {
        return inUnit;
    }

    public void setInUnit(Integer inUnit) {
        this.inUnit = inUnit;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getCateId() {
        return cateId;
    }

    public void setCateId(Long cateId) {
        this.cateId = cateId;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }
}
