package com.dili.trace.dto;

import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.customer.sdk.domain.dto.CharacterTypeGroupDto;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;

import java.util.List;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/12/29
 */
public class CustomerExtendOutPutDto extends CustomerExtendDto {
    private Long marketId;
    private String marketName;
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
