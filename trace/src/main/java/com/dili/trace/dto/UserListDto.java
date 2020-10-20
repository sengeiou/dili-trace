package com.dili.trace.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.User;

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
     * 
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

    @Column(name = "`id`")
    @Operator(Operator.IN)
    List<Long> getIds();

    void setIds(List<Long> ids);

    @Transient
    Long getUserId();

    void setUserId(Long userId);

    /**
     * 手机号模糊查询
     * @return
     */
    @Column(name = "phone")
    @Like
    String getLikePhone();

    /**
     * 手机号模糊查询
     * @param likePhone
     */
    void setLikePhone(String likePhone);

    /**
     * 经营户卡号模糊查询
     * @return
     */
    @Column(name = "third_party_code")
    @Like
    String getLikeThirdPartyCode();

    /**
     * 经营户卡号模糊查询
     * @param likeThirdPartyCode
     */
    void setLikeThirdPartyCode(String likeThirdPartyCode);
}