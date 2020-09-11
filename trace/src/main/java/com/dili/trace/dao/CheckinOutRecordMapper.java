package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.api.input.TradeReportDto;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.dto.*;
import com.dili.trace.dto.thirdparty.report.ReportCheckInDto;

import java.util.List;
import java.util.Map;

public interface CheckinOutRecordMapper extends MyMapper<CheckinOutRecord> {

    public List<BillReportDto> queryBillReport(BillReportQueryDto query);

    public List<TraceReportDto> groupCountCommonBillByColor(TraceReportQueryDto query);

    public List<TraceReportDto> groupCountSupplementBillByColor(TraceReportQueryDto query);

    public List<ReportCheckInDto> selectCheckInReport(RegisterBillDto query);

    List<TradeReportDto> getUserBillReport(Map<String, Object> map);

    List<TradeReportDto> getUserBuyerTradeReport(Map<String, Object> map);

    List<TradeReportDto> getUserSellerTradeReport(Map<String, Object> map);
}