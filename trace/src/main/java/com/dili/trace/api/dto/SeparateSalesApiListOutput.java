package com.dili.trace.api.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.glossary.RegisterBillStateEnum;

public class SeparateSalesApiListOutput extends BaseDomain {
    private Long id;
    private BigDecimal weight;
    private BigDecimal storeWeight;
    private String productName;
    private Integer state;
    private Integer detectState;
    private Integer salesType;

    private Long latestDetectRecordId;
    private String latestDetectOperator;
    private Date latestDetectTime;
    private String latestPdResult;
    private String originName;
    private String plate;
    private Date created;
    private String upstreamName;
    
    
    public String getUpstreamName() {
		return upstreamName;
	}

	public void setUpstreamName(String upstreamName) {
		this.upstreamName = upstreamName;
	}

	public Integer getDetectState() {
		return detectState;
	}

	public void setDetectState(Integer detectState) {
		this.detectState = detectState;
	}

	public Integer getSalesType() {
		return salesType;
	}

	public void setSalesType(Integer salesType) {
		this.salesType = salesType;
	}

	public String getStateName(){
        try {
            if (getState() == null) {
                return "";
            }
        }catch (Exception e){
            return "";
        }
        return RegisterBillStateEnum.getRegisterBillStateEnum(getState()).getName();
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
     * @return BigDecimal return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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
     * @return Integer return the state
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return Long return the latestDetectRecordId
     */
    public Long getLatestDetectRecordId() {
        return latestDetectRecordId;
    }

    /**
     * @param latestDetectRecordId the latestDetectRecordId to set
     */
    public void setLatestDetectRecordId(Long latestDetectRecordId) {
        this.latestDetectRecordId = latestDetectRecordId;
    }

    /**
     * @return String return the latestDetectOperator
     */
    public String getLatestDetectOperator() {
        return latestDetectOperator;
    }

    /**
     * @param latestDetectOperator the latestDetectOperator to set
     */
    public void setLatestDetectOperator(String latestDetectOperator) {
        this.latestDetectOperator = latestDetectOperator;
    }

    /**
     * @return Date return the latestDetectTime
     */
    public Date getLatestDetectTime() {
        return latestDetectTime;
    }

    /**
     * @param latestDetectTime the latestDetectTime to set
     */
    public void setLatestDetectTime(Date latestDetectTime) {
        this.latestDetectTime = latestDetectTime;
    }

    /**
     * @return String return the latestPdResult
     */
    public String getLatestPdResult() {
        return latestPdResult;
    }

    /**
     * @param latestPdResult the latestPdResult to set
     */
    public void setLatestPdResult(String latestPdResult) {
        this.latestPdResult = latestPdResult;
    }

    /**
     * @return String return the originName
     */
    public String getOriginName() {
        return originName;
    }

    /**
     * @param originName the originName to set
     */
    public void setOriginName(String originName) {
        this.originName = originName;
    }

    /**
     * @return String return the plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     * @param plate the plate to set
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }


    /**
     * @return BigDecimal return the storeWeight
     */
    public BigDecimal getStoreWeight() {
        return storeWeight;
    }

    /**
     * @param storeWeight the storeWeight to set
     */
    public void setStoreWeight(BigDecimal storeWeight) {
        this.storeWeight = storeWeight;
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

}