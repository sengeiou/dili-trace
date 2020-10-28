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
    private String optType;

    @Column(name = "opt_category")
    private String optCategory;

    @Column(name = "opt_value")
    private String optValue;

    @Column(name = "`market_id`")
    private Long marketId;

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

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getOptCategory() {
        return optCategory;
    }

    public void setOptCategory(String optCategory) {
        this.optCategory = optCategory;
    }

    public String getOptValue() {
        return optValue;
    }

    public void setOptValue(String optValue) {
        this.optValue = optValue;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
}
