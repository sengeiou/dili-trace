package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.ApproverInfo;

import javax.persistence.Column;
import java.util.Date;

public class ApproverInfoQueryDto extends ApproverInfo {

    /**
     * 用户名模糊查询
     */
    @Column(name = "`user_name`")
    @Like(Like.RIGHT)
    private String likeUserName;

    /**
     * 查询登记开始时间
     */
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private Date createdStart;

    /**
     * 查询登记结束时间
     */
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private Date createdEnd;

    public String getLikeUserName() {
        return likeUserName;
    }

    public void setLikeUserName(String likeUserName) {
        this.likeUserName = likeUserName;
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

}
