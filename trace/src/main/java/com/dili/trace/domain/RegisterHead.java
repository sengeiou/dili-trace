package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.TFEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 进门主台账单实体类
 *
 * @author Lily
 */
@Table(name = "`register_head`")
public class RegisterHead extends BaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;

	@ApiModelProperty(value = "主台账编号")
	@Column(name = "`code`")
	private String code;

	@ApiModelProperty(value = "单据类型。10-正常进场 20-补单 30-外冷分批进场。")
	@Column(name = "`bill_type`")
	private Integer billType;

	@ApiModelProperty(value = "业户ID")
	@Column(name = "`user_id`")
	private Long userId;

	@ApiModelProperty(value = "业户姓名")
	@Column(name = "`name`")
	private String name;

	@ApiModelProperty(value = "业户身份证号")
	@Column(name = "`id_card_no`")
	private String idCardNo;

	@ApiModelProperty(value = "经营户卡号")
	@Column(name = "`third_party_code`")
	private String thirdPartyCode;

	@ApiModelProperty(value = "身份地址")
	@Column(name = "`addr`")
	private String addr;

	@ApiModelProperty(value = "业户手机号")
	@Column(name = "`phone`")
	private String phone;

	@ApiModelProperty(value = "车牌号")
	@Column(name = "`plate`")
	private String plate;

	@ApiModelProperty(value = "商品ID")
	@Column(name = "`product_id`")
	private Long productId;

	@ApiModelProperty(value = "商品名称")
	@Column(name = "`product_name`")
	private String productName;

	@ApiModelProperty(value = "计量类型。10-计件 20-计重。默认计件。")
	@Column(name = "`measure_type`")
	private Integer measureType;

	@ApiModelProperty(value = "件数")
	@Column(name = "`piece_num`")
	private BigDecimal pieceNum;

	@ApiModelProperty(value = "件重")
	@Column(name = "`piece_weight`")
	private BigDecimal pieceWeight;

	@ApiModelProperty(value = "总重量")
	@Column(name = "`weight`")
	private BigDecimal weight;

	@ApiModelProperty(value = "重量单位。1-斤 2-公斤。默认1")
	@Column(name = "`weight_unit`")
	private Integer weightUnit;

	@ApiModelProperty(value = "上游信息ID")
	@Column(name = "`upstream_id`")
	private Long upStreamId;

	@ApiModelProperty(value = "规格名称")
	@Column(name = "`spec_name`")
	private String specName;

	@ApiModelProperty(value = "产地ID")
	@Column(name = "`origin_id`")
	private Long originId;

	@ApiModelProperty(value = "产地名")
	@Column(name = "`origin_name`")
	private String originName;

	@ApiModelProperty(value = "品牌ID")
	@Column(name = "`brand_id`")
	private Long brandId;

	@ApiModelProperty(value = "品牌名称")
	@Column(name = "`brand_name`")
	private String brandName;

	@ApiModelProperty(value = "备注")
	@Column(name = "`remark`")
	private String remark;

	@ApiModelProperty(value = "创建人")
	@Column(name = "`create_user`")
	private String createUser;

	@ApiModelProperty(value = "创建时间")
	@Column(name = "`created`")
	private Date created;

	@ApiModelProperty(value = "修改人")
	@Column(name = "`modify_user`")
	private String modifyUser;

	@ApiModelProperty(value = "修改时间")
	@Column(name = "`modified`")
	private Date modified;

	@ApiModelProperty(value = "是否作废。0-否 1-是")
	@Column(name = "`is_deleted`")
	private Integer isDeleted;

	@ApiModelProperty(value = "作废人")
	@Column(name = "`delete_user`")
	private String deleteUser;

	@ApiModelProperty(value = "作废时间")
	@Column(name = "`delete_time`")
	private Date deleteTime;

	@ApiModelProperty(value = "版本")
	@Column(name = "`version`")
	private Integer version;

	@ApiModelProperty(value = "原因")
	@Column(name = "`reason`")
	private String reason;

	@ApiModelProperty(value = "是否启用。0-否 1-是")
	@Column(name = "`active`")
	private Integer active;

	@ApiModelProperty(value = "市场ID")
	@Column(name = "market_id")
	private Long marketId;


	@Transient
	private String weightUnitName;

	@Transient
	private List<ImageCert> imageCertList;

	@Transient
	private List<RegisterBill> registerBills;

	@Transient
	private String upStreamName;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getThirdPartyCode() {
		return thirdPartyCode;
	}

	public void setThirdPartyCode(String thirdPartyCode) {
		this.thirdPartyCode = thirdPartyCode;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
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

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Long getMarketId() {
		return marketId;
	}

	public void setMarketId(Long marketId) {
		this.marketId = marketId;
	}

	public String getWeightUnitName() {
		return weightUnitName;
	}

	public void setWeightUnitName(String weightUnitName) {
		this.weightUnitName = weightUnitName;
	}

	public List<ImageCert> getImageCertList() {
		return imageCertList;
	}

	public void setImageCertList(List<ImageCert> imageCertList) {
		this.imageCertList = imageCertList;
	}

	public List<RegisterBill> getRegisterBills() {
		return registerBills;
	}

	public void setRegisterBills(List<RegisterBill> registerBills) {
		this.registerBills = registerBills;
	}

	public String getUpStreamName() {
		return upStreamName;
	}

	public void setUpStreamName(String upStreamName) {
		this.upStreamName = upStreamName;
	}
}