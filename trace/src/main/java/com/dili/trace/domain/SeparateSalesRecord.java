package com.dili.trace.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`separate_sales_record`")
public class SeparateSalesRecord extends BaseDomain {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;

	/**
	 * 分销用户ID
	 */
	@ApiModelProperty(value = "分销用户ID")
	@Column(name = "`sales_user_id`")
	private Long salesUserId;

	/**
	 * 分销用户
	 */
	@ApiModelProperty(value = "分销用户")
	@Column(name = "`sales_user_name`")
	private String salesUserName;

	/**
	 * 交易单号
	 */
	@ApiModelProperty(value = "交易单号")
	@Column(name = "`trade_no`")
	private String tradeNo;

	/**
	 * 分销城市ID
	 */
	@ApiModelProperty(value = "分销城市ID")
	@Column(name = "`sales_city_id`")
	private Long salesCityId;

	/**
	 * 分销城市
	 */
	@ApiModelProperty(value = "分销城市")
	@Column(name = "`sales_city_name`")
	private String salesCityName;

	/**
	 * 分销重量KG
	 */
	@ApiModelProperty(value = "分销重量KG")
	@Column(name = "`sales_weight`")
	private BigDecimal salesWeight;

	/**
	 * 车牌号
	 */
	@ApiModelProperty(value = "车牌号")
	@Column(name = "`sales_plate`")
	private String salesPlate;

	/**
	 * 被分销的登记单
	 */
	@ApiModelProperty(value = "被分销的登记单")
	@Column(name = "`register_bill_code`")
	private String registerBillCode;

	/**
	 * 最初登记单ID
	 */
	@ApiModelProperty(value = "最初登记单ID")
	@Column(name = "`bill_id`")
	private Long billId;

	/**
	 * 销售类型 {@link com.dili.sg.trace.glossary.SalesTypeEnum}
	 */
	@Column(name = "`sales_type`")
	private Integer salesType;

	/**
	 * 创建时间
	 */
	@Column(name = "`created`")
	private Date created;

	/**
	 * 修改时间
	 */
	@Column(name = "`modified`")
	Date modified;

	/**
	 * 分销自
	 */
	@ApiModelProperty(value = "分销自")
	@Column(name = "`parent_id`")
	private Long parentId;

	/**
	 * 总重量
	 */
	@ApiModelProperty(value = "总重量")
	@Column(name = "`store_weight`")
	private BigDecimal storeWeight;

	/**
	 * 进场审核ID
	 */
	@ApiModelProperty(value = "进场审核ID")
	@Column(name = "`checkin_record_id`")
	private Long checkinRecordId;

	/**
	 * 出场审核ID
	 */
	@ApiModelProperty(value = "出场审核ID")
	@Column(name = "`checkout_record_id`")
	private Long checkoutRecordId;

	/**
	 * 理货区号
	 */
	@ApiModelProperty(value = "理货区号")
	@Column(name = "`tally_area_no`")
	private String tallyAreaNo;

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

	public BigDecimal getSalesWeight() {
		return salesWeight;
	}

	public void setSalesWeight(BigDecimal salesWeight) {
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

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Integer getSalesType() {
		return salesType;
	}

	public void setSalesType(Integer salesType) {
		this.salesType = salesType;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public BigDecimal getStoreWeight() {
		return storeWeight;
	}

	public void setStoreWeight(BigDecimal storeWeight) {
		this.storeWeight = storeWeight;
	}

	public Long getCheckinRecordId() {
		return checkinRecordId;
	}

	public void setCheckinRecordId(Long checkinRecordId) {
		this.checkinRecordId = checkinRecordId;
	}

	public Long getCheckoutRecordId() {
		return checkoutRecordId;
	}

	public void setCheckoutRecordId(Long checkoutRecordId) {
		this.checkoutRecordId = checkoutRecordId;
	}

	public String getTallyAreaNo() {
		return tallyAreaNo;
	}

	public void setTallyAreaNo(String tallyAreaNo) {
		this.tallyAreaNo = tallyAreaNo;
	}
}