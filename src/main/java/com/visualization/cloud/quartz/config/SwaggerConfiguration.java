package com.visualization.cloud.quartz.config;/**
 * @Auter 李孝在
 * @Date 2025/1/23
 */

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.quartz.config
 * @className: SwaggerConfig
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/23 00:48
 * @version: 1.0
 */
@Configuration
@EnableAutoConfiguration
@ConditionalOnProperty(name = "swagger.enable", matchIfMissing = true)
public class SwaggerConfiguration {

    @Bean
    public OpenAPI docsOpen2Api()
    {
        return new OpenAPI()
                .info(new Info()
                        .title("QGV")
                        .description("这是介绍")
                        .termsOfService("https://www.baidu.com")
                        .version("版本")
                        .contact(new Contact().name("作者姓名"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("接口有问题?点这里!")
                        .url("https://baidu.com/"));
    }

    //分组
    @Bean
    public GroupedOpenApi baseApi()
    {
        return GroupedOpenApi.builder().group("基础增删改查起停")
                        .pathsToMatch(
                        "/task/Add/**",
                        "/task/Delete/**",
                        "/task/Pause/**",
                        "/task/Select/**",
                        "/task/Start/**",
                        "/task/Update/**"
                ).build();
    }
    @Bean
    public GroupedOpenApi utilApi()
    {
        return GroupedOpenApi.builder().group("工具")
                .pathsToMatch(
                        "/task/Reflect/**",
                        "/task/Scheduler/**",
                        "/task/Util/**"
                        ).build();
    }
    @Bean
    public GroupedOpenApi jobBuilderApi()
    {
        return GroupedOpenApi.builder().group("脚本加载与保存").pathsToMatch("/scriptBuilder/**").build();
    }

    @Bean
    public GroupedOpenApi SSEApi()
    {
        return GroupedOpenApi.builder().group("SSE").pathsToMatch("/sse/**","/see/config/**").build();
    }

    @Bean
    public GroupedOpenApi groovyBeanApi()
    {
        return GroupedOpenApi.builder().group("虚拟类管理").pathsToMatch("/groovyBean/**").build();
    }
    @Bean
    public GroupedOpenApi quartzConfigApi()
    {
        return GroupedOpenApi.builder().group("Quartz配置文件").pathsToMatch("/quartzConfig/**").build();
    }

}
