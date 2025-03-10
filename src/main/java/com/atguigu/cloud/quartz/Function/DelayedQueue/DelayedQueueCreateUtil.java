package com.atguigu.cloud.quartz.Function.DelayedQueue;/**
 * @Auter zzh
 * @Date 2025/2/5
 */

import com.atguigu.cloud.quartz.jobService.IJobService;
import com.atguigu.cloud.util.DynamicBeanOperate;
import jakarta.annotation.Resource;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Component;
import com.atguigu.cloud.quartz.config.SpringContextHolder;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.DelayedQueue.test
 * @className: DelayedQueueStart
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/5 14:01
 * @version: 1.0
 */
@Component
public class DelayedQueueCreateUtil  {
    private static IJobService jobService;
    private static Scheduler scheduler;
    private static DynamicBeanOperate dynamicBeanOperate;

    @Resource
    public void setJobService(IJobService jobService) {
        this.jobService = jobService;
    }
    @Resource
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
    @Resource
    public void setDynamicBeanOperate(DynamicBeanOperate dynamicBeanOperate) {
        this.dynamicBeanOperate = dynamicBeanOperate;
    }

    public static void createDelayedQueue(DelayedJob job, Object parameter, String iD, String triggerName) throws SchedulerException {
       Trigger trigger= SpringContextHolder.getBean(triggerName);
        jobService.createDelayedQueue(job, parameter,iD,trigger);
    }

    public static void createDelayedQueue(DelayedJob job, Object parameter, String ID,Long time ) throws SchedulerException {
        jobService.createDelayedQueue(job, parameter,ID,time);
    }

    //编程式管理手动删除延时队列
    public static void killDelayedQueue(String ID) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(ID,"DelayedQueue");
        scheduler.deleteJob(jobKey);
        dynamicBeanOperate.unregisterBean(jobKey.getName());
    }

}
