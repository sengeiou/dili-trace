package com.dili.sg.trace.dao;

import java.util.List;

import com.dili.sg.trace.domain.UserHistory;
import com.dili.sg.trace.dto.UserHistoryListDto;
import com.dili.sg.trace.dto.UserHistoryStaticsDto;
import com.dili.ss.base.MyMapper;

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