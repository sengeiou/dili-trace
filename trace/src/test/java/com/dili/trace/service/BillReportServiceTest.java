package com.dili.trace.service;

import com.dili.ss.domain.BasePage;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.dto.BillReportDto;
import com.dili.trace.dto.BillReportQueryDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BillReportServiceTest extends AutoWiredBaseTest {
    @Autowired
    BillReportService billReportService;

    @Test
    public void queryBillReport() {
        BillReportQueryDto query = new BillReportQueryDto();
        query.setBillCreatedStart("2019-01-31");
        query.setBillCreatedEnd("2020-12-31");
        query.setIncludeUnCheckin(true);
        // query.setCheckinCreatedStart("2019-01-01");
        // query.setCheckinCreatedEnd("2020-12-01");
        BasePage<BillReportDto> page = this.billReportService.listPageBillReport(query);
        System.out.println(page);
    }

}