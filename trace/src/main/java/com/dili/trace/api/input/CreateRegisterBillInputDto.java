package com.dili.trace.api.input;

import java.math.BigDecimal;
import java.util.List;

import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.enums.TruckTypeEnum;

import org.apache.commons.lang3.StringUtils;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;

public class CreateRegisterBillInputDto {
	@ApiModelProperty(value = "报备单ID")
	private Long billId;

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

	@ApiModelProperty(value = "拼车类型")
	private Integer truckType;

	@ApiModelProperty(value = "报备类型")
	private Integer billType;

	@ApiModelProperty(value = "品牌名称")
	private String brandName;

	@ApiModelProperty(value = "品牌ID")
	private Long brandId;
	@ApiModelProperty(value = "车牌")
	private String plate;
	@ApiModelProperty(value = "上游企业ID")
	private Long upStreamId;

	@ApiModelProperty(value = "主台账编号")
	private String registerHeadCode;

	@ApiModelProperty(value = "计量类型。10-计件 20-计重。默认计件。")
	private Integer measureType;

	@ApiModelProperty(value = "件数")
	private BigDecimal pieceNum;

	@ApiModelProperty(value = "件重")
	private BigDecimal pieceWeight;

	@ApiModelProperty(value = "区号")
	private String area;

	@ApiModelProperty(value = "包装")
	private String packaging;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "查验状态")
	private Integer verifyStatus;

	@ApiModelProperty(value = "是否废弃")
	private Integer isDeleted;

	/**
	 * 图片证明列表
	 */
	private List<ImageCert> imageCertList;

	public RegisterBill build(User user) {
		RegisterBill registerBill = new RegisterBill();
		registerBill.setId(this.getBillId());
		// registerBill.setOperatorName(user.getName());
		// registerBill.setOperatorId(user.getId());
		registerBill.setUserId(user.getId());
		registerBill.setName(user.getName());
		registerBill.setTallyAreaNo(user.getTallyAreaNos());
		registerBill.setAddr(user.getAddr());
		registerBill.setIdCardNo(user.getCardNo());
		registerBill.setThirdPartyCode(user.getThirdPartyCode());
		registerBill.setWeight(this.getWeight());
		registerBill.setWeightUnit(this.getWeightUnit());
		registerBill.setOriginId(this.getOriginId());
		registerBill.setOriginName(this.getOriginName());
		registerBill.setProductId(this.getProductId());
		registerBill.setProductName(this.getProductName());
		registerBill.setSpecName(StringUtils.trim(this.getSpecName()));
		registerBill.setPreserveType(this.getPreserveType());
		registerBill.setBillType(this.getBillType());
		registerBill.setTruckType(this.getTruckType());
		registerBill.setBrandId(this.getBrandId());
		registerBill.setBrandName(StringUtils.trim(this.getBrandName()));
		registerBill.setPlate(this.getPlate());
		registerBill.setUpStreamId(this.getUpStreamId());
		registerBill.setRegisterHeadCode(this.getRegisterHeadCode());
		registerBill.setMeasureType(this.getMeasureType());
		registerBill.setPieceNum(this.getPieceNum());
		registerBill.setPieceWeight(this.getPieceWeight());
		registerBill.setArea(this.getArea());
		registerBill.setPackaging(this.getPackaging());
		registerBill.setRemark(this.getRemark());
		registerBill.setVerifyStatus(this.getVerifyStatus());
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
     * @return Long return the brandId
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * @param brandId the brandId to set
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
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
     * @return Long return the upStreamId
     */
    public Long getUpStreamId() {
        return upStreamId;
    }

    /**
     * @param upStreamId the upStreamId to set
     */
    public void setUpStreamId(Long upStreamId) {
        this.upStreamId = upStreamId;
    }

	public String getRegisterHeadCode() {
		return registerHeadCode;
	}

	public void setRegisterHeadCode(String registerHeadCode) {
		this.registerHeadCode = registerHeadCode;
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

	public BigDecimal getPieceWeight() {
		return pieceWeight;
	}

	public void setPieceWeight(BigDecimal pieceWeight) {
		this.pieceWeight = pieceWeight;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPackaging() {
		return packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
}
