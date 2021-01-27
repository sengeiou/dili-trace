package com.dili.trace.dto;

import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.domain.DetectRecord;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author laikui
 * @date 2019/8/15
 */
public class DetectRecordParam extends DetectRecord {
    /**
     * 仪器编号
     *
     * @return
     */
    @ApiModelProperty(value = "仪器编号")
    private String exeMachineNo;

    /**
     * 环境标记
     *
     * @return
     */
    @ApiModelProperty(value = "环境标记")
    private String tag;
    /**
     * 采样编号
     *
     * @return
     */
    @Transient
    private String sampleCode;
    /**
     * 抽检类型（1，抽检检测、2.抽检人工录入结果）
     *
     * @return
     */
    @Transient
    private Integer spotCheckType;

    /**
     * 报备单id
     *
     * @return
     */
    @Transient
    private Long billId;
    /**
     * 检测人员id
     *
     * @return
     */
    @Transient
    private Long detectOperatorId;

    /**
     * 检测费用
     *
     * @return
     */
    @Transient
    private BigDecimal detectFee;

    /**
     * 检测时间查询开始（上报）
     *
     * @return
     */
    @Transient
    private Date detectTimeStart;

    /**
     * 身份证号（上报）
     *
     * @return
     */
    @Transient
    private String idCardNo;

    /**
     * 市场id（上报）
     *
     * @return
     */
    @Transient
    private Long marketId;
    /**
     * 商品名称（上报）
     *
     * @return
     */
    @Transient
    private String productName;
    /**
     * 商品id（上报）
     *
     * @return
     */
    @Transient
    private Long productId;
    /**
     * 检测类型（上报）
     *
     * @return
     */
    @Transient
    private Integer detectType;


    /**
     * 商品码（上报）
     *
     * @return
     */
    @Transient
    private String goodsCode;

    public BigDecimal getDetectFee() {
        return detectFee;
    }

    public void setDetectFee(BigDecimal detectFee) {
        this.detectFee = detectFee;
    }

    public Long getDetectOperatorId() {
        return detectOperatorId;
    }

    public void setDetectOperatorId(Long detectOperatorId) {
        this.detectOperatorId = detectOperatorId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Integer getSpotCheckType() {
        return spotCheckType;
    }

    public void setSpotCheckType(Integer spotCheckType) {
        this.spotCheckType = spotCheckType;
    }

    public Date getDetectTimeStart() {
        return detectTimeStart;
    }

    public void setDetectTimeStart(Date detectTimeStart) {
        this.detectTimeStart = detectTimeStart;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public Integer getDetectType() {
        return detectType;
    }

    @Override
    public void setDetectType(Integer detectType) {
        this.detectType = detectType;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getExeMachineNo() {
        return exeMachineNo;
    }

    public void setExeMachineNo(String exeMachineNo) {
        this.exeMachineNo = exeMachineNo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }
}
