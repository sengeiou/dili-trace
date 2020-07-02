package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.api.input.TradeRequestInputDto;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.domain.User;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeRequestStatusEnum;
import com.dili.trace.enums.TradeRequestTypeEnum;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.StreamEx;

@Service
public class TradeRequestService extends BaseServiceImpl<TradeRequest, Long> {
    private static final Logger logger=LoggerFactory.getLogger(TradeRequestService.class);
    @Autowired
    UserService userService;
    @Autowired
    BatchStockService batchStockService;
    @Autowired
    TradeRequestService tradeRequestService;
    @Autowired
    TradeDetailService tradeDetailService;

    /**
     * 创建购买请求
     * 
     * @param request
     * @return
     */
    @Transactional
    public Long createBuyRequest(TradeRequest request) {

        if (request.getBuyerId() == null) {
            throw new TraceBusinessException("买家ID不能为空");
        }

        if (request.getBatchStockId() == null) {
            throw new TraceBusinessException("购买商品ID不能为空");
        }
        if (request.getTradeWeight() == null || request.getTradeWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TraceBusinessException("购买重量不能为空或小于0");
        }
        User buyer = this.userService.get(request.getBuyerId());
        if (buyer == null) {
            throw new TraceBusinessException("买家信息不存在");
        }
        BatchStock batchStock = this.batchStockService.get(request.getBatchStockId());
        if (batchStock == null) {
            throw new TraceBusinessException("购买商品不存在");
        }
        User seller = this.userService.get(batchStock.getUserId());
        if (seller == null) {
            throw new TraceBusinessException("卖家信息不存在");
        }
        request.setSellerId(seller.getId());
        request.setBuyerName(batchStock.getUserName());
        request.setTradeRequestStatus(TradeRequestStatusEnum.NONE.getCode());
        request.setTradeRequestType(TradeRequestTypeEnum.BUY.getCode());
        this.insertSelective(request);
        return request.getId();
    }

    /**
     * 创建销售请求
     */
    @Transactional
    public Long createSellRequest(TradeRequest request, List<TradeRequestInputDto> tradeRequestInputDtoList) {
        if (request.getBuyerId() == null) {
            throw new TraceBusinessException("买家ID不能为空");
        }
        if (request.getTradeWeight() == null || request.getTradeWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TraceBusinessException("购买总重量不能为空或小于0");
        }
        User buyer = this.userService.get(request.getBuyerId());
        if (buyer == null) {
            throw new TraceBusinessException("买家信息不存在");
        }
        BatchStock batchStock = this.batchStockService.get(request.getBatchStockId());
        if (batchStock == null) {
            throw new TraceBusinessException("购买商品不存在");
        }
        User seller = this.userService.get(batchStock.getUserId());
        if (seller == null) {
            throw new TraceBusinessException("卖家信息不存在");
        }
        request.setSellerName(seller.getName());
        request.setSellerId(seller.getId());
        request.setBuyerName(buyer.getName());
        logger.info("buyer id:{},seller id:{}",request.getBuyerId(),request.getSellerId());
        List<MutablePair<Long, BigDecimal>> tradeDetailIdList = StreamEx
                .of(CollectionUtils.emptyIfNull(tradeRequestInputDtoList)).nonNull().map(tr -> {
                    if (tr.getTradeWeight() == null || BigDecimal.ZERO.compareTo(tr.getTradeWeight()) >= 0) {
                        throw new TraceBusinessException("购买批次重量不能为空或小于0");
                    }
                    return MutablePair.of(tr.getTradeDetailId(), tr.getTradeWeight());
                }).toList();
        if (tradeDetailIdList.isEmpty() && request.getBatchStockId() == null) {
            throw new TraceBusinessException("参数错误:没有选定正确的商品");
        }
        request.setTradeRequestStatus(TradeRequestStatusEnum.FINISHED.getCode());
        request.setTradeRequestType(TradeRequestTypeEnum.SELL.getCode());
        this.tradeRequestService.insertSelective(request);

        if (tradeDetailIdList.isEmpty()) {
            TradeDetail tradeDetailQuery = new TradeDetail();
            tradeDetailQuery.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
            tradeDetailQuery.setBatchStockId(batchStock.getId());

            List<TradeDetail> tradeDetailList = StreamEx.of(this.tradeDetailService.listByExample(tradeDetailQuery))
                    .filter(td -> {
                        return td.getStockWeight().compareTo(BigDecimal.ZERO) > 0;
                    }).sortedBy(TradeDetail::getCreated).toList();
            BigDecimal totalStockWeight = StreamEx.of(tradeDetailList).map(TradeDetail::getStockWeight)
                    .reduce(BigDecimal.ZERO, (s, t) -> {
                        return s.add(t);
                    });
            if (totalStockWeight.compareTo(request.getTradeWeight()) < 0) {
                throw new TraceBusinessException("购买重量不能超过总库存重量");
            }
            AtomicReference<BigDecimal> totalTradeWeightAt = new AtomicReference<BigDecimal>(request.getTradeWeight());
            List<TradeDetail> resultList = StreamEx.of(tradeDetailList).map(td -> {
                BigDecimal tradeWeight = totalTradeWeightAt.get();
                if (tradeWeight.compareTo(td.getStockWeight()) >= 0) {
                    tradeWeight = td.getStockWeight();
                    totalTradeWeightAt.set(totalTradeWeightAt.get().subtract(td.getStockWeight()));
                }
                if (tradeWeight.compareTo(BigDecimal.ZERO) <= 0) {
                    return null;
                }
                TradeDetail tradeDetail = this.tradeDetailService.createTradeDetail(request.getId(), td.getId(),
                        tradeWeight, seller, buyer);
                return tradeDetail;

            }).toList();

        } else {
            List<TradeDetail> resultList = StreamEx.of(tradeDetailIdList).map(p -> {
                Long tradeDetailId = p.getKey();
                BigDecimal tradeWeight = p.getValue();
                TradeDetail tradeDetail = this.tradeDetailService.createTradeDetail(request.getId(), tradeDetailId,
                        tradeWeight, buyer, buyer);
                return tradeDetail;

            }).toList();

        }
        return request.getId();
    }

    /**
     * 处理购买请求
     * 
     * @return
     */
    public Long handleBuyRequest(Long tradeRequestId) {

        //request.setTradeRequestStatus(TradeRequestStatusEnum.NONE.getCode());
        //request.setTradeRequestType(TradeRequestTypeEnum.BUY.getCode());

        TradeRequest tradeRequest=this.get(tradeRequestId);
        if(tradeRequest==null){
            throw new TraceBusinessException("交易请求不存在");
        }
        if(!TradeRequestStatusEnum.NONE.equalsToCode(tradeRequest.getTradeRequestStatus())){
            throw new TraceBusinessException("不能对当前状态交易请求进行处理");
        }
        return null;
    }
}