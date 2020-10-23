package com.dili.trace.domain;


import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 *  国外国家类
 * @createTime 2020-10-20 10:07:08
 * @author Lily
 */
@Table(name = "`countries`")
public class Countries  extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "地区代码")
    @Column(name = "`code`")
    private String code;

    /**
     * 简称
     */
    @ApiModelProperty(value = "名称")
    @Column(name = "`cname`")
    private String cname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}