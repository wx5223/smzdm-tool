package com.shawn.microservice.core;


/**
 * 自动检查所有的接口任务
 * Created by Shawn on 2016/12/14.
 */
public class AutoCheckTask implements Runnable {

    @Override
    public void run() {
        System.out.println("开始自动检查最新的数据: Starting auto check new data...");
        SmzdmHandler.getArticleNow();
    }
}
