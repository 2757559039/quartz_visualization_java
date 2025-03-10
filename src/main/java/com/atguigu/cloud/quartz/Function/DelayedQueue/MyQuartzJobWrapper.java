package com.atguigu.cloud.quartz.Function.DelayedQueue;/**
 * @Auter zzh
 * @Date 2025/2/4
 */



import com.atguigu.cloud.quartz.Function.DelayedQueue.DelayedJob;
import com.atguigu.cloud.quartz.po.QuartzJobBeanAbstract;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.scheduling.quartz.QuartzJobBean;
import  com.atguigu.cloud.quartz.config.SpringContextHolder;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.po
 * @className: MyQuartzJobWrapper
 * @author: Eric
 * @date: 2025/2/4 18:58
 * @version: 1.0
 */

public class MyQuartzJobWrapper extends QuartzJobBeanAbstract {



    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        String beanName = dataMap.getString("targetBeanName");
        DelayedJob targetJob = SpringContextHolder.getBean(beanName, DelayedJob.class);
        // 调用实际业务逻辑
        targetJob.execute(context);
    }


}