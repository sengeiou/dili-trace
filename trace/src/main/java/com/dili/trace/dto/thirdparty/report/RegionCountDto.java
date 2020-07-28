package com.dili.trace.dto.thirdparty.report;

import java.util.List;

import com.dili.trace.enums.ReportDtoTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class RegionCountDto implements ReportDto {
    @JsonIgnore
    @Override
    public ReportDtoTypeEnum getType(){
        return ReportDtoTypeEnum.regionCount;
    };

    private List<RegionCountInfo> info;// 更新日期(格式:yyyy-MM-dd hh:mm:ss)

    /**
     * @return the info
     */
    public List<RegionCountInfo> getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(List<RegionCountInfo> info) {
        this.info = info;
    }


}