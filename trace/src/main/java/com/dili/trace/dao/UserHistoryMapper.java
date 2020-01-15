package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.UserHistory;
import com.dili.trace.dto.UserHistoryListDto;
import com.dili.trace.dto.UserHistoryStaticsDto;

public interface UserHistoryMapper extends MyMapper<UserHistory> {
	public UserHistoryStaticsDto queryUserHistoryStatics(UserHistoryListDto dto);
}