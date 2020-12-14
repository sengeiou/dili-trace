package com.dili.trace.api.output;

public class QrOutputDto {
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

    /**
     * base64图片
     */
    private String base64QrCode;

    public String getBase64QrCode() {
        return base64QrCode;
    }

    public void setBase64QrCode(String base64QrCode) {
        this.base64QrCode = base64QrCode;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
