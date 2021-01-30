package com.dili.trace.api.input;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.domain.User;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.util.Date;

public class UserQueryDto extends User {
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
