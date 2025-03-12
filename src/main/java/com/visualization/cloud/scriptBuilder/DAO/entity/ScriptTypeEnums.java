package com.visualization.cloud.scriptBuilder.DAO.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzh
 * @Auter 李孝在
 * @Date 2025/1/25
 */
@AllArgsConstructor
@Getter
public enum ScriptTypeEnums {
//    SimpleTrigger,CalendarIntervalTrigger,DailyTimeIntervalTrigger,CronTrigger
    JOB("job"),
    JOB_DETAIL("job_detail"),
    SIMPLE_TRIGGER("SimpleTrigger"),
    CALENDAR_INTERVAL_TRIGGER("CalendarIntervalTrigger"),
    DAILY_TIME_INTERVAL_TRIGGER("DailyTimeIntervalTrigger"),
    CRON_TRIGGER("CronTrigger"),
    SIMPLE_UPDATE_TRIGGER("SimpleUpdateTrigger"),
    CALENDAR_UPDATE_INTERVAL_TRIGGER("CalendarUpdateIntervalTrigger"),
    DAILY_UPDATE_TIME_INTERVAL_TRIGGER("DailyUpdateTimeIntervalTrigger"),
    CRON_UPDATE_TRIGGER("CronUpdateTrigger");



    private final String type;

    // 新增方法：获取所有枚举值的 type 字段列表
    public static List<String> getAllTypes() {
        return Arrays.stream(ScriptTypeEnums.values())
                .map(ScriptTypeEnums::getType)
                .collect(Collectors.toList());
    }
}
