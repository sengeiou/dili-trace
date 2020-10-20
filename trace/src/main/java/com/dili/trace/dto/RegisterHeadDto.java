package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.RegisterHead;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;

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
}
