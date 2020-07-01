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
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;

import io.swagger.annotations.ApiModelProperty;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:34.
 */
@Table(name = "`register_bill`")
public class RegisterBill extends BaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;

	@ApiModelProperty(value = "编号")
	@Column(name = "`code`")
	private String code;

	@ApiModelProperty(value = "采样编号")
	@Column(name = "`sample_code`")
	private String sampleCode;

	@ApiModelProperty(value = "业户姓名")
	@Column(name = "`name`")
	private String name;

	@ApiModelProperty(value = "身份证号")
	@Column(name = "`id_card_no`")
	private String idCardNo;

	@ApiModelProperty(value = "身份地址")
	@Column(name = "`addr`")
	private String addr;

	@ApiModelProperty(value = "业户手机号")
	@Column(name = "`phone`")
	private String phone;

	@ApiModelProperty(value = "用户iD")
	@Column(name = "`user_id`")
	private Long userId;

	@ApiModelProperty(value = "车牌")
	@Column(name = "`plate`")
	private String plate;

	@ApiModelProperty(value = "1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中")
	@Column(name = "`state`")
	private Integer state;

	@ApiModelProperty(value = "1:采样检测 2:主动送检")
	@Column(name = "`sample_source`")
	private Integer sampleSource;

	@ApiModelProperty(value = "商品名称")
	@Column(name = "`product_name`")
	private String productName;

	@ApiModelProperty(value = "商品ID")
	@Column(name = "`product_id`")
	private Long productId;

	@ApiModelProperty(value = "产地ID")
	@Column(name = "`origin_id`")
	private Long originId;

	@ApiModelProperty(value = "产地名")
	@Column(name = "`origin_name`")
	private String originName;

	@ApiModelProperty(value = "重量KG")
	@Column(name = "`weight`")
	private BigDecimal weight;

	@ApiModelProperty(value = "重量单位")
	@Column(name = "`weight_unit`")
	private Integer weightUnit;

	@ApiModelProperty(value = "1.合格 2.不合格 3.复检合格 4.复检不合格")
	@Column(name = "`detect_state`")
	private Integer detectState;

	@ApiModelProperty(value = "检测记录ID")
	@Column(name = "`latest_detect_record_id`")
	private Long latestDetectRecordId;

	@ApiModelProperty(value = "检测记录时间")
	@Column(name = "`latest_detect_time`")
	private Date latestDetectTime;

	@ApiModelProperty(value = "检测人员")
	@Column(name = "`latest_detect_operator`")
	private String latestDetectOperator;

	@ApiModelProperty(value = "检测值结果")
	@Column(name = "`latest_pd_result`")
	private String latestPdResult;


	@ApiModelProperty(value = "版本")
	@Column(name = "`version`")
	private Integer version;

	@Column(name = "`created`")
	private Date created;

	@Column(name = "`modified`")
	private Date modified;

	@ApiModelProperty(value = "操作人")
	@Column(name = "`operator_name`")
	private String operatorName;

	@ApiModelProperty(value = "操作人ID")
	@Column(name = "`operator_id`")
	private Long operatorId;

	@ApiModelProperty(value = "上游信息ID")
	@Column(name = "`upstream_id`")
	private Long upStreamId;

	@ApiModelProperty(value = "数据是否完整")
	@Column(name = "`complete`")
	private Integer complete;

	@ApiModelProperty(value = "查验状态值")
	@Column(name = "`verify_status`")
	private Integer verifyStatus;
	
	@ApiModelProperty(value = "保存类型")
	@Column(name = "`preserve_type`")
	private Integer preserveType;

	@ApiModelProperty(value = "查验历史ID")
	@Column(name = "`verified_history_bill_id`")
	private Long verifiedHistoryBillId;
	
	@ApiModelProperty(value = "查验类型")
	@Column(name = "`verify_type`")
	private Integer verifyType;

	@ApiModelProperty(value = "规格名称")
	@Column(name="`spec_name`")
	private String specName;

	@ApiModelProperty(value = "报备类型")
	@Column(name="`bill_type`")
	private Integer billType;
	@ApiModelProperty(value = "拼车类型")
	@Column(name="`truck_type`")
	private Integer truckType;

	@ApiModelProperty(value = "品牌名称")
	@Column(name = "`brand_name`")
	private String brandName;

	@ApiModelProperty(value = "品牌ID")
	@Column(name = "`brand_id`")
	private Long brandId;

	
	@ApiModelProperty(value = "是否有效")
	@Column(name="`yn`")
	private Integer yn;

	@ApiModelProperty(value = "退回原因")
	@Column(name = "`returned_reason`")
	private String returnedReason;
	

	public Integer getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	@Transient
	public String getDetectStateName() {
		try {
			if (getDetectState() == null) {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
		BillDetectStateEnum state = BillDetectStateEnum.getBillDetectStateEnum(getDetectState());
		return state.getName();

	}

	@Transient
	public String getStateName() {
		try {
			if (getState() == null) {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
		return RegisterBillStateEnum.getRegisterBillStateEnum(getState()).getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSampleCode() {
		return sampleCode;
	}

	public void setSampleCode(String sampleCode) {
		this.sampleCode = sampleCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getSampleSource() {
		return sampleSource;
	}

	public void setSampleSource(Integer sampleSource) {
		this.sampleSource = sampleSource;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getOriginId() {
		return originId;
	}

	public void setOriginId(Long originId) {
		this.originId = originId;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public Integer getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(Integer weightUnit) {
		this.weightUnit = weightUnit;
	}

	public Integer getDetectState() {
		return detectState;
	}

	public void setDetectState(Integer detectState) {
		this.detectState = detectState;
	}

	public Long getLatestDetectRecordId() {
		return latestDetectRecordId;
	}

	public void setLatestDetectRecordId(Long latestDetectRecordId) {
		this.latestDetectRecordId = latestDetectRecordId;
	}

	public Date getLatestDetectTime() {
		return latestDetectTime;
	}

	public void setLatestDetectTime(Date latestDetectTime) {
		this.latestDetectTime = latestDetectTime;
	}

	public String getLatestDetectOperator() {
		return latestDetectOperator;
	}

	public void setLatestDetectOperator(String latestDetectOperator) {
		this.latestDetectOperator = latestDetectOperator;
	}

	public String getLatestPdResult() {
		return latestPdResult;
	}

	public void setLatestPdResult(String latestPdResult) {
		this.latestPdResult = latestPdResult;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

	public Long getUpStreamId() {
		return upStreamId;
	}

	public void setUpStreamId(Long upStreamId) {
		this.upStreamId = upStreamId;
	}

	public Integer getComplete() {
		return complete;
	}

	public void setComplete(Integer complete) {
		this.complete = complete;
	}


    /**
     * @return Integer return the preserveType
     */
    public Integer getPreserveType() {
        return preserveType;
    }

    /**
     * @param preserveType the preserveType to set
     */
    public void setPreserveType(Integer preserveType) {
        this.preserveType = preserveType;
    }


    /**
     * @return Long return the verifiedHistoryBillId
     */
    public Long getVerifiedHistoryBillId() {
        return verifiedHistoryBillId;
    }

    /**
     * @param verifiedHistoryBillId the verifiedHistoryBillId to set
     */
    public void setVerifiedHistoryBillId(Long verifiedHistoryBillId) {
        this.verifiedHistoryBillId = verifiedHistoryBillId;
    }


    /**
     * @return Integer return the verifyType
     */
    public Integer getVerifyType() {
        return verifyType;
    }

    /**
     * @param verifyType the verifyType to set
     */
    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }


    /**
     * @return String return the specName
     */
    public String getSpecName() {
        return specName;
    }

    /**
     * @param specName the specName to set
     */
    public void setSpecName(String specName) {
        this.specName = specName;
    }



    /**
     * @return Integer return the billType
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
     * @return Long return the brandId
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * @param brandId the brandId to set
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }


    /**
     * @return String return the brandName
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * @param brandName the brandName to set
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }


    /**
     * @return Integer return the yn
     */
    public Integer getYn() {
        return yn;
    }

    /**
     * @param yn the yn to set
     */
    public void setYn(Integer yn) {
        this.yn = yn;
    }


    /**
     * @return Integer return the truckType
     */
    public Integer getTruckType() {
        return truckType;
    }

    /**
     * @param truckType the truckType to set
     */
    public void setTruckType(Integer truckType) {
        this.truckType = truckType;
    }


    /**
     * @return String return the returnedReason
     */
    public String getReturnedReason() {
        return returnedReason;
    }

    /**
     * @param returnedReason the returnedReason to set
     */
    public void setReturnedReason(String returnedReason) {
        this.returnedReason = returnedReason;
    }

}