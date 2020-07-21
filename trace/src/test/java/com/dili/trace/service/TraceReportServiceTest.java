package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.TraceReportDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TraceReportServiceTest extends AutoWiredBaseTest{
    @Autowired
    TraceReportService traceReportService;
    @Test
    public void traceUserReport(){
        RegisterBillDto query=new RegisterBillDto();
        List<TraceReportDto>list=this.traceReportService.traceUserReport(query);
        assertNotNull(list);
    }
    
}