package com.dili.trace.dto.thirdparty.report;

public class GoodsDto {
    private String marketId = "330110800";
    private String goodsName;
    private String thirdGoodsId;
    private String thirdSmallClassId;

    private String goodsCode;

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
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
