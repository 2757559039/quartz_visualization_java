package com.atguigu.cloud.quartz.showpo;/**
 * @Auter zzh
 * @Date 2025/2/7
 */

import com.atguigu.cloud.quartz.po.CreatJobDetail;
import com.atguigu.cloud.quartz.po.QuartzJobBeanAbstract;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.po
 * @className: DefaultJobDetail
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/7 21:12
 * @version: 1.0
 */

public class DefaultJobDetail111 extends CreatJobDetail {
    public DefaultJobDetail111(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    public JobDetail createdetail(String Jobclassname, String Jobname, String Jobgroup, String Description) throws ClassNotFoundException {
        Class<? extends QuartzJobBeanAbstract> jobClass = this.applicationContext.getType(Jobclassname).asSubclass(QuartzJobBeanAbstract.class);
        System.out.println(jobClass);
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(Jobname, Jobgroup).withDescription(Description).storeDurably().build();
        return jobDetail;
    }


}
