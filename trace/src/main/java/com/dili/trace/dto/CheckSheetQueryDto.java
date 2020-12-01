package com.dili.trace.dto;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.trace.domain.CheckSheet;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

public class CheckSheetQueryDto extends CheckSheet {
	@Transient
	private String likeApproverUserName;


	@ApiModelProperty(value = "查询登记开始时间")
	@Column(name = "`created`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private String createdStart;



	@ApiModelProperty(value = "查询登记结束时间")
	@Column(name = "`created`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private String createdEnd;


	@Column(name = "`operator_name`")
	@Like(Like.RIGHT)
	private String likeOperatorName;


	@Transient
	private Integer billType;


	public String getLikeApproverUserName() {
		return likeApproverUserName;
	}


	public void setLikeApproverUserName(String likeApproverUserName) {
		this.likeApproverUserName =StringUtils.trimToNull(likeApproverUserName);
	}


	public String getCreatedStart() {
		return createdStart;
	}


	public void setCreatedStart(String createdStart) {
		this.createdStart = StringUtils.trimToNull(createdStart);
	}


	public String getCreatedEnd() {
		return createdEnd;
	}


	public void setCreatedEnd(String createdEnd) {
		this.createdEnd = StringUtils.trimToNull(createdEnd);;
	}


	public String getLikeOperatorName() {
		return likeOperatorName;
	}


	public void setLikeOperatorName(String likeOperatorName) {
		this.likeOperatorName = StringUtils.trimToNull(likeOperatorName);
	}


	public Integer getBillType() {
		return billType;
	}


	public void setBillType(Integer billType) {
		this.billType = billType;
	}


}
