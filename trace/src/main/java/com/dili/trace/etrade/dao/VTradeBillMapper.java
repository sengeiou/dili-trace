package com.dili.trace.etrade.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.etrade.domain.VTradeBill;
import com.dili.trace.etrade.domain.dto.VTradeBillQueryDTO;

public interface VTradeBillMapper extends MyMapper<VTradeBill> {
	public List<VTradeBill> selectTopRemoteData(VTradeBillQueryDTO dto);

	/**
	 * 基于maxBillId查找增量数据
	 * 
	 * @param maxBillId
	 * @return
	 */
	public VTradeBill selectRemoteLatestData();
	/**
	 * 判断相同orderid的数据有几条
	 * @param dto
	 * @return
	 */
	
	public Long selectRemoteRepeatData(VTradeBillQueryDTO dto);

}
