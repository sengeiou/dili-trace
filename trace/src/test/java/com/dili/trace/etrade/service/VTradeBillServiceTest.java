package com.dili.trace.etrade.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dili.ss.datasource.SwitchDataSource;
import com.dili.trace.AutoWiredBaseTest;
import com.dili.trace.etrade.domain.VTradeBill;
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VTradeBillServiceTest extends AutoWiredBaseTest {
	@Autowired
	VTradeBillService vTradeBillService;

	//@Test

	public void testlistByExample() {
		List<VTradeBill>list=this.vTradeBillService.listByExample(new VTradeBill());
		System.out.println(list);
	}
	@Test
	public void testselectRemoteLatestData() {
		
		VTradeBill vTradeBill=this.vTradeBillService.selectRemoteLatestData();
		Assertions.assertNotNull(vTradeBill);
	}
}
