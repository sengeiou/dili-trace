package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.enums.CheckinOutTypeEnum;
import com.dili.trace.enums.WeightUnitEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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

    /**
     * 进出门状态
     */
    @ApiModelProperty(value = "进出门状态")
    @Column(name = "`inout`")
    private Integer inout;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Column(name = "`remark`")
    private String remark;

    /**
     * 操作人姓名
     */
    @ApiModelProperty(value = "操作人姓名")
    @Column(name = "`operator_name`")
    private String operatorName;

    /**
     * 操作人ID
     */
    @ApiModelProperty(value = "操作人ID")
    @Column(name = "`operator_id`")
    private Long operatorId;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @Column(name = "`modified`")
    private Date modified;

    /**
     * 业户ID
     */
    @ApiModelProperty(value = "业户ID")
    @Column(name = "`user_id`")
    private Long userId;

    /**
     * 业户名称
     */
    @ApiModelProperty(value = "业户名称")
    @Column(name = "`user_name`")
    private String userName;


    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    @Column(name = "`product_name`")
    private String productName;


    /**
     * 审核状态值
     */
    @ApiModelProperty(value = "审核状态值")
	@Column(name = "`verify_status`")
    private Integer verifyStatus;

    /**
     * 登记单类型
     */
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


    /**
     * 进出门重量
     */
    @ApiModelProperty(value = "进出门重量")
    @Column(name = "`inout_weight`")
    private BigDecimal inoutWeight;

    /**
     * 重量单位
     */
    @ApiModelProperty(value = "重量单位")
	@Column(name = "`weight_unit`")
	private Integer weightUnit;

    /**
     * 分销ID
     */
    @ApiModelProperty(value = "分销ID")
    @Column(name = "`trade_detail_id`")
    private Long tradeDetailId;

    /**
     * 报备单ID
     */

    @ApiModelProperty(value = "报备单ID")
    @Column(name = "`bill_id`")
    private Long billId;

    /**
     *车牌号
     */
    @ApiModelProperty(value = "车牌号")
    @Transient
    private String plate;

    /**
     *市场ID
     */
    @ApiModelProperty(value = "市场ID")
    @Column(name = "market_id")
    private Long marketId;

    /**
     *进出门状态名称
     */
    @Transient
    public String getInoutName(){
       return  CheckinOutTypeEnum.fromCode(this.getInout()).map(CheckinOutTypeEnum::getDesc).orElse("");
    }

    /**
     *重量单位名称
     */
    @Transient
    public String getWeightUnitName(){
       return  WeightUnitEnum.fromCode(this.getWeightUnit()).map(WeightUnitEnum::getName).orElse("");
    }

    public String getPlate() { return plate; }

    public void setPlate(String plate) { this.plate = plate; }

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

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
}
