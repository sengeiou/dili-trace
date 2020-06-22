package com.dili.trace.api.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.dili.trace.glossary.RegisterBillStateEnum;

public class CheckInApiListOutput {

    private Long billId;
    private Integer state;
    private String code;
    private String productName;

    private String phone;
    private String upstreamName;
    private String upstreamTelphone;
    private Date created;
    private BigDecimal weight=BigDecimal.ZERO;

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        if (weight != null) {
            this.weight = weight;
        } else {
            this.weight = BigDecimal.ZERO;
        }
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStateName() {
        try {
            if (getState() == null) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return RegisterBillStateEnum.getRegisterBillStateEnum(getState()).getName();
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUpstreamName() {
        return upstreamName;
    }

    public void setUpstreamName(String upstreamName) {
        this.upstreamName = upstreamName;
    }

    public String getUpstreamTelphone() {
        return upstreamTelphone;
    }

    public void setUpstreamTelphone(String upstreamTelphone) {
        this.upstreamTelphone = upstreamTelphone;
    }

}
