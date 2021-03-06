package com.dili.trace.service;

import java.util.List;

import com.dili.ss.base.BaseService;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.api.output.SeparateSalesApiListOutput;
import com.dili.trace.api.output.SeparateSalesApiListQueryInput;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface SeparateSalesRecordService extends BaseService<SeparateSalesRecord, Long> {

    List<SeparateSalesRecord> findByRegisterBillCode(String registerBillCode);

    Integer alreadySeparateSalesWeight(String registerBillCode);

    Integer getAlreadySeparateSalesWeightByTradeNo(String tradeNo);

    public List<SeparateSalesApiListOutput> listByQueryInput(SeparateSalesApiListQueryInput queryInput);

    public EasyuiPageOutput listPageByQueryInput(SeparateSalesApiListQueryInput queryInput) throws Exception;

    
    public int deleteSeparateSalesRecordByBillId(Long billId);

}
