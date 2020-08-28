package com.dili.trace.dto.thirdparty.report;

import com.dili.trace.enums.ReportDtoTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReportCheckInDto implements ReportDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inTime;// 进门时间
    private String marketId;// 市场id
    private String thirdAccId;// 经营户id
    private String thirdEnterIds;// 报备id,多个逗号分隔

    @JsonIgnore
    @Override
    public ReportDtoTypeEnum getType(){
        return ReportDtoTypeEnum.inDoor;
    };

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getThirdAccId() {
        return thirdAccId;
    }

    public void setThirdAccId(String thirdAccId) {
        this.thirdAccId = thirdAccId;
    }

    public String getThirdEnterIds() {
        return thirdEnterIds;
    }

    public void setThirdEnterIds(String thirdEnterIds) {
        this.thirdEnterIds = thirdEnterIds;
    }
}