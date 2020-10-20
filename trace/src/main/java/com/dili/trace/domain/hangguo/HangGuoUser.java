package com.dili.trace.domain.hangguo;

import com.dili.ss.domain.BaseDomain;

import java.util.Date;

/**
 * 杭果溯源，经营户、会员信息
 * @author asa.lee
 */
public class HangGuoUser extends BaseDomain {

    /**
     * 供应商卡号
     */
    private String supplierNo;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     *证件类型
     */
    private String credentialType;
    /**
     *证件名称
     */
    private String credentialName;
    /**
     *证件号码
     */
    private String credentialNumber;
    /**
     *性别
     */
    private String sex;
    /**
     *营业执照号
     */
    private String liscensNo;
    /**
     *移动电话
     */
    private String mobileNumber;
    /**
     *固定电话
     */
    private String phoneNumber;
    /**
     *供应商状态
     */
    private Integer status;
    /**
     *住宅地址
     */
    private String addr;
    /**
     *手续费折扣率
     */
    private String chargeRate;
    /**
     *包装管理费折扣率
     */
    private String mangerRate;
    /**
     *仓储费折扣率
     */
    private String storageRate;
    /**
     *员工考核折扣率
     */
    private String assessRate;
    /**
     *折扣率批准人
     */
    private String approver;
    /**
     * 供应商类型（大客户、临时客户）
     */
    private String supplierType;
    /**
     *身份证地址
     */
    private String idAddr;
    /**
     *经营地址(通讯
     * 地址)
     */
    private String operateAddr;
    /**
     *卡有效期
     */
    private Date effectiveDate;
    /**
     *备注
     */
    private String remarkMemo;

    /**
     *备注
     */
    private String name;
    /**
     * 会员卡号
     */
    private String memberNo;
    /**
     *发卡日期
     */
    private Date enableDate;

    /**
     *经营性质
     */
    private String operateType;

    /**
     *联系电话
     */
    private String phoneNum;

    /**
     * 商品去向
     */
    private String whereis;

    /**
     * 授信额度
     */
    private String creditLimit;

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(Date enableDate) {
        this.enableDate = enableDate;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getWhereis() {
        return whereis;
    }

    public void setWhereis(String whereis) {
        this.whereis = whereis;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getCredentialName() {
        return credentialName;
    }

    public void setCredentialName(String credentialName) {
        this.credentialName = credentialName;
    }

    public String getCredentialNumber() {
        return credentialNumber;
    }

    public void setCredentialNumber(String credentialNumber) {
        this.credentialNumber = credentialNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLiscensNo() {
        return liscensNo;
    }

    public void setLiscensNo(String liscensNo) {
        this.liscensNo = liscensNo;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(String chargeRate) {
        this.chargeRate = chargeRate;
    }

    public String getMangerRate() {
        return mangerRate;
    }

    public void setMangerRate(String mangerRate) {
        this.mangerRate = mangerRate;
    }

    public String getStorageRate() {
        return storageRate;
    }

    public void setStorageRate(String storageRate) {
        this.storageRate = storageRate;
    }

    public String getAssessRate() {
        return assessRate;
    }

    public void setAssessRate(String assessRate) {
        this.assessRate = assessRate;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public String getIdAddr() {
        return idAddr;
    }

    public void setIdAddr(String idAddr) {
        this.idAddr = idAddr;
    }

    public String getOperateAddr() {
        return operateAddr;
    }

    public void setOperateAddr(String operateAddr) {
        this.operateAddr = operateAddr;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getRemarkMemo() {
        return remarkMemo;
    }

    public void setRemarkMemo(String remarkMemo) {
        this.remarkMemo = remarkMemo;
    }
}
