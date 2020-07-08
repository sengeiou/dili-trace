package com.dili.trace.service;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.domain.User;
import com.dili.trace.enums.TradeOrderStatusEnum;
import com.dili.trace.enums.TradeOrderTypeEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TradeOrderService extends BaseServiceImpl<TradeOrder, Long> {

    @Autowired
    UserService userService;

    public TradeOrder createTradeOrder(Long sellerId, Long buyerId,TradeOrderTypeEnum orderType) {
        User seller = this.userService.get(sellerId);
        User buyer = this.userService.get(buyerId);
        if(seller==null){
            throw new TraceBusinessException("卖家不存在");
        }
        if(buyer==null){
            throw new TraceBusinessException("买家不存在");
        }
        return this.createTradeOrder(seller, buyer,orderType,TradeOrderStatusEnum.NONE);

    }
    public TradeOrder createTradeOrder(Long sellerId, Long buyerId,TradeOrderTypeEnum orderType,TradeOrderStatusEnum tradeOrderStatusEnum) {
        User seller = this.userService.get(sellerId);
        User buyer = this.userService.get(buyerId);
        if(seller==null){
            throw new TraceBusinessException("卖家不存在");
        }
        if(buyer==null){
            throw new TraceBusinessException("买家不存在");
        }
        return this.createTradeOrder(seller, buyer,orderType,tradeOrderStatusEnum);

    }

    public TradeOrder createTradeOrder(User seller, User buyer,TradeOrderTypeEnum orderType,TradeOrderStatusEnum tradeOrderStatusEnum) {

        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setBuyerId(buyer.getId());
        tradeOrder.setBuyerName(buyer.getName());

        tradeOrder.setSellerId(seller.getId());
        tradeOrder.setSellerName(seller.getName());
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