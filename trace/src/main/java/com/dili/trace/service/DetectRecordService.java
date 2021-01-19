package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.dto.DetectRecordInputDto;
import com.dili.uap.sdk.domain.UserTicket;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
public interface DetectRecordService extends BaseService<DetectRecord, Long> {

	/**
	 * 保存检测结果
	 * @param detectRecord
	 * @return
	 */
	public int saveDetectRecord(DetectRecord detectRecord);

	/**
	 * 根据登记单编号查询检测结果
	 * @param registerBillCode
	 * @return
	 */
	DetectRecord findByRegisterBillCode(String registerBillCode);

	/**
	 * 查询最近两条检测结果
	 * @param registerBillCode
	 * @return
	 */
	List<DetectRecord> findTop2AndLatest(String registerBillCode);

	/**
	 * 根据id查询检测记录,返回registerNillCode与recordMap
	 * @param ids
	 * @return
	 */
	Map<String, DetectRecord> findMapRegisterBillByIds(List<Long> ids);

	/**
	 * 手动填写检测结果
	 * @param input
	 * @param userTicket
	 * @return
	 */
	public int saveDetectRecordManually(DetectRecordInputDto input, UserTicket userTicket);
}