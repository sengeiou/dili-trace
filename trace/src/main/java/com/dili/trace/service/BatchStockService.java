package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.BatchStockMapper;
import com.dili.trace.domain.ProductStore;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

@Service
public class BatchStockService extends BaseServiceImpl<ProductStore, Long> {
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    UserService userService;

    @Transactional
    public Optional<ProductStore> selectByIdForUpdate(Long id) {
        return ((BatchStockMapper) this.getDao()).selectByIdForUpdate(id);
    }

    public List<ProductStore> findByIdList(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return Lists.newArrayList();
        }
        Example e = new Example(ProductStore.class);
        e.and().andIn("id", idList);
        return this.getDao().selectByExample(e);
    }

    @Transactional
    public ProductStore findOrCreateBatchStock(Long buyerId, RegisterBill billItem) {

        ProductStore query = new ProductStore();
        query.setUserId(buyerId);
        // query.setPreserveType(tradeDetailItem.getPreserveType());
        query.setProductId(billItem.getProductId());
        query.setWeightUnit(billItem.getWeightUnit());
        query.setSpecName(billItem.getSpecName());
        query.setBrandId(billItem.getBrandId());
        ProductStore batchStockItem = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {
            // 创建初始BatchStock
            User userItem = this.userService.get(buyerId);
            return this.registerBillService.selectByIdForUpdate(billItem.getId()).map(bill -> {
                ProductStore batchStock = new ProductStore();
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
                return new TraceBusinessException("创建库存失败");
            });

        });
        return batchStockItem;
    }
}
