package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.dto.thirdparty.report.ReportInspectionImgDto;
import com.dili.trace.dto.thirdparty.report.ReportInspectionItemDto;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author asa.lee
 */
@Table(name = "check_order")
public class CheckOrder extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;
    @Column(name = "`id_card`")
    private String idCard;

    @Column(name = "`check_no`")
    private String checkNo;
    @Column(name = "`check_org_code`")
    private String checkOrgCode;
    @Column(name = "`check_org_name`")
    private String checkOrgName;
    @Column(name = "`check_result`")
    private String checkResult;
    @Column(name = "`check_time`")
    private Date checkTime;
    @Column(name = "`check_type`")
    private String checkType;
    @Column(name = "`checker`")
    private String checker;
    @Column(name = "`goods_name`")
    private String goodName;
    @Column(name = "`goods_code`")
    private String goodsCode;
    @Column(name = "`market_id`")
    private String marketId;
    @Column(name = "`report_flag`")
    private Integer reportFlag;
    @Transient
    private List<ReportInspectionImgDto> checkImgList;
    @Transient
    private List<ReportInspectionItemDto> checkItem;

    public Date getCheckTime() {
        return checkTime;
    }

    public String getCheckOrgCode() {
        return checkOrgCode;
    }

    public void setCheckOrgCode(String checkOrgCode) {
        this.checkOrgCode = checkOrgCode;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReportFlag() {
        return reportFlag;
    }

    public void setReportFlag(Integer reportFlag) {
        this.reportFlag = reportFlag;
    }

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
