package com.dili.trace.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author asa.lee
 */
public class CommodityWeightReportDto {

    private Date created;
    private String createdStart;
    private String createdEnd;
    private String varietyName;
    private String likeVarietyName;
    private Integer approachBillCount;
    private BigDecimal weight;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedStart() {
        return createdStart;
    }

    public void setCreatedStart(String createdStart) {
        this.createdStart = createdStart;
    }

    public String getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(String createdEnd) {
        this.createdEnd = createdEnd;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    public String getLikeVarietyName() {
        return likeVarietyName;
    }

    public void setLikeVarietyName(String likeVarietyName) {
        this.likeVarietyName = likeVarietyName;
    }

    public Integer getApproachBillCount() {
        return approachBillCount;
    }

    public void setApproachBillCount(Integer approachBillCount) {
        this.approachBillCount = approachBillCount;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
