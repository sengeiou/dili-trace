package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.RegisterHead;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

/**
 * 进门主台账单实体类扩展
 *
 * @author Lily
 */
public class RegisterHeadDto extends RegisterHead {
	@ApiModelProperty(value = "查询开始时间")
	@Column(name = "`created`")
	@Operator(Operator.GREAT_EQUAL_THAN)
	private String createdStart;

	@ApiModelProperty(value = "查询结束时间")
	@Column(name = "`created`")
	@Operator(Operator.LITTLE_EQUAL_THAN)
	private String createdEnd;

	@Transient
	private String keyword;

	@Transient
	private Integer verifyRemainWeight;

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getVerifyRemainWeight() {
		return verifyRemainWeight;
	}

	public void setVerifyRemainWeight(Integer verifyRemainWeight) {
		this.verifyRemainWeight = verifyRemainWeight;
	}
}
