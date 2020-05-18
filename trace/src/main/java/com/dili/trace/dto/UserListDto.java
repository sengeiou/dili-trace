package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.domain.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
public interface UserListDto extends User {
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getCreatedStart();
    void setCreatedStart(Date createdStart);

    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getCreatedEnd();
    void setCreatedEnd(Date createdEnd);

    /**
     * 昵称模糊查询
     * @return
     */
    @Column(name = "tally_area_nos")
    @Like
    String getLikeTallyAreaNos();
    void setLikeTallyAreaNos(String likeTallyAreaNos);
    
    @Transient
    Boolean getHasBusinessLicense();
    void setHasBusinessLicense(Boolean hasBusinessLicense);

    @Column(name = "name")
    @Like
    String getLikeName();
    void setLikeName(String likeName);

}