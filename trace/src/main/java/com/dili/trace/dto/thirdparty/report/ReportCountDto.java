package com.dili.trace.dto.thirdparty.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dili.trace.enums.ReportDtoTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ReportCountDto implements ReportDto {
    private BigDecimal reportTotal=BigDecimal.ZERO;//    今日报备数量（单位:kg）
    private Integer reportBatch=0;// 今日报备批次(数量)
    private Integer checkBatch=0;// 今日检测批次
    private Integer transactionBatch=0;// 今日交易批次
    private BigDecimal transactionTotal=BigDecimal.ZERO;// 今日交易数量（单位:kg）
    private Integer unqualifiedBatch=0;//今日不合格批次
    private Integer unqualifiedPdtCount=0;//今日不合格品种数量（注意是:品种数量）
    private List<UnqualifiedPdtInfo>unqualifiedPdtInfo;//今日不合格品种数据明细
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;// 更新日期(格式:yyyy-MM-dd hh:mm:ss)

    @JsonIgnore
    @Override
    public ReportDtoTypeEnum getType(){
        return ReportDtoTypeEnum.reportCount;
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
     * @return the reportTotal
     */
    public BigDecimal getReportTotal() {
        return reportTotal;
    }

    /**
     * @param reportTotal the reportTotal to set
     */
    public void setReportTotal(BigDecimal reportTotal) {
        this.reportTotal = reportTotal;
    }

    /**
     * @return the reportBatch
     */
    public Integer getReportBatch() {
        return reportBatch;
    }

    /**
     * @param reportBatch the reportBatch to set
     */
    public void setReportBatch(Integer reportBatch) {
        this.reportBatch = reportBatch;
    }

    /**
     * @return the checkBatch
     */
    public Integer getCheckBatch() {
        return checkBatch;
    }

    /**
     * @param checkBatch the checkBatch to set
     */
    public void setCheckBatch(Integer checkBatch) {
        this.checkBatch = checkBatch;
    }

    /**
     * @return the transactionBatch
     */
    public Integer getTransactionBatch() {
        return transactionBatch;
    }

    /**
     * @param transactionBatch the transactionBatch to set
     */
    public void setTransactionBatch(Integer transactionBatch) {
        this.transactionBatch = transactionBatch;
    }

    /**
     * @return the transactionTotal
     */
    public BigDecimal getTransactionTotal() {
        return transactionTotal;
    }

    /**
     * @param transactionTotal the transactionTotal to set
     */
    public void setTransactionTotal(BigDecimal transactionTotal) {
        this.transactionTotal = transactionTotal;
    }

    /**
     * @return the unqualifiedBatch
     */
    public Integer getUnqualifiedBatch() {
        return unqualifiedBatch;
    }

    /**
     * @param unqualifiedBatch the unqualifiedBatch to set
     */
    public void setUnqualifiedBatch(Integer unqualifiedBatch) {
        this.unqualifiedBatch = unqualifiedBatch;
    }

    /**
     * @return the unqualifiedPdtCount
     */
    public Integer getUnqualifiedPdtCount() {
        return unqualifiedPdtCount;
    }

    /**
     * @param unqualifiedPdtCount the unqualifiedPdtCount to set
     */
    public void setUnqualifiedPdtCount(Integer unqualifiedPdtCount) {
        this.unqualifiedPdtCount = unqualifiedPdtCount;
    }

    /**
     * @return the unqualifiedPdtInfo
     */
    public List<UnqualifiedPdtInfo> getUnqualifiedPdtInfo() {
        return unqualifiedPdtInfo;
    }

    /**
     * @param unqualifiedPdtInfo the unqualifiedPdtInfo to set
     */
    public void setUnqualifiedPdtInfo(List<UnqualifiedPdtInfo> unqualifiedPdtInfo) {
        this.unqualifiedPdtInfo = unqualifiedPdtInfo;
    }


}