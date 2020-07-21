package com.dili.trace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.Map;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.dto.TraceReportDto;
import com.dili.trace.dto.TraceReportQueryDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TraceReportServiceTest extends AutoWiredBaseTest {
    @Autowired
    TraceReportService traceReportService;

    @Test
    public void traceUserReport() {
        TraceReportQueryDto query = new TraceReportQueryDto();
        query.setCreatedEnd(new Date());
        query.setCreatedStart(new Date());
        Map<String, TraceReportDto> data = this.traceReportService.getTraceBillReportData(query);
        assertNotNull(data);
    }

}