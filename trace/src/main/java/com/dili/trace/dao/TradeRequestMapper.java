package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.input.TradeRequestInputDto;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.dto.PushDataQueryDto;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.dto.thirdparty.report.ReportDeliveryOrderDto;
import com.dili.trace.dto.thirdparty.report.ReportOrderDetailDto;
import com.dili.trace.dto.thirdparty.report.ReportScanCodeOrderDto;

import java.util.List;

public interface TradeRequestMapper extends MyMapper<TradeRequest> {

   public List<TradeRequest> queryListByOrderStatus(TradeRequestInputDto dto);

   /**
    * 查询近7天有买商品的用户
    * @param user
    * @return
    */
   List<Long> selectBuyerIdWithouTradeRequest(UserListDto user);

   public List<ReportScanCodeOrderDto> selectScanOrderReport(PushDataQueryDto dto);

   public List<ReportDeliveryOrderDto> selectDeliveryOrderReport(PushDataQueryDto dto);

   public List<ReportOrderDetailDto> selectOrderDetailReport(List<String> ids);
}