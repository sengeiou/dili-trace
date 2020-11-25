package com.dili.trace.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

@Table(name = "seperate_print_report")
public class SeperatePrintReport extends BaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;

	@ApiModelProperty(value = "编号")
	@Column(name = "`code`")
	private String code;

	@Column(name = "`bill_id`")
	private Long billId;

	@Column(name = "`seperate_recocrd_id`")
	private Long seperateRecocrdId;

	@ApiModelProperty(value = "商品别名")
	@Column(name = "`product_alias_name`")
	private String productAliasName;

	@ApiModelProperty(value = "分销重量KG")
	@Column(name = "`sales_weight`")
	private BigDecimal salesWeight;

	@ApiModelProperty(value = "车牌号")
	@Column(name = "`sales_plate`")
	private String salesPlate;

	@Column(name = "`sales_user_name`")
	public String salesUserName;

	@Column(name = "`tally_area_no`")
	private String tallyAreaNo;

	@Column(name = "`valid_period`")
	private Integer validPeriod;

	@ApiModelProperty(value = "创建时间")
	@Column(name = "`created`")
	private Date created;

	@ApiModelProperty(value = "修改时间")
	@Column(name = "`modified`")
	private Date modified;

	@ApiModelProperty(value = "操作人姓名")
	@Column(name = "`operator_name`")
	private String operatorName;
	@ApiModelProperty(value = "操作人ID")
	@Column(name = "`operator_id`")
	private Long operatorId;

	@Column(name = "`approver_info_id`")
	private Long approverInfoId;

	@Transient
	private String approverUserName;

	@Transient
	public String printState;

	
	public String getApproverUserName() {
		return approverUserName;
	}

	public void setApproverUserName(String approverUserName) {
		this.approverUserName = approverUserName;
	}

	public String getPrintState() {
		return printState;
	}

	public void setPrintState(String printState) {
		this.printState = printState;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTallyAreaNo() {
		return tallyAreaNo;
	}

	public void setTallyAreaNo(String tallyAreaNo) {
		this.tallyAreaNo = tallyAreaNo;
	}

	public Long getApproverInfoId() {
		return approverInfoId;
	}

	public void setApproverInfoId(Long approverInfoId) {
		this.approverInfoId = approverInfoId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Long getSeperateRecocrdId() {
		return seperateRecocrdId;
	}

	public void setSeperateRecocrdId(Long seperateRecocrdId) {
		this.seperateRecocrdId = seperateRecocrdId;
	}

	public String getProductAliasName() {
		return productAliasName;
	}

	public void setProductAliasName(String productAliasName) {
		this.productAliasName = productAliasName;
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

	public String getSalesUserName() {
		return salesUserName;
	}

	public void setSalesUserName(String salesUserName) {
		this.salesUserName = salesUserName;
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

	public Integer getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}

}
