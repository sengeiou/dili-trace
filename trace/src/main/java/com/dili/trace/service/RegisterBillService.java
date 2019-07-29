package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.RegisterBill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:34.
 */
public interface RegisterBillService extends BaseService<RegisterBill, Long> {
    List<RegisterBill> findByExeMachineNo(String exeMachineNo,int pageSize);
    List<RegisterBill> findByProductName( String productName);
    RegisterBill findByCode(Long code);
    RegisterBill findByTradeNo(Long tradeNo);
}