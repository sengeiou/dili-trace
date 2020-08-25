package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.input.TradeRequestInputDto;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.dto.UserListDto;

import java.util.List;

public interface TradeRequestMapper extends MyMapper<TradeRequest> {

   public List<TradeRequest> queryListByOrderStatus(TradeRequestInputDto dto);

   /**
    * 查询近7天有买商品的用户
    * @param user
    * @return
    */
   List<Long> selectBuyerIdWithouTradeRequest(UserListDto user);

}