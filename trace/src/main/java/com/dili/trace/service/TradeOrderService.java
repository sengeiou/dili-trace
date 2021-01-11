package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.enums.TradeOrderStatusEnum;
import com.dili.trace.enums.TradeOrderTypeEnum;
import com.dili.trace.rpc.service.CustomerRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交易订单服务
 */
@Service
@Transactional
public class TradeOrderService extends BaseServiceImpl<TradeOrder, Long> {

    @Autowired
    UserService userService;
    @Autowired
    CustomerRpcService customerRpcService;

    /**
     * 创建 tradeOrder
     * @param sellerId 卖家
     * @param buyerId 买家
     * @param orderType 交易类型（购买、销售）
     * @param marketId 市场
     * @return
     */
    public TradeOrder createTradeOrder(Long sellerId, Long buyerId,TradeOrderTypeEnum orderType, Long marketId) {

        CustomerExtendDto seller = customerRpcService.findCustomerById(sellerId, marketId).orElseThrow(() -> {
            return new TraceBizException("卖家不存在");
        });

        CustomerExtendDto buyer = customerRpcService.findCustomerById(buyerId, marketId).orElseThrow(() -> {
            return new TraceBizException("买家不存在");
        });

        return this.createTradeOrder(seller, buyer,orderType,TradeOrderStatusEnum.NONE);

    }

    /**
     * 创建订单
     * @param sellerId
     * @param buyerId
     * @param orderType
     * @param tradeOrderStatusEnum
     * @param marketId
     * @return
     */
    public TradeOrder createTradeOrder(Long sellerId, Long buyerId,TradeOrderTypeEnum orderType,TradeOrderStatusEnum tradeOrderStatusEnum,
                                       Long marketId) {

        CustomerExtendDto seller = customerRpcService.findCustomerById(sellerId, marketId).orElseThrow(() -> {
            return new TraceBizException("卖家不存在");
        });

        CustomerExtendDto buyer = customerRpcService.findCustomerById(buyerId, marketId).orElseThrow(() -> {
            return new TraceBizException("买家不存在");
        });

        return this.createTradeOrder(seller, buyer,orderType,tradeOrderStatusEnum);

    }

    /**
     * 创建订单
     * @param seller
     * @param buyer
     * @param orderType
     * @param tradeOrderStatusEnum
     * @return
     */
    public TradeOrder createTradeOrder(CustomerExtendDto seller, CustomerExtendDto buyer,TradeOrderTypeEnum orderType,TradeOrderStatusEnum tradeOrderStatusEnum) {

        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setBuyerId(buyer.getId());
        tradeOrder.setBuyerName(buyer.getName());
        tradeOrder.setBuyerMarketId(buyer.getCustomerMarket().getMarketId());

        tradeOrder.setSellerId(seller.getId());
        tradeOrder.setSellerName(seller.getName());
        tradeOrder.setSellerMarketId(seller.getCustomerMarket().getMarketId());
        tradeOrder.setOrderStatus(tradeOrderStatusEnum.getCode());
        tradeOrder.setOrderType(orderType.getCode());
        this.insertSelective(tradeOrder);
        return tradeOrder;
    }

    /**
     * 处理订单
     * @param tradeOrderItem
     * @param tradeStatusEnum
     */
    public void handleTradeOrder(TradeOrder tradeOrderItem, TradeOrderStatusEnum tradeStatusEnum) {
        tradeOrderItem.setOrderStatus(tradeStatusEnum.getCode());
        this.updateSelective(tradeOrderItem);
    }

}