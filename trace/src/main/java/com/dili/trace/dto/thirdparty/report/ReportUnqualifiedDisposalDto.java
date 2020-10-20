package com.dili.trace.dto.thirdparty.report;

import java.util.List;

/**
 * @author asa.lee
 */
public class ReportUnqualifiedDisposalDto {
    private List<ReportInspectionImgDto> checkFailImgList;
    private String checkNo;
    private String chuZhiDate;
    private String chuZhiDesc;
    private String chuZhiNum;
    private String chuZhiResult;
    private String chuZhiType;
    private String chuZhier;
    private String marketId;

    public List<ReportInspectionImgDto> getCheckFailImgList() {
        return checkFailImgList;
    }

    public void setCheckFailImgList(List<ReportInspectionImgDto> checkFailImgList) {
        this.checkFailImgList = checkFailImgList;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getChuZhiDate() {
        return chuZhiDate;
    }

    public void setChuZhiDate(String chuZhiDate) {
        this.chuZhiDate = chuZhiDate;
    }

    public String getChuZhiDesc() {
        return chuZhiDesc;
    }

    public void setChuZhiDesc(String chuZhiDesc) {
        this.chuZhiDesc = chuZhiDesc;
    }

    public String getChuZhiNum() {
        return chuZhiNum;
    }

    public void setChuZhiNum(String chuZhiNum) {
        this.chuZhiNum = chuZhiNum;
    }

    public String getChuZhiResult() {
        return chuZhiResult;
    }

    public void setChuZhiResult(String chuZhiResult) {
        this.chuZhiResult = chuZhiResult;
    }

    public String getChuZhiType() {
        return chuZhiType;
    }

    public void setChuZhiType(String chuZhiType) {
        this.chuZhiType = chuZhiType;
    }

    public String getChuZhier() {
        return chuZhier;
    }

    public void setChuZhier(String chuZhier) {
        this.chuZhier = chuZhier;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }
}
