package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.QualityTraceTradeBillSyncPoint;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface QualityTraceTradeBillSyncPointService extends BaseService<QualityTraceTradeBillSyncPoint, Long> {


	/**
	 * 同步数据
	 * 
	 * @return
	 */
	public  List<QualityTraceTradeBill> syncData(QualityTraceTradeBillSyncPoint pointItem, List<QualityTraceTradeBill> billList);


	/**
	 * 锁定同步点
	 * 
	 * @param id
	 * @return
	 */
	public QualityTraceTradeBillSyncPoint selectByIdForUpdate(Long id);
}