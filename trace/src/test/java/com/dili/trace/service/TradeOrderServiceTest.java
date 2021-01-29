package com.dili.trace.service;

import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.rpc.CustomerRpc;
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
import com.dili.trace.rpc.service.CustomerRpcService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EnableDiscoveryClient
public class TradeOrderServiceTest extends AutoWiredBaseTest {
    @Autowired
    TradeOrderService tradeOrderService;
    //对真实的bean进行一次spy之后再注入。
    @SpyBean
    CustomerRpcService customerRpcService;
    //用mock的bean来代码本次testcase相关的所有依赖的bean
    @MockBean
    CustomerRpc customerRpc;

//    @MockBean

    @Test
    public void findCustomerById() {
        System.out.println(this.customerRpc);
        //当调用方法时直接返回doreturn的值 ，而不调用真实方法
        Mockito.doReturn(Optional.empty()).when(customerRpcService).findCustomerById(1L, 8L);
        this.customerRpcService.findCustomerById(1L, 8L).ifPresent(c -> {
            System.out.println(c);
        });
        //调用真实方法
        Mockito.doCallRealMethod().when(customerRpcService).findCustomerById(1L, 8L);
        this.customerRpcService.findCustomerById(1L, 8L).ifPresent(c -> {
            System.out.println(c);
        });
        //当调用方法时直接调用真实方法,之后返回thenreturn的值
        Mockito.when(this.customerRpcService.findCustomerById(1L, 8L)).thenReturn(Optional.empty());

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

        TradeOrder tradeOrder = this.tradeOrderService.createBuyTrade(tradeDto, batchStockInputList);
        Assertions.assertNotNull(tradeOrder);


    }

    @DisplayName("配送交易")
    @Test
    public void createSeperateTrade() {

        CustomerExtendDto cust=new CustomerExtendDto();
        cust.setId(31L);
        cust.setName("zhangsan");
        Mockito.doReturn(Optional.of(cust)).when(customerRpcService).findCustomerById(31L, 8L);
//        this.customerRpcService.findCustomerById(31L, 8L).ifPresent(c -> {
//            System.out.println(c);
//        });


        TradeDto tradeDto = new TradeDto();
        tradeDto.setMarketId(8L);
        tradeDto.setTradeOrderType(TradeOrderTypeEnum.SELL);

        tradeDto.getSeller().setSellerId(31L);
        tradeDto.getSeller().setSellerName("zhangsan");

        tradeDto.getBuyer().setBuyerType(BuyerTypeEnum.DOWNSTREAM_BUYER);
        tradeDto.getBuyer().setBuyerId(32L);
        tradeDto.getBuyer().setBuyerName("lisi");


        List<ProductStockInput> batchStockInputList = new ArrayList<>();
        ProductStockInput input = new ProductStockInput();
        input.setProductStockId(33L);
        input.setTradeWeight(BigDecimal.valueOf(430));
        batchStockInputList.add(input);
        TradeOrder tradeOrder = this.tradeOrderService.createSeperateTrade(tradeDto, batchStockInputList);
        Assertions.assertNotNull(tradeOrder);
    }

    @Transactional
    @Test
    @Commit
    public void createSellTrade() {
//        Optional<CustomerExtendDto>opt=new

//        Mockito.doAnswer(invocation -> {
//            CustomerExtendDto dto=new CustomerExtendDto();
//            dto.setId(31L);
//            dto.setName("zhangsan");
//            return Optional.of(dto);
//
//        }).when(Mockito.spy(this.customerRpcService)).findCustomerById(31L,Mockito.anyLong());
//
//        Mockito.doAnswer(invocation -> {
//            CustomerExtendDto dto=new CustomerExtendDto();
//            dto.setId(2L);
//            dto.setName("lisi");
//            return Optional.of(dto);
//
//        }).when(Mockito.spy(this.customerRpcService)).findCustomerById(2L,Mockito.anyLong());

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
        input.setProductStockId(33L);
        input.setTradeWeight(BigDecimal.valueOf(430));
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
