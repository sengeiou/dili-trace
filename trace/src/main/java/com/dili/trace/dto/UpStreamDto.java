package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.UpStream;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

public class UpStreamDto extends UpStream {

    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private Date createdStart;

    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private Date createdEnd;

    @Column(name = "`id`")
    @Operator(Operator.IN)
    private List<Long> ids;

    @Column(name = "name")
    @Like
    private String likeName;

    //业户名称模糊查
    @Transient
    private String likeUserName;

    @Transient
    private List<Long> userIds;

    @Transient
    private String userNames;

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

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public String getLikeUserName() {
        return likeUserName;
    }

    public void setLikeUserName(String likeUserName) {
        this.likeUserName = likeUserName;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }
}
