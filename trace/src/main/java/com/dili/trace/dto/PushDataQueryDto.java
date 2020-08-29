package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.ThirdPartyReportData;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;

public class PushDataQueryDto extends ThirdPartyReportData {

    @ApiModelProperty(value = "查询创建开始时间")
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private String createdStart;

    @ApiModelProperty(value = "查询创建结束时间")
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private String createdEnd;

    @ApiModelProperty(value = "查询修改开始时间")
    @Column(name = "`modified`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private String modifiedStart;

    @ApiModelProperty(value = "查询修改结束时间")
    @Column(name = "`modified`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private String modifiedEnd;

    @ApiModelProperty(value = "查询交易类型10-配送交易 20-扫码交易")
    @Column(name = "`source`")
    private Integer orderType;

    @ApiModelProperty(value = "交易单主键")
    @Column(name = "`trade_request_id`")
    private String tradeRequestId;

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

    public String getModifiedStart() {
        return modifiedStart;
    }

    public void setModifiedStart(String modifiedStart) {
        this.modifiedStart = modifiedStart;
    }

    public String getModifiedEnd() {
        return modifiedEnd;
    }

    public void setModifiedEnd(String modifiedEnd) {
        this.modifiedEnd = modifiedEnd;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getTradeRequestId() {
        return tradeRequestId;
    }

    public void setTradeRequestId(String tradeRequestId) {
        this.tradeRequestId = tradeRequestId;
    }
}