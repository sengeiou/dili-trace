package com.dili.trace.service;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.api.input.ProductStockInput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.dto.TradeDto;
import com.dili.trace.enums.BuyerTypeEnum;
import com.dili.trace.enums.TradeOrderStatusEnum;
import com.dili.trace.enums.TradeOrderTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public void createBuyTrade() {
        TradeDto tradeDto = new TradeDto();
        tradeDto.setMarketId(8L);
        tradeDto.setTradeOrderType(TradeOrderTypeEnum.BUY);

        tradeDto.getSeller().setSellerId(1L);
        tradeDto.getSeller().setSellerName("zhangsan");

        tradeDto.getBuyer().setBuyerType(BuyerTypeEnum.NORMAL_BUYER);
        tradeDto.getBuyer().setBuyerId(2L);
        tradeDto.getBuyer().setBuyerName("lisi");


        TradeOrder tradeOrderItem = new TradeOrder();
        tradeOrderItem.setId(10L);


        List<ProductStockInput> batchStockInputList = new ArrayList<>();
        ProductStockInput input = new ProductStockInput();
        input.setProductStockId(1L);
        input.setTradeWeight(BigDecimal.ONE);
        batchStockInputList.add(input);

        TradeOrder tradeOrder =   this.tradeOrderService.createBuyTrade(tradeDto,batchStockInputList);
        Assertions.assertNotNull(tradeOrder);


    }

    @Test
    public void createSellTrade() {

        TradeDto tradeDto = new TradeDto();
        tradeDto.setMarketId(8L);
        tradeDto.setTradeOrderType(TradeOrderTypeEnum.SELL);

        tradeDto.getSeller().setSellerId(31L);
        tradeDto.getSeller().setSellerName("zhangsan");

        tradeDto.getBuyer().setBuyerType(BuyerTypeEnum.NORMAL_BUYER);
        tradeDto.getBuyer().setBuyerId(2L);
        tradeDto.getBuyer().setBuyerName("lisi");


        List<ProductStockInput> batchStockInputList = new ArrayList<>();
        ProductStockInput input = new ProductStockInput();
        input.setProductStockId(1L);
        input.setTradeWeight(BigDecimal.ONE);
        batchStockInputList.add(input);
        TradeOrder tradeOrder = this.tradeOrderService.createSellTrade(tradeDto, batchStockInputList);
        Assertions.assertNotNull(tradeOrder);

    }

    @Test
    public void createTradeFromRegisterBill() {
        TradeOrder tradeOrder = this.tradeOrderService.createTradeFromRegisterBill(this.registerBillService.get(4L));
        Assertions.assertNotNull(tradeOrder);
    }
}
