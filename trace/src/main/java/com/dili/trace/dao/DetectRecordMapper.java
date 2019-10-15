package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.DetectRecord;

public interface DetectRecordMapper extends MyMapper<DetectRecord> {
	List<DetectRecord> findTop2AndLatest(String registerBillCode);
}