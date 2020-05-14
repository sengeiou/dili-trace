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
@Table(name = "`user_qr_item_detail`")
public class UserQrItemDetail extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    /**
     * 二维码条目ID
     */
    @Column(name = "`user_qr_item_id`")
    private Long userQrItemId;

    /**
     * 参数ID
     */
    @Column(name = "`object_id`")
    private String objectId;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`modified`")
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
     * @return Long return the userQrItemId
     */
    public Long getUserQrItemId() {
        return userQrItemId;
    }

    /**
     * @param userQrItemId the userQrItemId to set
     */
    public void setUserQrItemId(Long userQrItemId) {
        this.userQrItemId = userQrItemId;
    }

    /**
     * @return String return the objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * @param objectId the objectId to set
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}