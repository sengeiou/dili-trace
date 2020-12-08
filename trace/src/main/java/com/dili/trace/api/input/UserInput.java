package com.dili.trace.api.input;

import com.dili.ss.domain.BasePage;

public class UserInput extends BasePage {
    /**
     * ID
     */
    private Long id;
    /**
     * 校验状态
     */
    private Integer validateState;
    /**
     * 摊位号
     */
    private String tallyAreaNos;
    /**
     * 名称
     */
    private String name;
    /**
     * 拒绝理由
     */
    private String refuseReason;
    /**
     * 市场id
     */
    private Long marketId;

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValidateState() {
        return validateState;
    }

    public void setValidateState(Integer validateState) {
        this.validateState = validateState;
    }

    public String getTallyAreaNos() {
        return tallyAreaNos;
    }

    public void setTallyAreaNos(String tallyAreaNos) {
        this.tallyAreaNos = tallyAreaNos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
}
