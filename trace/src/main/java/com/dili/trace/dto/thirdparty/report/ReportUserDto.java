package com.dili.trace.dto.thirdparty.report;

import java.util.List;

/**
 * @author asa.lee
 */
public class ReportUserDto {
    private String marketId;
    private String accountName;
    private Integer accountType;
    private String address;
    private List<ReportUserImgDto> blAccountImgList;
    private String boothNo;
    private String cardNo;
    private String className;
    private String code;
    private String comeName;
    private Integer custType;
    private String legal;
    private String license;
    private Integer status;
    private String telphone;
    private String thirdAccId;

    private String idCard;
    private String tel;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ReportUserImgDto> getBlAccountImgList() {
        return blAccountImgList;
    }

    public void setBlAccountImgList(List<ReportUserImgDto> blAccountImgList) {
        this.blAccountImgList = blAccountImgList;
    }

    public String getBoothNo() {
        return boothNo;
    }

    public void setBoothNo(String boothNo) {
        this.boothNo = boothNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComeName() {
        return comeName;
    }

    public void setComeName(String comeName) {
        this.comeName = comeName;
    }

    public Integer getCustType() {
        return custType;
    }

    public void setCustType(Integer custType) {
        this.custType = custType;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getThirdAccId() {
        return thirdAccId;
    }

    public void setThirdAccId(String thirdAccId) {
        this.thirdAccId = thirdAccId;
    }
}
