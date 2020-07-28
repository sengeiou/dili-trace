package com.dili.trace.dto.thirdparty.report;

import java.util.Date;
import java.util.List;

import com.dili.trace.enums.ReportDtoTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CodeCountDto implements ReportDto {
    private Integer greenCount;// 绿码数量
    private Integer yellowCount;// 黄码数量
    private Integer redCount;// 红码数量
    private List<WaringInfoDto> waringInfo;// 预警数据明细
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;// 更新日期(格式:yyyy-MM-dd hh:mm:ss)

    @JsonIgnore
    @Override
    public ReportDtoTypeEnum getType(){
        return ReportDtoTypeEnum.codeCount;
    };

    /**
     * @return the greenCount
     */
    public Integer getGreenCount() {
        return greenCount;
    }

    /**
     * @param greenCount the greenCount to set
     */
    public void setGreenCount(Integer greenCount) {
        this.greenCount = greenCount;
    }

    /**
     * @return the yellowCount
     */
    public Integer getYellowCount() {
        return yellowCount;
    }

    /**
     * @param yellowCount the yellowCount to set
     */
    public void setYellowCount(Integer yellowCount) {
        this.yellowCount = yellowCount;
    }

    /**
     * @return the redCount
     */
    public Integer getRedCount() {
        return redCount;
    }

    /**
     * @param redCount the redCount to set
     */
    public void setRedCount(Integer redCount) {
        this.redCount = redCount;
    }

    /**
     * @return the waringInfo
     */
    public List<WaringInfoDto> getWaringInfo() {
        return waringInfo;
    }

    /**
     * @param waringInfo the waringInfo to set
     */
    public void setWaringInfo(List<WaringInfoDto> waringInfo) {
        this.waringInfo = waringInfo;
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