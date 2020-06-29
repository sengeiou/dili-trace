package com.dili.trace.api.input;

import java.math.BigDecimal;
import java.util.List;

import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;

import io.swagger.annotations.ApiModelProperty;

public class CreateRegisterBillInputDto {

	@ApiModelProperty(value = "保存类型")
	private Integer preserveType;

	@ApiModelProperty(value = "商品名称")
	private String productName;

	@ApiModelProperty(value = "商品ID")
	private Long productId;

	@ApiModelProperty(value = "产地ID")
	private Long originId;

	@ApiModelProperty(value = "产地名")
	private String originName;

	@ApiModelProperty(value = "重量")
	private BigDecimal weight;

	@ApiModelProperty(value = "重量单位")
	private Integer weightUnit;

	@ApiModelProperty(value = "规格")
	private String specName;

	@ApiModelProperty(value = "报备类型")
	private Integer billType;

	/**
	 * 图片证明列表
	 */
	private List<ImageCert> imageCertList;

	public RegisterBill build(User user) {
		RegisterBill registerBill = new RegisterBill();
		registerBill.setOperatorName(user.getName());
		registerBill.setOperatorId(user.getId());
		registerBill.setUserId(user.getId());
		registerBill.setName(user.getName());
		registerBill.setAddr(user.getAddr());
		registerBill.setIdCardNo(user.getCardNo());
		registerBill.setWeight(this.getWeight());
		registerBill.setWeightUnit(this.getWeightUnit());
		registerBill.setOriginId(this.getOriginId());
		registerBill.setOriginName(this.getOriginName());
		registerBill.setProductId(this.getProductId());
		registerBill.setProductName(this.getProductName());
		registerBill.setSpecName(this.getSpecName());
		registerBill.setPreserveType(this.getPreserveType());
		registerBill.setBillType(this.getBillType());
		return registerBill;
	}

	public List<ImageCert> getImageCertList() {
		return imageCertList;
	}

	public void setImageCertList(List<ImageCert> imageCertList) {
		this.imageCertList = imageCertList;
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

	public Long getOriginId() {
		return originId;
	}

	public void setOriginId(Long originId) {
		this.originId = originId;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public Integer getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(Integer weightUnit) {
		this.weightUnit = weightUnit;
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

}
