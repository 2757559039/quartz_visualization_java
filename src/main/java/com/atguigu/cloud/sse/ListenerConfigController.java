package com.atguigu.cloud.sse;/**
 * @Auter 李孝在
 * @Date 2025/1/26
 */

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.sse
 * @className: asd
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/26 05:40
 * @version: 1.0
 */

import com.atguigu.cloud.util.resp.ResultData;
import com.atguigu.cloud.sse.listener.manager.ListenerEventManager;
import com.atguigu.cloud.sse.listener.manager.ListenerManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.quartz.SchedulerException;

import java.util.Map;

@RestController
@RequestMapping("/sse/config")
@Tag(name = "监听器管理")
public class ListenerConfigController {
    private final ListenerManager listenerManager;
    private final ListenerEventManager listenerEventManager;

    public ListenerConfigController(ListenerManager listenerManager, ListenerEventManager listenerEventManager) {
        this.listenerManager = listenerManager;
        this.listenerEventManager=listenerEventManager;
    }


    @GetMapping("/getListenerStates")
    @Operation(description = "获取监听器状态")
    public ResultData<Map<String, Boolean>> getListenerStates() {
        return ResultData.success(listenerManager.getListenerStates());
    }
    @PostMapping("/getListenerEventStates")
    @Operation(description = "获得不同监听器监听的监听的事件状态")
    public ResultData<Map<String, Boolean>> getListenerEventStates(@RequestParam  @Schema(description = "job|trigger|scheduler") String listener) {
        return ResultData.success(listenerEventManager.getListenerEvent(listener));
    }

    // 切换监听器状态
    @PostMapping("/toggleListenerState")
    @Operation(description = "切换监听器的状态")
    public ResultData<String> toggleListener(
            @RequestParam  @Schema(description = "job|trigger|scheduler" )String listener,
            @RequestParam  @Schema(description = "true|false" ) boolean enabled
    ) throws SchedulerException {
        listenerManager.toggleListener(listener, enabled);
        return ResultData.success("切换成功");
    }

    // 切换监听器状态
    @PostMapping("/toggleListenerEventState")
    @Operation(description = "切换监听器中指定事件的状态")
    public  ResultData<String> toggleListenerEventState(
            @RequestParam  @Schema(description = "job|trigger|scheduler" ) String listener,
            @RequestParam  @Schema(description = "使用/sse/config/getListenerEventStates获得的值" )String event,
            @RequestParam @Schema(description = "true|false" )boolean state
    ) {
        listenerEventManager.setListenerEvent(listener,event,state);
        return ResultData.success("更新成功");
    }

}
