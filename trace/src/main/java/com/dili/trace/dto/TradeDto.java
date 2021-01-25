package com.dili.trace.dto;

import com.dili.trace.enums.BuyerTypeEnum;
import com.dili.trace.enums.TradeOrderTypeEnum;

public class TradeDto {

    public TradeDto() {
        this.buyer = new Buyer();
        this.seller = new Seller();
    }

    public TradeDto(TradeOrderTypeEnum tradeOrderType, Long marketId, Long sellerId) {
        this.buyer = new Buyer();
        this.seller = new Seller();
        this.marketId = marketId;
        this.tradeOrderType = tradeOrderType;
    }

    private Long marketId;
    private Seller seller;
    private Buyer buyer;
    private TradeOrderTypeEnum tradeOrderType;

    public static class Seller {
        private Long sellerId;
        private String sellerName;

        public Long getSellerId() {
            return sellerId;
        }

        public void setSellerId(Long sellerId) {
            this.sellerId = sellerId;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }
    }

    public static class Buyer {
        private String buyerName;
        private Long buyerId;
        private BuyerTypeEnum buyerType;

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public Long getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(Long buyerId) {
            this.buyerId = buyerId;
        }

        public BuyerTypeEnum getBuyerType() {
            return buyerType;
        }

        public void setBuyerType(BuyerTypeEnum buyerType) {
            this.buyerType = buyerType;
        }
    }

    public Seller getSeller() {
        return seller;
    }


    public Buyer getBuyer() {
        return buyer;
    }


    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }


    public TradeOrderTypeEnum getTradeOrderType() {
        return tradeOrderType;
    }

    public void setTradeOrderType(TradeOrderTypeEnum tradeOrderType) {
        this.tradeOrderType = tradeOrderType;
    }
}
