package com.atguigu.cloud.quartz.showpo;/**
 * @Auter zzh
 * @Date 2025/2/26
 */

import com.atguigu.cloud.quartz.po.TriggerAbstract;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.sql.Date;
import java.util.TimeZone;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.showpo
 * @className: update_trigger111
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/26 19:22
 * @version: 1.0
 */
public class update_trigger111 extends TriggerAbstract {
    @Override
    public CronTrigger updateCrontrigger (TriggerBuilder triggerBuilder){
        CronTrigger build =(CronTrigger)  triggerBuilder.withSchedule(
                        CronScheduleBuilder
                                .cronSchedule("2/10 * * * * ?")
                                // 设置时区,默认上海
                                .inTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                )
                .startAt(Date.valueOf("2024-12-13"))
                // 设置结束时间,默认无穷大
                .endAt(Date.valueOf("9999-12-31"))
                //设置优先级
                .withPriority(Integer.parseInt("5"))
                .build();

        return build;

    }
}
