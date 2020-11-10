package com.dili.trace.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.util.Date;

/**
 * Description:
 *
 * @author Ron.Peng
 * @date 2020/11/3
 */
public class CheckExcelDto extends BaseRowModel {
    @ExcelProperty(value = "检验批号", index = 0)
    private String checkNo;
    @ExcelProperty(value = "经营户编号", index = 1)
    private String thirdPartyCode;
    @ExcelProperty(value = "经营户名称", index = 2)
    private String userName;
    @ExcelProperty(value = "产品编号", index = 3)
    private String goodsCode;
    @ExcelProperty(value = "产品名称", index = 4)
    private String goodsName;
    @ExcelProperty(value = "检测项目", index = 5)
    private String project;
    @ExcelProperty(value = "标准值", index = 6)
    private String normalValue;
    @ExcelProperty(value = "实测值", index = 7)
    private String value;
    @ExcelProperty(value = "检测人", index = 8)
    private String checker;
    @ExcelProperty(value = "检测机构", index = 9)
    private String checkOrgName;
    @ExcelProperty(value = "检测类型", index = 10)
    private String checkType;
    @ExcelProperty(value = "检测结果", index = 11)
    private String checkResult;
    @ExcelProperty(value = "检测时间", index = 12)
    private Date checkTime;
    @ExcelProperty(value = "进货批次号", index = 13)
    private String inboundNo;




    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getThirdPartyCode() {
        return thirdPartyCode;
    }

    public void setThirdPartyCode(String thirdPartyCode) {
        this.thirdPartyCode = thirdPartyCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getNormalValue() {
        return normalValue;
    }

    public void setNormalValue(String normalValue) {
        this.normalValue = normalValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getCheckOrgName() {
        return checkOrgName;
    }

    public void setCheckOrgName(String checkOrgName) {
        this.checkOrgName = checkOrgName;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getInboundNo() {
        return inboundNo;
    }

    public void setInboundNo(String inboundNo) {
        this.inboundNo = inboundNo;
    }
}
