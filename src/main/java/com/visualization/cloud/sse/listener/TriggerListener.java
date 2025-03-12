package com.visualization.cloud.sse.listener;/**
 * @Auter 李孝在
 * @Date 2025/1/25
 */

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.sse
 * @className: asd
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/25 03:53
 * @version: 1.0
 */
import com.visualization.cloud.sse.SSEUtil;
import com.visualization.cloud.sse.listener.manager.ListenerEventManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.listeners.TriggerListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
//触发器监视
public class TriggerListener extends TriggerListenerSupport {
    private final Map<String,Boolean> EventListenerState;
    @Autowired
    public TriggerListener(ListenerEventManager listenerEventManager) {
        this.EventListenerState = listenerEventManager.getListenerEvent("trigger");

    }

    @Override
    public String getName() {
        return "DynamicTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        if (EventListenerState.get("triggerFired")) {
            log.info("触发器即将触发");
            System.out.println("触发器即将触发...");
            subEventInfo(trigger,"触发器即将触发...");
        }
    }

    @Override
    public void triggerComplete(Trigger trigger,
                                JobExecutionContext context,
                                Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        if (EventListenerState.get("triggerComplete")) {

            System.out.println("任务执行完成...");
            subEventInfo(trigger,"触发器已触发...");
        }
    }

    private void subEventInfo(Trigger trigger,Object data){
        TriggerKey triggerKey= trigger.getKey();
        SSEUtil.simpleBroadcastEvent("trigger",data.toString());
        SSEUtil.simpleBroadcastEvent("trigger:"+ triggerKey.getGroup(),data.toString());
        SSEUtil.simpleBroadcastEvent("trigger:"+ triggerKey,data.toString());
    }

}
