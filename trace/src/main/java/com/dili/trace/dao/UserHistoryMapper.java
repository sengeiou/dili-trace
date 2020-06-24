package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.UserHistory;
import com.dili.trace.dto.UserHistoryListDto;
import com.dili.trace.dto.UserHistoryStaticsDto;

public interface UserHistoryMapper extends MyMapper<UserHistory> {
	public UserHistoryStaticsDto queryUserHistoryStatics(UserHistoryListDto dto);
	/**
	 * 查询历史数据
	 * @param dto
	 * @return
	 */
	public List<UserHistory>queryUserHistory(UserHistoryListDto dto);
	/**
	 * 查询历史数据总量
	 * @param dto
	 * @return
	 */
	public Long queryUserHistoryCount(UserHistoryListDto dto);
}