package com.dili.trace.service;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.dto.TradeDto;
import com.dili.trace.enums.BuyerTypeEnum;
import com.dili.trace.enums.TradeOrderTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@EnableDiscoveryClient
@Commit
public class TradeOrderServiceTest extends AutoWiredBaseTest {
    @Autowired
    TradeOrderService tradeOrderService;

    @Test
    @Transactional
    public void createTradeDetail() {

        TradeDto tradeDto = new TradeDto();
        tradeDto.setMarketId(8L);
        tradeDto.setTradeOrderType(TradeOrderTypeEnum.BUY);

        tradeDto.getSeller().setSellerId(1L);
        tradeDto.getSeller().setSellerName("zhangsan");

        tradeDto.getBuyer().setBuyerType(BuyerTypeEnum.NORMAL_BUYER);
        tradeDto.getBuyer().setBuyerId(2L);
        tradeDto.getBuyer().setBuyerName("lisi");


        RegisterBill registerBill = new RegisterBill();
        registerBill.setId(100L);
        registerBill.setProductName("茄子");
//        TradeDetail tradeDetail = this.tradeOrderService.createTradeDetail(tradeDto, registerBill, 200L,300L, BigDecimal.valueOf(111));
//        Assertions.assertNotNull(tradeDetail);


    }
    @Test
    public void createTradeRequest(){
        TradeDto tradeDto = new TradeDto();
        tradeDto.setMarketId(8L);
        tradeDto.setTradeOrderType(TradeOrderTypeEnum.BUY);

        tradeDto.getSeller().setSellerId(1L);
        tradeDto.getSeller().setSellerName("zhangsan");

        tradeDto.getBuyer().setBuyerType(BuyerTypeEnum.NORMAL_BUYER);
        tradeDto.getBuyer().setBuyerId(2L);
        tradeDto.getBuyer().setBuyerName("lisi");


        TradeOrder tradeOrderItem=new TradeOrder();
        tradeOrderItem.setId(10L);

        RegisterBill registerBill = new RegisterBill();
        registerBill.setId(100L);
        registerBill.setProductName("茄子");
        registerBill.setOriginName("成都");
//        TradeRequest tradeRequest = this.tradeOrderService.createTradeRequest(tradeDto, tradeOrderItem,registerBill, 200L, BigDecimal.valueOf(111));
//        Assertions.assertNotNull(tradeRequest);



    }
}
