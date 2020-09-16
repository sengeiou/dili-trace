package com.dili.trace.jobs;

import com.dili.trace.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author asa.lee
 */
@Component
public class UserActiveStatisticsJob implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(UserActiveStatisticsJob.class);
    
    @Autowired
    UserService userService;
    
    @Override
    public void run(String... args) throws Exception {

    }

    /**
     * 每天中午12点更新一次数据
     */
    @Scheduled(cron = "0 0/10 9-22 * * ?")
    public void pushData() {
        try {
            updateUserActive();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void updateUserActive() {
            userService.updateUserActiveByTime();
    }
}