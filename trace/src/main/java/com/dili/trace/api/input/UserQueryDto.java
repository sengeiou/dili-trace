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

public interface UserQueryDto extends User {
    @Column(name = "`name`")
    @Like
    String getName();

    void setName(String name);

    @Column(name = "`tally_area_nos`")
    @Like
    String getTallyAreaNos();

    void setTallyAreaNos(String tallyAreaNos);


}
