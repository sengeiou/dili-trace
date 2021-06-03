package com.dili.trace.dto.thirdparty.report;

import java.math.BigDecimal;
import java.util.Date;


public class RegionCountInfo implements ReportDto {
    private String regionName;// 来源地区
    private BigDecimal weight;// 重量(单位:kg)

    private Date updateTime;// 更新日期(格式:yyyy-MM-dd hh:mm:ss)

    /**
     * @return the regionName
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * @param regionName the regionName to set
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    /**
     * @return the weight
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
     * @return the updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


}