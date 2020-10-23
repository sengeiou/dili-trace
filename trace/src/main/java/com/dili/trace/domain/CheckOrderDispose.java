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
@Table(name = "check_order_dispose")
public class CheckOrderDispose extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;
    @Column(name = "`check_no`")
    private String checkNo;
    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`dispose_date`")
    private Date disposeDate;
    @Column(name = "`dispose_desc`")
    private String disposeDesc;
    @Column(name = "`dispose_num`")
    private String disposeNum;
    @Column(name = "`dispose_result`")
    private String disposeResult;
    @Column(name = "`dispose_type`")
    private String disposeType;
    @Column(name = "`disposer`")
    private String disposeUser;
    @Column(name = "`disposer_id`")
    private String disposerId;
    @Column(name = "`report_flag`")
    private Integer reportFlag;
    @Transient
    private List<ReportInspectionImgDto> checkImgList;
    @Transient
    private List<ReportInspectionItemDto> checkItem;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDisposerId() {
        return disposerId;
    }

    public void setDisposerId(String disposerId) {
        this.disposerId = disposerId;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Date getDisposeDate() {
        return disposeDate;
    }

    public void setDisposeDate(Date disposeDate) {
        this.disposeDate = disposeDate;
    }

    public String getDisposeDesc() {
        return disposeDesc;
    }

    public void setDisposeDesc(String disposeDesc) {
        this.disposeDesc = disposeDesc;
    }

    public String getDisposeNum() {
        return disposeNum;
    }

    public void setDisposeNum(String disposeNum) {
        this.disposeNum = disposeNum;
    }

    public String getDisposeResult() {
        return disposeResult;
    }

    public void setDisposeResult(String disposeResult) {
        this.disposeResult = disposeResult;
    }

    public String getDisposeType() {
        return disposeType;
    }

    public void setDisposeType(String disposeType) {
        this.disposeType = disposeType;
    }

    public String getDisposeUser() {
        return disposeUser;
    }

    public void setDisposeUser(String disposeUser) {
        this.disposeUser = disposeUser;
    }

    public Integer getReportFlag() {
        return reportFlag;
    }

    public void setReportFlag(Integer reportFlag) {
        this.reportFlag = reportFlag;
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
}
