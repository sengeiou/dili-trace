package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.dto.TradeDto;
import com.dili.trace.enums.TradeOrderStatusEnum;
import com.dili.trace.enums.TradeOrderTypeEnum;
import com.dili.trace.rpc.service.CustomerRpcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * tradeorder服务
 */
@Service
@Transactional
public class TradeOrderService extends BaseServiceImpl<TradeOrder, Long> {
    /**
     * 创建TradeOrder
     * @param tradeOrderStatusEnum
     * @return
     */
    public TradeOrder createTradeOrder(TradeDto tradeDto, TradeOrderStatusEnum tradeOrderStatusEnum) {
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setBuyerId(tradeDto.getBuyer().getBuyerId());
        tradeOrder.setBuyerName(tradeDto.getBuyer().getBuyerName());
        tradeOrder.setBuyerMarketId(tradeDto.getMarketId());

        tradeOrder.setSellerId(tradeDto.getSeller().getSellerId());
        tradeOrder.setSellerName(tradeDto.getSeller().getSellerName());
        tradeOrder.setSellerMarketId(tradeDto.getMarketId());
        tradeOrder.setOrderStatus(tradeOrderStatusEnum.getCode());
        tradeOrder.setOrderType(tradeDto.getTradeOrderType().getCode());
        this.insertSelective(tradeOrder);
        return tradeOrder;
    }

    /**
     * 处理tradeorder
     * @param tradeOrderItem
     * @param tradeStatusEnum
     */

    public void handleTradeOrder(TradeOrder tradeOrderItem, TradeOrderStatusEnum tradeStatusEnum) {
        tradeOrderItem.setOrderStatus(tradeStatusEnum.getCode());
        this.updateSelective(tradeOrderItem);
    }

}