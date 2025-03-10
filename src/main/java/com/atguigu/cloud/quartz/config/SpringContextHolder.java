package com.atguigu.cloud.quartz.config;
/*
  @Auter zzh
 * @Date 2025/1/25
 */

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * &#064;projectName:  quartz_visualization
 * &#064;package:  com.atguigu.cloud.quartz.config
 * &#064;className:  SpringContextHolder
 * &#064;author:  Eric
 * &#064;date:  2025/1/25 17:57
 * &#064;version:  1.0
 * @author zzh
 */
// SpringContextHolder.java
@Component
public class SpringContextHolder implements ApplicationContextAware {
    public static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {

        return context.getBean(beanClass);
    }
    public static <T> T getBean(String name, Class<T> beanClass) {
        return context.getBean(name, beanClass);
    }
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }

    public static Boolean containsBean(String name) {
        return context.containsBean(name);
    }
}
