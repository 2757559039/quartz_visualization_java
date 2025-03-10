package com.atguigu.cloud.quartz.config;/**
 * @Auter zzh
 * @Date 2025/1/25
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.config
 * @className: CorsConfig
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/25 21:14
 * @version: 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 添加映射路径
        registry.addMapping("/**")
                .allowedOrigins("*") // 允许哪些域的请求，星号代表允许所有
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE") // 允许的方法
                .allowedHeaders("*") // 允许的头部设置
                .allowCredentials(false) // 是否发送cookie
                .maxAge(168000); // 预检间隔时间
    }
}
