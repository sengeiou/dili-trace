package com.dili.trace.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;

@Table(name = "`process_config`")
public class ProcessConfig extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JSONField(serialize = false)
    private Long id;

    /**
     * 市场ID
     */
    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 是否登记审核
     */

    @Column(name = "`is_audit_after_regist`")
    private Integer isAuditAfterRegist;

    /**
     * 是否进门称重
     */
    @Column(name = "`is_weight_before_checkin`")
    private Integer isWeightBeforeCheckin;

    /**
     * 是否进门审核
     */
    @Column(name = "`is_audit_before_checkin`")
    private Integer isAuditBeforeCheckin;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Integer getIsAuditAfterRegist() {
        return isAuditAfterRegist;
    }

    public void setIsAuditAfterRegist(Integer isAuditAfterRegist) {
        this.isAuditAfterRegist = isAuditAfterRegist;
    }

    public Integer getIsWeightBeforeCheckin() {
        return isWeightBeforeCheckin;
    }

    public void setIsWeightBeforeCheckin(Integer isWeightBeforeCheckin) {
        this.isWeightBeforeCheckin = isWeightBeforeCheckin;
    }

    public Integer getIsAuditBeforeCheckin() {
        return isAuditBeforeCheckin;
    }

    public void setIsAuditBeforeCheckin(Integer isAuditBeforeCheckin) {
        this.isAuditBeforeCheckin = isAuditBeforeCheckin;
    }
}
