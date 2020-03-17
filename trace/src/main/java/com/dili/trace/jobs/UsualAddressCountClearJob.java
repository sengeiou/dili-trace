package com.dili.trace.jobs;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dili.ss.dto.DTOUtils;
import com.dili.trace.dao.UsualAddressMapper;
import com.dili.trace.domain.UsualAddress;

@EnableAsync
@Component
public class UsualAddressCountClearJob {
	private static final Logger logger = LoggerFactory.getLogger(UsualAddressCountClearJob.class);
	@Autowired
	UsualAddressMapper usualAddressMapper;

	@PostConstruct
	public void init() {
		this.checkAndClearUsedCount();
	}

	// 间隔两分钟同步数据
	@Scheduled(cron = "1 0 0 * * ?")
	public void execute() {
		this.checkAndClearUsedCount();
	}

	private boolean checkAndClearUsedCount() {

		try {
			UsualAddress input = DTOUtils.newDTO(UsualAddress.class);
			input.setClearTime(new Date());

			this.usualAddressMapper.checkAndResetOutOfDate(input);
			this.usualAddressMapper.checkAndUpdateCountData(input);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

		return true;
	}

}
