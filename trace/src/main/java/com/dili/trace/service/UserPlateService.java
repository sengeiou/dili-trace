package com.dili.trace.service;

import java.util.List;
import java.util.Map;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-10-11 10:05:11.
 */
public interface UserPlateService extends BaseService<UserPlate, Long> {
	
	/**
	 * 根据userId查询UserPlate集合
	 * @param userId
	 * @return
	 */
	public List<UserPlate> findUserPlateByUserId(Long userId);

	/**
	 * 根据plateList查询所有的UserPlate
	 * @param plateList
	 * @return
	 */
	public List<UserPlate> findUserPlateByPlates(List<String> plateList);
	
	/**
	 * 根据userId删除所有UserPlate,并插入plateList
	 * @param userId
	 * @param plateList
	 * @return
	 */
	public int deleteAndInsertUserPlate(Long userId,List<String>plateList);

		/**
	 * 根据userId删除所有UserPlate,并插入plateList
	 * @param userId
	 * @return
	 */
	public int checkAndInsertUserPlate(Long userId,String plate);
	
	/**
	 * 根据user集合查询Map<userid,List<UserPlate>>
	 * @return
	 */
	public Map<Long,List<UserPlate>> findUserPlateByUserIdList(List<Long>userIdList);
	
	/**
	 * 根据tallyAreaNo查询UserPlate集合
	 * @return
	 */
	public List<UserPlate> findUserPlateByTallyAreaNo(String tallyAreaNo);
}