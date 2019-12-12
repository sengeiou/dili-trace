package com.dili.trace.service;

import java.util.List;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.UserPlate;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-10-11 10:05:11.
 */
public interface UserPlateService extends BaseService<UserPlate, Long> {
	public List<UserPlate> findUserPlateByUserId(Long userId);

	public List<UserPlate> findUserPlateByPlates(List<String> plateList);
	
	
	public int deleteAndInsertUserPlate(Long userId,List<String>plateList);
}