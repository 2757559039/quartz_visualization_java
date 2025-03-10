package com.atguigu.cloud.quartz.Function.DelayedQueue;/**
 * @Auter zzh
 * @Date 2025/2/21
 */

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.Function.DelayedQueue
 * @className: testjob
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/21 17:56
 * @version: 1.0
 */
public class testjob extends DelayedJob{

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("执行任务11111111");
    }
}
