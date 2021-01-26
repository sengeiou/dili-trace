package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.api.input.ProductStockInput;
import com.dili.trace.api.input.TradeDetailInputDto;
import com.dili.trace.api.input.TradeDetailQueryDto;
import com.dili.trace.domain.*;
import com.dili.trace.dto.TradeDetailInputWrapperDto;
import com.dili.trace.dto.TradeDto;
import com.dili.trace.dto.TradeRequestWrapperDto;
import com.dili.trace.enums.*;
import com.dili.trace.rpc.service.CustomerRpcService;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * tradeorder服务
 */
@Service
@Transactional
public class TradeOrderService extends BaseServiceImpl<TradeOrder, Long> {
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    TradeRequestService tradeRequestService;
    @Autowired
    ProductStockService productStockService;
    @Autowired
    BillService billService;

    /**
     * 创建TradeOrder
     *
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
     * create trade
     * @param tradeDto
     * @param batchStockInputList
     * @param tradeOrderStatusEnum
     */
    public void createTrade(TradeDto tradeDto, List<ProductStockInput> batchStockInputList, TradeOrderStatusEnum tradeOrderStatusEnum) {
        TradeOrder tradeOrder = this.createTradeOrder(tradeDto);

        List<TradeRequestWrapperDto> tradeRequestWrapperDtoList = StreamEx.ofNullable(batchStockInputList).flatCollection(Function.identity())
                .nonNull().map(productStockInput -> {
                    TradeRequest tradeRequest = this.createTradeRequest(tradeDto, productStockInput, tradeOrder, productStockInput.getTradeWeight());

                    List<TradeDetailInputWrapperDto> tradeDetailWrapperDtoList = this.buildTradeDetailWrapper(productStockInput.getTradeDetailInputList(), tradeRequest);

                    TradeRequestWrapperDto wdto = new TradeRequestWrapperDto();
                    wdto.setTradeRequest(tradeRequest);
                    wdto.setTradeDetailWrapperDtoList(tradeDetailWrapperDtoList);
                    return wdto;
                }).toList();
        List<TradeDetail> tradeDetailList = new ArrayList<>();
        for (TradeRequestWrapperDto wrapperDto : tradeRequestWrapperDtoList) {

            TradeRequest tradeRequest = wrapperDto.getTradeRequest();
            List<TradeDetailInputWrapperDto> tradeDetailWrapperDtoList = wrapperDto.getTradeDetailWrapperDtoList();

            for (TradeDetailInputWrapperDto tdwrapper : tradeDetailWrapperDtoList) {
                TradeDetail tradeDetail = tdwrapper.getTradeDetail();
                RegisterBill registerBill = this.billService.get(tradeDetail.getBillId());
                BigDecimal tradeWeight = tdwrapper.getRequestTradeWeight();
                TradeDetail buyerTradeDetail = this.createTradeDetail(tradeDto, registerBill, tradeRequest, tradeWeight);
                tradeDetailList.add(buyerTradeDetail);
            }
        }
        this.dealTradeOrder(tradeOrder.getTradeOrderId(), tradeOrderStatusEnum);

    }

    /**
     * deal trade order
     * @param tradeOrderId
     * @param tradeOrderStatusEnum
     */
    private void dealTradeOrder(Long tradeOrderId, TradeOrderStatusEnum tradeOrderStatusEnum) {

        if (TradeOrderStatusEnum.NONE == tradeOrderStatusEnum) {
            return;
        }
        TradeOrder tradeOrderItem = this.get(tradeOrderId);
        if (tradeOrderStatusEnum.equalsToCode(tradeOrderItem.getOrderStatus())) {
            return;
        }


        TradeOrder updatableTradeOrder = new TradeOrder();
        updatableTradeOrder.setId(tradeOrderItem.getId());
        updatableTradeOrder.setOrderStatus(tradeOrderStatusEnum.getCode());
        this.updateSelective(updatableTradeOrder);

        TradeRequest trQ = new TradeRequest();
        trQ.setTradeOrderId(tradeOrderId);
        List<Long> tradeRequestIdList = StreamEx.of(this.tradeRequestService.listByExample(trQ)).map(TradeRequest::getId).toList();

        TradeDetailQueryDto tdQ = new TradeDetailQueryDto();
        tdQ.setSaleStatus(SaleStatusEnum.NOT_FOR_SALE.getCode());
        tdQ.setTradeRequestIdList(tradeRequestIdList);

        StreamEx.of(this.tradeDetailService.listByExample(tdQ)).forEach(td -> {



            TradeDetail updatableTD = new TradeDetail();
            updatableTD.setId(td.getId());
            if (TradeOrderStatusEnum.FINISHED == tradeOrderStatusEnum) {
                updatableTD.setStockWeight(td.getSoftWeight());
                updatableTD.setSoftWeight(BigDecimal.ZERO);
                updatableTD.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
            } else if (TradeOrderStatusEnum.CANCELLED == tradeOrderStatusEnum) {
                updatableTD.setStockWeight(BigDecimal.ZERO);
                updatableTD.setSoftWeight(BigDecimal.ZERO);
                updatableTD.setSaleStatus(SaleStatusEnum.NOT_FOR_SALE.getCode());


                TradeDetail fromTD = new TradeDetail();
                fromTD.setParentId(td.getParentId());
                fromTD.setStockWeight(td.getSoftWeight());
                this.tradeDetailService.updateSelective(fromTD);
            }
            this.tradeDetailService.updateSelective(updatableTD);
        });


    }

    /**
     * sss
     *
     * @param batchStockInputList
     */
    public void ddd(List<ProductStockInput> batchStockInputList) {


    }

    /**
     * 创建TradeOrder
     *
     * @return
     */
    public TradeOrder createTradeOrder(TradeDto tradeDto) {
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setBuyerId(tradeDto.getBuyer().getBuyerId());
        tradeOrder.setBuyerName(tradeDto.getBuyer().getBuyerName());
        tradeOrder.setBuyerMarketId(tradeDto.getMarketId());

        tradeOrder.setSellerId(tradeDto.getSeller().getSellerId());
        tradeOrder.setSellerName(tradeDto.getSeller().getSellerName());
        tradeOrder.setSellerMarketId(tradeDto.getMarketId());
        tradeOrder.setOrderStatus(TradeOrderStatusEnum.NONE.getCode());
        tradeOrder.setOrderType(tradeDto.getTradeOrderType().getCode());
        this.insertSelective(tradeOrder);
        return tradeOrder;
    }

    /**
     * @param tradeDetailInputList
     */
    public List<TradeDetailInputWrapperDto> buildTradeDetailWrapper(List<TradeDetailInputDto> tradeDetailInputList, TradeRequest tradeRequest) {
        List<TradeDetailInputDto> detailInputList = StreamEx.ofNullable(tradeDetailInputList).flatCollection(Function.identity())
                .nonNull().toList();

        boolean invalidTradeWeight = StreamEx.of(detailInputList).anyMatch(trinput -> {
            return trinput.getTradeWeight() == null || BigDecimal.ZERO.compareTo(trinput.getTradeWeight()) >= 0;
        });
        if (invalidTradeWeight) {
            throw new TraceBizException("购买批次重量不能为空或小于0");
        }

        List<TradeDetailInputWrapperDto> tradeDetailInputWrapperDtoList = StreamEx.of(detailInputList).map(trinput -> {
            TradeDetail tradeDetail = this.tradeDetailService.get(trinput.getTradeDetailId());
            if (tradeDetail == null) {
                throw new TraceBizException("批次数据不存在");
            }
            if (tradeDetail.getStockWeight().compareTo(trinput.getTradeWeight()) < 0) {
                throw new TraceBizException("购买[" + tradeDetail.getProductName() + "]重量不能批次库存重量");
            }
            TradeDetailInputWrapperDto wrapperDto = new TradeDetailInputWrapperDto();
            wrapperDto.setRequestTradeWeight(trinput.getTradeWeight());
            wrapperDto.setTradeDetail(tradeDetail);
            return wrapperDto;

        }).toList();


        ProductStock productStockItem = this.productStockService.selectByIdForUpdate(tradeRequest.getProductStockId())
                .orElseThrow(() -> {
                    return new TraceBizException("操作库存失败");
                });

        BigDecimal totalTradeWeight = tradeRequest.getTradeWeight();

        if (productStockItem.getStockWeight().compareTo(totalTradeWeight) < 0) {
            throw new TraceBizException("购买重量不能超过总库存重量");
        }
        TradeDetailQueryDto tradeDetailQuery = new TradeDetailQueryDto();
        tradeDetailQuery.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
        tradeDetailQuery.setProductStockId(productStockItem.getId());
        tradeDetailQuery.setSort("created");
        tradeDetailQuery.setOrder("asc");
        tradeDetailQuery.setMinStockWeight(BigDecimal.ZERO);
        List<TradeDetail> tradeDetailList = this.tradeDetailService.listByExample(tradeDetailQuery);
        if (tradeDetailInputWrapperDtoList.isEmpty()) {
            for (TradeDetail td : tradeDetailList) {
                TradeDetailInputWrapperDto wrapperDto = new TradeDetailInputWrapperDto();
                wrapperDto.setTradeDetail(td);
                if (totalTradeWeight.compareTo(td.getSoftWeight()) >= 0) {
                    wrapperDto.setRequestTradeWeight(td.getStockWeight());
                    totalTradeWeight = totalTradeWeight.subtract(td.getStockWeight());
                } else {
                    wrapperDto.setRequestTradeWeight(totalTradeWeight);
                }
            }
        }
        return tradeDetailInputWrapperDtoList;
    }

    /**
     * createOrFind
     *
     * @return
     */
    private ProductStock createOrFindProductStock(RegisterBill registerBill, Long userId, String userName) {
        ProductStock psQuery = new ProductStock();
        psQuery.setUserId(userId);
        // query.setPreserveType(tradeDetailItem.getPreserveType());
        psQuery.setProductId(registerBill.getProductId());
        psQuery.setWeightUnit(registerBill.getWeightUnit());
        psQuery.setSpecName(registerBill.getSpecName());
        psQuery.setBrandId(registerBill.getBrandId());


        ProductStock batchStockItem = StreamEx.of(this.productStockService.listByExample(psQuery)).findFirst().orElseGet(() -> {
            //创建初始BatchStock
            ProductStock batchStock = new ProductStock();
            batchStock.setUserId(userId);
            batchStock.setUserName(userName);
            batchStock.setPreserveType(registerBill.getPreserveType());
            batchStock.setProductId(registerBill.getProductId());
            batchStock.setProductName(registerBill.getProductName());
            batchStock.setStockWeight(BigDecimal.ZERO);
            batchStock.setTotalWeight(BigDecimal.ZERO);
            batchStock.setWeightUnit(registerBill.getWeightUnit());
            batchStock.setSpecName(registerBill.getSpecName());
            batchStock.setBrandId(registerBill.getBrandId());
            batchStock.setBrandName(registerBill.getBrandName());
            batchStock.setTradeDetailNum(0);
            this.productStockService.insertSelective(batchStock);
            return batchStock;

        });
        return batchStockItem;
    }

    /**
     * 创建TradeRequest
     *
     * @param tradeDto
     * @param tradeOrderItem
     * @return
     */
    public TradeRequest createTradeRequest(TradeDto tradeDto, ProductStockInput input, TradeOrder tradeOrderItem, BigDecimal tradeWeight) {
        TradeDto.Seller seller = tradeDto.getSeller();
        TradeDto.Buyer buyer = tradeDto.getBuyer();


        if (input.getTradeWeight() == null || BigDecimal.ZERO.compareTo(input.getTradeWeight()) >= 0) {
            throw new TraceBizException("购买重量不能小于0");
        }
        if (input.getProductStockId() == null) {
            throw new TraceBizException("购买商品ID不能为空");
        }

        if (input.getTradeWeight() == null || input.getTradeWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TraceBizException("购买总重量不能为空或小于0");
        }

        ProductStock productStock = this.productStockService.get(input.getProductStockId());
        if (productStock == null) {
            throw new TraceBizException("购买商品不存在");
        }
        if (!seller.getSellerId().equals(productStock.getUserId())) {
            throw new TraceBizException("没有权限销售当前商品");
        }


        Date now = new Date();

        TradeRequest tradeRequest = new TradeRequest();

        tradeRequest.setAmount(BigDecimal.ZERO);
        tradeRequest.setBatchNo(null);

        tradeRequest.setSellerId(seller.getSellerId());
        tradeRequest.setSellerMarketId(tradeDto.getMarketId());
        tradeRequest.setSellerName(seller.getSellerName());
        tradeRequest.setSellerNo(null);
        tradeRequest.setCode(null);
        tradeRequest.setCreated(now);
        tradeRequest.setModified(now);
        tradeRequest.setNumber(0);
        tradeRequest.setPackageNumber(0);
        tradeRequest.setWeightUnit(productStock.getWeightUnit());
        tradeRequest.setOrderDate(now);

        tradeRequest.setTradeWeight(tradeWeight);
        tradeRequest.setProductStockId(productStock.getProductStockId());
//        tradeRequest.setOriginName(batchStock.getOriginName());
        tradeRequest.setProductName(productStock.getProductName());
        tradeRequest.setTradeOrderId(tradeOrderItem.getTradeOrderId());

        tradeRequest.setBuyerId(buyer.getBuyerId());
        tradeRequest.setBuyerMarketId(tradeDto.getMarketId());
        tradeRequest.setBuyerName(buyer.getBuyerName());
        tradeRequest.setBuyerNo(null);

        tradeRequest.setReturnStatus(TradeReturnStatusEnum.NONE.getCode());

        this.tradeRequestService.insertSelective(tradeRequest);

        return tradeRequest;
    }

    /**
     * 创建交易详情
     *
     * @param tradeDto
     * @param registerBillItem
     * @param tradeRequest
     * @return
     */
    public TradeDetail createTradeDetail(TradeDto tradeDto, RegisterBill registerBillItem, TradeRequest tradeRequest, BigDecimal tradeWeight) {
        TradeDto.Seller seller = tradeDto.getSeller();
        TradeDto.Buyer buyer = tradeDto.getBuyer();
        TradeDetail tradeDetail = new TradeDetail();
        tradeDetail.setSoftWeight(tradeWeight);
        tradeDetail.setStockWeight(BigDecimal.ZERO);
        tradeDetail.setTotalWeight(BigDecimal.ZERO);
        tradeDetail.setBuyerType(buyer.getBuyerType().getCode());
        tradeDetail.setBuyerId(buyer.getBuyerId());
        tradeDetail.setBuyerName(buyer.getBuyerName());

        tradeDetail.setSellerId(seller.getSellerId());
        tradeDetail.setSellerName(seller.getSellerName());
        tradeDetail.setCreated(new Date());
        tradeDetail.setModified(new Date());
        tradeDetail.setWeightUnit(registerBillItem.getWeightUnit());
        tradeDetail.setBillId(registerBillItem.getBillId());
        tradeDetail.setCheckinStatus(CheckinStatusEnum.NONE.getCode());
        tradeDetail.setCheckoutStatus(CheckoutStatusEnum.NONE.getCode());
        tradeDetail.setProductName(registerBillItem.getProductName());
        tradeDetail.setProductStockId(tradeRequest.getProductStockId());
        tradeDetail.setTradeRequestId(tradeRequest.getTradeRequestId());
        tradeDetail.setTradeType(TradeTypeEnum.NONE.getCode());
        tradeDetail.setSaleStatus(SaleStatusEnum.NONE.getCode());
        this.tradeDetailService.insertSelective(tradeDetail);
        return tradeDetail;
    }

    /**
     * 处理tradeorder
     *
     * @param tradeOrderItem
     * @param tradeStatusEnum
     */

    public void handleTradeOrder(TradeOrder tradeOrderItem, TradeOrderStatusEnum tradeStatusEnum) {
        tradeOrderItem.setOrderStatus(tradeStatusEnum.getCode());
        this.updateSelective(tradeOrderItem);
    }

}