package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.SeparateSalesRecordMapper;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.service.SeparateSalesRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class SeparateSalesRecordServiceImpl extends BaseServiceImpl<SeparateSalesRecord, Long> implements SeparateSalesRecordService {

    public SeparateSalesRecordMapper getActualDao() {
        return (SeparateSalesRecordMapper)getDao();
    }

    @Override
    public List<SeparateSalesRecord> findByRegisterBillCode(Long registerBillCode) {
        SeparateSalesRecord separateSalesRecord = DTOUtils.newDTO(SeparateSalesRecord.class);
        separateSalesRecord.setRegisterBillCode(registerBillCode);
        return  list(separateSalesRecord);
    }
}