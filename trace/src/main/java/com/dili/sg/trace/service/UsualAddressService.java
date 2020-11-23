package com.dili.sg.trace.service;

import java.util.List;

import com.dili.sg.trace.glossary.UsualAddressTypeEnum;
import com.dili.ss.base.BaseService;
import com.dili.sg.trace.domain.UsualAddress;

public interface UsualAddressService extends BaseService<UsualAddress, Long> {
	/**
	 * 插入常用地址
	 * 
	 * @param input
	 * @return
	 */
	public int insertUsualAddress(UsualAddress input);

	/**
	 * 更新常用地址
	 * 
	 * @param input
	 * @return
	 */
	public int updateUsualAddress(UsualAddress input);

	/**
	 * 通过id删除常用地址
	 * 
	 * @param id
	 * @return
	 */

	public int deleteUsualAddress(Long id);

	/**
	 * 通过类型获得地址列表
	 * 
	 * @param usualAddressType
	 * @return
	 */
	public List<UsualAddress> findUsualAddressByType(UsualAddressTypeEnum usualAddressType);
	

	
	/**
	 * 增加地址当天使用统计
	 * @param id
	 * @return
	 */
	public int increaseUsualAddressTodayCount(UsualAddressTypeEnum type,Long id);
	
	
	/**
	 * 增加地址当天使用统计（若新地址id与原地址id不相同，则对新地址使用统计增加1）
	 * @param oldId 原地址id
	 * @param newId 新地址id
	 * @return
	 */

	public int increaseUsualAddressTodayCount(UsualAddressTypeEnum type,Long oldId,Long newId);
}
