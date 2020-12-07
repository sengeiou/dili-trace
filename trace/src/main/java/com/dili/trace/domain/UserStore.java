package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`user_store`")
public class UserStore extends BaseDomain {
    /**
     * 店铺主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    /**
     * 店铺名称
     */
    @Column(name = "`store_name`")
    private String storeName;

    /**
     * 业户主键
     */
    @Column(name = "`user_id`")
    private Long userId;

    /**
     * 店铺创建时间
     */
    @Column(name = "`created`")
    private Date created;

    /**
     * 店铺修改时间
     */
    @Column(name = "`modified`")
    private Date modified;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
