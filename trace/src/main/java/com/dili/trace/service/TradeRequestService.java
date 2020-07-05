package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.api.input.BatchStockInput;
import com.dili.trace.api.input.TradeDetailInputDto;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.domain.User;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeRequestTypeEnum;
import com.dili.trace.enums.TradeReturnStatusEnum;
import com.dili.trace.enums.TradeStatusEnum;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.EntryStream;
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
     * 创建销售请求并处理为完成
     */
    public List<TradeRequest> createSellRequest(Long sellerId, Long buyerId,
            List<BatchStockInput> batchStockInputList) {
        return EntryStream
                .of(this.createTradeRequestList(sellerId, buyerId, TradeRequestTypeEnum.SELL, batchStockInputList))
                .mapKeyValue((request, tradeDetailInputList) -> {
                    return this.hanleRequest(request, TradeStatusEnum.FINISHED, tradeDetailInputList);
                }).toList();

    }

    /**
     * 创建购买请求
     * 
     * @param request
     * @return
     */
    @Transactional
    public List<TradeRequest> createBuyRequest(Long buyerId, List<BatchStockInput> batchStockInputList) {

        return EntryStream
                .of(this.createTradeRequestList(null, buyerId, TradeRequestTypeEnum.SELL, batchStockInputList))
                .mapKeyValue((request, tradeDetailInputList) -> {
                    // return this.hanleRequest(request, TradeStatusEnum.FINISHED,
                    // tradeDetailInputList);
                    return request;
                }).toList();

    }

    /**
     * 创建请求
     * 
     * @param sellerId
     * @param buyerId
     * @param tradeRequestType
     * @param input
     * @return
     */
     TradeRequest createTradeRequest(Long sellerId, Long buyerId, TradeRequestTypeEnum tradeRequestType,
            BatchStockInput input) {
        if (input.getTradeWeight() == null || BigDecimal.ZERO.compareTo(input.getTradeWeight()) >= 0) {
            throw new TraceBusinessException("购买重量不能小于0");
        }
        if (input.getBatchStockId() == null) {
            throw new TraceBusinessException("购买商品ID不能为空");
        }
        if (tradeRequestType == null) {
            throw new TraceBusinessException("交易类型不能为空");
        }
        // if(TradeRequestTypeEnum.BUY==tradeRequestType){

        // }else if(TradeRequestTypeEnum.SELL==tradeRequestType){

        // }else{

        // }

        if (input.getTradeWeight() == null || input.getTradeWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TraceBusinessException("购买总重量不能为空或小于0");
        }

        User buyer = this.userService.get(buyerId);
        if (buyer == null) {
            throw new TraceBusinessException("买家信息不存在");
        }
        BatchStock batchStock = this.batchStockService.get(input.getBatchStockId());
        if (batchStock == null) {
            throw new TraceBusinessException("购买商品不存在");
        }
        if (sellerId != null && !sellerId.equals(batchStock.getUserId())) {
            throw new TraceBusinessException("没有权限销售当前商品");
        }
        User seller = this.userService.get(batchStock.getUserId());
        if (seller == null) {
            throw new TraceBusinessException("卖家信息不存在");
        }

        TradeRequest request = new TradeRequest();
        request.setBatchStockId(input.getBatchStockId());
        request.setTradeWeight(input.getTradeWeight());
        request.setReturnStatus(TradeReturnStatusEnum.NONE.getCode());
        request.setSellerName(seller.getName());
        request.setSellerId(seller.getId());
        request.setBuyerName(buyer.getName());
        request.setBuyerId(buyer.getId());
        request.setTradeRequestType(tradeRequestType.getCode());
        request.setTradeStatus(TradeStatusEnum.NONE.getCode());
        logger.info("buyer id:{},seller id:{}", request.getBuyerId(), request.getSellerId());
        this.insertSelective(request);
        return request;
    }

    /**
     * 处理请求
     */
     TradeRequest hanleRequest(TradeRequest requestItem, TradeStatusEnum tradeStatusEnum,
            List<TradeDetailInputDto> tradeDetailInputList) {
        if (tradeStatusEnum == null) {
            throw new TraceBusinessException("交易状态不能为空");
        }
        if (tradeStatusEnum == TradeStatusEnum.NONE) {
            throw new TraceBusinessException("不支持的交易状态变更");
        }
        TradeRequest request = new TradeRequest();
        request.setId(requestItem.getId());
        request.setTradeStatus(tradeStatusEnum.getCode());
        if (TradeStatusEnum.CANCELLED == tradeStatusEnum) {
            this.updateSelective(request);
            return this.get(requestItem.getId());
        } else if (TradeStatusEnum.FINISHED == tradeStatusEnum) {
            List<MutablePair<Long, BigDecimal>> tradeDetailIdWeightList = StreamEx
                    .of(CollectionUtils.emptyIfNull(tradeDetailInputList)).nonNull().map(tr -> {
                        if (tr.getTradeWeight() == null || BigDecimal.ZERO.compareTo(tr.getTradeWeight()) >= 0) {
                            throw new TraceBusinessException("购买批次重量不能为空或小于0");
                        }
                        return MutablePair.of(tr.getTradeDetailId(), tr.getTradeWeight());
                    }).toList();
            BatchStock batchStock = this.batchStockService.get(requestItem.getBatchStockId());
            User buyer = this.userService.get(requestItem.getBuyerId());
            User seller = this.userService.get(requestItem.getSellerId());

            TradeDetail tradeDetailQuery = new TradeDetail();
            tradeDetailQuery.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
            tradeDetailQuery.setBatchStockId(batchStock.getId());

            List<TradeDetail> tradeDetailList = StreamEx.of(this.tradeDetailService.listByExample(tradeDetailQuery))
                    .filter(td -> {
                        return td.getStockWeight().compareTo(BigDecimal.ZERO) > 0;
                    }).sortedBy(TradeDetail::getCreated).toList();
            if (batchStock.getStockWeight().compareTo(requestItem.getTradeWeight()) < 0) {
                throw new TraceBusinessException("购买重量不能超过总库存重量");
            }
            if (tradeDetailIdWeightList.isEmpty()) {

                AtomicReference<BigDecimal> totalTradeWeightAt = new AtomicReference<BigDecimal>(
                        requestItem.getTradeWeight());
                List<TradeDetail> resultList = StreamEx.of(tradeDetailList).map(td -> {
                    BigDecimal tradeWeight = totalTradeWeightAt.get();
                    if (tradeWeight.compareTo(td.getStockWeight()) >= 0) {
                        tradeWeight = td.getStockWeight();
                        totalTradeWeightAt.set(totalTradeWeightAt.get().subtract(td.getStockWeight()));
                    }
                    if (tradeWeight.compareTo(BigDecimal.ZERO) <= 0) {
                        return null;
                    }
                    TradeDetail tradeDetail = this.tradeDetailService.createTradeDetail(requestItem.getId(), td.getId(),
                            tradeWeight, seller, buyer);
                    return tradeDetail;
                }).toList();
            } else {
                List<TradeDetail> resultList = StreamEx.of(tradeDetailIdWeightList).map(p -> {
                    Long tradeDetailId = p.getKey();
                    BigDecimal tradeWeight = p.getValue();
                    TradeDetail tradeDetail = this.tradeDetailService.createTradeDetail(requestItem.getId(),
                            tradeDetailId, tradeWeight, buyer, buyer);
                    return tradeDetail;

                }).toList();
            }
            this.updateSelective(request);
            return this.get(requestItem.getId());
        } else {
            throw new TraceBusinessException("交易状态错误");
        }

    }

    /**
     * 创建批量交易请求
     * @param sellerId
     * @param buyerId
     * @param tradeRequestType
     * @param batchStockInputList
     * @return
     */
     Map<TradeRequest, List<TradeDetailInputDto>> createTradeRequestList(Long sellerId, Long buyerId,
            TradeRequestTypeEnum tradeRequestType, List<BatchStockInput> batchStockInputList) {
        Map<TradeRequest, List<TradeDetailInputDto>> map = StreamEx.of(batchStockInputList).nonNull()
                .mapToEntry(input -> {
                    TradeRequest request = this.createTradeRequest(sellerId, buyerId, tradeRequestType, input);
                    return request;
                }, input -> {
                    return StreamEx.of(CollectionUtils.emptyIfNull(input.getTradeDetailInputList())).nonNull().toList();
                }).toMap();
        return map;

    }


    /**
     * 处理购买请求
     * 
     * @return
     */
    public Long handleBuyRequest(Long tradeRequestId, TradeStatusEnum tradeRequestStatus,
    List<TradeDetailInputDto> tradeDetailInputList) {

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

        return this.hanleRequest(tradeRequestItem, tradeRequestStatus,tradeDetailInputList).getId();
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