package com.dili.trace.dto.thirdparty.report;

import java.util.Date;
import java.util.List;

public class ReportDeliveryOrderDto {

    private String marketId;// 市场id
    private Date orderTime;// 交易时间
    private String thirdAccId;// 卖家id
    private String thirdDsId;// 下游id
    private String thirdOrderId;// 交易表id
    private String thirdQrCode;// 食安码
    private List<ReportOrderDetailDto> tradeList;// 明细集合

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getThirdAccId() {
        return thirdAccId;
    }

    public void setThirdAccId(String thirdAccId) {
        this.thirdAccId = thirdAccId;
    }

    public String getThirdDsId() {
        return thirdDsId;
    }

    public void setThirdDsId(String thirdDsId) {
        this.thirdDsId = thirdDsId;
    }

    public String getThirdOrderId() {
        return thirdOrderId;
    }

    public void setThirdOrderId(String thirdOrderId) {
        this.thirdOrderId = thirdOrderId;
    }

    public String getThirdQrCode() {
        return thirdQrCode;
    }

    public void setThirdQrCode(String thirdQrCode) {
        this.thirdQrCode = thirdQrCode;
    }

    public List<ReportOrderDetailDto> getTradeList() {
        return tradeList;
    }

    public void setTradeList(List<ReportOrderDetailDto> tradeList) {
        this.tradeList = tradeList;
    }
}