package com.visualization.cloud.quartz.Function.QuartzConfigVisualization;/**
 * @Auter zzh
 * @Date 2025/2/6
 */


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import static com.visualization.cloud.quartz.Function.QuartzConfigVisualization.QuartzPropertiesCreateFactor.createQuartzPropertiesCreate;
import static com.visualization.cloud.quartz.config.SpringContextHolder.getBean;


/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.Function.QuartzConfigVisualization
 * @className: Controller
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/6 22:29
 * @version: 1.0
 */
@RestController
@RequestMapping("/quartzConfig")
public class Controller {
    private final SchedulerMigrator schedulerMigrator=new SchedulerMigrator();
    @Autowired
    private ContextRefresher contextRefresher;

    private static  AllQuartzProperties.SchedulerProperties schedulerProperties=getBean(AllQuartzProperties.SchedulerProperties.class);
    private static  AllQuartzProperties.ThreadPoolProperties threadPoolProperties=getBean(AllQuartzProperties.ThreadPoolProperties.class);
    private static  AllQuartzProperties.JobStoreProperties jobStoreProperties=getBean(AllQuartzProperties.JobStoreProperties.class);
    private static  AllQuartzProperties.DataSourceProperties dataSourceProperties=getBean(AllQuartzProperties.DataSourceProperties.class);
    private static  AllQuartzProperties.PluginProperties pluginProperties=getBean(AllQuartzProperties.PluginProperties.class);
    private static  AllQuartzProperties.TriggerListener triggerListener=getBean(AllQuartzProperties.TriggerListener.class);
    private static  AllQuartzProperties.JobListener jobListener=getBean(AllQuartzProperties.JobListener.class);

    @GetMapping(value = "/saveQuartzProperties")
    @ResponseBody
    public void saveQuartzProperties(HttpServletResponse response) throws IOException {
        // 生成 Properties 文件内容
        Properties pro = createQuartzPropertiesCreate();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=quartz.properties");
        // 获取响应的输出流
        try (OutputStream out = response.getOutputStream()) {
            // 将Properties对象直接存储到响应输出流
            pro.store(out, "配置文件");
        }
    }
    @PostMapping(value = "/updateschedulerProperties")
    @ResponseBody
    public void updateschedulerProperties(@RequestBody AllQuartzProperties.SchedulerProperties schedulerPropertiesTO) throws Exception {
        schedulerProperties.setInstanceName(schedulerPropertiesTO.getInstanceName());
        schedulerMigrator.hotReload("org.quartz.scheduler-",schedulerPropertiesTO);
    }
    @PostMapping(value = "/updatethreadPoolProperties")
    @ResponseBody
    public void updatethreadPoolProperties(@RequestBody AllQuartzProperties.ThreadPoolProperties threadPoolPropertiesTO) throws Exception {
        threadPoolProperties= threadPoolPropertiesTO;
        schedulerMigrator.hotReload("org.quartz.threadpool-",threadPoolPropertiesTO);
    }
    @PostMapping(value = "/updatejobStoreProperties")
    @ResponseBody
    public void updatejobStoreProperties(@RequestBody AllQuartzProperties.JobStoreProperties jobStorePropertiesTO) throws Exception {
        jobStoreProperties= jobStorePropertiesTO;
        schedulerMigrator.hotReload("org.quartz.jobstore-",jobStorePropertiesTO);
    }
    @PostMapping(value = "/updatedataSourceProperties")
    @ResponseBody
    public void updatedataSourceProperties(@RequestBody AllQuartzProperties.DataSourceProperties dataSourcePropertiesTO) throws Exception {
        dataSourceProperties= dataSourcePropertiesTO;
        schedulerMigrator.hotReload("org.quartz.datasource-",dataSourcePropertiesTO);
    }
    @PostMapping(value = "/updatepluginProperties")
    @ResponseBody
    public void updatepluginProperties(@RequestBody AllQuartzProperties.PluginProperties pluginPropertiesTO) throws Exception {
        pluginProperties= pluginPropertiesTO;
        schedulerMigrator.hotReload("org.quartz.plugin-",pluginPropertiesTO);
    }
    @PostMapping(value = "/updatetriggerListener")
    @ResponseBody
    public void updatetriggerListener(@RequestBody AllQuartzProperties.TriggerListener triggerListenerTO) throws Exception {
        triggerListener= triggerListenerTO;
        schedulerMigrator.hotReload("org.quartz.triggerlistener-",triggerListenerTO);
    }
    @PostMapping(value = "/updatejobListener")
    @ResponseBody
    public void updatejobListener(@RequestBody AllQuartzProperties.JobListener jobListenerTO) throws Exception {
        jobListener= jobListenerTO;
        schedulerMigrator.hotReload("org.quartz.joblistener-",jobListenerTO);
    }

    @GetMapping(value = "/schedulerProperties")
    @ResponseBody
    public AllQuartzProperties.SchedulerProperties getschedulerProperties() {
        return schedulerProperties;
    }
    @GetMapping(value = "/threadPoolProperties")
    @ResponseBody
    public AllQuartzProperties.ThreadPoolProperties getthreadPoolProperties() {
        return threadPoolProperties;
    }
    @GetMapping(value = "/jobStoreProperties")
    @ResponseBody
    public AllQuartzProperties.JobStoreProperties getjobStoreProperties() {
        return jobStoreProperties;
    }
    @GetMapping(value = "/dataSourceProperties")
    @ResponseBody
    public AllQuartzProperties.DataSourceProperties getdataSourceProperties() {
        return dataSourceProperties;
    }
    @GetMapping(value = "/pluginProperties")
    @ResponseBody
    public AllQuartzProperties.PluginProperties getpluginProperties() {
        return pluginProperties;
    }
    @GetMapping(value = "/triggerListener")
    @ResponseBody
    public AllQuartzProperties.TriggerListener gettriggerListener() {
        return triggerListener;
    }
    @GetMapping(value = "/jobListener")
    @ResponseBody
    public AllQuartzProperties.JobListener getjobListener() {
        return jobListener;
    }
    @GetMapping(value = "/getgroup")
    @ResponseBody
    public String getall() {
        return List.of("jobListener", "triggerListener", "pluginProperties", "dataSourceProperties", "jobStoreProperties", "threadPoolProperties", "schedulerProperties").toString();
    }
}
