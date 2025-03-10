package com.atguigu.cloud.sse.listener.manager;/**
 * @Auter 李孝在
 * @Date 2025/1/26
 */

import com.atguigu.cloud.sse.listener.SchedulerListener;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.sse.listener
 * @className: sdf
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/26 05:37
 * @version: 1.0
 */

import org.quartz.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ListenerManager {
    private final Scheduler scheduler;
    private final com.atguigu.cloud.sse.listener.JobListener jobListener;
    private final com.atguigu.cloud.sse.listener.TriggerListener triggerListener;
    private final com.atguigu.cloud.sse.listener.SchedulerListener schedulerListener;

    // 存储监听器状态
    private final Map<String, Boolean> listenerStates = new ConcurrentHashMap<>();

    public ListenerManager(Scheduler scheduler,
                           com.atguigu.cloud.sse.listener.JobListener jobListener,
                           com.atguigu.cloud.sse.listener.TriggerListener triggerListener,
                           SchedulerListener schedulerListener) throws SchedulerException {
        this.scheduler = scheduler;
        this.jobListener = jobListener;
        this.triggerListener = triggerListener;
        this.schedulerListener = schedulerListener;
        initDefaultStates();
    }

    private void initDefaultStates() throws SchedulerException {
        // 默认开启所有监听器
        listenerStates.put("job", true);
        listenerStates.put("trigger", true);
        listenerStates.put("scheduler", true);
        scheduler.getListenerManager().addJobListener(jobListener);
        scheduler.getListenerManager().addTriggerListener(triggerListener);
        scheduler.getListenerManager().addSchedulerListener(schedulerListener);
    }

    // 动态切换监听器状态
    public void toggleListener(String listenerType, boolean enabled) throws SchedulerException {
        // 1,调度器中移除
        // 2,设置Map的状态
        switch (listenerType) {
            case "job":
                if (enabled) {
                    scheduler.getListenerManager().addJobListener(jobListener);
                } else {
                    scheduler.getListenerManager().removeJobListener(jobListener.getName());
                }
                break;
            case "trigger":
                if (enabled) {
                    scheduler.getListenerManager().addTriggerListener(triggerListener);
                } else {
                    scheduler.getListenerManager().removeTriggerListener(triggerListener.getName());
                }
                break;
            case "scheduler":
                if (enabled) {
                    scheduler.getListenerManager().addSchedulerListener(schedulerListener);
                } else {
                    scheduler.getListenerManager().removeSchedulerListener(schedulerListener);
                }
                break;
        }
        listenerStates.put(listenerType.toUpperCase(), enabled);
    }


    // 获取当前状态
    public Map<String, Boolean> getListenerStates() {;
        return listenerStates;
    }
}