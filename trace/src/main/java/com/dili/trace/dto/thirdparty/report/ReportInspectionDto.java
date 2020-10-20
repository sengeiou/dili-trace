package com.dili.trace.dto.thirdparty.report;

import java.util.List;

/**
 * @author asa.lee
 */
public class ReportInspectionDto {
    private String idCard;
    private List<ReportInspectionImgDto> checkImgList;
    private List<ReportInspectionItemDto> checkItem;
    private String checkNo;
    private String checkOrgName;
    private String checkResult;
    private String checkTime;
    private String checkType;
    private String checker;
    private String goodName;
    private String goodsCode;
    private String marketId;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public List<ReportInspectionImgDto> getCheckImgList() {
        return checkImgList;
    }

    public void setCheckImgList(List<ReportInspectionImgDto> checkImgList) {
        this.checkImgList = checkImgList;
    }

    public List<ReportInspectionItemDto> getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(List<ReportInspectionItemDto> checkItem) {
        this.checkItem = checkItem;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getCheckOrgName() {
        return checkOrgName;
    }

    public void setCheckOrgName(String checkOrgName) {
        this.checkOrgName = checkOrgName;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }
}
