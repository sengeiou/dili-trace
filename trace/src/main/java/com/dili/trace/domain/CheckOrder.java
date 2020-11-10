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
    private String goodsName;
    @Column(name = "`goods_code`")
    private String goodsCode;
    @Column(name = "`market_id`")
    private Long marketId;
    @Column(name = "`report_flag`")
    private Integer reportFlag;

    @Column(name = "`user_id`")
    private String userId;
    @Column(name = "`user_name`")
    private String userName;
    @Column(name = "`tally_area_nos`")
    private String tallyAreaNos;
    @Column(name = "`inbound_no`")
    private String inboundNo;
    @Column(name = "`third_party_code`")
    private String thirdPartyCode;

    public String getThirdPartyCode() {
        return thirdPartyCode;
    }

    public void setThirdPartyCode(String thirdPartyCode) {
        this.thirdPartyCode = thirdPartyCode;
    }

    private CheckOrderData checkOrderData;

    private ImageCert imageCert;

    public ImageCert getImageCert() {
        return imageCert;
    }

    public void setImageCert(ImageCert imageCert) {
        this.imageCert = imageCert;
    }

    public CheckOrderData getCheckOrderData() {
        return checkOrderData;
    }

    public void setCheckOrderData(CheckOrderData checkOrderData) {
        this.checkOrderData = checkOrderData;
    }

    @Transient
    private List<ReportInspectionImgDto> checkImgList;
    @Transient
    private List<ReportInspectionItemDto> checkItem;

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTallyAreaNos() {
        return tallyAreaNos;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTallyAreaNos(String tallyAreaNos) {
        this.tallyAreaNos = tallyAreaNos;
    }

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getInboundNo() {
        return inboundNo;
    }

    public void setInboundNo(String inboundNo) {
        this.inboundNo = inboundNo;
    }
}
