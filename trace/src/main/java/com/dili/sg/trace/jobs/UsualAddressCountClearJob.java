package com.dili.sg.trace.jobs;

import java.util.Date;

import javax.annotation.PostConstruct;

import com.dili.trace.dao.UsualAddressMapper;
import com.dili.trace.domain.UsualAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dili.ss.dto.DTOUtils;

@EnableAsync
@Component
public class UsualAddressCountClearJob {
	private static final Logger logger = LoggerFactory.getLogger(UsualAddressCountClearJob.class);
	@Autowired
	UsualAddressMapper usualAddressMapper;
	//应用启用时处理一次数据，防止上一次任务未执行
	@PostConstruct
	public void init() {
		this.checkAndClearUsedCount();
	}

	//每天00:00:01的时候执行数据处理
	@Scheduled(cron = "1 0 0 * * ?")
	public void execute() {
		this.checkAndClearUsedCount();
	}

	private boolean checkAndClearUsedCount() {

		try {
			UsualAddress input = DTOUtils.newDTO(UsualAddress.class);
			input.setClearTime(new Date());
			Integer count = this.usualAddressMapper.checkAndUpdateCountData(input);
			logger.info("共[{}]条常用地址统计数据被更新", count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

		return true;
	}

}
