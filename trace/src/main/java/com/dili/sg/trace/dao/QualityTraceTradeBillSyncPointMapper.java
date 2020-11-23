package com.dili.sg.trace.dao;

import com.dili.sg.trace.domain.QualityTraceTradeBillSyncPoint;
import com.dili.ss.base.MyMapper;

public interface QualityTraceTradeBillSyncPointMapper extends MyMapper<QualityTraceTradeBillSyncPoint> {
	public QualityTraceTradeBillSyncPoint selectByIdForUpdate(Long id);

}
