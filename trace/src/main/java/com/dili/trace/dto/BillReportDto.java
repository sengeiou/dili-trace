package com.dili.trace.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.domain.BaseDomain;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.TFEnum;

public class BillReportDto extends BaseDomain{
    private Long billId;
    private String billCode;
    private String userName;
    private String phone;
    private String plate;
    private String brandName;
    private String tallyAreaNos;
    private String legalPerson;
    private Date billCreated;
    private Date checkinCreated;
    private Integer checkinStatus;
    private Integer billType;
    private String productName;
    private Integer preserveType ;
    private String originName; 
    private Integer verifyStatus;  
    private BigDecimal weight;
    private Integer weightUnit;
    private Integer isDeleted;
    private String thirdPartyCode;
    private String area;
    private Integer measureType;
    private BigDecimal pieceNum;
    private String pieceWeight;
    private String remark;

    public String getIsDeletedName() {
        if(YesOrNoEnum.YES.getCode().equals(this.getIsDeleted())){
            return "是";
        };
        if( YesOrNoEnum.NO.getCode().equals(this.getIsDeleted())){
            return "否";
        };
        return "";
    }
    public String getBillTypeName(){
        return BillTypeEnum.fromCode(this.getBillType()).map(BillTypeEnum::getName).orElse("");
    }
    public String getCheckinStatusName(){
        return Optional.ofNullable(CheckinStatusEnum.fromCode(this.getCheckinStatus())).map(CheckinStatusEnum::getDesc).orElse("");
    }
    public String getPreserveTypeName(){
        return PreserveTypeEnum.fromCode(this.getPreserveType()).map(PreserveTypeEnum::getName).orElse("");
    }
    public String getWeightUnitName(){
        return WeightUnitEnum.fromCode(this.getWeightUnit()).map(WeightUnitEnum::getName).orElse("");
    }
    public String getMeasureTypeName(){
        return MeasureTypeEnum.fromCode(this.getMeasureType()).map(MeasureTypeEnum::getName).orElse("");
    }

    /**
     * @return Long return the billId
     */
    public Long getBillId() {
        return billId;
    }

    /**
     * @param billId the billId to set
     */
    public void setBillId(Long billId) {
        this.billId = billId;
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
     * @return String return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return String return the tallyAreaNos
     */
    public String getTallyAreaNos() {
        return tallyAreaNos;
    }

    /**
     * @param tallyAreaNos the tallyAreaNos to set
     */
    public void setTallyAreaNos(String tallyAreaNos) {
        this.tallyAreaNos = tallyAreaNos;
    }

    /**
     * @return String return the legalPerson
     */
    public String getLegalPerson() {
        return legalPerson;
    }

    /**
     * @param legalPerson the legalPerson to set
     */
    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    /**
     * @return Date return the billCreated
     */
    public Date getBillCreated() {
        return billCreated;
    }

    /**
     * @param billCreated the billCreated to set
     */
    public void setBillCreated(Date billCreated) {
        this.billCreated = billCreated;
    }

    /**
     * @return Date return the checkinCreated
     */
    public Date getCheckinCreated() {
        return checkinCreated;
    }

    /**
     * @param checkinCreated the checkinCreated to set
     */
    public void setCheckinCreated(Date checkinCreated) {
        this.checkinCreated = checkinCreated;
    }

  
    /**
     * @return String return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }


    /**
     * @return String return the billCode
     */
    public String getBillCode() {
        return billCode;
    }

    /**
     * @param billCode the billCode to set
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
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
     * @return Integer return the preserveType
     */
    public Integer getPreserveType() {
        return preserveType;
    }

    /**
     * @param preserveType the preserveType to set
     */
    public void setPreserveType(Integer preserveType) {
        this.preserveType = preserveType;
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
     * @return Integer return the verifyStatus
     */
    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    /**
     * @param verifyStatus the verifyStatus to set
     */
    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }


    /**
     * @return Integer return the checkinStatus
     */
    public Integer getCheckinStatus() {
        return checkinStatus;
    }

    /**
     * @param checkinStatus the checkinStatus to set
     */
    public void setCheckinStatus(Integer checkinStatus) {
        this.checkinStatus = checkinStatus;
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
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * @return the isDeleted
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }


    /**
     * @param billType the billType to set
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    /**
     * @return the billType
     */
    public Integer getBillType() {
        return billType;
    }

    public String getThirdPartyCode() {
        return thirdPartyCode;
    }

    public void setThirdPartyCode(String thirdPartyCode) {
        this.thirdPartyCode = thirdPartyCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getMeasureType() {
        return measureType;
    }

    public void setMeasureType(Integer measureType) {
        this.measureType = measureType;
    }

    public BigDecimal getPieceNum() {
        return pieceNum;
    }

    public void setPieceNum(BigDecimal pieceNum) {
        this.pieceNum = pieceNum;
    }

    public String getPieceWeight() {
        return pieceWeight;
    }

    public void setPieceWeight(String pieceWeight) {
        this.pieceWeight = pieceWeight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}