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

    @Column(name = "`is_autoverify_passed`")//is_audit_after_regist
    private Integer isAutoVerifyPassed;

    /**
     * 称重后自动进门
     */
    @Column(name = "`can_docheckin_without_weight`")//is_weight_before_checkin
    private Integer canDoCheckInWithoutWeight;

    /**
     * 是否进门审核
     */
    @Column(name = "`is_manully_checkIn`")//is_audit_before_checkin
    private Integer isManullyCheckIn;


    /**
     * 采样检测
     */
    @Column(name = "`is_sample_detect`")//is_sample_detect
    private Integer isSampleDetect;

    /**
     * 场内审核
     */
    @Column(name = "`is_audit_after_checkin`")//is_audit_after_checkin
    private Integer isAuditAfterCheckin;

    /**
     * 检测接单
     */
    @Column(name = "`is_confirm_detect`")//is_confirm_detect
    private Integer isConfirmDetect;

    public Integer getIsSampleDetect() {
        return isSampleDetect;
    }

    public void setIsSampleDetect(Integer isSampleDetect) {
        this.isSampleDetect = isSampleDetect;
    }

    public Integer getIsAuditAfterCheckin() {
        return isAuditAfterCheckin;
    }

    public void setIsAuditAfterCheckin(Integer isAuditAfterCheckin) {
        this.isAuditAfterCheckin = isAuditAfterCheckin;
    }

    public Integer getIsConfirmDetect() {
        return isConfirmDetect;
    }

    public void setIsConfirmDetect(Integer isConfirmDetect) {
        this.isConfirmDetect = isConfirmDetect;
    }

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

    public Integer getIsAutoVerifyPassed() {
        return isAutoVerifyPassed;
    }

    public void setIsAutoVerifyPassed(Integer isAutoVerifyPassed) {
        this.isAutoVerifyPassed = isAutoVerifyPassed;
    }

    public Integer getCanDoCheckInWithoutWeight() {
        return canDoCheckInWithoutWeight;
    }

    public void setCanDoCheckInWithoutWeight(Integer canDoCheckInWithoutWeight) {
        this.canDoCheckInWithoutWeight = canDoCheckInWithoutWeight;
    }

    public Integer getIsManullyCheckIn() {
        return isManullyCheckIn;
    }

    public void setIsManullyCheckIn(Integer isManullyCheckIn) {
        this.isManullyCheckIn = isManullyCheckIn;
    }
}
