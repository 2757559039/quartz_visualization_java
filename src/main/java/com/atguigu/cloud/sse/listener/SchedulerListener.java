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
 * @date: 2025/1/25 07:02
 * @version: 1.0
 */
import com.atguigu.cloud.sse.SSEController;
import com.atguigu.cloud.sse.SSEUtil;
import com.atguigu.cloud.sse.listener.manager.ListenerEventManager;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

//调度器触发器
@Component
public class SchedulerListener implements org.quartz.SchedulerListener {
    private final Map<String,Boolean> EventListenerState;

    @Autowired
    public SchedulerListener(ListenerEventManager listenerEventManager) {
        this.EventListenerState = listenerEventManager.getListenerEvent("scheduler");
    }
    /**
     *
     *
     * 调度器部分
     *
     *
     * */
    @Override
    //调度器启动中
    public void schedulerStarting() {
        if(EventListenerState.get("schedulerStarting")) {
            subEventInfo("调度器启动中...");
        }
    }

    @Override
    //调度器完全启动后
    public void schedulerStarted() {
        if(EventListenerState.get("schedulerStarted")) {
            subEventInfo("调度器完全启动后...");
        }
    }

    @Override
    //调度器进入待机模式:即暂停响应触发器,但未关闭时
    public void schedulerInStandbyMode() {
        if(EventListenerState.get("schedulerInStandbyMode")) {
            subEventInfo("调度器进入待机模式...");
        }
    }
    @Override
    //调度器开始关闭
    public void schedulerShuttingdown() {

        if(EventListenerState.get("schedulerShuttingdown")) {
            subEventInfo("调度器开始关闭...");
        }
    }
    @Override
    //调度器关闭后
    public void schedulerShutdown() {
        if(EventListenerState.get("schedulerShutdown")) {
            subEventInfo("调度器关闭后...");
        }
    }

    @Override
    //当调度器数据被清空（通过 scheduler.clear()）时调用。
    public void schedulingDataCleared() {
        if(EventListenerState.get("schedulingDataCleared")) {
            subEventInfo("调度器数据被清空...");
        }
    }

    @Override
    //当调度器内部发生错误（如任务执行异常、持久化失败）时调用
    public void schedulerError(String s, SchedulerException e) {
        if(EventListenerState.get("schedulerError")) {
            subEventInfo("调度器内部发生错误...");
        }
    }
    /**
     *
     *
     * 任务部分
     *
     *
     * */
    @Override
    //当任务被添加到调度器（通过 scheduler.scheduleJob()）时调用
    public void jobScheduled(Trigger trigger) {
        if(EventListenerState.get("jobScheduled")) {
            subEventInfo("任务被添加到调度器...");
        }
    }

    @Override
    //当任务被从调度器中移除（通过 scheduler.unscheduleJob()）时调用
    public void jobUnscheduled(TriggerKey triggerKey) {
        if(EventListenerState.get("jobUnscheduled")) {
            subEventInfo("任务被从调度器中移除...");
        }
    }
    @Override
    //当任务定义（JobDetail）被显式添加（通过 scheduler.addJob()）时调用
    public void jobAdded(JobDetail jobDetail) {
        if(EventListenerState.get("jobAdded")) {
            subEventInfo("任务定义...");
        }

    }

    @Override
    //当任务定义被删除（通过 scheduler.deleteJob()）时调用
    public void jobDeleted(JobKey jobKey) {
        if(EventListenerState.get("jobDeleted")) {
            subEventInfo("任务定义被删除...");
        }
    }

    @Override
    //当任务被暂停（通过 scheduler.pauseJob()）时调用
    public void jobPaused(JobKey jobKey) {
        if(EventListenerState.get("jobPaused")) {
            subEventInfo("任务被暂停...");
        }

    }

    @Override
    //当整个任务组被暂停（通过 scheduler.pauseJobs(GroupMatcher)）时调用
    public void jobsPaused(String s) {
        if(EventListenerState.get("jobsPaused")) {
            subEventInfo("整个任务组被暂停...");
        }
    }

    @Override
    //当任务被恢复（通过 scheduler.resumeJob()）时调用
    public void jobResumed(JobKey jobKey) {

        if(EventListenerState.get("jobResumed")) {
            subEventInfo("任务被恢复...");
        }
    }

    @Override
    //当整个任务组被恢复（通过 scheduler.resumeJobs(GroupMatcher)）时调用
    public void jobsResumed(String s) {

        if(EventListenerState.get("jobsResumed")) {
            subEventInfo("整个任务组被恢复...");}
    }

    /**
     *
     *
     * 触发器部分
     *
     *
     * */
    @Override
    //当触发器不再触发（例如达到重复次数上限或到达结束时间）时调用
    public void triggerFinalized(Trigger trigger) {
        if(EventListenerState.get("triggerFinalized")) {
            subEventInfo("触发器不再触发...");}
    }

    @Override
    //当单个触发器被暂停（通过 scheduler.pauseTrigger()）时调用
    public void triggerPaused(TriggerKey triggerKey) {
        if(EventListenerState.get("triggerPaused")) {
            subEventInfo("单个触发器被暂停...");}
    }

    @Override
    //当整个触发器组被暂停（通过 scheduler.pauseTriggers(GroupMatcher)）时调用
    public void triggersPaused(String s) {
        if(EventListenerState.get("triggersPaused")) {
            subEventInfo("整个触发器组被暂停...");}
    }

    @Override
    //当单个触发器被恢复（通过 scheduler.resumeTrigger()）时调用
    public void triggerResumed(TriggerKey triggerKey) {
        if(EventListenerState.get("triggerResumed")) {
            subEventInfo("单个触发器被恢复...");}
    }

    @Override
    //当整个触发器组被恢复（通过 scheduler.resumeTriggers(GroupMatcher)）时调用
    public void triggersResumed(String s) {
        if(EventListenerState.get("triggersResumed")) {
            subEventInfo("整个触发器组被恢复...");}
    }


    private void subEventInfo(Object data){

        SSEUtil.simpleBroadcastEvent("scheduler",data.toString());
    }


}