package com.visualization.cloud.quartz.config;/**
 * @Auter zzh
 * @Date 2025/1/25
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.config
 * @className: QuartzConfig
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/25 14:58
 * @version: 1.0
 */
@Configuration
@DependsOn("groovyClassLoader")
public class QuartzConfig {

    @Bean
    public CascadingClassLoadHelper cascadingClassLoadHelper() {
        return new CascadingClassLoadHelper();
    }
}