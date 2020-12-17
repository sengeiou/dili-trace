package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.DetectRecord;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface DetectRecordService extends BaseService<DetectRecord, Long> {
	public int saveDetectRecord(DetectRecord detectRecord);

	DetectRecord findByRegisterBillCode(String registerBillCode);

	List<DetectRecord> findTop2AndLatest(String registerBillCode);

	/**
	 * 根据id查询检测记录,返回registerNillCode与recordMap
	 * @param ids
	 * @return
	 */
	Map<String, DetectRecord> findMapRegisterBillByIds(List<Long> ids);
}