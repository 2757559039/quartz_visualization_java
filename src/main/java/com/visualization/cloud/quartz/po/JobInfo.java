package com.visualization.cloud.quartz.po;/**
 * @Auter zzh
 * @Date 2024/12/10
 */

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz
 * @className: JobInfo
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/10 19:46
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@Tag(name = "JobInfo", description = "任务信息")
public class JobInfo implements Serializable {
    private static final long serialVersionUID = 8026140551673459050L;
    private int id;
    //必须项
    // 任务名称,必填
    @Schema(description = "任务名称:必填")
    private String jobname;
    // 任务分组,必填
    @Schema(description = "任务分组:必填")
    private String jobgroup;
    // 任务类,必填
    @Schema(description = "任务分组:必填")
    private String jobclassname;
    // 任务描述,必填,也可以设置成非必填,但前端一定要返回一个东西可以是空的字符串
    @Schema(description = "任务分组:至少需要空字符串")
    private String description;
    //设定触发器的类型,必填,一共有四种类型,SimpleTrigger,CalendarIntervalTrigger,DailyTimeIntervalTrigger,CronTrigger
    @Schema(description = "设定触发器的类型:必填\n四种类型\nSimpleTrigger,CalendarIntervalTrigger,DailyTimeIntervalTrigger,CronTrigger")
    private String type;



//    //给当前时间加上1秒
//    LocalDateTime currentDateTime = LocalDateTime.now();
//    // 在当前时间上加1秒
//    LocalDateTime newDateTime = currentDateTime.plus(1, ChronoUnit.SECONDS);
//    // 将LocalDateTime转换为Date
//    Date newDate = Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());
//    //时间格式化
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //任务开始时间,必填,默认比当前时间晚1秒
    @Schema(description ="任务开始时间:必填")
    private String startime=null;
    //任务结束时间,必填,默认为无穷大
    @Schema(description ="任务结束时间:必填")
    private String endtime=null;
    //优先级,统一默认为5,越小越高,在同一个job下
    @Schema(description ="任务优先级")
    private String priority;





    //是否使用自定义的jobDetail,有默认值为false,如果为true,则需要填写jobDetail
    @Schema(description ="自定义jobDetail是否开启,默认否")
    private String isCustomJobDetail;
    //如果没有开启触发器自动装配,则需要填写类名
    //任务的数据类,如果开启了自定义装配,则为必填项
    @Schema(description ="自定义jobDetail内容")
    private String jobDetail;



    //非必须项


    //是否使用自定义的trigger,有默认值为false,如果为true,则需要填写trigger,此时不应该填写triggername和triggergroup,填了也失效
    private String isCustomTrigger;
    //任务的触发器类,如果开启了自定义装配,则为必填项
    //在updateTriggerargument接口时候,应该使用updatetrigger路径下的类
    private String trigger;

    // 触发器名称,
    // 当isCustomTrigger为true时候,禁止填写,
    // 当isCustomTrigger为false时候,可填可不填
    //在使用updateTriggerargument和jobTOtri接口时,不开启自定义触发器,则必须要填写
    // 当关闭自定义配置的时候胡,默认为任务分组为触发器分组
    private String triggername;
    // 触发器分组,
    // 当isCustomTrigger为true时候,禁止填写,
    // 当isCustomTrigger为false时候,可填可不填
    //在使用updateTriggerargument和jobTOtri接口时,不开启自定义触发器,则必须要填写
    // 当关闭自定义配置的时候胡,默认为任务分组为触发器分组
    private String triggergroup;



    //设置SimpleTrigger的时间
    //withIntervalInSeconds(2)表示每隔2秒执行一次
    private String simpletimesecond;
    //设置一共的执行次数
    private String repeatcount;


    //设置crontrigger的时间
    // 触发器的触发时间cron版本
    private String cronexpression;


    //设置CalendarIntervalTrigger的时间
    //设置时间间隔的单位,可选项为"second,minute,hour,day,week,month,year",默认为秒
    private String calendartime;
    //设置间隔的次数
    private String calendarnum;
//这里涉及到两个配置：preserveHourOfDayAcrossDaylightSavings和skipDayIfHourDoesNotExist，分别表示是否保持夏令时时间的24小时制的小时数和当小时不存在时是否跳过这一天。
//
//以一个2018/3/10 2:43:22触发的间隔为1天的任务来作为demo：
//
//当preserveHourOfDayAcrossDaylightSavings和skipDayIfHourDoesNotExist均为false时，计算出的下一次触发时间是2018/3/11 1:43:22。
//
//当preserveHourOfDayAcrossDaylightSavings为true，skipDayIfHourDoesNotExist为false时，计算出的下一次触发时间是2018/3/11 3:43:22。
//
//当preserveHourOfDayAcrossDaylightSavings为true，skipDayIfHourDoesNotExist为true时，计算出的下一次触发时间是2018/3/12 2:43:22。
//
//夏令时切换冬令时
//不受影响。
//
//冬令时切换夏令时
//当间隔的时间单位大于等于天，并且预期时间是2点时会收到影响，其余不受影响。

    //设置是否保持夏令时时间的24小时制的小时数
    private String preserveHourOfDayAcrossDaylightSavings;
    //设置当小时不存在时是否跳过这一天
    private String skipDayIfHourDoesNotExist;
    //设置时区
    private String timezone;




    //设置DailyTimeIntervalTrigger的时间
    //这个比较特殊,需要设置开始时间和结束时间
    //设置时间间隔的单位,可选项为"second,minute,hour"
    private String dailytime;
    //设置间隔的次数,默认为1
    private String dailynum;
    //设置一共执行的次数,默认为1
    private String dailyrepeatcount;
    //设置一周中执行的天数,可以取"1,2,3,4,5,6,7,all,workday,weekend"这几个值,默认全天
    private Set<String> dailyworkday;
    private String dailystarttime;
    private  String dailyendtime;


//    SimpleTrigger：这种触发器可以在给定时刻触发作业，并且可选择以指定的时间间隔重复。
//    CronTrigger：用过定时任务的小伙伴应该会猜到这个是干什么的吧。这个触发器可以设置一个Cron表达式，指定任务的执行周期。
//    CalendarIntervalTrigger：用于根据重复的日历时间间隔触发。
//    DailyTimeIntervalTrigger：用于根据每天重复的时间间隔触发任务的触发器

    //不需要填写的字段
    //  任务状态PAUSED和NORMAL
    private String triggers_state;
    // 任务状态
    private String job_state;
    //问题1.1.1检测触发器的开始时间和结束时间的要求和通用设置的要求是不是一样的
    //上一次触发时间
    private String PreviousFireTime;
    //下一次触发时间
    private String NextFireTime;
    //还有多久触发
    private String RemainingTime;
    //最终触发时间
    private String FinalFireTime;
    //是否禁止并发执行
    private String isConcurrentExectionDisallowed;
    //触发器的Misfire指令
    private String misfireInstruction;
//用于查询接口的字段
    //触发器在指定时间后的下一次的触发时间
    private String FireTimeAfter;
    private Integer triggersNumbers;







}
