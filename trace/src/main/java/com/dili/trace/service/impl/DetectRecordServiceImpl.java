package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.DetectRecordMapper;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class DetectRecordServiceImpl extends BaseServiceImpl<DetectRecord, Long> implements DetectRecordService {
    @Autowired
    private RegisterBillService registerBillService;

    public DetectRecordMapper getActualDao() {
        return (DetectRecordMapper)getDao();
    }

    @Override
    @Transactional
    public int saveDetectRecord(DetectRecord detectRecord) {
        saveOrUpdate(detectRecord);
        RegisterBill registerBill =  registerBillService.findByCode(detectRecord.getRegisterBillCode());
        registerBill.setLatestDetectRecordId(detectRecord.getId());
        registerBill.setLatestDetectTime(detectRecord.getDetectTime());
        registerBill.setDetectState(detectRecord.getDetectState());
        registerBillService.update(registerBill);
        return 1;
    }

    @Override
    public DetectRecord findByRegisterBillCode(String registerBillCode) {
        DetectRecord detectRecord = DTOUtils.newDTO(DetectRecord.class);
        detectRecord.setRegisterBillCode(registerBillCode);
        List<DetectRecord> list = list(detectRecord);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }
}