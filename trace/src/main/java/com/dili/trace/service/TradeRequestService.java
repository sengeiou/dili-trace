package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.api.input.BatchStockInput;
import com.dili.trace.api.input.TradeRequestInputDto;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.domain.User;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeStatusEnum;
import com.dili.trace.enums.TradeRequestTypeEnum;
import com.dili.trace.enums.TradeReturnStatusEnum;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.StreamEx;

@Service
@Transactional
public class TradeRequestService extends BaseServiceImpl<TradeRequest, Long> {
    private static final Logger logger = LoggerFactory.getLogger(TradeRequestService.class);
    @Autowired
    UserService userService;
    @Autowired
    BatchStockService batchStockService;

    @Autowired
    TradeDetailService tradeDetailService;


    /**
     * 创建购买请求
     * 
     * @param request
     * @return
     */
    @Transactional
    public  List<TradeRequest> createBuyRequest(Long buyerId, List<BatchStockInput> batchStockInputList) {

        List<TradeRequest> list = StreamEx.of(batchStockInputList).nonNull().map(input -> {
            if (input.getTradeWeight() == null || BigDecimal.ZERO.compareTo(input.getTradeWeight()) >= 0) {
                throw new TraceBusinessException("购买重量不能小于0");
            }
            if(input.getBatchStockId()==null){
                throw new TraceBusinessException("购买商品ID不能为空");
            }
            TradeRequest request=new TradeRequest();
            request.setBuyerId(buyerId);
            request.setBatchStockId(input.getBatchStockId());
            request.setTradeWeight(input.getTradeWeight());
            return this.createBuyRequest(request);
        }).toList();
        return list;
    }

    private TradeRequest createBuyRequest(TradeRequest request) {

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
        request.setTradeStatus(TradeStatusEnum.NONE.getCode());
        request.setReturnStatus(TradeReturnStatusEnum.NONE.getCode());
        request.setTradeRequestType(TradeRequestTypeEnum.BUY.getCode());
        this.insertSelective(request);
        return request;
    }
    public List<TradeRequest> createSellRequest(Long sellerId,Long buyerId, List<BatchStockInput> batchStockInputList) {

        List<TradeRequest> list = StreamEx.of(batchStockInputList).nonNull().map(input -> {
            if (input.getTradeWeight() == null || BigDecimal.ZERO.compareTo(input.getTradeWeight()) >= 0) {
                throw new TraceBusinessException("购买重量不能小于0");
            }
            if(input.getBatchStockId()==null){
                throw new TraceBusinessException("购买商品ID不能为空");
            }
            TradeRequest request=new TradeRequest();
            request.setSellerId(sellerId);
            request.setBuyerId(buyerId);
            request.setBatchStockId(input.getBatchStockId());
            request.setTradeWeight(input.getTradeWeight());
            return this.createSellRequest(request,input.getTradeRequestList());
        }).toList();
        return list;

    }

    private TradeRequest createSellRequest(TradeRequest request, List<TradeRequestInputDto> tradeRequestInputDtoList) {
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
        logger.info("buyer id:{},seller id:{}", request.getBuyerId(), request.getSellerId());
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
        request.setTradeStatus(TradeStatusEnum.FINISHED.getCode());
        request.setReturnStatus(TradeReturnStatusEnum.NONE.getCode());
        request.setTradeRequestType(TradeRequestTypeEnum.SELL.getCode());
        this.insertSelective(request);

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
        return request;
    }

    /**
     * 处理购买请求
     * 
     * @return
     */
    public Long handleBuyRequest(Long tradeRequestId, TradeStatusEnum tradeRequestStatus) {

        // request.setTradeRequestStatus(TradeRequestStatusEnum.NONE.getCode());
        // request.setTradeRequestType(TradeRequestTypeEnum.BUY.getCode());
        if (tradeRequestId == null || tradeRequestStatus == null) {
            throw new TraceBusinessException("参数错误");
        }
        TradeRequest tradeRequestItem = this.get(tradeRequestId);
        if (tradeRequestItem == null) {
            throw new TraceBusinessException("交易请求不存在");
        }
        if (TradeStatusEnum.NONE == tradeRequestStatus) {
            throw new TraceBusinessException("参数错误");
        }
        if (!TradeStatusEnum.NONE.equalsToCode(tradeRequestItem.getTradeStatus())) {
            throw new TraceBusinessException("不能对当前状态交易请求进行处理");
        }
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setId(tradeRequestItem.getId());
        tradeRequestItem.setTradeStatus(tradeRequestStatus.getCode());
        this.updateSelective(tradeRequest);

        return tradeRequest.getId();
    }

    /**
     * 申请退货
     * 
     * @param input
     * @param userId
     * @return
     */
    @Transactional
    public Long createReturning(Long tradeRequestId, Long userId) {
        if (tradeRequestId == null || userId == null) {
            throw new TraceBusinessException("参数错误");
        }
        TradeRequest tradeRequestItem = this.get(tradeRequestId);
        if (tradeRequestItem == null) {
            throw new TraceBusinessException("数据不存在");
        }
        if (!tradeRequestItem.getBuyerId().equals(userId)) {
            throw new TraceBusinessException("没有权限访问当前数据");
        }
        if (!TradeStatusEnum.FINISHED.equalsToCode(tradeRequestItem.getTradeStatus())) {
            throw new TraceBusinessException("交易状态错误");
        }
        if (!TradeReturnStatusEnum.NONE.equalsToCode(tradeRequestItem.getReturnStatus())) {
            throw new TraceBusinessException("退货状态错误");
        }
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setId(tradeRequestItem.getId());
        tradeRequest.setReturnStatus(TradeReturnStatusEnum.RETURNING.getCode());
        this.updateSelective(tradeRequest);
        return tradeRequest.getId();
    }

    /**
     * 完成退货处理
     * 
     * @param input
     * @param userId
     * @return
     */
    @Transactional
    public Long handleReturning(Long tradeRequestId, Long userId, TradeReturnStatusEnum returnStatus, String reason) {
        if (tradeRequestId == null || userId == null) {
            throw new TraceBusinessException("参数错误");
        }
        TradeRequest tradeRequestItem = this.get(tradeRequestId);
        if (tradeRequestItem == null) {
            throw new TraceBusinessException("数据不存在");
        }
        if (!tradeRequestItem.getSellerId().equals(userId)) {
            throw new TraceBusinessException("没有权限访问当前数据");
        }
        if (TradeReturnStatusEnum.REFUSE == returnStatus || TradeReturnStatusEnum.RETURNED == returnStatus) {
            // do nothing
        } else {
            throw new TraceBusinessException("处理状态错误");
        }
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setId(tradeRequestItem.getId());
        tradeRequest.setReturnStatus(returnStatus.getCode());
        tradeRequest.setReason(reason);
        this.updateSelective(tradeRequest);
        return tradeRequest.getId();
    }
}