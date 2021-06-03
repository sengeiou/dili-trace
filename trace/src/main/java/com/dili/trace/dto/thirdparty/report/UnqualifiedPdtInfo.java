package com.dili.trace.dto.thirdparty.report;

import java.math.BigDecimal;
import java.util.Date;


public class UnqualifiedPdtInfo implements ReportDto {
    private Date updateTime;// 数据日期(格式:yyyy-MM-dd hh:mm:ss)
    private String stallNo;// 摊位号
    private String subjectName;// 经营主体名称
    private String pdtName;// 品种名称
    private String batchNo;// 批次
    private String pdtPlace;// 产地
    private String pdtSpec;// 规格
    private BigDecimal weight;// 重量

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
     * @return the stallNo
     */
    public String getStallNo() {
        return stallNo;
    }

    /**
     * @param stallNo the stallNo to set
     */
    public void setStallNo(String stallNo) {
        this.stallNo = stallNo;
    }

    /**
     * @return the subjectName
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * @param subjectName the subjectName to set
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * @return the pdtName
     */
    public String getPdtName() {
        return pdtName;
    }

    /**
     * @param pdtName the pdtName to set
     */
    public void setPdtName(String pdtName) {
        this.pdtName = pdtName;
    }

    /**
     * @return the batchNo
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * @param batchNo the batchNo to set
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    /**
     * @return the pdtPlace
     */
    public String getPdtPlace() {
        return pdtPlace;
    }

    /**
     * @param pdtPlace the pdtPlace to set
     */
    public void setPdtPlace(String pdtPlace) {
        this.pdtPlace = pdtPlace;
    }

    /**
     * @return the pdtSpec
     */
    public String getPdtSpec() {
        return pdtSpec;
    }

    /**
     * @param pdtSpec the pdtSpec to set
     */
    public void setPdtSpec(String pdtSpec) {
        this.pdtSpec = pdtSpec;
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
    
}