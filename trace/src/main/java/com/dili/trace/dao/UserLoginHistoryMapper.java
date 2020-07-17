package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.UserLoginHistory;
import com.dili.trace.dto.BillReportDto;
import com.dili.trace.dto.UserLoginHistoryQueryDto;

public interface UserLoginHistoryMapper extends MyMapper<UserLoginHistory> {
    List<BillReportDto> queryUserLoginHistory(UserLoginHistoryQueryDto query);
}