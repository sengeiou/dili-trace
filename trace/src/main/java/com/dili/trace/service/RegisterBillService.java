package com.dili.trace.service;

import java.util.List;
import java.util.Map;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.RegisterBillStaticsDto;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:34.
 */
public interface RegisterBillService extends BaseService<RegisterBill, Long> {
    List<RegisterBill> findByExeMachineNo(String exeMachineNo,int taskCount);
    List<RegisterBill> findByProductName( String productName);
    RegisterBill findByCode(String code);
    RegisterBillOutputDto findByTradeNo(String tradeNo);
    BaseOutput createRegisterBill(RegisterBill registerBill);
    int auditRegisterBill(Long id,Boolean pass);
    int undoRegisterBill(Long id);
    int autoCheckRegisterBill(Long id);
    int samplingCheckRegisterBill(Long id);
    int reviewCheckRegisterBill(Long id);
    public RegisterBillOutputDto findAndBind(String tradeNo);
    /**
     * 根据状态统计数据
     * @param dto
     * @return
     */
    public RegisterBillStaticsDto groupByState(RegisterBillDto dto);
    public RegisterBillOutputDto conversionDetailOutput(RegisterBill registerBill);
}