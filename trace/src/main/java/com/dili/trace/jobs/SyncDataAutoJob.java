package com.dili.trace.jobs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dili.trace.etrade.domain.VTradeBill;
import com.dili.trace.etrade.service.VTradeBillService;

@Component
public class SyncDataAutoJob {
	private static final Logger logger=LoggerFactory.getLogger(SyncDataAutoJob.class);
	@Autowired
	VTradeBillService vTradeBillService;

	//间隔两分钟同步数据
	@Scheduled(fixedDelay = 1000L*60L*2L)
	public void execute() {

		logger.info("===sync data===");
		//List<VTradeBill>list=this.vTradeBillService.listByExample(new VTradeBill());
		//System.out.println(list);
	}

}
