package com.visualization.cloud.quartz.Function.QuartzConfigVisualization;/**
 * @Auter zzh
 * @Date 2025/2/7
 */

//import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

import static com.visualization.cloud.quartz.Function.QuartzConfigVisualization.QuartzPropertiesCreateFactor.createQuartzPropertiesCreate;
import static com.visualization.cloud.quartz.config.SpringContextHolder.context;
import static com.visualization.cloud.quartz.config.SpringContextHolder.getBean;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.Function.QuartzConfigVisualization
 * @className: SchedulerMigrator
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/7 1:02
 * @version: 1.0
 */
public class SchedulerMigrator {



    @Autowired
    private DataSource dataSource;

    public <T> void hotReload( String prefix, T config) throws Exception {
        // 1. 关闭旧调度器
        SchedulerFactoryBean oldFactory = getBean(SchedulerFactoryBean.class);
        oldFactory.getScheduler().shutdown(true);
        oldFactory.destroy();

        // 2. 刷新配置（例如从外部加载）
        Properties quartzProperties = createQuartzPropertiesCreate();

        // 3. 创建新调度器工厂
        SchedulerFactoryBean newFactory = new SchedulerFactoryBean();
        newFactory.setDataSource(dataSource);
        newFactory.setQuartzProperties(quartzProperties);
        newFactory.setOverwriteExistingJobs(true);
        newFactory.afterPropertiesSet();  // 关键：触发初始化
        newFactory.start();

        // 4. 注册新Bean到Spring容器
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)
                context.getAutowireCapableBeanFactory();
        beanFactory.destroySingleton("schedulerFactoryBean");
        beanFactory.registerSingleton("schedulerFactoryBean", newFactory);
        var scheduler = newFactory.getScheduler();
        beanFactory.destroySingleton("scheduler");
        beanFactory.registerSingleton("scheduler", scheduler);

        beanFactory.destroySingleton(prefix+config.getClass().getName());
        beanFactory.registerSingleton(prefix+config.getClass().getName(), config);

        T config1 = (T) getBean(config.getClass());
        System.out.println(createQuartzPropertiesCreate());
        System.out.println(config1);
    }





}

