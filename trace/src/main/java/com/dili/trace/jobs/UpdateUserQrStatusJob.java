package com.dili.trace.jobs;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.dili.trace.service.RegisterBillService;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserQrStatusJob implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserQrStatusJob.class);
    @Autowired
    RegisterBillService billService;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void execute() {
        LocalDateTime now = LocalDateTime.now();
        Date start = this.start(now);
        Date end = this.end(now);
        logger.info("开始执行任务: 根据 {}-{} 之内报备单数据更新用户颜色码",DateFormatUtils.format(start, "yyyy-MM-dd HH:mm:ss"),DateFormatUtils.format(end, "yyyy-MM-dd HH:mm:ss") );
        try {
            this.billService.updateAllUserQrStatusByRegisterBillNum(start, end);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.execute();

    }

    protected Date start(LocalDateTime now) {
        Date start = Date.from(
                now.minusDays(6).atZone(ZoneId.systemDefault()).toInstant());
        return start;
    }

    protected Date end(LocalDateTime now) {
        Date end = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        return end;
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        UpdateUserQrStatusJob job = new UpdateUserQrStatusJob();
        Date start = job.start(now);
        Date end = job.end(now);
        logger.info("{}-{}",DateFormatUtils.format(start, "yyyy-MM-dd HH:mm:ss"),DateFormatUtils.format(end, "yyyy-MM-dd HH:mm:ss") );
    }

}