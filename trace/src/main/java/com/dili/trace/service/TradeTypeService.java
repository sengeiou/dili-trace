package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.TradeType;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-31 14:56:14.
 */
public interface TradeTypeService extends BaseService<TradeType, Long> {
    List<TradeType> findAll();
}