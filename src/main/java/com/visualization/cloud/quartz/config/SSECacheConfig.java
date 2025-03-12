package com.visualization.cloud.quartz.config;/**
 * @Auter zzh
 * @Date 2025/3/1
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.config
 * @className: SSECacheConfig
 * @author: Eric
 * @description: TODO
 * @date: 2025/3/1 22:54
 * @version: 1.0
 */
//
@Configuration
public class SSECacheConfig {
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);  // 设置线程池大小
        return scheduler;
    }
}
