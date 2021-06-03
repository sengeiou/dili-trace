package com.dili.trace.dto.thirdparty.report;

import java.util.Date;

import com.dili.trace.enums.ReportDtoTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class MarketCountDto implements ReportDto {
    private Integer subjectCount;// 经营户备案数量
    private Integer pdtCount;// 品种数量
    private Date updateTime;// 更新日期(格式:yyyy-MM-dd hh:mm:ss)


    @JsonIgnore
    @Override
    public ReportDtoTypeEnum getType(){
        return ReportDtoTypeEnum.marketCount;
    };

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

    /**
     * @return the subjectCount
     */
    public Integer getSubjectCount() {
        return subjectCount;
    }

    /**
     * @param subjectCount the subjectCount to set
     */
    public void setSubjectCount(Integer subjectCount) {
        this.subjectCount = subjectCount;
    }

    /**
     * @return the pdtCount
     */
    public Integer getPdtCount() {
        return pdtCount;
    }

    /**
     * @param pdtCount the pdtCount to set
     */
    public void setPdtCount(Integer pdtCount) {
        this.pdtCount = pdtCount;
    }

}