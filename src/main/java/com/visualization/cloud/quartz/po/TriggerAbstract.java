package com.visualization.cloud.quartz.po;/**
 * @Auter zzh
 * @Date 2024/12/12
 */

import jakarta.annotation.Resource;
import org.quartz.*;

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz.config
 * @className: crearTrigger
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/12 23:25
 * @version: 1.0
 */
public abstract class TriggerAbstract {
    @Resource
    public Scheduler scheduler;

    public SimpleTrigger CreateSimpleTrigger(JobDetail jobDetail) throws Exception {
        System.out.println("执行了抽象类的");
        return null;
    }


    public CalendarIntervalTrigger CreatCalendarIntervalTrigger(JobDetail jobDetail) throws Exception {
        System.out.println("执行了抽象类的");
        return null;
    }

    public DailyTimeIntervalTrigger CreateDailyTimeIntervalTrigger(JobDetail jobDetail) throws Exception {
        System.out.println("执行了抽象类的");
        return null;
    }

    public  CronTrigger CreateCronTrigger(JobDetail jobDetail) throws ClassNotFoundException {
        System.out.println("执行了抽象类的");
        return null;
    }


    public  CronTrigger updateCrontrigger(TriggerBuilder triggerBuilder) throws SchedulerException{
        System.out.println("执行了抽象类的");
        return null;
    }
//同样的update方法来狗仔四个触发器
    public  SimpleTrigger updateSimpletrigger(TriggerBuilder triggerBuilder) throws SchedulerException{
        System.out.println("执行了抽象类的");
        return null;
    }

    public  CalendarIntervalTrigger updateCalendarIntervalTrigger(TriggerBuilder triggerBuilder) throws SchedulerException{
        System.out.println("执行了抽象类的");
        return null;
    }

    public  DailyTimeIntervalTrigger updateDailyTimeIntervalTrigger(TriggerBuilder triggerBuilder) throws SchedulerException{
        System.out.println("执行了抽象类的");
        return null;
    }
}
