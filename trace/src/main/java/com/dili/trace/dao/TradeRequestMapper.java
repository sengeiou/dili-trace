package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.input.TradeRequestInputDto;
import com.dili.trace.domain.TradeRequest;

import java.util.List;

public interface TradeRequestMapper extends MyMapper<TradeRequest> {

   public List<TradeRequest> queryListByOrderStatus(TradeRequestInputDto dto);
}