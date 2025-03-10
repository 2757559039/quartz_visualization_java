package com.atguigu.cloud.util;/**
 * @Auter 李孝在
 * @Date 2025/1/24
 */

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.util
 * @className: asd
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/24 22:59
 * @version: 1.0
 */

//动态Bean注册工具
@Component
public class DynamicBeanOperate implements ApplicationContextAware {

    private static BeanDefinitionRegistry beanDefinitionRegistry;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext;
    }

    /**
     * 将动态编译的类注册为 Spring Bean
     * @param beanName   Bean名称
     * @param beanClass  Bean类型
     */
    public void registerBean(String beanName, Class<?> beanClass) {
        BeanDefinition definition = BeanDefinitionBuilder
                .genericBeanDefinition(beanClass)
                // 这里使用单例模式
                .setScope(BeanDefinition.SCOPE_SINGLETON)
                .getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition(beanName, definition);
    }

    /**
     * 动态删除 Bean
     *
     * @param beanName   Bean 名称
     * @return 如果删除成功返回 true，否则返回 false
     */
    public boolean unregisterBean(String beanName) {
        if (beanDefinitionRegistry.containsBeanDefinition(beanName)) {
            beanDefinitionRegistry.removeBeanDefinition(beanName);
            return true;
        } else {
            return false;
        }
    }

    public void registerBeanByPro(String beanName, Class<?> beanClass, Object parameter) {
        BeanDefinition definition = BeanDefinitionBuilder
                .genericBeanDefinition(beanClass)
                .addPropertyValue("parameter", parameter)
                .setScope(BeanDefinition.SCOPE_SINGLETON)
                .getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition(beanName, definition);
    }
}