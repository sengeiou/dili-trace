//package com.dili.trace.worker;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
//@EnableAsync
//public class SyncOrderWorker {
//
////    @Bean
////    public TaskScheduler taskScheduler() {
////        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
////        taskScheduler.setPoolSize(2);//我这里设置的线程数是2,可以根据需求调整
////        return taskScheduler;
////    }
//
//    //上次任务结束后2分钟再次同步
////    @Scheduled(fixedDelay = 2*60*60)
//    @Scheduled(fixedDelay = 2000)
//    public void configureTasks() {
//        System.out.println("静态定时任务0开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
//    }
//}