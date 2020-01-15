package com.dili.trace.service;

import java.util.List;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserHistory;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.domain.UserTallyArea;

public interface UserHistoryService extends BaseService<UserHistory, Long> {
	/**
	 * 保存用户历史数据
	 * 
	 * @param user
	 * @return
	 */
	public int insertUserHistory(User user);

	/**
	 * 保存用户历史数据
	 * 
	 * @param userId
	 * @return
	 */

	public int insertUserHistory(Long userId);

	/**
	 * 保存用户历史数据
	 * 
	 * @param user
	 * @param userPlateList
	 * @param tallyAreaList
	 * @return
	 */
	public int insertUserHistory(User user, List<UserPlate> userPlateList, List<UserTallyArea> tallyAreaList);
}
