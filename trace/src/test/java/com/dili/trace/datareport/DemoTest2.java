package com.dili.trace.datareport;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.jobs.ThirdPartyReportJob;
import com.dili.trace.service.DataReportService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DemoTest2 extends AutoWiredBaseTest {
    @Autowired
    DataReportService dataReportService;
    @Autowired
    ThirdPartyReportJob thirdPartyReportJob;

    @Test
    public void getAccessToken() {
       // this.thirdPartyReportService.refreshToken(false);

        this.thirdPartyReportJob.reportData();

    }

}