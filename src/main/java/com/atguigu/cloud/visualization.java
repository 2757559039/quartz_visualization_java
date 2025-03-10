package com.atguigu.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.Properties;

/**
 * @Auter zzh
 * @Date 2025/1/21
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.atguigu.cloud.scriptBuilder.DAO.mapper")
public class visualization extends SpringBootServletInitializer implements CommandLineRunner {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(visualization.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(visualization.class, args);
    }

    @Autowired
    private ApplicationContext appContext;

//    @Override
//    public void run(String... args) throws Exception {
//
//    }

    @Override
    public void run(String... args) throws Exception
    {
        String[] beans = appContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans)
        {
            System.out.println(bean + " of Type :: " + appContext.getBean(bean).getClass());
        }
        Properties quartzProperties = new Properties();
        quartzProperties.put("org.quartz.dataSource.quartz.URL","jdbc:mysql://114.132.71.250:3307/visualization?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true");
        quartzProperties.put("org.quartz.plugin.jobInitializer.class", "org.quartz.plugins.xml.XMLSchedulingDataProcessorPlugin");
        System.out.println("quartzProperties:="+quartzProperties);
    }
}