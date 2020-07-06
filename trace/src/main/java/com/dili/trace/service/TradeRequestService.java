package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.input.BatchStockInput;
import com.dili.trace.api.input.TradeDetailInputDto;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.domain.User;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.enums.TradeOrderStatusEnum;
import com.dili.trace.enums.TradeOrderTypeEnum;
import com.dili.trace.enums.TradeReturnStatusEnum;
import com.dili.trace.util.BasePageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

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
    @Autowired
    TradeOrderService tradeOrderService;

    /**
     * 创建销售请求并处理为完成
     */
    public List<TradeRequest> createSellRequest(Long sellerId, Long buyerId,
            List<BatchStockInput> batchStockInputList) {

        StreamEx.of(CollectionUtils.emptyIfNull(batchStockInputList)).nonNull().map(batchStockInput -> {
            BatchStock batchStock = this.batchStockService.get(batchStockInput.getBatchStockId());
            if (batchStock == null) {
                throw new TraceBusinessException("数据不存在");
            }
            if(batchStock.getUserId().equals(sellerId)){
                throw new TraceBusinessException("卖家参数错误");
            }
            List<Long> tradeDetailIdList = StreamEx
                    .of(CollectionUtils.emptyIfNull(batchStockInput.getTradeDetailInputList())).nonNull().map(item -> {
                        return item.getTradeDetailId();
                    }).toList();
            List<TradeDetail> tradeDetailList = this.tradeDetailService.findTradeDetailByIdList(tradeDetailIdList);
            boolean notMatch = StreamEx.of(tradeDetailList)
                    .anyMatch(td -> !td.getBatchStockId().equals(batchStock.getId()));
            if (notMatch) {
                throw new TraceBusinessException("参数不匹配");
            }
            return batchStockInput;
        }).toList();

        TradeOrder tradeOrderItem = this.tradeOrderService.createTradeOrder(sellerId, buyerId, TradeOrderTypeEnum.BUY);
        List<TradeRequest> list = EntryStream
                .of(this.createTradeRequestList(tradeOrderItem, sellerId, buyerId, batchStockInputList))
                .mapKeyValue((request, tradeDetailInputList) -> {
                    return this.hanleRequest(request, tradeDetailInputList);
                }).toList();
        this.tradeOrderService.handleTradeOrder(tradeOrderItem, TradeOrderStatusEnum.FINISHED);
        return list;

    }

    /**
     * 创建购买请求
     * 
     * @param request
     * @return
     */
    @Transactional
    public List<TradeRequest> createBuyRequest(Long buyerId, List<BatchStockInput> batchStockInputList) {
        if (batchStockInputList == null || batchStockInputList.isEmpty()) {
            throw new TraceBusinessException("参数错误");
        }
        List<Long> batchStockId = StreamEx.of(batchStockInputList).nonNull().map(BatchStockInput::getBatchStockId)
                .toList();
        List<Long> sellerUserIdList = StreamEx.of(this.batchStockService.findByIdList(batchStockId))
                .map(BatchStock::getUserId).nonNull().distinct().toList();
        if (sellerUserIdList.size() != 1) {
            throw new TraceBusinessException("参数错误");
        }
        TradeOrder tradeOrderItem = this.tradeOrderService.createTradeOrder(sellerUserIdList.get(0), buyerId,
                TradeOrderTypeEnum.SELL);
        return EntryStream.of(this.createTradeRequestList(tradeOrderItem, null, buyerId, batchStockInputList))
                .mapKeyValue((request, tradeDetailInputList) -> {
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
    TradeRequest createTradeRequest(TradeOrder tradeOrderItem, Long sellerId, Long buyerId, BatchStockInput input) {
        if (input.getTradeWeight() == null || BigDecimal.ZERO.compareTo(input.getTradeWeight()) >= 0) {
            throw new TraceBusinessException("购买重量不能小于0");
        }
        if (input.getBatchStockId() == null) {
            throw new TraceBusinessException("购买商品ID不能为空");
        }

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
        request.setTradeOrderId(tradeOrderItem.getId());
        request.setCode(this.getNextCode());
        request.setProductName(batchStock.getProductName());
        request.setWeightUnit(batchStock.getWeightUnit());
        logger.info("buyer id:{},seller id:{}", request.getBuyerId(), request.getSellerId());
        this.insertSelective(request);
        return request;
    }

    private String getNextCode() {
        return String.valueOf(System.currentTimeMillis());

    }

    /**
     * 处理请求
     */
    TradeRequest hanleRequest(TradeRequest requestItem, List<TradeDetailInputDto> tradeDetailInputList) {

        List<MutablePair<TradeDetail, BigDecimal>> tradeDetailIdWeightList = StreamEx
                .of(CollectionUtils.emptyIfNull(tradeDetailInputList)).nonNull().map(tr -> {
                    if (tr.getTradeWeight() == null || BigDecimal.ZERO.compareTo(tr.getTradeWeight()) >= 0) {
                        throw new TraceBusinessException("购买批次重量不能为空或小于0");
                    }
                    TradeDetail tradeDetail = this.tradeDetailService.get(tr.getTradeDetailId());
                    if (tradeDetail == null) {
                        throw new TraceBusinessException("数据不存在");
                    }
                    return MutablePair.of(tradeDetail, tr.getTradeWeight());
                }).toList();
        BatchStock batchStockItem = this.batchStockService.get(requestItem.getBatchStockId());
        User buyer = this.userService.get(requestItem.getBuyerId());
        User seller = this.userService.get(requestItem.getSellerId());

        TradeDetail tradeDetailQuery = new TradeDetail();
        tradeDetailQuery.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
        tradeDetailQuery.setBatchStockId(batchStockItem.getId());

        List<TradeDetail> tradeDetailList = StreamEx.of(this.tradeDetailService.listByExample(tradeDetailQuery))
                .filter(td -> {
                    return td.getStockWeight().compareTo(BigDecimal.ZERO) > 0;
                }).sortedBy(TradeDetail::getCreated).toList();
        if (batchStockItem.getStockWeight().compareTo(requestItem.getTradeWeight()) < 0) {
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
                TradeDetail tradeDetail = this.tradeDetailService.createTradeDetail(requestItem.getId(), td,
                        tradeWeight, seller.getId(), buyer);
                return tradeDetail;
            }).toList();
        } else {
            List<TradeDetail> resultList = StreamEx.of(tradeDetailIdWeightList).map(p -> {
                TradeDetail tradeDetaiItem = p.getKey();
                BigDecimal tradeWeight = p.getValue();
                TradeDetail tradeDetail = this.tradeDetailService.createTradeDetail(requestItem.getId(), tradeDetaiItem,
                        tradeWeight, seller.getId(), buyer);
                return tradeDetail;

            }).toList();
        }
        BatchStock batchStock = new BatchStock();
        batchStock.setId(batchStockItem.getId());
        batchStock.setStockWeight(batchStockItem.getStockWeight().subtract(requestItem.getTradeWeight()));
        this.batchStockService.updateSelective(batchStock);

        return this.get(requestItem.getId());

    }

    /**
     * 创建批量交易请求
     * 
     * @param sellerId
     * @param buyerId
     * @param tradeRequestType
     * @param batchStockInputList
     * @return
     */
    Map<TradeRequest, List<TradeDetailInputDto>> createTradeRequestList(TradeOrder tradeOrderItem, Long sellerId,
            Long buyerId, List<BatchStockInput> batchStockInputList) {
        Map<TradeRequest, List<TradeDetailInputDto>> map = StreamEx.of(batchStockInputList).nonNull()
                .mapToEntry(input -> {
                    TradeRequest request = this.createTradeRequest(tradeOrderItem, sellerId, buyerId, input);
                    return request;
                }, input -> {
                    return StreamEx.of(CollectionUtils.emptyIfNull(input.getTradeDetailInputList())).nonNull().toList();
                }).toMap();
        return map;

    }

    // /**
    // * 处理购买请求
    // *
    // * @return
    // */
    // public Long handleBuyRequest(Long tradeRequestId, TradeOrderStatusEnum
    // tradeRequestStatus,
    // List<TradeDetailInputDto> tradeDetailInputList) {

    // // request.setTradeRequestStatus(TradeRequestStatusEnum.NONE.getCode());
    // // request.setTradeRequestType(TradeRequestTypeEnum.BUY.getCode());
    // if (tradeRequestId == null || tradeRequestStatus == null) {
    // throw new TraceBusinessException("参数错误");
    // }
    // TradeRequest tradeRequestItem = this.get(tradeRequestId);
    // if (tradeRequestItem == null) {
    // throw new TraceBusinessException("交易请求不存在");
    // }
    // if (TradeOrderStatusEnum.NONE == tradeRequestStatus) {
    // throw new TraceBusinessException("参数错误");
    // }

    // return this.hanleRequest(tradeRequestItem, tradeDetailInputList).getId();
    // }

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

    public BasePage<TradeRequest> listPageTradeRequestByBuyerIdOrSellerId(TradeRequest tradeRequest) {
        if(tradeRequest.getPage()==null){
            tradeRequest.setPage(1);
        }
        if(tradeRequest.getRows()==null){
            tradeRequest.setRows(10);
        }

        if (tradeRequest == null || (tradeRequest.getBuyerId() == null && tradeRequest.getSellerId() == null)) {
            return BasePageUtil.empty(tradeRequest.getPage(), tradeRequest.getRows());
        }
        PageHelper.startPage(tradeRequest.getPage(), tradeRequest.getRows());

        Example e = new Example(TradeRequest.class);
        e.or().orEqualTo("buyerId", tradeRequest.getBuyerId()).orEqualTo("sellerId", tradeRequest.getSellerId());
        List<TradeRequest> list = getDao().selectByExample(e);
        Page<TradeRequest> page = (Page) list;
        BasePage<TradeRequest> result = new BasePage<TradeRequest>();
        result.setDatas(list);
        result.setPage(page.getPageNum());
        result.setRows(page.getPageSize());
        result.setTotalItem(Integer.parseInt(String.valueOf(page.getTotal())));
        result.setTotalPage(page.getPages());
        result.setStartIndex(page.getStartRow());
        return result;

    }
}