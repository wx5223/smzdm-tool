package com.shawn.microservice.core;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shawn on 2016/12/14.
 */
@Component
public class TaskScheduler {
    private static ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @PostConstruct
    public static void init() {
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadNamePrefix("IfJob");
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.initialize();
        AutoCheckTask autoCheckTask = new AutoCheckTask();
        Date startTime = new Date(System.currentTimeMillis()+10000);
        //threadPoolTaskScheduler.scheduleAtFixedRate(autoCheckTask, startTime, 1000*60*30);
        threadPoolTaskScheduler.schedule(autoCheckTask, new CronTrigger("0 0/20 7,8,9,10,11,12,13,14,15,16,17,18,19,20 * * ? "));
    }

    @PreDestroy
    public static void stop() {
        ScheduledExecutorService scheduledExecutorService = threadPoolTaskScheduler.getScheduledExecutor();
        try {
            scheduledExecutorService.shutdownNow();
            if (!scheduledExecutorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS)) {
                System.err.println("Pool did not terminated");
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
