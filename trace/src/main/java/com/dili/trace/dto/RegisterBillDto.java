package com.dili.trace.dto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.RegisterBill;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by laikui on 2019/7/30.
 */
public class RegisterBillDto extends RegisterBill {
	@ApiModelProperty(value = "查询登记开始时间")
	@Column(name = "`created`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private String createdStart;

	@ApiModelProperty(value = "查询登记结束时间")
	@Column(name = "`created`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private String createdEnd;

	@ApiModelProperty(value = "查询检测开始时间")
	@Column(name = "`latest_detect_time`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private String latestDetectTimeTimeStart;
	@ApiModelProperty(value = "查询检测结束时间")
	@Column(name = "`latest_detect_time`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private String latestDetectTimeTimeEnd;

	@ApiModelProperty(value = "商品名称LIKE")
	@Column(name = "`product_name`")
	@Like(value = "RIGHT")
	private String likeProductName;

	@Transient
	private String attr;
	@Transient
	private String attrValue;

//    @Transient
//    Boolean getHasReport();
//    void setHasReport(Boolean hasReport);

	@Transient
	private Boolean hasDetectReport;

	@Transient
	private Boolean hasHandleResult;

	@Transient
	Boolean hasOriginCertifiy;

	@Transient
	private Boolean hasCheckSheet;

	@Column(name = "`state`")
	@Operator(Operator.IN)
	private List<Integer> stateList;

	@Column(name = "`detect_state`")
	@Operator(Operator.IN)
	private List<Integer> detectStateList;

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
	private String tag;

	@Transient
	private String aliasName;

	public String getCreatedStart() {
		return createdStart;
	}

	public void setCreatedStart(String createdStart) {
		this.createdStart = createdStart;
	}

	public String getCreatedEnd() {
		return createdEnd;
	}

	public void setCreatedEnd(String createdEnd) {
		this.createdEnd = createdEnd;
	}

	public String getLatestDetectTimeTimeStart() {
		return latestDetectTimeTimeStart;
	}

	public void setLatestDetectTimeTimeStart(String latestDetectTimeTimeStart) {
		this.latestDetectTimeTimeStart = latestDetectTimeTimeStart;
	}

	public String getLatestDetectTimeTimeEnd() {
		return latestDetectTimeTimeEnd;
	}

	public void setLatestDetectTimeTimeEnd(String latestDetectTimeTimeEnd) {
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

	public Boolean getHasDetectReport() {
		return hasDetectReport;
	}

	public void setHasDetectReport(Boolean hasDetectReport) {
		this.hasDetectReport = hasDetectReport;
	}

	public Boolean getHasHandleResult() {
		return hasHandleResult;
	}

	public void setHasHandleResult(Boolean hasHandleResult) {
		this.hasHandleResult = hasHandleResult;
	}

	public Boolean getHasOriginCertifiy() {
		return hasOriginCertifiy;
	}

	public void setHasOriginCertifiy(Boolean hasOriginCertifiy) {
		this.hasOriginCertifiy = hasOriginCertifiy;
	}

	public Boolean getHasCheckSheet() {
		return hasCheckSheet;
	}

	public void setHasCheckSheet(Boolean hasCheckSheet) {
		this.hasCheckSheet = hasCheckSheet;
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

}
