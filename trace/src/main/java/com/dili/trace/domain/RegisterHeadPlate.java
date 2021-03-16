package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`register_head`")
public class RegisterHeadPlate extends BaseDomain {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    /**
     * 台账ID
     */
    @Column(name = "`register_head_id`")
    private Long registerHeadId;

    /**
     * 车牌
     */
    @Column(name = "`plate`")
    private String plate;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
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

    public Long getRegisterHeadId() {
        return registerHeadId;
    }

    public void setRegisterHeadId(Long registerHeadId) {
        this.registerHeadId = registerHeadId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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
