package com.dili.trace.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dili.trace.service.QualityTraceTradeBillService;

@Component
public class QualityTraceTradeBillAutoMatchJob {
	@Autowired QualityTraceTradeBillService qualityTraceTradeBillService;
	// 间隔两分钟同步数据
	@Scheduled(fixedDelay = 1000L * 60L * 2L)
	public void autoMatch() {
		
		
		
		
		
		
	}

}
