package com.dili.trace.api.output;

import java.util.Date;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;

public class SeparateSalesApiListOutput extends BaseDomain {

    private Long id;
    private Integer weight = 0;
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
    private String detectStateName;

    public String getDetectStateName() {
        if (this.detectState == null) {
            return this.detectStateName;
        }
        return BillDetectStateEnum.fromCode(this.getDetectState()).map(state -> state.getName()).orElse("");
    }

    public void setDetectStateName(String detectStateName) {
        this.detectStateName = detectStateName;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        if (weight != null) {
            this.weight = weight;
        } else {
            this.weight = 0;
        }
    }

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
