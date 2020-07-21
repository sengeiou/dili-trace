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
import com.dili.trace.enums.CheckinOutTypeEnum;
import com.dili.trace.enums.WeightUnitEnum;

import io.swagger.annotations.ApiModelProperty;

@Table(name = "`checkinout_record`")
public class CheckinOutRecord extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;
    /**
     * 状态 {@link com.dili.trace.enums.CheckinStatusEnum}
     */
    @ApiModelProperty(value = "状态")
    @Column(name = "`status`")
    private Integer status;

    @ApiModelProperty(value = "状态")
    @Column(name = "`inout`")
    private Integer inout;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "备注")
    @Column(name = "`remark`")
    private String remark;

    @ApiModelProperty(value = "操作人姓名")
    @Column(name = "`operator_name`")
    private String operatorName;

    @ApiModelProperty(value = "操作人ID")
    @Column(name = "`operator_id`")
    private Long operatorId;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

    @ApiModelProperty(value = "更新时间")
    @Column(name = "`modified`")
    private Date modified;


    @ApiModelProperty(value = "业户ID")
    @Column(name = "`user_id`")
    private Long userId;

    @ApiModelProperty(value = "业户名称")
    @Column(name = "`user_name`")
    private String userName;

    @ApiModelProperty(value = "商品名称")
    @Column(name = "`product_name`")
    private String productName;

    @ApiModelProperty(value = "查验状态值")
	@Column(name = "`verify_status`")
    private Integer verifyStatus;
    
    @Column(name = "`bill_type`")
	private Integer billType;

//    @ApiModelProperty(value = "商品重量")
//    @Column(name = "`sales_weight`")
//    private Integer salesWeight;
//    
//
//    @ApiModelProperty(value = "分销ID")
//    @Column(name = "`seperate_sales_id`")
//    private Long seperateSalesId;
//    
    
    @ApiModelProperty(value = "进出门重量")
    @Column(name = "`inout_weight`")
    private BigDecimal inoutWeight;

    @ApiModelProperty(value = "重量单位")
	@Column(name = "`weight_unit`")
	private Integer weightUnit;
    
    @ApiModelProperty(value = "分销ID")
    @Column(name = "`trade_detail_id`")
    private Long tradeDetailId;
    
    
    @ApiModelProperty(value = "报备单ID")
    @Column(name = "`bill_id`")
    private Long billId;

    @Transient
    public String getInoutName(){
       return  CheckinOutTypeEnum.fromCode(this.getInout()).map(CheckinOutTypeEnum::getDesc).orElse("");
    }

    @Transient
    public String getWeightUnitName(){
       return  WeightUnitEnum.fromCode(this.getWeightUnit()).map(WeightUnitEnum::getName).orElse("");
    }

    public BigDecimal getInoutWeight() {
		return inoutWeight;
	}

	public void setInoutWeight(BigDecimal inoutWeight) {
		this.inoutWeight = inoutWeight;
	}

	public Long getTradeDetailId() {
		return tradeDetailId;
	}

	public void setTradeDetailId(Long tradeDetailId) {
		this.tradeDetailId = tradeDetailId;
	}


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInout() {
        return inout;
    }

    public void setInout(Integer inout) {
        this.inout = inout;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    /**
     * @return the verifyStatus
     */
    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    /**
     * @param verifyStatus the verifyStatus to set
     */
    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    /**
     * @return the billType
     */
    public Integer getBillType() {
        return billType;
    }

    /**
     * @param billType the billType to set
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    /**
     * @return the billId
     */
    public Long getBillId() {
        return billId;
    }

    /**
     * @param billId the billId to set
     */
    public void setBillId(Long billId) {
        this.billId = billId;
    }


    /**
     * @return Long return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
