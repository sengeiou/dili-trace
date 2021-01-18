package com.dili.trace.service;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dao.TruckEnterRecordMapper;
import com.dili.trace.domain.TruckEnterRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 司机进门报备数据
 */
@Service
public class TruckEnterRecordService extends BaseServiceImpl<TruckEnterRecord, Long> {
    @Autowired
    TruckEnterRecordMapper truckEnterRecordMapper;

    public BaseOutput addTruckEnterRecord(TruckEnterRecord truckEnterRecord) {
        try {
            truckEnterRecordMapper.insertSelective(truckEnterRecord);
            return BaseOutput.success();
        } catch (Exception e){
            e.printStackTrace();
            return BaseOutput.failure();
        }
    }

    public BaseOutput updateTruckEnterRecord(TruckEnterRecord truckEnterRecord) {
        try {
            truckEnterRecordMapper.updateByPrimaryKeySelective(truckEnterRecord);
            return BaseOutput.success();
        } catch (Exception e){
            e.printStackTrace();
            return BaseOutput.failure();
        }
    }
}
