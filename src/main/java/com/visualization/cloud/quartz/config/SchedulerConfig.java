package com.visualization.cloud.quartz.config;/**
 * @Auter zzh
 * @Date 2024/12/10
 */

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.config
 * @className: SchedulerConfig
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/10 10:15
 * @version: 1.0
 */

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.concurrent.Executor;

import static com.visualization.cloud.quartz.Function.QuartzConfigVisualization.QuartzPropertiesCreateFactor.createQuartzPropertiesCreate;

/**
 * @description: 调度器配置
 * @author: 黎剑
 * @create: 2024-04-13 23:04
 **/

@Configuration
public class SchedulerConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ApplicationContext applicationContext;
//    @Autowired
//    private JobListener jobListener;
//    @Autowired
//    private TriggerListener triggerListener;


    @Bean
    public Scheduler scheduler() throws IOException, SchedulerException {

        return schedulerFactoryBean().getScheduler();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName("cluster_scheduler");
        // 注入数据源
        factory.setDataSource(dataSource);
        // 选填
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        // 读线程池配置
        factory.setTaskExecutor(schedulerThreadPool());
        // 等待设置其他属性
        factory.setResourceLoader(applicationContext);
//        factory.setGlobalJobListeners(jobListener);
//        factory.setGlobalTriggerListeners(triggerListener);

        factory.setQuartzProperties(createQuartzPropertiesCreate());
//        factory.setQuartzProperties(quartzProperties());
        System.out.println("createQuartzPropertiesCreate()"+createQuartzPropertiesCreate());
        return factory;
    }

//    @Bean
//    public Properties quartzProperties() throws IOException {
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        propertiesFactoryBean.setLocation(new ClassPathResource("/spring-quartz.properties"));
//        propertiesFactoryBean.afterPropertiesSet();
//        return propertiesFactoryBean.getObject();
//    }

    @Bean
    public Executor schedulerThreadPool() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 处理器的核心数
        taskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        // 最大线程数
        taskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        // 容量
        taskExecutor.setQueueCapacity(Runtime.getRuntime().availableProcessors());
        return taskExecutor;
    }
}
