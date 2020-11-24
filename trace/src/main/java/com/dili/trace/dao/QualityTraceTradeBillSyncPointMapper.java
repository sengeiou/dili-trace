package com.dili.trace.dao;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.sg.QualityTraceTradeBillSyncPoint;

public interface QualityTraceTradeBillSyncPointMapper extends MyMapper<QualityTraceTradeBillSyncPoint> {
	public QualityTraceTradeBillSyncPoint selectByIdForUpdate(Long id);

}
