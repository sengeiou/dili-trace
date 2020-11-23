package com.dili.trace.service;

import java.util.List;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.DetectRecord;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface DetectRecordService extends BaseService<DetectRecord, Long> {
	public int saveDetectRecord(DetectRecord detectRecord);

	DetectRecord findByRegisterBillCode(String registerBillCode);

	List<DetectRecord> findTop2AndLatest(String registerBillCode);

}