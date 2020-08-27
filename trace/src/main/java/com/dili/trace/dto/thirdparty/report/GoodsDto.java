package com.dili.trace.dto.thirdparty.report;

public class GoodsDto {
    private String market = "1";
    private String goodsName;
    private String thirdGoodsId;
    private String thirdSmallClassId;

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getThirdGoodsId() {
        return thirdGoodsId;
    }

    public void setThirdGoodsId(String thirdGoodsId) {
        this.thirdGoodsId = thirdGoodsId;
    }

    public String getThirdSmallClassId() {
        return thirdSmallClassId;
    }

    public void setThirdSmallClassId(String thirdSmallClassId) {
        this.thirdSmallClassId = thirdSmallClassId;
    }
}
