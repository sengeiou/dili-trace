package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.SeparateSalesRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
public interface SeparateSalesRecordService extends BaseService<SeparateSalesRecord, Long> {
    List<SeparateSalesRecord> findByRegisterBillCode(Long registerBillCode);
}