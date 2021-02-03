package com.dili.trace.dao;

import org.apache.ibatis.annotations.Param;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.UserQrHistory;
import com.dili.trace.dto.query.UserQrHistoryQueryDto;

public interface UserQrHistoryMapper extends MyMapper<UserQrHistory> {
	public int updateQrStatusByQrHistory(UserQrHistoryQueryDto historyQueryDto);

}