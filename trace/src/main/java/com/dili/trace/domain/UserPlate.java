package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-10-11 10:05:11.
 */
@Table(name = "`user_plate`")
public class UserPlate extends BaseDomain {
    /**
     * ID
     * @return
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;


    /**
     * 车牌号
     * @return
     */
    @Column(name = "`plate`")
    private String plate;

    /**
     * 用户id
     * @return
     */
    @Column(name = "`user_id`")
    private Long userId;


    /**
     * 创建时间
     * @return
     */
    @Column(name = "`created`")
    private Date created;

    /**
     * 修改时间
     * @return
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

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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