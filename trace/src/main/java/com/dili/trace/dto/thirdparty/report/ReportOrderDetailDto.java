package com.dili.trace.dto.thirdparty.report;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class ReportOrderDetailDto {

    private String detailId;// 订单明细id
    private BigDecimal orderNum=BigDecimal.ZERO;// 重量
    private BigDecimal price;// 单价
    private String thirdEnterId;// 报备id
    private String thirdOrderParentId;// 上级订单id(溯源关联用)
    private String thirdOrderDetailParentId;// 上级订单明细id(溯源关联用)
    private String unitName;// 单位

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public BigDecimal getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(BigDecimal orderNum) {
        this.orderNum = orderNum;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getThirdEnterId() {
        return thirdEnterId;
    }

    public void setThirdEnterId(String thirdEnterId) {
        this.thirdEnterId = thirdEnterId;
    }

    public String getThirdOrderParentId() {
        return thirdOrderParentId;
    }

    public void setThirdOrderParentId(String thirdOrderParentId) {
        this.thirdOrderParentId = thirdOrderParentId;
    }

    public String getThirdOrderDetailParentId() {
        return thirdOrderDetailParentId;
    }

    public void setThirdOrderDetailParentId(String thirdOrderDetailParentId) {
        this.thirdOrderDetailParentId = thirdOrderDetailParentId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}