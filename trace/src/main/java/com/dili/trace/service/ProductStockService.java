package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.ProductStockMapper;
import com.dili.trace.domain.ProductStock;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

@Service
public class ProductStockService extends BaseServiceImpl<ProductStock, Long> {
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    UserService userService;

    @Transactional
    public Optional<ProductStock> selectByIdForUpdate(Long id) {
        return ((ProductStockMapper) this.getDao()).selectByIdForUpdate(id);
    }

    public List<ProductStock> findByIdList(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return Lists.newArrayList();
        }
        Example e = new Example(ProductStock.class);
        e.and().andIn("id", idList);
        return this.getDao().selectByExample(e);
    }

    @Transactional
    public ProductStock findOrCreateBatchStock(Long buyerId, RegisterBill billItem) {

        ProductStock query = new ProductStock();
        query.setUserId(buyerId);
        // query.setPreserveType(tradeDetailItem.getPreserveType());
        query.setProductId(billItem.getProductId());
        query.setWeightUnit(billItem.getWeightUnit());
        query.setSpecName(billItem.getSpecName());
        query.setBrandId(billItem.getBrandId());
        ProductStock batchStockItem = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {
            // 创建初始BatchStock
            User userItem = this.userService.get(buyerId);
            return this.registerBillService.selectByIdForUpdate(billItem.getId()).map(bill -> {
                ProductStock batchStock = new ProductStock();
                batchStock.setUserId(userItem.getId());
                batchStock.setUserName(userItem.getName());
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
}
