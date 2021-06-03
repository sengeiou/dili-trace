package com.dili.trace.dto.thirdparty.report;

import java.util.Date;


public class WaringInfoDto implements ReportDto {
    private String marketName;
    private String marketId;
    private String stallNo;// 摊位号
    private String subjectName;// 经营主体名称
    private String codeStatus;// 码状态（黄码，红码）
    private String warningReason;// 黄码，红码原因
    private Date updateTime;// 更新日期(格式:yyyy-MM-dd hh:mm:ss)

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
     * @return the codeStatus
     */
    public String getCodeStatus() {
        return codeStatus;
    }

    /**
     * @param codeStatus the codeStatus to set
     */
    public void setCodeStatus(String codeStatus) {
        this.codeStatus = codeStatus;
    }

    /**
     * @return the warningReason
     */
    public String getWarningReason() {
        return warningReason;
    }

    /**
     * @param warningReason the warningReason to set
     */
    public void setWarningReason(String warningReason) {
        this.warningReason = warningReason;
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


    /**
     * @return String return the marketName
     */
    public String getMarketName() {
        return marketName;
    }

    /**
     * @param marketName the marketName to set
     */
    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    /**
     * @return String return the marketId
     */
    public String getMarketId() {
        return marketId;
    }

    /**
     * @param marketId the marketId to set
     */
    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

}