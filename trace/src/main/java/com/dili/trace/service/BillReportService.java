package com.dili.trace.service;

import com.dili.trace.dao.CheckinOutRecordMapper;
import com.dili.trace.dto.BillReportQueryDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillReportService {
    @Autowired
    CheckinOutRecordMapper  checkinOutRecordMapper;
    public void queryBillReport(){
        BillReportQueryDto query=new BillReportQueryDto();
        this.checkinOutRecordMapper.queryBillReport(query);


    }
}