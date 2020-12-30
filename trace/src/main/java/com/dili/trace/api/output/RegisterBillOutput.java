package com.dili.trace.api.output;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.TruckTypeEnum;
import com.dili.trace.enums.WeightUnitEnum;

public class RegisterBillOutput {
    private Long billId;
    private Long userId;
    private Integer verifyStatus;
    private String verifyStatusDesc;
    private String productName;
    private String plate;
    private BigDecimal weight;
    private Integer weightUnit;
    private String weightUnitName;
    private Integer truckType;
    private String truckTypeName;
    private Integer verifyType;
    private Integer billType;
    private String billTypeName;
    private String specName;
    private Date created;
    private String reason;
    private Integer isCheckin;

    private String userName;
    private String tallyAreaNo;
    private String originName;
    // 上游名称
    private String upStreamName;
    // 编号
    private String code;
    // 品牌
    private String brandName;
    // 图片列表
    private List<ImageCert> imageCertList;

    // private Integer color;

    public static RegisterBillOutput build(RegisterBill bill) {
        RegisterBillOutput out = new RegisterBillOutput();
        out.setBillId(bill.getId());
        out.setVerifyStatus(bill.getVerifyStatus());
        out.setProductName(bill.getProductName());
        out.setVerifyStatusDesc(
                BillVerifyStatusEnum.fromCode(bill.getVerifyStatus()).map(BillVerifyStatusEnum::getName).orElse(""));
        out.setUserId(bill.getUserId());
        out.setPlate(bill.getPlate());
        out.setWeight(bill.getWeight());
        out.setWeightUnit(bill.getWeightUnit());
        out.setCreated(bill.getCreated());
        out.setTruckType(bill.getTruckType());
        out.setTruckTypeName(TruckTypeEnum.fromCode(bill.getTruckType()).map(TruckTypeEnum::getName).orElse(""));
        out.setUserName(bill.getName());
        out.setOriginName(bill.getOriginName());
        out.setTallyAreaNo(bill.getTallyAreaNo());
        out.setWeightUnitName(WeightUnitEnum.fromCode(bill.getWeightUnit()).map(WeightUnitEnum::getName).orElse(""));
        out.setBrandName(bill.getBrandName());
        out.setCode(bill.getCode());
        out.setBillType(bill.getBillType());
        out.setBillTypeName(BillTypeEnum.fromCode(bill.getBillType()).map(BillTypeEnum::getName).orElse(""));
        out.setSpecName(bill.getSpecName());
        out.setVerifyType(bill.getVerifyType());
        out.setReason(bill.getReason());
        out.setIsCheckin(bill.getIsCheckin());
        out.setTallyAreaNo(bill.getTallyAreaNo());
        return out;
    }



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getVerifyStatusDesc() {
        return verifyStatusDesc;
    }

    public void setVerifyStatusDesc(String verifyStatusDesc) {
        this.verifyStatusDesc = verifyStatusDesc;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    /**
     * @return Long return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return String return the plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     * @param plate the plate to set
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }

    /**
     * @return BigDecimal return the weight
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

    /**
     * @return Integer return the weightUnit
     */
    public Integer getWeightUnit() {
        return weightUnit;
    }

    /**
     * @param weightUnit the weightUnit to set
     */
    public void setWeightUnit(Integer weightUnit) {
        this.weightUnit = weightUnit;
    }

    /**
     * @return Date return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return Integer return the truckType
     */
    public Integer getTruckType() {
        return truckType;
    }

    /**
     * @param truckType the truckType to set
     */
    public void setTruckType(Integer truckType) {
        this.truckType = truckType;
    }

    /**
     * @return String return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return String return the tallyAreaNo
     */
    public String getTallyAreaNo() {
        return tallyAreaNo;
    }

    /**
     * @param tallyAreaNo the tallyAreaNo to set
     */
    public void setTallyAreaNo(String tallyAreaNo) {
        this.tallyAreaNo = tallyAreaNo;
    }

    /**
     * @return String return the originName
     */
    public String getOriginName() {
        return originName;
    }

    /**
     * @param originName the originName to set
     */
    public void setOriginName(String originName) {
        this.originName = originName;
    }

    /**
     * @return String return the weightUnitName
     */
    public String getWeightUnitName() {
        return weightUnitName;
    }

    /**
     * @param weightUnitName the weightUnitName to set
     */
    public void setWeightUnitName(String weightUnitName) {
        this.weightUnitName = weightUnitName;
    }

    /**
     * @return String return the upStreamName
     */
    public String getUpStreamName() {
        return upStreamName;
    }

    /**
     * @param upStreamName the upStreamName to set
     */
    public void setUpStreamName(String upStreamName) {
        this.upStreamName = upStreamName;
    }

    /**
     * @return String return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return String return the brandName
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * @param brandName the brandName to set
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * @return List<ImageCert> return the imageCertList
     */
    public List<ImageCert> getImageCertList() {
        return imageCertList;
    }

    /**
     * @param imageCertList the imageCertList to set
     */
    public void setImageCertList(List<ImageCert> imageCertList) {
        this.imageCertList = imageCertList;
    }

    /**
     * @return Integer return the verifyType
     */
    public Integer getVerifyType() {
        return verifyType;
    }

    /**
     * @param verifyType the verifyType to set
     */
    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    /**
     * @return Integer return the billType
     */
    public Integer getBillType() {
        return billType;
    }

    /**
     * @param billType the billType to set
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }


    /**
     * @return String return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }



    /**
     * @return String return the truckTypeName
     */
    public String getTruckTypeName() {
        return truckTypeName;
    }

    /**
     * @param truckTypeName the truckTypeName to set
     */
    public void setTruckTypeName(String truckTypeName) {
        this.truckTypeName = truckTypeName;
    }

    /**
     * @return String return the billTypeName
     */
    public String getBillTypeName() {
        return billTypeName;
    }

    /**
     * @param billTypeName the billTypeName to set
     */
    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }

    /**
     * @return String return the specName
     */
    public String getSpecName() {
        return specName;
    }

    /**
     * @param specName the specName to set
     */
    public void setSpecName(String specName) {
        this.specName = specName;
    }


    /**
     * @param isCheckin the isCheckin to set
     */
    public void setIsCheckin(Integer isCheckin) {
        this.isCheckin = isCheckin;
    }

}
