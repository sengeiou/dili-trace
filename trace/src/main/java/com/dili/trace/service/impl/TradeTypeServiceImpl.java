package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.datasource.SwitchDataSource;
import com.dili.trace.dao.TradeTypeMapper;
import com.dili.trace.domain.TradeType;
import com.dili.trace.service.TradeTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-31 14:56:14.
 */
@Service
public class TradeTypeServiceImpl extends BaseServiceImpl<TradeType, Long> implements TradeTypeService {

    public TradeTypeMapper getActualDao() {
        return (TradeTypeMapper)getDao();
    }

    @SwitchDataSource("etradeDS")
    @Override
    public List<TradeType> findAll() {
        return getActualDao().findAll();
    }
}