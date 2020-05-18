package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.trace.domain.UpStream;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

public class UpStreamDto extends UpStream {
    @Column(name = "name")
    @Like
    private String likeName;

    @Transient
    private List<Long> userIds;

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
