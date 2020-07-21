package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.dto.BillReportDto;
import com.dili.trace.dto.BillReportQueryDto;
import com.dili.trace.dto.TraceReportDto;
import com.dili.trace.dto.TraceReportQueryDto;

public interface CheckinOutRecordMapper extends MyMapper<CheckinOutRecord> {

    public List<BillReportDto> queryBillReport(BillReportQueryDto query);

    public List<TraceReportDto> groupCountCommonBillByColor(TraceReportQueryDto query);

    public List<TraceReportDto> groupCountSupplementBillByColor(TraceReportQueryDto query);
}