package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.RUserUpstream;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

public class RUserUpstreamDto extends RUserUpstream {
    @ApiModelProperty(value = "业户ID批量查")
    @Column(name = "`user_id`")
    @Operator(Operator.IN)
    private List<Long> userIds;

    @ApiModelProperty(value = "上游用户ID批量查")
    @Column(name = "`upstream_id`")
    @Operator(Operator.IN)
    private List<Long> upstreamIds;

    //业户名称模糊查
    @Transient
    private String likeUserName;

    //业户名称模糊查
    @Transient
    private String likeUpstreamName;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<Long> getUpstreamIds() {
        return upstreamIds;
    }

    public void setUpstreamIds(List<Long> upstreamIds) {
        this.upstreamIds = upstreamIds;
    }

    public String getLikeUserName() {
        return likeUserName;
    }

    public void setLikeUserName(String likeUserName) {
        this.likeUserName = likeUserName;
    }

    public String getLikeUpstreamName() {
        return likeUpstreamName;
    }

    public void setLikeUpstreamName(String likeUpstreamName) {
        this.likeUpstreamName = likeUpstreamName;
    }
}
