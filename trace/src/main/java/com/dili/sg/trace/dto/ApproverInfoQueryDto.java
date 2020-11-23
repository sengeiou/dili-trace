package com.dili.trace.dto;

import javax.persistence.Column;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.ApproverInfo;

import io.swagger.annotations.ApiModelProperty;

public interface ApproverInfoQueryDto extends ApproverInfo{
    @Column(name = "`user_name`")
    @Like(Like.RIGHT)
    public String getLikeUserName();
	public void setLikeUserName(String likeUserName);
	
	
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
