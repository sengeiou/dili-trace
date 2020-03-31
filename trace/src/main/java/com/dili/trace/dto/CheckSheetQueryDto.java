package com.dili.trace.dto;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.CheckSheet;

import io.swagger.annotations.ApiModelProperty;

public interface CheckSheetQueryDto extends CheckSheet {
	@Transient
	public String getLikeApproverUserName();

	public void setLikeApproverUserName(String likeApproverUserName);
	
	
    @ApiModelProperty(value = "查询登记开始时间")
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    String getCreatedStart();
    void setCreatedStart(String createdStart);

    @ApiModelProperty(value = "查询登记结束时间")
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    String getCreatedEnd();
    void setCreatedEnd(String createdEnd);

}
