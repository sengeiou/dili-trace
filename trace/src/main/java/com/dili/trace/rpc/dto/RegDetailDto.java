package com.dili.trace.rpc.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RegDetailDto   implements Serializable {
    private static final long serialVersionUID = 1L;
    /**货主卡账户*/
    private Long accountId;
    /**客户id*/
    private Long customerId;
    /**客户姓名*/
    private String customerName;
    /**入库单位1斤2公斤3件*/
    private Integer inUnit;
    /**单价*/
    private Long price;
    /**等级*/
    private String level;
    /**规格型号*/
    private String spec;
    /**品牌*/
    private String brand;
    /**产地*/
    private String place;
    /**供应商*/
    private String supplier;
    /**分类ID*/
    private Long cateId;
    /**分类名称*/
    private String cname;
    /**重量*/
    private BigDecimal weight;

    public Integer getInUnit() {
        return inUnit;
    }

    public void setInUnit(Integer inUnit) {
        this.inUnit = inUnit;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

	public Long getCateId() {
		return cateId;
	}

	public void setCateId(Long cateId) {
		this.cateId = cateId;
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

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}