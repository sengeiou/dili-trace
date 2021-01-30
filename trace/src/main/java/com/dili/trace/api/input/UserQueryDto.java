package com.dili.trace.api.input;

import com.dili.ss.domain.annotation.Like;
import com.dili.trace.domain.UserInfo;

import javax.persistence.Column;

public class UserQueryDto extends UserInfo {
    @Column(name = "`name`")
    @Like
    private String name;

    @Column(name = "`tally_area_nos`")
    @Like
    private String tallyAreaNos;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTallyAreaNos() {
        return tallyAreaNos;
    }

    @Override
    public void setTallyAreaNos(String tallyAreaNos) {
        this.tallyAreaNos = tallyAreaNos;
    }
}
