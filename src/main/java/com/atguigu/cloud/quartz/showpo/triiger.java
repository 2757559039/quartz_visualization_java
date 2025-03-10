package com.atguigu.cloud.quartz.showpo;/**
 * @Auter zzh
 * @Date 2025/2/26
 */

import com.atguigu.cloud.quartz.po.TriggerAbstract;
import lombok.Data;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.TriggerBuilder;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.showpo
 * @className: triiger111
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/26 12:57
 * @version: 1.0
 */
@Data
public class triiger extends TriggerAbstract {

@Override
    public CronTrigger CreateCronTrigger(JobDetail jobDetail) throws ClassNotFoundException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        // 将当前时间加1秒
        LocalDateTime newDateTime = currentDateTime.plusSeconds(1);
        // 定义格式化模式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 将LocalDateTime格式化为字符串
        String formattedDate = newDateTime.format(formatter);
    System.out.println("执行了子类的");
    CronTrigger a=
            TriggerBuilder.newTrigger()
            .withIdentity("1234","1234")
            .withSchedule(CronScheduleBuilder
                    .cronSchedule("0/1 * * * * ?")
            )
            .startAt(Date.valueOf(formattedDate))
            .startNow()
            .forJob(jobDetail)
            // 设置结束时间,默认无穷大
            .endAt(Date.valueOf("2099-12-31"))
            //设置优先级
            .withPriority(5)
            .build();
    System.out.println(a);
        return a;

    }


}
