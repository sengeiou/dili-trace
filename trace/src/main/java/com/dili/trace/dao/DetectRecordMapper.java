package com.dili.trace.dao;

import java.util.List;

import com.dili.ss.base.MyMapper;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.dto.DetectRecordParam;

public interface DetectRecordMapper extends MyMapper<DetectRecord> {
	List<DetectRecord> findTop2AndLatest(String registerBillCode);

    /**
     * 查询检测记录
     * @param detectRecord
     * @return
     */
    List<DetectRecordParam> listBillByRecord(DetectRecordParam detectRecord);
}