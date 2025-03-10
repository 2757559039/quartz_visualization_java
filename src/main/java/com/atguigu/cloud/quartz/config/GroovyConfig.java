package com.atguigu.cloud.quartz.config;/**
 * @Auter 李孝在
 * @Date 2025/1/23
 */

import groovy.lang.GroovyClassLoader;
import jakarta.annotation.PostConstruct;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.junit.jupiter.api.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.quartz.config
 * @className: G
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/23 05:48
 * @version: 1.0
 */

@Configuration
public class GroovyConfig {


    @Bean
    public GroovyClassLoader groovyClassLoader() {
        return new GroovyClassLoaderKillClassEnhance(ClassLoader.getSystemClassLoader());
    }
}
