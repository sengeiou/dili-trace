package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.domain.User;
import com.dili.trace.enums.TradeOrderStatusEnum;
import com.dili.trace.enums.TradeOrderTypeEnum;
import com.dili.trace.rpc.service.CustomerRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TradeOrderService extends BaseServiceImpl<TradeOrder, Long> {

    @Autowired
    UserService userService;
    @Autowired
    CustomerRpcService customerRpcService;

    public TradeOrder createTradeOrder(Long sellerId, Long buyerId,TradeOrderTypeEnum orderType, Long marketId) {

        User seller = customerRpcService.findUserFromCustomerById(sellerId, marketId).orElseThrow(() -> {
            return new TraceBizException("卖家不存在");
        });

        User buyer = customerRpcService.findUserFromCustomerById(buyerId, marketId).orElseThrow(() -> {
            return new TraceBizException("买家不存在");
        });

        return this.createTradeOrder(seller, buyer,orderType,TradeOrderStatusEnum.NONE);

    }
    public TradeOrder createTradeOrder(Long sellerId, Long buyerId,TradeOrderTypeEnum orderType,TradeOrderStatusEnum tradeOrderStatusEnum,
                                       Long marketId) {

        User seller = customerRpcService.findUserFromCustomerById(sellerId, marketId).orElseThrow(() -> {
            return new TraceBizException("卖家不存在");
        });

        User buyer = customerRpcService.findUserFromCustomerById(buyerId, marketId).orElseThrow(() -> {
            return new TraceBizException("买家不存在");
        });

        return this.createTradeOrder(seller, buyer,orderType,tradeOrderStatusEnum);

    }

    public TradeOrder createTradeOrder(User seller, User buyer,TradeOrderTypeEnum orderType,TradeOrderStatusEnum tradeOrderStatusEnum) {

        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setBuyerId(buyer.getId());
        tradeOrder.setBuyerName(buyer.getName());
        tradeOrder.setBuyerMarketId(buyer.getMarketId());

        tradeOrder.setSellerId(seller.getId());
        tradeOrder.setSellerName(seller.getName());
        tradeOrder.setSellerMarketId(seller.getMarketId());
        tradeOrder.setOrderStatus(tradeOrderStatusEnum.getCode());
        tradeOrder.setOrderType(orderType.getCode());
        this.insertSelective(tradeOrder);
        return tradeOrder;
    }

    public void handleTradeOrder(TradeOrder tradeOrderItem, TradeOrderStatusEnum tradeStatusEnum) {
        tradeOrderItem.setOrderStatus(tradeStatusEnum.getCode());
        this.updateSelective(tradeOrderItem);
    }

}