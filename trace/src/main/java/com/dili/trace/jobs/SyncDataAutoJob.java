package com.dili.trace.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SyncDataAutoJob {
	private static final Logger logger=LoggerFactory.getLogger(SyncDataAutoJob.class);
	//间隔两分钟同步数据
	@Scheduled(fixedDelay = 1000L*60L*2L)
	public void execute() {
		logger.info("===sync data===");
		
	}

}
