package com.dili.trace.api.input;

import java.math.BigDecimal;
import java.util.List;

import com.dili.trace.domain.ImageCert;

import io.swagger.annotations.ApiModelProperty;

public class CreateRegisterBillInputDto {
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

	/**
	 * 图片证明列表
	 */
	private List<ImageCert> imageCertList;

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

}
