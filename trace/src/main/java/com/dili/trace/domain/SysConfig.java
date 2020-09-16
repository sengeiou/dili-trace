package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;

/**
 * 微信APP
 *
 * @author asa
 */
@Table(name = "`sys_config`")
public class SysConfig extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "opt_type")
    private String opt_type;

    @Column(name = "opt_category")
    private String opt_category;

    @Column(name = "opt_value")
    private String opt_value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getOpt_type() {
        return opt_type;
    }

    public void setOpt_type(String opt_type) {
        this.opt_type = opt_type;
    }

    public String getOpt_category() {
        return opt_category;
    }

    public void setOpt_category(String opt_category) {
        this.opt_category = opt_category;
    }

    public String getOpt_value() {
        return opt_value;
    }

    public void setOpt_value(String opt_value) {
        this.opt_value = opt_value;
    }
}
