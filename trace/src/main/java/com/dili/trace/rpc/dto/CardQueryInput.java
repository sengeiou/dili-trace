package com.dili.trace.rpc.dto;

import java.util.List;

public class CardQueryInput {
    private String customerCode;
    private List<String> cardNos;
    private Long firmId;
    private Integer cardState = 1;

    public List<String> getCardNos() {
        return cardNos;
    }

    public void setCardNos(List<String> cardNos) {
        this.cardNos = cardNos;
    }

    public Integer getCardState() {
        return cardState;
    }

    public void setCardState(Integer cardState) {
        this.cardState = cardState;
    }

    public Long getFirmId() {
        return firmId;
    }

    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}