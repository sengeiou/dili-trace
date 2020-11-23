package com.dili.sg.trace.dao;

import java.util.List;

import com.dili.sg.trace.domain.DetectRecord;
import com.dili.ss.base.MyMapper;

public interface DetectRecordMapper extends MyMapper<DetectRecord> {
	List<DetectRecord> findTop2AndLatest(String registerBillCode);
}