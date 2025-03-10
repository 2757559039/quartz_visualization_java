package com.atguigu.cloud.sse.listener;/**
 * @Auter 李孝在
 * @Date 2025/1/25
 */

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.sse.listener
 * @className: asd
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/25 07:00
 * @version: 1.0
 */
import com.atguigu.cloud.sse.SSEController;
import com.atguigu.cloud.sse.SSEUtil;
import com.atguigu.cloud.sse.listener.manager.ListenerEventManager;
import com.atguigu.cloud.util.DynamicBeanOperate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;


//任务监视器

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
@Component
public class JobListener implements org.quartz.JobListener {
    private final Map<String,Boolean> EventListenerState;
    @Resource
    private Scheduler scheduler;
    @Resource
    private DynamicBeanOperate dynamicBeanOperate;

    @Autowired
    public JobListener(ListenerEventManager listenerEventManager) {
        this.EventListenerState = listenerEventManager.getListenerEvent("job");
    }


    @Override
    public String getName() {
        return "DynamicJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        if (EventListenerState.get("jobToBeExecuted")) {
            subEventInfo(context,"任务即将执行...");
        }
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        System.out.println("任务已执行完成...");
        // 获取任务所属的组名
        JobKey jobKey = context.getJobDetail().getKey();
        String groupName = jobKey.getGroup();
        // 指定需要打印参数的组名（例如"specificGroup"）
        if ("DelayedQueue".equals(groupName)) {
            // 获取任务参数

            try {
                scheduler.deleteJob(jobKey);
                dynamicBeanOperate.unregisterBean(jobKey.getName());
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }

        }
        if (EventListenerState.get("jobWasExecuted")) {
            subEventInfo(context, "任务已执行完成...");




        }
    }


    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        if (EventListenerState.get("jobExecutionVetoed")) {
            subEventInfo(context,"任务执行被否决...");
        }
    }

    private void subEventInfo(JobExecutionContext context,Object data){
        JobKey jobKey = context.getJobDetail().getKey();
        SSEUtil.simpleBroadcastEvent("job",data.toString());
        SSEUtil.simpleBroadcastEvent("job:"+ jobKey.getGroup(),data.toString());
        SSEUtil.simpleBroadcastEvent("job:"+ jobKey,data.toString());

    }
}