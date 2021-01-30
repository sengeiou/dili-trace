package com.dili.trace.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.UserInfo;

/**
 * 由MyBatis Generator工具自动生成
 * <p>
 * This file was generated on 2019-07-26 09:20:35.
 */
public class UserListDto extends UserInfo {
    /**
     * 创建时间开始
     *
     * @return
     */
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private Date createdStart;


    /**
     * 创建时间结束
     *
     * @return
     */
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private Date createdEnd;

    /**
     * 昵称模糊查询
     *
     * @return
     */
    @Column(name = "tally_area_nos")
    @Like
    private String likeTallyAreaNos;

    /**
     * 是否有营业执照
     *
     * @return
     */
    @Transient
    private Boolean hasBusinessLicense;


    /**
     * 业户名称
     *
     * @return
     */
    @Column(name = "name")
    @Like
    private String likeName;

    /**
     * 业户id集合
     *
     * @return
     */
    @Column(name = "`id`")
    @Operator(Operator.IN)
    private List<Long> Ids;


    /**
     * 业户id
     *
     * @return
     */
    @Transient
    private Long userId;


    /**
     * 姓名/手机号/经营户卡号模糊查询
     *
     * @return
     */
    @Column(name = "name")
    @Like
    private String keyword;

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

    public String getLikeTallyAreaNos() {
        return likeTallyAreaNos;
    }

    public void setLikeTallyAreaNos(String likeTallyAreaNos) {
        this.likeTallyAreaNos = likeTallyAreaNos;
    }

    public Boolean getHasBusinessLicense() {
        return hasBusinessLicense;
    }

    public void setHasBusinessLicense(Boolean hasBusinessLicense) {
        this.hasBusinessLicense = hasBusinessLicense;
    }

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public List<Long> getIds() {
        return Ids;
    }

    public void setIds(List<Long> ids) {
        Ids = ids;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


}