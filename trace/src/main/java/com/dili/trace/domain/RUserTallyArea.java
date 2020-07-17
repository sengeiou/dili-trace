package com.dili.trace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;

import io.swagger.annotations.ApiModelProperty;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`r_user_tally_area`")
public class RUserTallyArea extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Column(name = "`user_id`")
    private Long userId;

    
    @Column(name = "`tally_area_no_id`")
    private Long tallyAreaNoId;
    

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

    @ApiModelProperty(value = "修改时间")
    @Column(name = "`modified`")
    private Date modified;

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Long return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return Date return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return Date return the modified
     */
    public Date getModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }


    /**
     * @return Long return the tallyAreaNoId
     */
    public Long getTallyAreaNoId() {
        return tallyAreaNoId;
    }

    /**
     * @param tallyAreaNoId the tallyAreaNoId to set
     */
    public void setTallyAreaNoId(Long tallyAreaNoId) {
        this.tallyAreaNoId = tallyAreaNoId;
    }

}