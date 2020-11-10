package com.dili.trace.dto.thirdparty.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReportScanCodeOrderDto {

    private Integer flag = 0;// 0(市场内交易) ,1 (市场外交易)
    private String marketId;// 市场id
    private Date orderTime;// 交易时间
    private String thirdBuyId;// 买家ID
    private String thirdOrderId;// 交易表id
    private String thirdQrCode;// 食安码
    private String thirdSellId;// 卖家id
    private BigDecimal price;
    private List<ReportOrderDetailDto> tradeList;// 明细集合

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

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

    public String getThirdBuyId() {
        return thirdBuyId;
    }

    public void setThirdBuyId(String thirdBuyId) {
        this.thirdBuyId = thirdBuyId;
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

    public String getThirdSellId() {
        return thirdSellId;
    }

    public void setThirdSellId(String thirdSellId) {
        this.thirdSellId = thirdSellId;
    }

    public List<ReportOrderDetailDto> getTradeList() {
        return tradeList;
    }

    public void setTradeList(List<ReportOrderDetailDto> tradeList) {
        this.tradeList = tradeList;
    }

}