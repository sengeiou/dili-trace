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
 * <p>
 * This file was generated on 2019-07-26 09:20:35.
 */
public interface UserListDto extends User {
    /**
     * 创建时间开始
     *
     * @return
     */
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    Date getCreatedStart();

    void setCreatedStart(Date createdStart);

    /**
     * 创建时间结束
     *
     * @return
     */
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

    /**
     * 是否有营业执照
     *
     * @return
     */
    @Transient
    Boolean getHasBusinessLicense();

    void setHasBusinessLicense(Boolean hasBusinessLicense);

    /**
     * 业户名称
     *
     * @return
     */
    @Column(name = "name")
    @Like
    String getLikeName();

    void setLikeName(String likeName);

    /**
     * 业户id集合
     *
     * @return
     */
    @Column(name = "`id`")
    @Operator(Operator.IN)
    List<Long> getIds();

    void setIds(List<Long> ids);

    /**
     * 业户id
     *
     * @return
     */
    @Transient
    Long getUserId();

    void setUserId(Long userId);


    /**
     * 姓名/手机号/经营户卡号模糊查询
     *
     * @return
     */
    @Column(name = "name")
    @Like
    String getKeyword();

    /**
     * 姓名/手机号/经营户卡号模糊查询
     *
     * @param keyword
     */
    void setKeyword(String keyword);
}