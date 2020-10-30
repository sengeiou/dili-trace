package com.dili.trace.api.input;

import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.RegisterHead;
import com.dili.trace.domain.User;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 进门主台账单参数接收类
 *
 * @author Lily
 */
public class CreateRegisterHeadInputDto {
	@ApiModelProperty(value = "进门主台账单ID")
	private Long id;

	@ApiModelProperty(value = "单据类型")
	private Integer billType;

	@ApiModelProperty(value = "商品名称")
	private String productName;

	@ApiModelProperty(value = "商品ID")
	private Long productId;

	@ApiModelProperty(value = "计量类型")
	private Integer measureType;

	@ApiModelProperty(value = "件数")
	private BigDecimal pieceNum;

	@ApiModelProperty(value = "件重")
	private BigDecimal pieceWeight;

	@ApiModelProperty(value = "总重量")
	private BigDecimal weight;

	@ApiModelProperty(value = "重量单位")
	private Integer weightUnit;

	@ApiModelProperty(value = "上游企业ID")
	private Long upStreamId;

	@ApiModelProperty(value = "产地ID")
	private Long originId;

	@ApiModelProperty(value = "产地名")
	private String originName;

	@ApiModelProperty(value = "规格")
	private String specName;

	@ApiModelProperty(value = "品牌名称")
	private String brandName;

	@ApiModelProperty(value = "品牌ID")
	private Long brandId;

	@ApiModelProperty(value = "车牌")
	private String plate;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "是否启用")
	private Integer active;

    @ApiModelProperty(value = "是否废弃")
	private Integer isDeleted;

	@ApiModelProperty(value = "经营户ID")
    private Long userId;

	/**
	 * 图片证明列表
	 */
	private List<ImageCert> imageCertList;

	public RegisterHead build(User user) {
		RegisterHead registerHead = new RegisterHead();
		registerHead.setId(this.getId());
		registerHead.setUserId(user.getId());
		registerHead.setName(user.getName());
		registerHead.setAddr(user.getAddr());
		registerHead.setIdCardNo(user.getCardNo());
		registerHead.setPhone(user.getPhone());
		registerHead.setThirdPartyCode(user.getThirdPartyCode());
		registerHead.setBillType(this.getBillType());
		registerHead.setProductId(this.getProductId());
		registerHead.setProductName(this.getProductName());
		registerHead.setMeasureType(this.getMeasureType());
		registerHead.setPieceWeight(this.getPieceWeight());
		registerHead.setPieceNum(this.getPieceNum());
		registerHead.setWeight(this.getWeight());
		registerHead.setWeightUnit(this.getWeightUnit());
		registerHead.setUpStreamId(this.getUpStreamId());
		registerHead.setOriginId(this.getOriginId());
		registerHead.setOriginName(this.getOriginName());
		registerHead.setSpecName(StringUtils.trim(this.getSpecName()));
		registerHead.setBrandId(this.getBrandId());
		registerHead.setBrandName(StringUtils.trim(this.getBrandName()));
		registerHead.setPlate(this.getPlate());
		registerHead.setRemark(this.getRemark());
		registerHead.setActive(this.getActive());
		return registerHead;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
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

	public Long getUpStreamId() {
		return upStreamId;
	}

	public void setUpStreamId(Long upStreamId) {
		this.upStreamId = upStreamId;
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

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ImageCert> getImageCertList() {
		return imageCertList;
	}

	public void setImageCertList(List<ImageCert> imageCertList) {
		this.imageCertList = imageCertList;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}