package com.dili.trace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@SuppressWarnings("serial")
@Table(name = "`separate_sales_record`")
public class SeparateSalesRecord extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;


    @ApiModelProperty(value = "分销用户ID")
    @Column(name = "`sales_user_id`")
    private Long salesUserId;

    @ApiModelProperty(value = "分销用户")
    @Column(name = "`sales_user_name`")
    private String salesUserName;

    
    @ApiModelProperty(value = "交易单号")
    @Column(name = "`trade_no`")
    private String tradeNo;

    
    @ApiModelProperty(value = "分销城市ID")
    @Column(name = "`sales_city_id`")
    private Long salesCityId;

    @ApiModelProperty(value = "分销城市")
    @Column(name = "`sales_city_name`")
    private String salesCityName;

    @ApiModelProperty(value = "分销重量KG")
    @Column(name = "`sales_weight`")
    private Integer salesWeight;

    
    @ApiModelProperty(value = "车牌号")
    @Column(name = "`sales_plate`")
    private String salesPlate;

    
    @ApiModelProperty(value = "被分销的登记单")
    @Column(name = "`register_bill_code`")
    private String registerBillCode;


    @ApiModelProperty(value = "理货区号")
    @Column(name = "`tally_area_no`")
    private String tallyAreaNo;
    
    @Column(name = "`created`")
    private Date created;


    @Column(name = "`modified`")
    private Date modified;
    


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getSalesUserId() {
		return salesUserId;
	}


	public void setSalesUserId(Long salesUserId) {
		this.salesUserId = salesUserId;
	}


	public String getSalesUserName() {
		return salesUserName;
	}


	public void setSalesUserName(String salesUserName) {
		this.salesUserName = salesUserName;
	}


	public String getTradeNo() {
		return tradeNo;
	}


	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}


	public Long getSalesCityId() {
		return salesCityId;
	}


	public void setSalesCityId(Long salesCityId) {
		this.salesCityId = salesCityId;
	}


	public String getSalesCityName() {
		return salesCityName;
	}


	public void setSalesCityName(String salesCityName) {
		this.salesCityName = salesCityName;
	}


	public Integer getSalesWeight() {
		return salesWeight;
	}


	public void setSalesWeight(Integer salesWeight) {
		this.salesWeight = salesWeight;
	}


	public String getSalesPlate() {
		return salesPlate;
	}


	public void setSalesPlate(String salesPlate) {
		this.salesPlate = salesPlate;
	}


	public String getRegisterBillCode() {
		return registerBillCode;
	}


	public void setRegisterBillCode(String registerBillCode) {
		this.registerBillCode = registerBillCode;
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


	public String getTallyAreaNo() {
		return tallyAreaNo;
	}


	public void setTallyAreaNo(String tallyAreaNo) {
		this.tallyAreaNo = tallyAreaNo;
	}



}