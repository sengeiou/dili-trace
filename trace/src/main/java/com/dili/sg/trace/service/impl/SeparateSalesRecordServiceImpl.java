package com.dili.sg.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.sg.trace.dao.SeparateSalesRecordMapper;
import com.dili.sg.trace.domain.SeparateSalesRecord;
import com.dili.sg.trace.service.SeparateSalesRecordService;
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
    public List<SeparateSalesRecord> findByRegisterBillCode(String registerBillCode) {
        SeparateSalesRecord separateSalesRecord =  new SeparateSalesRecord();
        separateSalesRecord.setRegisterBillCode(registerBillCode);
        return  list(separateSalesRecord);
    }

    @Override
    public Integer alreadySeparateSalesWeight(String registerBillCode) {
        Integer alreadyWeight =getActualDao().alreadySeparateSalesWeight(registerBillCode);
        if(alreadyWeight == null){
            alreadyWeight=0;
        }
        return alreadyWeight;
    }

	@Override
	public Integer getAlreadySeparateSalesWeightByTradeNo(String tradeNo) {
		 Integer alreadyWeight =getActualDao().getAlreadySeparateSalesWeightByTradeNo(tradeNo);
	        if(alreadyWeight == null){
	            alreadyWeight=0;
	        }
	        return alreadyWeight;
	}
}