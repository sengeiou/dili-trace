package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.User;
import com.dili.trace.enums.SaleStatusEnum;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;
import tk.mybatis.mapper.entity.Example;

@Service
public class BatchStockService extends BaseServiceImpl<BatchStock, Long> {
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    TradeDetailService tradeDetailService;
    @Autowired
    UserService userService;

    public List<BatchStock> findByIdList(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return Lists.newArrayList();
        }
        Example e = new Example(BatchStock.class);
        e.and().andIn("id", idList);
        return this.getDao().selectByExample(e);
    }


    public BatchStock findOrCreateBatchStock(Long buyerId, RegisterBill billItem) {

        BatchStock query = new BatchStock();
        query.setUserId(buyerId);
        // query.setPreserveType(tradeDetailItem.getPreserveType());
        query.setProductId(billItem.getProductId());
        query.setWeightUnit(billItem.getWeightUnit());
        query.setSpecName(billItem.getSpecName());
        query.setBrandId(billItem.getBrandId());
        BatchStock batchStockItem = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {
            // 创建初始BatchStock
            User userItem = this.userService.get(buyerId);

            BatchStock batchStock = new BatchStock();
            batchStock.setUserId(userItem.getId());
            batchStock.setUserName(userItem.getName());
            batchStock.setPreserveType(billItem.getPreserveType());
            batchStock.setProductId(billItem.getProductId());
            batchStock.setProductName(billItem.getProductName());
            batchStock.setStockWeight(BigDecimal.ZERO);
            batchStock.setTotalWeight(BigDecimal.ZERO);
            batchStock.setWeightUnit(billItem.getWeightUnit());
            batchStock.setSpecName(billItem.getSpecName());
            batchStock.setBrandId(billItem.getBrandId());
            batchStock.setTradeDetailNum(0);
            this.insertSelective(batchStock);
            return batchStock;
        });
        return batchStockItem;
    }
}
