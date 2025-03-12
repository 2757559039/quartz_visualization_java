package com.visualization.cloud.sse.listener.manager;/**
 * @Auter 李孝在
 * @Date 2025/1/26
 */

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.sse.listener
 * @className: asd
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/26 06:15
 * @version: 1.0
 */

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ListenerEventManager{
    // 监听器开关状态
    private Map<String, Map<String,Boolean>> listenerEvents = new ConcurrentHashMap<>();

    public ListenerEventManager() {
        // 初始化默认状态
        listenerEvents.put("job", new ConcurrentHashMap<>(){{
            put("jobToBeExecuted", false);
            put("jobWasExecuted", false);
            put("jobExecutionVetoed", false);
        }});

        listenerEvents.put("trigger", new ConcurrentHashMap<>(){{
            put("triggerComplete", false);
            put("triggerFired", false);
        }});
        Map<String, Boolean> schedulerEvents = new HashMap<>();
        schedulerEvents.put("schedulerStarting", false);
        schedulerEvents.put("schedulerStarted", false);
        schedulerEvents.put("schedulerInStandbyMode", false);
        schedulerEvents.put("schedulerShuttingdown", false);
        schedulerEvents.put("schedulerShutdown", false);
        schedulerEvents.put("schedulingDataCleared", false);
        schedulerEvents.put("schedulerError", false);
        schedulerEvents.put("jobScheduled", false);
        schedulerEvents.put("jobUnscheduled", false);
        schedulerEvents.put("jobAdded", false);
        schedulerEvents.put("jobDeleted", false);
        schedulerEvents.put("jobPaused", false);
        schedulerEvents.put("jobsPaused", false);
        schedulerEvents.put("jobResumed", false);
        schedulerEvents.put("jobsResumed", false);
        schedulerEvents.put("triggerFinalized", false);
        schedulerEvents.put("triggerPaused", false);
        schedulerEvents.put("triggersPaused", false);
        schedulerEvents.put("triggerResumed", false);
        schedulerEvents.put("triggersResumed", false);
        listenerEvents.put("scheduler", schedulerEvents);

    }

    public Map<String,Boolean> getListenerEvent(String type){
        return listenerEvents.get(type);
    }

    public void setListenerEvent(String type,String event,Boolean state){
        Map<String, Boolean> events = listenerEvents.get(type);
        synchronized (events) {  // 添加同步块
            events.put(event, state);
        }
    }


}