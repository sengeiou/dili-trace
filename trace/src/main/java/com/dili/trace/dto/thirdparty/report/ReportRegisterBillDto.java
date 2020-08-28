package com.dili.trace.dto.thirdparty.report;

import com.dili.trace.enums.ReportDtoTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReportRegisterBillDto implements ReportDto {

    private String approvalName;// 审核人姓名
    private Integer approvalStatus;// 审核状态 0-默认未审核 1-通过 2-退回 3-未通过
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approvalTime;// 审核时间
    private String brand;// 品牌
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date enterTime;// 报备时间
    private String marketId;// 市场id
    private String productionStr;// 产地字符串 省市区之间用-分隔
    private List<CredentialInfoDto> pzAddVoList;// 照片集合
    private String specification;// 规格
    private String thirdAccId;// 经营户id
    private String thirdEnterId;// 报备id
    private String thirdGoodsId;// 商品id
    private String thirdGoodsName;// 商品名字
    private String thirdUpId;// 上游id
    private String transporterId;// 车牌号
    private BigDecimal enterNum=BigDecimal.ZERO;// 数量
    private String unitName;// 计量单位名称(斤 /公斤)

    @JsonIgnore
    @Override
    public ReportDtoTypeEnum getType(){
        return ReportDtoTypeEnum.registerBill;
    };

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getProductionStr() {
        return productionStr;
    }

    public void setProductionStr(String productionStr) {
        this.productionStr = productionStr;
    }

    public List<CredentialInfoDto> getPzAddVoList() {
        return pzAddVoList;
    }

    public void setPzAddVoList(List<CredentialInfoDto> pzAddVoList) {
        this.pzAddVoList = pzAddVoList;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getThirdAccId() {
        return thirdAccId;
    }

    public void setThirdAccId(String thirdAccId) {
        this.thirdAccId = thirdAccId;
    }

    public String getThirdEnterId() {
        return thirdEnterId;
    }

    public void setThirdEnterId(String thirdEnterId) {
        this.thirdEnterId = thirdEnterId;
    }

    public String getThirdGoodsId() {
        return thirdGoodsId;
    }

    public void setThirdGoodsId(String thirdGoodsId) {
        this.thirdGoodsId = thirdGoodsId;
    }

    public String getThirdGoodsName() {
        return thirdGoodsName;
    }

    public void setThirdGoodsName(String thirdGoodsName) {
        this.thirdGoodsName = thirdGoodsName;
    }

    public String getThirdUpId() {
        return thirdUpId;
    }

    public void setThirdUpId(String thirdUpId) {
        this.thirdUpId = thirdUpId;
    }

    public String getTransporterId() {
        return transporterId;
    }

    public void setTransporterId(String transporterId) {
        this.transporterId = transporterId;
    }

    public BigDecimal getEnterNum() {
        return enterNum;
    }

    public void setEnterNum(BigDecimal enterNum) {
        this.enterNum = enterNum;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}