package com.dili.trace.dto;

import com.dili.customer.sdk.domain.dto.CustomerExtendDto;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/12/29
 */
public class CustomerExtendOutPutDto extends CustomerExtendDto {
    /**
     * 市场主键
     */
    private Long marketId;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 园区卡号
     */
    private String tradePrintingCard;

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public CustomerExtendOutPutDto() {
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getTradePrintingCard() {
        return tradePrintingCard;
    }

    public void setTradePrintingCard(String tradePrintingCard) {
        this.tradePrintingCard = tradePrintingCard;
    }
}
