package com.dili.trace.etrade.jobs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.etrade.domain.VTradeBill;
import com.dili.trace.jobs.QualityTraceTradeBillSyncJob;

public class SyncDataAutoJobTest extends AutoWiredBaseTest{
	@Autowired
	QualityTraceTradeBillSyncJob syncDataAutoJob;
	@Test
	@Transactional(propagation = Propagation.NEVER)
	public void testexecute() {
		syncDataAutoJob.execute();
	}

}
