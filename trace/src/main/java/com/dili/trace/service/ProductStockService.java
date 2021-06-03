package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.api.input.TradeDetailQueryDto;
import com.dili.trace.dao.ProductStockMapper;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.SaleStatusEnum;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.google.common.collect.Lists;

import com.google.common.collect.Maps;
import one.util.streamex.EntryStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

/**
 * 库存service
 */
@Service
public class ProductStockService extends BaseServiceImpl<ProductStock, Long> {
    private static final Logger logger = LoggerFactory.getLogger(ProductStockService.class);
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    CustomerRpcService customerRpcService;
    @Autowired
    DetectRequestService detectRequestService;

    /**
     * 查询 锁定数据行
     *
     * @param id
     * @return
     */
    @Transactional
    public Optional<ProductStock> selectByIdForUpdate(Long id) {
        return ((ProductStockMapper) this.getDao()).selectByIdForUpdate(id);
    }

    /**
     * 根据id查找 数据
     *
     * @param idList
     * @return
     */
    public List<ProductStock> findByIdList(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return Lists.newArrayList();
        }
        Example e = new Example(ProductStock.class);
        e.and().andIn("id", idList);
        return this.getDao().selectByExample(e);
    }

    /**
     * 查询或创建
     *
     * @param buyerId
     * @param billItem
     * @return
     */
    @Transactional
    public ProductStock findOrCreateBatchStock(Long buyerId, RegisterBill billItem) {
        Long marketId = billItem.getMarketId();
        ProductStock query = new ProductStock();
        query.setUserId(buyerId);
        query.setMarketId(marketId);
        // query.setPreserveType(tradeDetailItem.getPreserveType());
        query.setProductId(billItem.getProductId());
        query.setWeightUnit(billItem.getWeightUnit());
        query.setSpecName(billItem.getSpecName());
        query.setBrandId(billItem.getBrandId());
        ProductStock batchStockItem = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {
            // 创建初始BatchStock
            CustomerExtendDto cust = this.customerRpcService.findCustomerByIdOrEx(buyerId, marketId);
            return this.registerBillService.selectByIdForUpdate(billItem.getId()).map(bill -> {
                ProductStock batchStock = new ProductStock();
                batchStock.setUserId(cust.getId());
                batchStock.setUserName(cust.getName());
                batchStock.setMarketId(marketId);
                batchStock.setPreserveType(bill.getPreserveType());
                batchStock.setProductId(bill.getProductId());
                batchStock.setProductName(bill.getProductName());
                batchStock.setStockWeight(BigDecimal.ZERO);
                batchStock.setTotalWeight(BigDecimal.ZERO);
                batchStock.setWeightUnit(bill.getWeightUnit());
                batchStock.setSpecName(bill.getSpecName());
                batchStock.setBrandId(bill.getBrandId());
                batchStock.setBrandName(bill.getBrandName());
                batchStock.setTradeDetailNum(0);
                this.insertSelective(batchStock);
                return batchStock;
            }).orElseThrow(() -> {
                return new TraceBizException("创建库存失败");
            });

        });
        return batchStockItem;
    }

    /**
     * 返还重量(上架,退货,交易取消)
     *
     * @param tradeDetailId
     * @param returnedWeight
     * @return
     */
    @Transactional
    public int returnWeight(Long tradeDetailId, BigDecimal returnedWeight) {
        if (Objects.isNull(tradeDetailId)) {
            throw new TraceBizException("参数错误");
        }
        TradeDetail tradeDetailItem = this.tradeDetailService.get(tradeDetailId);
        if (Objects.isNull(tradeDetailItem)) {
            throw new TraceBizException("批次库存数据不存在");
        }

        Long productStockId = tradeDetailItem.getProductStockId();

        ProductStock productStockItem = this.selectByIdForUpdate(productStockId).orElseThrow(() -> {
            throw new TraceBizException("库存数据不存在");
        });


        DetectResultEnum detectResultEnum = StreamEx.of(this.detectRequestService.findDetectRequestByBillId(tradeDetailItem.getBillId())).map(detectRequest -> {
            return detectRequest.getDetectResult();
        }).map(DetectResultEnum::fromCode).filter(Optional::isPresent).map(Optional::get).findFirst().orElse(DetectResultEnum.NONE);

        ProductStock psUpdatable = new ProductStock();
        psUpdatable.setId(productStockItem.getId());

        if (DetectResultEnum.PASSED == detectResultEnum) {
            psUpdatable.setStockWeight(productStockItem.getStockWeight().add(returnedWeight));
        } else if (DetectResultEnum.FAILED == detectResultEnum) {
            psUpdatable.setDetectFailedWeight(productStockItem.getDetectFailedWeight().add(returnedWeight));
        } else if (DetectResultEnum.NONE == detectResultEnum) {
            psUpdatable.setStockWeight(productStockItem.getStockWeight().add(returnedWeight));
        } else {
            throw new TraceBizException("检测结果错误");
        }
        return this.updateSelective(psUpdatable);
    }

    /**
     * 更新检测结果
     *
     * @param billId
     * @return
     */
    public int updateProductStockAndTradeDetailAfterDetect(Long billId) {


        DetectResultEnum detectResultEnum = StreamEx.of(this.detectRequestService.findDetectRequestByBillId(billId)).map(detectRequest -> {
            return detectRequest.getDetectResult();
        }).map(DetectResultEnum::fromCode).filter(Optional::isPresent).map(Optional::get).findFirst().orElse(DetectResultEnum.NONE);

        if (detectResultEnum == null) {
            return 0;
        }
        if (DetectResultEnum.NONE == detectResultEnum) {
            return 0;
        }
        RegisterBill billItem = this.registerBillService.getAndCheckById(billId).orElseThrow(() -> {
            return new TraceBizException("报备单不存在");
        });

        TradeDetail tdq = new TradeDetail();
        tdq.setBillId(billId);
        tdq.setDetectResult(detectResultEnum.getCode());
        Map<Long, List<TradeDetail>> productIdTradeDetailListMap = StreamEx.of(StreamEx.of(this.tradeDetailService.listByExample(tdq))).groupingBy(TradeDetail::getProductStockId);

        return EntryStream.of(productIdTradeDetailListMap).mapKeyValue((productStockId, tradeDetailList) -> {
            int rowCount = StreamEx.of((tradeDetailList)).mapToInt(tradeDetail -> {
                TradeDetail upTD = new TradeDetail();
                upTD.setDetectResult(detectResultEnum.getCode());
                upTD.setId(tradeDetail.getId());
                if (DetectResultEnum.FAILED == detectResultEnum) {
                    upTD.setSaleStatus(SaleStatusEnum.NOT_FOR_SALE.getCode());
                } else {
                    upTD.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
                }
                return this.tradeDetailService.updateSelective(upTD);
            }).sum();

            this.updateProductStock(productStockId);

            return rowCount;
        }).mapToInt(Integer::intValue).sum();
    }


    /**
     * 根据相关的tradedetail数据,更新库存productstock相关数据
     *
     * @param productStockId
     * @return
     */
    public int updateProductStock(Long productStockId) {
        if (productStockId == null) {
            return 0;
        }
        ProductStock ps = this.selectByIdForUpdate(productStockId).orElseThrow(() -> {
            return new TraceBizException("库存信息不存在");
        });


        Map<Boolean, TradeDetail> mappedSumData = StreamEx.of(this.tradeDetailService.groupSumWeightByProductStockId(productStockId)).mapToEntry(item -> {
            return !DetectResultEnum.FAILED.equalsToCode(item.getDetectResult());
        }, Function.identity()).mapValues(list -> {
            TradeDetail identity = new TradeDetail();
            identity.setStockWeight(BigDecimal.ZERO);
            identity.setTotalWeight(BigDecimal.ZERO);
            identity.setSoftWeight(BigDecimal.ZERO);
            return StreamEx.of(list).reduce(identity, (t, v) -> {
                t.setStockWeight(t.getStockWeight().add(v.getStockWeight()));
                t.setSoftWeight(t.getSoftWeight().add(v.getSoftWeight()));
                t.setTotalWeight(t.getTotalWeight().add(v.getTotalWeight()));
                return t;
            });
        }).toMap();

        TradeDetail succeed = mappedSumData.get(true);
        TradeDetail failed = mappedSumData.get(false);


        BigDecimal softWeight = StreamEx.of(succeed, failed).nonNull().map(TradeDetail::getSoftWeight).reduce(BigDecimal.ZERO, BigDecimal::add);


        TradeDetailQueryDto tdq = new TradeDetailQueryDto();
        tdq.setProductStockId(ps.getProductStockId());
        tdq.setMinStockWeight(BigDecimal.ZERO);
        tdq.setSaleStatus(SaleStatusEnum.FOR_SALE.getCode());
        int tradenum = this.tradeDetailService.listByExample(tdq).size();

        ProductStock up = new ProductStock();
        up.setId(ps.getId());
        up.setTradeDetailNum(tradenum);
        up.setStockWeight(succeed == null ? BigDecimal.ZERO : succeed.getStockWeight());
        up.setDetectFailedWeight(failed == null ? BigDecimal.ZERO : failed.getTotalWeight());
        up.setSoftWeight(softWeight);
        return this.updateSelective(up);
    }
}
