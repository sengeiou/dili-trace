package com.dili.trace.service;

import java.util.List;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.UsualAddress;
import com.dili.trace.glossary.UsualAddressTypeEnum;

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
}
