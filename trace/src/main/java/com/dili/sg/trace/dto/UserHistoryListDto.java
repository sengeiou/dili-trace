package com.dili.sg.trace.dto;

import com.dili.sg.trace.domain.UserHistory;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.*;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
public interface UserHistoryListDto extends UserHistory {
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getCreatedStart();
    void setCreatedStart(Date createdStart);

    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getCreatedEnd();
    void setCreatedEnd(Date createdEnd);
    
    
    @Column(name = "`modified`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getModifiedStart();
    void setModifiedStart(Date modifiedStart);

    @Column(name = "`modified`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    Date getModifiedEnd();
    void setModifiedEnd(Date modifiedEnd);

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
    
    @Column(name = "user_plates")
    @Like
    String getLikeUserPlates();
    void setLikeUserPlates(String likeUserPlates);
    
    @Transient
    Long getOffSet();
    void setOffSet(Long offSet);

}