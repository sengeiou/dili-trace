package com.dili.trace.api.output;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

public class QualityTraceTradeBillOutput {

    private Long billId;

    @ApiModelProperty(value = "检测记录时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date latestDetectTime;

    @ApiModelProperty(value = "检测人员")
    private String latestDetectOperator;

    @ApiModelProperty(value = "检测值结果")
    private String latestPdResult;

    /**
     * 检测结果code
     */
    private Integer detectState;
    /**
     * 检测结果描述
     */
    private String detectStateDesc;

    public Integer getDetectState() {
        return detectState;
    }

    public void setDetectState(Integer detectState) {
        this.detectState = detectState;
    }

    public String getDetectStateDesc() {
        return BillDetectStateEnum.fromCode(this.detectState).map(BillDetectStateEnum::getName).orElse("");
    }

    public void setDetectStateDesc(String detectStateDesc) {
        this.detectStateDesc = detectStateDesc;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
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

}
