package com.dili.trace.service;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.ss.util.DateUtils;
import com.dili.trace.api.input.ProductStockInput;
import com.dili.trace.api.input.TradeDetailInputDto;
import com.dili.trace.api.input.TradeRequestHandleDto;
import com.dili.trace.api.input.TradeRequestInputDto;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.domain.*;
import com.dili.trace.dto.MessageInputDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.TFEnum;
import com.dili.trace.glossary.UpStreamTypeEnum;
import com.dili.trace.glossary.UserTypeEnum;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
public class TradeRequestService extends BaseServiceImpl<TradeRequest, Long> {
    private static final Logger logger = LoggerFactory.getLogger(TradeRequestService.class);
    @Autowired
    UserService userService;
    @Autowired
    ProductStockService batchStockService;
    @Autowired
    CodeGenerateService codeGenerateService;
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    TradeOrderService tradeOrderService;
    @Autowired
    UpStreamService upStreamService;
    @Autowired
    ImageCertService imageCertService;
    @Autowired
    MessageService messageService;
    /**
     * 检查参数是否正确
     * 
     * @param sellerId
     * @param batchStockInputList
     */
    private void checkInput(Long sellerId, List<ProductStockInput> batchStockInputList) {
        
        Map<Long, List<Long>> batchIdDetailIdMap = StreamEx.of(CollectionUtils.emptyIfNull(batchStockInputList))
                .nonNull().toMap(batchStockInput -> {
                    return batchStockInput.getProductStockId();
                }, batchStockInput -> {
                    return StreamEx.of(CollectionUtils.emptyIfNull(batchStockInput.getTradeDetailInputList())).nonNull()
                            .map(item -> {
                                return item.getTradeDetailId();
                            }).toList();
                });

        List<Long> batchStockIdList = StreamEx.of(batchIdDetailIdMap.keySet()).nonNull().toList();
        if (batchStockIdList.isEmpty()) {
            throw new TraceBusinessException("参数错误");
        }
        List<Long> tradeDetailIdList = StreamEx.of(batchIdDetailIdMap.values()).flatMap(List::stream).toList();
        if (!tradeDetailIdList.isEmpty()) {
            List<TradeDetail> tradeDetailList = this.tradeDetailService.findTradeDetailByIdList(tradeDetailIdList);
            // 判断是否全部是卖家的商品
            boolean notBelongToSeller = StreamEx.of(tradeDetailList).map(TradeDetail::getBuyerId).distinct()
                    .anyMatch(uid -> !uid.equals(sellerId));
            if (notBelongToSeller) {
                throw new TraceBusinessException("参数不匹配:有批次不属于当前卖家");
            }
        }
        // 判断是否全部是卖家的库存信息
        boolean notSellerOwnedBatchBlock = StreamEx.of(this.batchStockService.findByIdList(batchStockIdList))
                .map(ProductStock::getUserId).distinct().anyMatch(uid -> !uid.equals(sellerId));
        if (notSellerOwnedBatchBlock) {
            throw new TraceBusinessException("参数不匹配:有库存不属于当前卖家");
        }

    }

    /**
     * 创建销售请求并处理为完成
     */
    public List<TradeRequest> createSellRequest(Long sellerId, Long buyerId,
            List<ProductStockInput> batchStockInputList) {
        logger.info("sellerId:{},buyerId:{},checkinput:{}",sellerId,buyerId,Json.toJson(batchStockInputList));
        // 检查提交参数
        this.checkInput(sellerId, batchStockInputList);
        TradeOrder tradeOrderItem = this.tradeOrderService.createTradeOrder(sellerId, buyerId, TradeOrderTypeEnum.BUY,
                TradeOrderStatusEnum.FINISHED);
        List<TradeRequest> list = EntryStream
                .of(this.createTradeRequestList(tradeOrderItem, sellerId, buyerId, batchStockInputList))
                .mapKeyValue((request, tradeDetailInputList) -> {
                    return this.hanleRequest(request, tradeDetailInputList, TradeOrderTypeEnum.BUY);
                }).toList();
        // this.createUpStreamAndDownStream(sellerId, buyerId);

        //下单消息
        Set<String> productSet = new HashSet<>();
        batchStockInputList.stream().forEach(s ->
                {
                    ProductStock stock = this.batchStockService.get(s.getProductStockId());
                    if (null != stock) {
                        productSet.add(stock.getProductName());
                    }
                }
        );
        addMessage(sellerId,buyerId,MessageTypeEnum.SALERORDER.getCode(),productSet.toString());
        return list;

    }

    /**
     * 创建购买请求
     * 
     * @param request
     * @return
     */
    @Transactional
    public List<TradeRequest> createBuyRequest(Long buyerId, List<ProductStockInput> batchStockInputList) {
        if (batchStockInputList == null || batchStockInputList.isEmpty()) {
            throw new TraceBusinessException("参数错误");
        }
        List<Long> batchStockId = StreamEx.of(batchStockInputList).nonNull().map(ProductStockInput::getProductStockId)
                .toList();
        List<Long> sellerUserIdList = StreamEx.of(this.batchStockService.findByIdList(batchStockId))
                .map(ProductStock::getUserId).nonNull().distinct().toList();
        if (sellerUserIdList.size() != 1) {
            throw new TraceBusinessException("参数错误");
        }
        TradeOrder tradeOrderItem = this.tradeOrderService.createTradeOrder(sellerUserIdList.get(0), buyerId,
                TradeOrderTypeEnum.SELL);
        List<TradeRequest> tradeRequests = EntryStream.of(this.createTradeRequestList(tradeOrderItem, null, buyerId, batchStockInputList))
                .mapKeyValue((request, tradeDetailInputList) -> {
                    return this.hanleRequest(request, tradeDetailInputList, TradeOrderTypeEnum.SELL);
                }).toList();

        //下单消息
        Set<String> productSet = new HashSet<>();
        batchStockInputList.stream().forEach(s ->
                {
                    ProductStock stock = this.batchStockService.get(s.getProductStockId());
                    if (null != stock) {
                    productSet.add(stock.getProductName());
                }
                }
        );
        addMessage(buyerId,sellerUserIdList.get(0),MessageTypeEnum.BUYERORDER.getCode(),productSet.toString());
        return tradeRequests;
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
    TradeRequest createTradeRequest(TradeOrder tradeOrderItem, Long sellerId, Long buyerId, ProductStockInput input) {
        if (input.getTradeWeight() == null || BigDecimal.ZERO.compareTo(input.getTradeWeight()) >= 0) {
            throw new TraceBusinessException("购买重量不能小于0");
        }
        if (input.getProductStockId() == null) {
            throw new TraceBusinessException("购买商品ID不能为空");
        }

        if (input.getTradeWeight() == null || input.getTradeWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TraceBusinessException("购买总重量不能为空或小于0");
        }

        User buyer = this.userService.get(buyerId);
        if (buyer == null) {
            throw new TraceBusinessException("买家信息不存在");
        }
        ProductStock batchStock = this.batchStockService.get(input.getProductStockId());
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
        request.setProductStockId(input.getProductStockId());
        request.setTradeWeight(input.getTradeWeight());
        request.setReturnStatus(TradeReturnStatusEnum.NONE.getCode());
        request.setSellerName(seller.getName());
        request.setSellerId(seller.getId());
        request.setBuyerName(buyer.getName());
        request.setCreated(new Date());
        request.setModified(new Date());
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
        return this.codeGenerateService.nextTradeRequestCode();

    }

    /**
     * 处理交易
     */
    @Transactional
    TradeRequest hanleRequest(TradeRequest requestItem, List<TradeDetailInputDto> tradeDetailInputList,
                              TradeOrderTypeEnum tradeOrderTypeEnum) {

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
        ProductStock batchStockItem = this.batchStockService.selectByIdForUpdate(requestItem.getProductStockId())
                .orElseThrow(() -> {
                    return new TraceBusinessException("操作库存失败");
                });

        User buyer = this.userService.get(requestItem.getBuyerId());
        User seller = this.userService.get(requestItem.getSellerId());
        BigDecimal totalTradeWeight=requestItem.getTradeWeight();


        //查询和统计(基于批次)用户总库存(其实可以通过总库存获得)
        TradeDetail tradeDetailQuery = new TradeDetail();
        tradeDetailQuery.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
        tradeDetailQuery.setProductStockId(batchStockItem.getId());
        tradeDetailQuery.setSort("created");
        tradeDetailQuery.setOrder("asc");
        List<TradeDetail> tradeDetailList = this.tradeDetailService.listByExample(tradeDetailQuery);
        if (batchStockItem.getStockWeight().compareTo(totalTradeWeight) < 0) {
            throw new TraceBusinessException("购买重量不能超过总库存重量");
        }

        //基于总库存进行交易
        if (tradeDetailIdWeightList.isEmpty()) {
            AtomicReference<BigDecimal> sumTradeWeightAt = new AtomicReference<BigDecimal>(BigDecimal.ZERO);
            List<TradeDetail> resultList = StreamEx.of(tradeDetailList).map(td -> {
                BigDecimal stockWeight=td.getStockWeight();
                sumTradeWeightAt.set(sumTradeWeightAt.get().add(stockWeight));
                logger.info("stockWeight={}",stockWeight);
                logger.info("sumTradeWeightAt={}",sumTradeWeightAt.get());
                logger.info("totalTradeWeight={}",totalTradeWeight);
                BigDecimal tradeWeight=BigDecimal.ZERO;
                if(sumTradeWeightAt.get().compareTo(totalTradeWeight)<=0){
                    tradeWeight=stockWeight;
                }else{
                    if(sumTradeWeightAt.get().subtract(stockWeight).compareTo(totalTradeWeight)<0){
                        tradeWeight=totalTradeWeight.subtract(sumTradeWeightAt.get().subtract(stockWeight));
                    }else{
                        return null;
                    }
                }

                logger.info("tradeWeight={}",tradeWeight);
                TradeDetail tradeDetail = this.tradeDetailService.createTradeDetail(requestItem.getId(), td,
                        tradeWeight, seller.getId(), buyer, tradeOrderTypeEnum);
                return tradeDetail;
            }).nonNull().toList();
        } else {
            //基于批次交易
            List<TradeDetail> resultList = StreamEx.of(tradeDetailIdWeightList).map(p -> {
                TradeDetail tradeDetaiItem = p.getKey();
                BigDecimal tradeWeight = p.getValue();
                TradeDetail tradeDetail = this.tradeDetailService.createTradeDetail(requestItem.getId(), tradeDetaiItem,
                        tradeWeight, seller.getId(), buyer, tradeOrderTypeEnum);
                return tradeDetail;

            }).toList();
        }
        // BatchStock batchStock = new BatchStock();
        // batchStock.setId(batchStockItem.getId());
        // batchStock.setStockWeight(batchStockItem.getStockWeight().subtract(requestItem.getTradeWeight()));
        // this.batchStockService.updateSelective(batchStock);

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
            Long buyerId, List<ProductStockInput> batchStockInputList) {
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

        TradeDetail tradeDetailQuery = new TradeDetail();
        tradeDetailQuery.setTradeRequestId(tradeRequestId);
        List<TradeDetail> tradeDetailList = this.tradeDetailService.listByExample(tradeDetailQuery);
        StreamEx.of(tradeDetailList).forEach(td -> {
            boolean changed = td.getTotalWeight().compareTo(td.getStockWeight()) != 0;
            if (changed) {
                throw new TraceBusinessException("不能对已销售的商品申请退货");
            }
            ProductStock batchStockItem = this.batchStockService.selectByIdForUpdate(td.getProductStockId())
                    .orElseThrow(() -> {
                        return new TraceBusinessException("操作库存失败");
                    });

            ProductStock batchStock = new ProductStock();
            batchStock.setId(batchStockItem.getId());
            batchStock.setTradeDetailNum(batchStockItem.getTradeDetailNum() - 1);
            batchStock.setStockWeight(batchStockItem.getStockWeight().subtract(td.getStockWeight()));
            this.batchStockService.updateSelective(batchStock);

            TradeDetail tradeDetail = new TradeDetail();
            tradeDetail.setId(td.getId());
            tradeDetail.setSaleStatus(SaleStatusEnum.NOT_FOR_SALE.getCode());
            this.tradeDetailService.updateSelective(tradeDetail);

        });

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

        TradeDetail tradeDetailQuery = new TradeDetail();
        tradeDetailQuery.setTradeRequestId(tradeRequestId);
        List<TradeDetail> tradeDetailList = this.tradeDetailService.listByExample(tradeDetailQuery);

        if (TradeReturnStatusEnum.REFUSE == returnStatus) {

            StreamEx.of(tradeDetailList).forEach(td -> {

                ProductStock batchStockItem = this.batchStockService.selectByIdForUpdate(td.getProductStockId())
                        .orElseThrow(() -> {
                            return new TraceBusinessException("操作库存失败");
                        });

                ProductStock batchStock = new ProductStock();
                batchStock.setId(batchStockItem.getId());
                batchStock.setTradeDetailNum(batchStockItem.getTradeDetailNum() + 1);
                batchStock.setStockWeight(batchStockItem.getStockWeight().add(td.getStockWeight()));
                this.batchStockService.updateSelective(batchStock);

                TradeDetail tradeDetail = new TradeDetail();
                tradeDetail.setId(td.getId());
                tradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
                this.tradeDetailService.updateSelective(tradeDetail);
            });
        } else {

            StreamEx.of(tradeDetailList).mapToEntry(buyerTd -> buyerTd, buyerTd -> {
                Long parentId = buyerTd.getParentId();
                return this.tradeDetailService.get(parentId);
            }).forKeyValue((buyertd, sellertd) -> {

                TradeDetail buyerTradeDetail = new TradeDetail();
                buyerTradeDetail.setId(buyertd.getId());
                buyerTradeDetail.setIsBatched(TFEnum.FALSE.getCode());
                this.tradeDetailService.updateSelective(buyerTradeDetail);

                ProductStock sellerBatchStockItem = this.batchStockService.selectByIdForUpdate(sellertd.getProductStockId())
                        .orElseThrow(() -> {
                            return new TraceBusinessException("操作库存失败");
                        });

                ProductStock sellerBatchStock = new ProductStock();
                sellerBatchStock.setId(sellerBatchStockItem.getId());
                sellerBatchStock.setStockWeight(sellerBatchStockItem.getStockWeight().add(buyertd.getStockWeight()));

                TradeDetail sellerTradeDetail = new TradeDetail();
                sellerTradeDetail.setId(sellertd.getId());
                sellerTradeDetail.setStockWeight(sellertd.getStockWeight().add(buyertd.getStockWeight()));

                if (SaleStatusEnum.FOR_SALE.equalsToCode(sellertd.getSaleStatus())) {
                    // do nothing
                } else {
                    sellerBatchStock.setTradeDetailNum(sellerBatchStockItem.getTradeDetailNum() + 1);
                    sellerTradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
                    sellerTradeDetail.setIsBatched(TFEnum.TRUE.getCode());
                }
                this.batchStockService.updateSelective(sellerBatchStock);
                this.tradeDetailService.updateSelective(sellerTradeDetail);
            });
        }

        return tradeRequest.getId();
    }

    void createUpStreamAndDownStream(Long sellerId, Long buyerId) {
        User seller = this.userService.get(sellerId);
        if (seller == null) {
            throw new TraceBusinessException("卖家信息不存在");
        }

        User buyer = this.userService.get(buyerId);
        if (buyer == null) {
            throw new TraceBusinessException("买家信息不存在");
        }

        UpStream buyersUStream = StreamEx
                .of(this.upStreamService.queryUpStreamByUserIdAndFlag(buyerId, UserFlagEnum.UP, sellerId)).findFirst()
                .orElse(null);

        UpStream sellersDStream = StreamEx
                .of(this.upStreamService.queryUpStreamByUserIdAndFlag(sellerId, UserFlagEnum.DOWN, buyerId)).findFirst()
                .orElse(null);
        if (buyersUStream == null) {
            UpStreamDto upStreamDto = this.createUpStreamDtoFromUser(seller);
            upStreamDto.setUpORdown(UserFlagEnum.UP.getCode());
            upStreamDto.setUserIds(Arrays.asList(buyerId));
            this.upStreamService.addUpstream(upStreamDto, new OperatorUser(null, null));
        }

        if (sellersDStream == null) {
            UpStreamDto upStreamDto = this.createUpStreamDtoFromUser(buyer);
            upStreamDto.setUpORdown(UserFlagEnum.DOWN.getCode());
            upStreamDto.setUserIds(Arrays.asList(sellerId));
            this.upStreamService.addUpstream(upStreamDto, new OperatorUser(null, null));
        }

    }

    private UpStreamDto createUpStreamDtoFromUser(User user) {
        UpStreamDto upStreamDto = new UpStreamDto();
        upStreamDto.setName(user.getName());
        upStreamDto.setIdCard(user.getCardNo());
        upStreamDto.setBusinessLicenseUrl(user.getBusinessLicenseUrl());
        upStreamDto.setCardNoBackUrl(user.getCardNoBackUrl());
        upStreamDto.setCardNoFrontUrl(user.getCardNoFrontUrl());
        upStreamDto.setLegalPerson(user.getLegalPerson());
        upStreamDto.setLicense(user.getLicense());
        upStreamDto.setManufacturingLicenseUrl(user.getManufacturingLicenseUrl());
        upStreamDto.setTelphone(user.getPhone());
        if (UserTypeEnum.USER.equalsCode(user.getUserType())) {
            upStreamDto.setUpstreamType(UpStreamTypeEnum.USER.getCode());
        } else {
            upStreamDto.setUpstreamType(UpStreamTypeEnum.CORPORATE.getCode());
        }
        return upStreamDto;
    }

    public BasePage<TradeRequest> listPageTradeRequestByBuyerIdOrSellerId(TradeRequestInputDto tradeRequest,
            Long userId) {
        tradeRequest.setMetadata(IDTO.AND_CONDITION_EXPR, "(buyer_id=" + userId + " OR seller_id=" + userId + ")");
        return this.listPageByExample(tradeRequest);

    }

    @Transactional(rollbackFor = Exception.class)
    public void handleBuyerRequest(TradeRequestHandleDto handleDto)
    {
        Long tradeRequestId = handleDto.getTradeRequestId();
        Integer handleStatus = handleDto.getHandleStatus();
        TradeRequest tradeRequest = this.get(tradeRequestId);
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setId(tradeRequest.getTradeOrderId());
        tradeOrder.setOrderStatus(handleStatus);
        this.tradeOrderService.updateSelective(tradeOrder);

        TradeDetail tradeDetailQuery = new TradeDetail();
        tradeDetailQuery.setTradeRequestId(tradeRequestId);
        List<TradeDetail> tradeDetailList = this.tradeDetailService.listByExample(tradeDetailQuery);
        StreamEx.of(tradeDetailList).forEach(td -> {
            if(handleStatus.equals(TradeOrderStatusEnum.FINISHED.getCode())) {
                TradeDetail tradeDetail = new TradeDetail();
                tradeDetail.setId(td.getId());
                tradeDetail.setStockWeight(td.getSoftWeight());
                tradeDetail.setSoftWeight(BigDecimal.ZERO);
                tradeDetail.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
                this.tradeDetailService.updateSelective(tradeDetail);

                ProductStock productStock = this.batchStockService.get(td.getProductStockId());
                ProductStock productStockUpdate = new ProductStock();
                productStockUpdate.setId(td.getProductStockId());
                productStockUpdate.setStockWeight(productStock.getStockWeight().add(td.getSoftWeight()));
                productStockUpdate.setTradeDetailNum(productStock.getTradeDetailNum() + 1);
                this.batchStockService.updateSelective(productStockUpdate);
            }
            else if(handleStatus.equals(TradeOrderStatusEnum.CANCELLED.getCode())){
                TradeDetail parentTradeDetail = this.tradeDetailService.get(td.getParentId());
                TradeDetail parentTradeDetailForUpdate = new TradeDetail();
                parentTradeDetailForUpdate.setId(parentTradeDetail.getId());
                parentTradeDetailForUpdate.setStockWeight(parentTradeDetail.getStockWeight().add(td.getSoftWeight()));
                this.tradeDetailService.updateSelective(parentTradeDetailForUpdate);
            }
        });

        //下单消息
        Set<String> productSet = new HashSet<>();
        Set<Long> buyerId=new HashSet<>();
        Set<Long> sellerid = new HashSet<>();
        StreamEx.of(tradeDetailList).forEach(td -> {
            productSet.add(td.getProductName());
            buyerId.add(td.getBuyerId());
            sellerid.add(td.getSellerId());
        });
        addMessage(buyerId.stream().findFirst().get(),sellerid.stream().findFirst().get(),MessageTypeEnum.BUYERORDER.getCode(),productSet.toString());
    }

    /**
     * 新增消息并发送短信
     * @param sendUserId
     * @param messageType
     * @param productNames
     */
    private void addMessage(Long sendUserId,Long receiUserId, Integer messageType, String productNames) {
        // 增加消息
        MessageInputDto messageInputDto = new MessageInputDto();
        messageInputDto.setCreatorId(sendUserId);
        messageInputDto.setMessageType(messageType);
        messageInputDto.setReceiverIdArray(new Long[]{receiUserId});

        //增加卖家短信
        Map<String,Object> sellmap = new HashMap<>();
        sellmap.put("userName",sendUserId);
        sellmap.put("created", DateUtils.format(new Date(),"yyyy年MM月dd日 HH:mm:ss"));
        sellmap.put("tradeInfo",productNames);
        messageInputDto.setSmsContentParam(sellmap);
        messageService.addMessage(messageInputDto);
    }

    public List<UserOutput> queryTradeSellerHistoryList(Long buyerId)
    {
        TradeRequest request = new TradeRequest();
        request.setBuyerId(buyerId);
        List<TradeRequest> tradeRequests = this.list(request);
        List<Long> sellerIds = StreamEx.of(this.list(request))
                .map(TradeRequest::getSellerId).nonNull().distinct().toList();
        List<UserOutput> outPutDtoList = new ArrayList<>();
        StreamEx.of(sellerIds).nonNull().forEach(td -> {
            UserOutput outPutDto = new UserOutput();

            User user = this.userService.get(td);
            if(user != null) {
                outPutDto.setId(td);
                outPutDto.setName(user.getName());
                outPutDto.setBusinessLicenseUrl(user.getBusinessLicenseUrl());
                outPutDto.setTallyAreaNos(user.getTallyAreaNos());
                outPutDto.setMarketName(user.getMarketName());
                outPutDtoList.add(outPutDto);
            }
        });
        return outPutDtoList;
    }
}