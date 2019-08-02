package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.QualityTraceTradeBillSyncPoint;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface QualityTraceTradeBillSyncPointService extends BaseService<QualityTraceTradeBillSyncPoint, Long> {
	/**
	 * 同步数据
	 * @param billId
	 * @param orderId
	 * @return
	 */
	public QualityTraceTradeBill syncData(Long localMaxBillId, QualityTraceTradeBill bill) ;
	/**
	 * 保存同步点
	 * @param point
	 * @return
	 */
	public QualityTraceTradeBillSyncPoint syncPoint(QualityTraceTradeBillSyncPoint point);

	/**
	 * 对同步的数据进行修正
	 */
	public void fixData();
}