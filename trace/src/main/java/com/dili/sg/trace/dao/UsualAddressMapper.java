package com.dili.sg.trace.dao;

import java.util.List;

import com.dili.sg.trace.domain.UsualAddress;
import com.dili.ss.base.MyMapper;

public interface UsualAddressMapper extends MyMapper<UsualAddress> {
	/**
	 * 通过地址类型查询常用地址
	 * 
	 * @param input
	 * @return
	 */
	public List<UsualAddress> findUsualAddressByType(UsualAddress input);

	
	/**
	 * 清理并更新统计数据
	 * 
	 * @param now
	 * @return
	 */

	public Integer checkAndUpdateCountData(UsualAddress input);
}