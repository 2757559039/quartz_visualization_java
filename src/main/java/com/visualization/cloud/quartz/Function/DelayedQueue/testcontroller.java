package com.visualization.cloud.quartz.Function.DelayedQueue;/**
 * @Auter zzh
 * @Date 2025/2/21
 */

import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.visualization.cloud.quartz.Function.DelayedQueue.DelayedQueueCreateUtil.createDelayedQueue;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.Function.DelayedQueue
 * @className: testcontroller
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/21 17:57
 * @version: 1.0
 */
@RestController
public class testcontroller {
    @PostMapping("/test")
    public void test() throws SchedulerException {
        createDelayedQueue(new testjob(),"注入!!!!","123456",50L);
    }
}
