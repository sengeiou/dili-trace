package com.dili.trace.api.output;

public class QrInputDto {
    /**
     * 客户类型
     */
    private Integer clientType;
    /**
     * 市场id
     */

    private Long marketId;
    /**
     * 客户id
     */
    private Long clientId;

    /**
     * 二维码颜色
     */
    private Integer color;

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
