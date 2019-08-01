package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.TradeType;

import java.util.List;

public interface TradeTypeMapper extends MyMapper<TradeType> {
    List<TradeType> findAll();
}