package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.trace.domain.UpStream;

import javax.persistence.Column;

public class UpStreamDto extends UpStream {
    @Column(name = "name")
    @Like
    private String likeName;

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }
}
