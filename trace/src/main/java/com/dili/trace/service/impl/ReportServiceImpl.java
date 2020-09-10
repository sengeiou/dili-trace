package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.dto.OrigionReportDto;
import com.dili.trace.dto.OrigionReportQueryDto;
import com.dili.trace.service.ReportService;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableRetry
public class ReportServiceImpl implements ReportService{


    @Autowired
    private RegisterBillMapper registerBillMapper;

    @Override
    public List<OrigionReportDto> origionReportList(OrigionReportQueryDto queryDto) {
        return registerBillMapper.queryOrigionReport(queryDto);
    }
}
