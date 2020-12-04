package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.RegisterBill;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;


public class RegisterBillDto extends RegisterBill {
	@ApiModelProperty(value = "查询登记开始时间")
	@Column(name = "`created`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private Date createdStart;

	@ApiModelProperty(value = "查询登记结束时间")
	@Column(name = "`created`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private Date createdEnd;

	@ApiModelProperty(value = "查询修改开始时间")
	@Column(name = "`modified`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private Date modifiedStart;

	@ApiModelProperty(value = "查询修改结束时间")
	@Column(name = "`modified`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private Date modifiedEnd;

	@ApiModelProperty(value = "查询检测开始时间")
	@Column(name = "`latest_detect_time`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private Date latestDetectTimeTimeStart;
	@ApiModelProperty(value = "查询检测结束时间")
	@Column(name = "`latest_detect_time`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private Date latestDetectTimeTimeEnd;

	@ApiModelProperty(value = "商品名称LIKE")
	@Column(name = "`product_name`")
	@Like(value = "RIGHT")
	private String likeProductName;

	@ApiModelProperty(value = "企业名称")
	@Column(name = "`corporate_name`")
	@Like(value="BOTH")
	private String likeCorporateName;

	@ApiModelProperty(value = "业户姓名")
	@Column(name = "`name`")
	@Like(value="BOTH")
	private String likeName;

	@Transient
	private String attr;
	@Transient
	private String attrValue;

	@Transient
	Boolean hasCheckSheet;

	@Column(name = "`state`")
	@Operator(Operator.IN)
	private List<Integer> stateList;
	
	@Column(name = "`verify_status`")
	@Operator(Operator.IN)
	private List<Integer> verifyStatusList;

	@Column(name = "`detect_state`")
	@Operator(Operator.IN)
	private List<Integer> detectStateList;
	
	@Column(name = "`plate`")
	@Operator(Operator.IN)
	private List<String> plateList;

	/**
	 * 昵称模糊查询
	 * 
	 * @return
	 */
	@Column(name = "tally_area_no")
	@Like
	private String likeTallyAreaNo;

	@Column(name = "`sample_code`")
	@Like
	private String likeSampleCode;

	@Column(name = "`sample_code`")
	@Operator(Operator.IN)
	private List<String> sampleCodeList;

	@ApiModelProperty(value = "IN商品ID")
	@Column(name = "`product_id`")
	private List<Long> productIdList;

	@ApiModelProperty(value = "IN ID")
	@Column(name = "`id`")
	@Operator(Operator.IN)
	private List<Long> idList;

	@ApiModelProperty(value = "车牌LIKE")
	@Column(name = "`plate`")
	@Like(value = "RIGHT")
	private String likePlate;
	@Transient
	private String keyword;
	
	@Transient
	private String tag;

	@Transient
	private String aliasName;

	@Transient
	private Long billId;

	@Transient
	private Date checkInDateStart;

	@Transient
	private Date checkInDateEnd;

	public Date getCheckInDateStart() {
		return checkInDateStart;
	}

	public void setCheckInDateStart(Date checkInDateStart) {
		this.checkInDateStart = checkInDateStart;
	}

	public Date getCheckInDateEnd() {
		return checkInDateEnd;
	}

	public void setCheckInDateEnd(Date checkInDateEnd) {
		this.checkInDateEnd = checkInDateEnd;
	}

	public List<Integer> getVerifyStatusList() {
		return verifyStatusList;
	}

	public void setVerifyStatusList(List<Integer> verifyStatusList) {
		this.verifyStatusList = verifyStatusList;
	}

	public Date getCreatedStart() {
		return createdStart;
	}

	public void setCreatedStart(Date createdStart) {
		this.createdStart = createdStart;
	}

	public Date getCreatedEnd() {
		return createdEnd;
	}

	public void setCreatedEnd(Date createdEnd) {
		this.createdEnd = createdEnd;
	}

	public Date getLatestDetectTimeTimeStart() {
		return latestDetectTimeTimeStart;
	}

	public void setLatestDetectTimeTimeStart(Date latestDetectTimeTimeStart) {
		this.latestDetectTimeTimeStart = latestDetectTimeTimeStart;
	}

	public Date getLatestDetectTimeTimeEnd() {
		return latestDetectTimeTimeEnd;
	}

	public void setLatestDetectTimeTimeEnd(Date latestDetectTimeTimeEnd) {
		this.latestDetectTimeTimeEnd = latestDetectTimeTimeEnd;
	}

	public String getLikeProductName() {
		return likeProductName;
	}

	public void setLikeProductName(String likeProductName) {
		this.likeProductName = likeProductName;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	

	public List<Integer> getStateList() {
		return stateList;
	}

	public void setStateList(List<Integer> stateList) {
		this.stateList = stateList;
	}

	public List<Integer> getDetectStateList() {
		return detectStateList;
	}

	public void setDetectStateList(List<Integer> detectStateList) {
		this.detectStateList = detectStateList;
	}

	public String getLikeTallyAreaNo() {
		return likeTallyAreaNo;
	}

	public void setLikeTallyAreaNo(String likeTallyAreaNo) {
		this.likeTallyAreaNo = likeTallyAreaNo;
	}

	public String getLikeSampleCode() {
		return likeSampleCode;
	}

	public void setLikeSampleCode(String likeSampleCode) {
		this.likeSampleCode = likeSampleCode;
	}

	public List<String> getSampleCodeList() {
		return sampleCodeList;
	}

	public void setSampleCodeList(List<String> sampleCodeList) {
		this.sampleCodeList = sampleCodeList;
	}

	public List<Long> getProductIdList() {
		return productIdList;
	}

	public void setProductIdList(List<Long> productIdList) {
		this.productIdList = productIdList;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

	public String getLikePlate() {
		return likePlate;
	}

	public void setLikePlate(String likePlate) {
		this.likePlate = likePlate;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
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
     * @return List<String> return the plateList
     */
    public List<String> getPlateList() {
        return plateList;
    }

    /**
     * @param plateList the plateList to set
     */
    public void setPlateList(List<String> plateList) {
        this.plateList = plateList;
    }


    /**
     * @return String return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

	public Date getModifiedStart() {
		return modifiedStart;
	}

	public void setModifiedStart(Date modifiedStart) {
		this.modifiedStart = modifiedStart;
	}

	public Date getModifiedEnd() {
		return modifiedEnd;
	}

	public void setModifiedEnd(Date modifiedEnd) {
		this.modifiedEnd = modifiedEnd;
	}

	public String getLikeCorporateName() {
		return likeCorporateName;
	}

	public void setLikeCorporateName(String likeCorporateName) {
		this.likeCorporateName = likeCorporateName;
	}

	public String getLikeName() {
		return likeName;
	}

	public void setLikeName(String likeName) {
		this.likeName = likeName;
	}

	public Boolean getHasCheckSheet() {
		return hasCheckSheet;
	}

	public void setHasCheckSheet(Boolean hasCheckSheet) {
		this.hasCheckSheet = hasCheckSheet;
	}
}
