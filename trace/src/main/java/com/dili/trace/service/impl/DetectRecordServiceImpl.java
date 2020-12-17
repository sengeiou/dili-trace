package com.dili.trace.service.impl;

import com.dili.trace.service.SgRegisterBillService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.DetectRecordMapper;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.service.DetectRecordService;
import com.google.common.collect.Maps;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class DetectRecordServiceImpl extends BaseServiceImpl<DetectRecord, Long> implements DetectRecordService {
    @Autowired
    private SgRegisterBillService registerBillService;

    public DetectRecordMapper getActualDao() {
        return (DetectRecordMapper)getDao();
    }

    @Override
    @Transactional
    public int saveDetectRecord(DetectRecord detectRecord) {
        saveOrUpdate(detectRecord);
        return 1;
    }

    @Override
    public DetectRecord findByRegisterBillCode(String registerBillCode) {
        DetectRecord detectRecord = DTOUtils.newDTO(DetectRecord.class);
        detectRecord.setRegisterBillCode(registerBillCode);
        detectRecord.setSort("id");
        detectRecord.setOrder("desc");
        List<DetectRecord> list = this.listByExample(detectRecord);
        if(list!=null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

	@Override
	public List<DetectRecord> findTop2AndLatest(String registerBillCode) {
		return this.getActualDao().findTop2AndLatest(registerBillCode);
	}

    @Override
    public  Map<String, DetectRecord>  findMapRegisterBillByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Maps.newHashMap();
        }
        Example example = new Example(DetectRecord.class);
        example.and().andIn("id", ids);
        return StreamEx.of(selectByExample(example)).toMap(DetectRecord::getRegisterBillCode, Function.identity());
    }
}