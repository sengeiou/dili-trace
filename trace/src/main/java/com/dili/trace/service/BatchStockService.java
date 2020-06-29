package com.dili.trace.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.BatchStock;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradeDetail;
import com.dili.trace.domain.User;
import com.dili.trace.enums.SaleStatusEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.util.streamex.StreamEx;

@Service
public class BatchStockService extends BaseServiceImpl<BatchStock, Long> {
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    UserService userService;

    private Optional<Long> addBatchStock(TradeDetail tradeDetail) {
        Long userId = tradeDetail.getBuyerId();
        RegisterBill billItem = this.registerBillService.get(tradeDetail.getBillId());

        BatchStock query = new BatchStock();
        query.setUserId(userId);
        query.setPreserveType(billItem.getPreserveType());
        query.setProductId(billItem.getProductId());
        query.setWeightUnit(billItem.getWeightUnit());
        query.setSpecName(billItem.getSpecName());
        BatchStock batchStockItem = StreamEx.of(this.listByExample(query)).findFirst().orElseGet(() -> {
            // 创建初始BatchStock
            User userItem = this.userService.get(userId);

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
            this.insertSelective(batchStock);
            return batchStock;
        });
        // 更新重量
        BatchStock batchStock = new BatchStock();
        batchStock.setId(batchStockItem.getId());
        batchStock.setStockWeight(batchStockItem.getStockWeight().add(tradeDetail.getStockWeight()));
        batchStock.setTotalWeight(batchStockItem.getTotalWeight().add(tradeDetail.getTotalWeight()));
        this.updateSelective(batchStock);
        return Optional.of(batchStock.getId());

    }

    private Optional<Long> substractBatchStock(TradeDetail tradeDetail) {
        Long userId = tradeDetail.getBuyerId();
        RegisterBill billItem = this.registerBillService.get(tradeDetail.getBillId());

        BatchStock query = new BatchStock();
        query.setUserId(userId);
        query.setPreserveType(billItem.getPreserveType());
        query.setProductId(billItem.getProductId());

        BatchStock batchStockItem = StreamEx.of(this.listByExample(query)).findFirst().orElse(null);
        if (batchStockItem == null) {
            return Optional.empty();
        }
        // 更新重量
        BatchStock batchStock = new BatchStock();
        batchStock.setId(batchStockItem.getId());
        batchStock.setStockWeight(batchStockItem.getStockWeight().subtract(tradeDetail.getStockWeight()));
        batchStock.setTotalWeight(batchStockItem.getTotalWeight().subtract(tradeDetail.getTotalWeight()));
        this.updateSelective(batchStock);
        return Optional.of(batchStock.getId());
    }

    public Optional<Long> createOrUpdateBatchStock(TradeDetail tradeDetail) {
		if(tradeDetail.getBatchStockId()==null){
			if(SaleStatusEnum.FOR_SALE.equalsToCode(tradeDetail.getSaleStatus())){
				return this.addBatchStock(tradeDetail);
			}
		}else{
			if(SaleStatusEnum.NOT_FOR_SALE.equalsToCode(tradeDetail.getSaleStatus())){
                return this.substractBatchStock(tradeDetail);
			}
		}
        return Optional.empty();
    }
}
