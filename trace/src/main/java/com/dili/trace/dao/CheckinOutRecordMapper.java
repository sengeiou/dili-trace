package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.dto.BillReportQueryDto;
import com.dili.trace.dto.UserLoginHistoryDto;

public interface CheckinOutRecordMapper extends MyMapper<CheckinOutRecord> {

    public List<UserLoginHistoryDto> queryBillReport(BillReportQueryDto query);
}