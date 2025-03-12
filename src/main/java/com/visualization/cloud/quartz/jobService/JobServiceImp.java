package com.visualization.cloud.quartz.jobService;/**
 * @Auter zzh
 * @Date 2024/12/10
 */



import cn.hutool.core.util.StrUtil;
import com.visualization.cloud.quartz.Function.DelayedQueue.DelayedJob;
import com.visualization.cloud.quartz.Function.DelayedQueue.MyQuartzJobWrapper;
import com.visualization.cloud.quartz.po.*;
import com.visualization.cloud.scriptBuilder.datahandling.VScriptTagScervice;
import com.visualization.cloud.util.DynamicBeanOperate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.DailyTimeIntervalTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.visualization.cloud.quartz.config.SpringContextHolder.getBean;

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz
 * @className: JobServiceImp
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/10 19:47
 * @version: 1.0
 */
@Service
@Slf4j
public class JobServiceImp implements IJobService {
    //定义默认触发器
    @Resource
    private DynamicBeanOperate dynamicBeanOperate;
    @Resource
    private VScriptTagScervice vScriptTagScervice;

    private String TRIGGER_GROUP_NAME="DefaultJobDetail"; // 移除 final 修饰符



    //Springboot已经为我们自动装配了任务调度器Scheduler，
    //无需额外配置便可以注入使用，由Springboot为我们管理调度器
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ApplicationContext applicationContext;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfsfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public int isJobExist(JobKey jobKey) {
        int result = 1;
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            if (jobDetail != null && triggers.size() >0){
                result = triggers.size();
            } else {
                result = -1;
            }
        } catch (SchedulerException e) {
            result = -1;
            log.info("任务不存在！");
        }
        return result;
    }

    @Override
    public List<JobInfo> getAllJobs() throws SchedulerException {
        List<JobInfo> jobInfos = new ArrayList<>();
        try {
            List<String> groups = scheduler.getJobGroupNames();
            int i = 0;
            for(String group :groups) {
                GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupEquals(group);
                Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
                for(JobKey jobKey:jobKeys) {

                    System.out.println("分隔符");
                    System.out.println(jobKey.getName());
                    System.out.println("分割符");
                    //单次任务执行后,会自动删除
                    //如果想保留,可以改为赋值状态
                    var list_Tri=scheduler.getTriggersOfJob(jobKey);

//                    //这里是分类,把没有调度的分开
//                    if (list_Tri.size() == 0) {
//                        continue;
//                    }
                    // 创建一个JobInfo实例来存储作业信息
                    JobInfo jobInfo = new JobInfo();

                    // 获取JobDetail和设置JobInfo
                    // 通过调度器根据作业键获取作业详细信息
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    //设置jobdetail
                    jobInfo.setJobDetail(vScriptTagScervice.getScriptId(jobKey.getName(),jobKey.getGroup(),"JobDetail"));
                    // 设置作业名称
                    jobInfo.setJobname(jobKey.getName());
                    // 设置作业组名
                    jobInfo.setJobgroup(jobKey.getGroup());
                    // 设置作业类名
                    jobInfo.setJobclassname(Arrays.stream(jobDetail.getJobClass().getName().split("\\.")).reduce((first, second) -> second).orElse("返回的字符串为空"));
                    // 设置作业描述
                    jobInfo.setDescription(jobDetail.getDescription());
//                    jobInfo.setJob_state(String.valueOf(scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()))) ); ;
                    //获取job的触发器数量
                    jobInfo.setTriggersNumbers(list_Tri.size());

                    //这里是遍历展示
//                    for(Trigger trigger:list_Tri) {
//                        //设置一个正则表达式,匹配以resumeNow结尾的字符串
//                        String regex = ".*resumeNow$";
//                        //如果匹配成功,则设置为正在执行
//                        if (trigger.getKey().getName().matches(regex)&&trigger.getKey().getGroup().matches(regex)) {
//                            System.out.println("正在执行");
//                            continue;
//                        }
//

//
//                        // 设置触发器类型
//                        triggerVO.setType(Arrays.stream(trigger.getClass().getName().split("\\.")).reduce((first, second) -> second).orElse("返回的字符串为空"));
//
//
//                        // 设置触发器开始时间
//                        triggerVO.setStartime(sdf.format(trigger.getStartTime()));
//
//
//                        // 设置触发器结束时间
//                        if (trigger.getEndTime() == null) {
//                            triggerVO.setEndtime(null);
//                        } else {triggerVO.setEndtime(sdf.format(trigger.getEndTime()));
//                        }
//
//                        // 设置触发器名称
//                        triggerVO.setTriggername(trigger.getKey().getName());
//
//                        // 设置触发器组名
//                        triggerVO.setTriggergroup(trigger.getKey().getGroup());
//                        triggerVO.setTriggers_state(scheduler.getTriggerState(trigger.getKey()).name());
//                        triggerVOList.add(triggerVO);

//                        // 设置触发器的优先级
//                        jobInfo.setPriority(String.valueOf(trigger.getPriority()));
//                        //设置触发器上一次触发时间
//                        jobInfo.setPreviousFireTime(String.valueOf(trigger.getPreviousFireTime()));
//                        //设置触发器下一次触发时间,如果下一次触发的时间小于当前时间则赋值为null
//                        if(trigger.getNextFireTime()!=null){
//                            if (trigger.getNextFireTime().before(new Date(System.currentTimeMillis()))) {
//                                jobInfo.setNextFireTime(null);
//                            } else {
//                                jobInfo.setNextFireTime(String.valueOf(trigger.getNextFireTime()));
//                            }
//                            //设置触发器还有多久触发 ,用下一次的触发时间,减去当前时间,精确到分钟,如果为负数,则赋值为null
//                            long remainingTime = (trigger.getNextFireTime().getTime()-new Date(System.currentTimeMillis()).getTime())/60000;
//                            if (remainingTime < 0) {
//                                jobInfo.setRemainingTime(null);
//                            } else {
//                                jobInfo.setRemainingTime(String.valueOf(remainingTime));
//                            }
//                        }


//                        //设置触发器最终触发时间
//                        if (trigger.getFinalFireTime() == null) {
//                            jobInfo.setFinalFireTime(null);
//                        } else {
//                            jobInfo.setFinalFireTime(String.valueOf(trigger.getFinalFireTime()));
//                        }
//                        //设置触发器的Misfire指令
//                        jobInfo.setMisfireInstruction(String.valueOf(trigger.getMisfireInstruction()));
//                        //设置触发器是否禁止并发执行
//                        jobInfo.setIsConcurrentExectionDisallowed(String.valueOf(jobDetail.isConcurrentExectionDisallowed()));
//
//
//
//                        //这里是分类,把还没有执行的单次任务和周期任务分开
//                        if (trigger.getClass().equals(SimpleTriggerImpl.class)) {
//                            System.out.println("SimpleTrigger");
//                            SimpleTriggerImpl TriggerImpl = (SimpleTriggerImpl)trigger;
//
//                            Trigger jobTrigger = scheduler.getTrigger(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
//
//                            Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
//                            Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
//                            //设置状态
//                            jobInfo.setTriggers_state(tState.name());
//                            jobInfo.setJob_state(jState.name());
//                            //设置专属参数
//                            jobInfo.setSimpletimesecond(String.valueOf(TriggerImpl.getRepeatInterval()));
//                            jobInfo.setRepeatcount(String.valueOf(TriggerImpl.getRepeatCount()));
//                            //jobInfo.setNextfiretime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(TriggerImpl.getNextFireTime()));
//                            //设置其他其次参数为null
//                            jobInfo.setCalendartime(null);
//                            jobInfo.setCalendarnum(null);
//                            jobInfo.setPreserveHourOfDayAcrossDaylightSavings(null);
//                            jobInfo.setSkipDayIfHourDoesNotExist(null);
//                            jobInfo.setTimezone(null);
//                            jobInfo.setDailytime(null);
//                            jobInfo.setDailyworkday(null);
//                            jobInfo.setDailyrepeatcount(null);
//                            jobInfo.setDailynum(null);
//
//                            //设置id
//                            jobInfo.setId(i);
//                            i += 1;
//                            //添加到list
//                            jobInfos.add(jobInfo);
//
//
//                        } else if (trigger.getClass().equals(CronTriggerImpl.class)) {
//                            System.out.println("CronTriggerImpl");
//                            CronTriggerImpl TriggerImpl = (CronTriggerImpl)trigger;
//                            Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
//                            Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
//                            //设置状态
//                            jobInfo.setTriggers_state(tState.name());
//                            jobInfo.setJob_state(jState.name());
//                            //设置专属参数
//                            jobInfo.setCronexpression(TriggerImpl.getCronExpression());
//                            //设置其他其次参数为null
//                            jobInfo.setCalendartime(null);
//                            jobInfo.setSimpletimesecond(null);
//                            jobInfo.setRepeatcount(null);
//                            jobInfo.setCalendarnum(null);
//                            jobInfo.setPreserveHourOfDayAcrossDaylightSavings(null);
//                            jobInfo.setSkipDayIfHourDoesNotExist(null);
//                            jobInfo.setTimezone(null);
//                            jobInfo.setDailytime(null);
//                            jobInfo.setDailyworkday(null);
//                            jobInfo.setDailyrepeatcount(null);
//                            jobInfo.setDailynum(null);
//                            //设置id
//                            jobInfo.setId(i);
//                            i += 1;
//
//                            jobInfos.add(jobInfo);
//                        } else if (trigger.getClass().equals(CalendarIntervalTriggerImpl.class)) {
//                            System.out.println("CalendarIntervalTriggerImpl");
//                            CalendarIntervalTriggerImpl TriggerImpl = (CalendarIntervalTriggerImpl)trigger;
//                            Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
//                            Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
//                            //设置状态
//                            jobInfo.setTriggers_state(tState.name());
//                            jobInfo.setJob_state(jState.name());
//                            //设置专属参数
//                            jobInfo.setCalendartime(TriggerImpl.getRepeatIntervalUnit().name());
//                            jobInfo.setCalendarnum(String.valueOf(TriggerImpl.getRepeatInterval()));
//                            jobInfo.setPreserveHourOfDayAcrossDaylightSavings(String.valueOf(TriggerImpl.isPreserveHourOfDayAcrossDaylightSavings()));
//                            jobInfo.setSkipDayIfHourDoesNotExist(String.valueOf(TriggerImpl.isSkipDayIfHourDoesNotExist()));
//                            jobInfo.setTimezone(TriggerImpl.getTimeZone().getID());
//                            //设置其他其次参数为null
//                            jobInfo.setSimpletimesecond(null);
//                            jobInfo.setRepeatcount(null);
//                            jobInfo.setDailytime(null);
//                            jobInfo.setDailyworkday(null);
//                            jobInfo.setDailyrepeatcount(null);
//                            jobInfo.setDailynum(null);
//                            //设置id
//                            jobInfo.setId(i);
//                            i += 1;
//                            jobInfos.add(jobInfo);
//
//                        } else if (trigger.getClass().equals(DailyTimeIntervalTriggerImpl.class)) {
//                            System.out.println("DailyTimeIntervalTriggerImpl");
//                            DailyTimeIntervalTriggerImpl TriggerImpl = (DailyTimeIntervalTriggerImpl)trigger;
//                            Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
//                            Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
//                            //设置状态
//                            jobInfo.setTriggers_state(tState.name());
//                            jobInfo.setJob_state(jState.name());
//                            //设置专属参数
//                            jobInfo.setDailytime(TriggerImpl.getRepeatIntervalUnit().name());
//
//                            // 假设 TriggerImpl.getDaysOfWeek() 返回的是 Set<Integer>
//                            Set<Integer> daysOfWeek = TriggerImpl.getDaysOfWeek();
//
//                            // 将 Set<Integer> 转换为 Set<String>
//                            Set<String> daysOfWeekString = daysOfWeek.stream()
//                                    .map(String::valueOf)
//                                    .collect(Collectors.toSet());
//                            jobInfo.setDailyworkday(daysOfWeekString);
//
//                            jobInfo.setStartime(TriggerImpl.getStartTimeOfDay().toString());
//                            jobInfo.setEndtime(TriggerImpl.getEndTimeOfDay().toString());
//                            //设置其他其次参数为null
//                            jobInfo.setSimpletimesecond(null);
//                            jobInfo.setRepeatcount(null);
//                            jobInfo.setCalendartime(null);
//                            jobInfo.setCalendarnum(null);
//                            jobInfo.setPreserveHourOfDayAcrossDaylightSavings(null);
//                            jobInfo.setSkipDayIfHourDoesNotExist(null);
//                            jobInfo.setTimezone(null);
//                            //设置id
//                            jobInfo.setId(i);
//                            i += 1;
//                            jobInfos.add(jobInfo);
//
//                        } else {
//                            System.out.println(list_Tri.get(0).getClass());
//                        }
//                    }
//                    jobInfo.setTriggerList(triggerVOList);
                    jobInfos.add(jobInfo);
                }
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return jobInfos;
    }

    @Override
    public List<JobInfo> getAllNoUseJobs(){
        List<JobInfo> jobInfos = new ArrayList<>();
        try {
            List<String> groups = scheduler.getJobGroupNames();
            System.out.println(1);
            System.out.println(scheduler.getCurrentlyExecutingJobs());
            int i = 0;
            for(String group :groups) {
                GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupEquals(group);
                Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
                for(JobKey jobKey:jobKeys) {
                    //单次任务执行后,会自动删除
                    //如果想保留,可以改为赋值状态
                    var list_Tri=scheduler.getTriggersOfJob(jobKey);

                    //这里是分类,把没有调度的分开
                    if (list_Tri.size() == 0) {
                        JobInfo jobInfo = new JobInfo();
                        //获取JobDetail和设置JobInfo
                        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

                        jobDetail.getJobDataMap();

                        jobInfo.setJobname(jobKey.getName());
                        jobInfo.setJobgroup(jobKey.getGroup());
                        jobInfo.setJobclassname(Arrays.stream(jobDetail.getJobClass().getName().split("\\.")).reduce((first, second) -> second).orElse("返回的字符串为空"));

                        jobInfos.add(jobInfo);
                    }
                }
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return jobInfos;
    }


    //通过jobgroup来查找job
    @Override
    public List<JobInfo> getJobByGroup(String jobGroup) {
        List<JobInfo> jobInfos = new ArrayList<>();
        try {
            GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupEquals(jobGroup);
            Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
            for(JobKey jobKey:jobKeys) {
                JobInfo jobInfo = new JobInfo();
                //获取job的触发器数量
                var list_Tri=scheduler.getTriggersOfJob(jobKey);
                jobInfo.setTriggersNumbers(list_Tri.size());
                //获取JobDetail和设置JobInfo
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                jobInfo.setJobname(jobKey.getName());
                jobInfo.setJobgroup(jobKey.getGroup());
                jobInfo.setJobDetail(vScriptTagScervice.getScriptId(jobKey.getName(),jobKey.getGroup(),"JobDetail"));
                jobInfo.setJobclassname(Arrays.stream(jobDetail.getJobClass().getName().split("\\.")).reduce((first, second) -> second).orElse("返回的字符串为空"));
                jobInfo.setDescription(jobDetail.getDescription());
                jobInfos.add(jobInfo);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return jobInfos;
    }

    @Override
    public List<String> getJobName(String jobGroup) throws SchedulerException {
        List<String> jobNames = new ArrayList<>();
        Set<JobKey> jobKeys ;
        if (jobGroup == null) {
             jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
        }else{
            GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupEquals(jobGroup);
            jobKeys = scheduler.getJobKeys(groupMatcher);
        }
        for(JobKey jobKey:jobKeys) {
            jobNames.add(jobKey.getName());
        }
        return jobNames;
    }

    //获取所有的jobgroup分组
    @Override
    public List<String> getJobGroupAll() {
        List<String> jobGroups = new ArrayList<>();
        try {
            jobGroups = scheduler.getJobGroupNames();
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return jobGroups;
    }
    //根据job获取所有相关的trigger
    @Override
    public List<JobInfo> getTriByJob(String jobName, String jobGroup) {
        List<JobInfo> jobInfos = new ArrayList<>();
        try {
            JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            int i = 0;
            //这里是遍历展示
            for(Trigger trigger:triggers) {
                //设置一个正则表达式,匹配以resumeNow结尾的字符串
                String regex = ".*resumeNow$";
                //如果匹配成功,则设置为正在执行
                if (trigger.getKey().getName().matches(regex)&&trigger.getKey().getGroup().matches(regex)) {
                    System.out.println("正在执行");
                    continue;
                }
                // 创建一个JobInfo实例来存储作业信息
                JobInfo jobInfo = new JobInfo();

                // 获取JobDetail和设置JobInfo
                // 通过调度器根据作业键获取作业详细信息
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);


//                // 设置作业名称
//                jobInfo.setJobname(jobKey.getName());
//                // 设置作业组名
//                jobInfo.setJobgroup(jobKey.getGroup());
//                // 设置作业类名
//                jobInfo.setJobclassname(Arrays.stream(jobDetail.getJobClass().getName().split("\\.")).reduce((first, second) -> second).orElse("返回的字符串为空"));
//                // 设置作业描述
//                jobInfo.setDescription(jobDetail.getDescription());
                // 设置触发器类型,删除字符串的后四位
                jobInfo.setType(Arrays.stream(trigger.getClass().getName().split("\\."))
                        .reduce((first, second) -> second)
                        .map(str -> str.length() > 4 ? str.substring(0, str.length() - 4) : str)
                        .orElse("返回的字符串为空"));


//                jobInfo.setType(Arrays.stream(trigger.getClass().getName().split("\\.")).reduce((first, second) -> second).orElse("返回的字符串为空"));
                // 设置触发器开始时间
                jobInfo.setStartime(sdf.format(trigger.getStartTime()));
                // 设置触发器结束时间
                jobInfo.setEndtime(sdf.format(trigger.getEndTime()));
                // 设置触发器名称
                jobInfo.setTriggername(trigger.getKey().getName());
                // 设置触发器组名
                jobInfo.setTriggergroup(trigger.getKey().getGroup());
                // 设置作业详细信息的类名
                jobInfo.setJobDetail(jobDetail.getClass().toString());
                // 设置触发器的类名
                jobInfo.setTrigger(trigger.getClass().toString());
                // 设置触发器的优先级
                jobInfo.setPriority(String.valueOf(trigger.getPriority()));
                //设置触发器上一次触发时间
                if (trigger.getPreviousFireTime() == null) {
                    jobInfo.setPreviousFireTime(null);
                } else {

                    jobInfo.setPreviousFireTime(sdfsfs.format(trigger.getPreviousFireTime()));
                }


                //设置触发器下一次触发时间,如果下一次触发的时间小于当前时间则赋值为null
                if(trigger.getNextFireTime()!=null){
                    if (trigger.getNextFireTime().before(new Date(System.currentTimeMillis()))) {
                        jobInfo.setNextFireTime(null);
                    } else {

                        jobInfo.setNextFireTime(sdfsfs.format(trigger.getNextFireTime()));
                    }
                    //设置触发器还有多久触发 ,用下一次的触发时间,减去当前时间,精确到秒,如果为负数,则赋值为null
                    long remainingTime = (trigger.getNextFireTime().getTime()-new Date(System.currentTimeMillis()).getTime())/1000;
                    if (remainingTime < 0) {
                        jobInfo.setRemainingTime(null);
                    } else {
                        jobInfo.setRemainingTime(String.valueOf(remainingTime));
                    }
                }


                //设置触发器最终触发时间
                if (trigger.getFinalFireTime() == null) {
                    jobInfo.setFinalFireTime(null);
                } else {
//                    jobInfo.setFinalFireTime(String.valueOf(trigger.getFinalFireTime()));
                    jobInfo.setFinalFireTime(sdfsfs.format(trigger.getFinalFireTime()));
                }

                //设置触发器的Misfire指令
                jobInfo.setMisfireInstruction(String.valueOf(trigger.getMisfireInstruction()));
                //设置触发器是否禁止并发执行
                jobInfo.setIsConcurrentExectionDisallowed(String.valueOf(jobDetail.isConcurrentExectionDisallowed()));





                //这里是分类,把还没有执行的单次任务和周期任务分开
                if (trigger.getClass().equals(SimpleTriggerImpl.class)) {
                    System.out.println("SimpleTrigger");
                    SimpleTriggerImpl TriggerImpl = (SimpleTriggerImpl)trigger;

                    Trigger jobTrigger = scheduler.getTrigger(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));

                    Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
                    Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
                    //设置状态
                    jobInfo.setTriggers_state(tState.name());
                    jobInfo.setJob_state(jState.name());
                    //设置专属参数
                    jobInfo.setSimpletimesecond(String.valueOf(TriggerImpl.getRepeatInterval()));
                    jobInfo.setRepeatcount(String.valueOf(TriggerImpl.getRepeatCount()));
                    //jobInfo.setNextfiretime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(TriggerImpl.getNextFireTime()));
                    //设置描述

                    //设置id
                    jobInfo.setId(i);
                    i += 1;
                    //添加到list
                    jobInfos.add(jobInfo);


                } else if (trigger.getClass().equals(CronTriggerImpl.class)) {
                    System.out.println("CronTriggerImpl");
                    CronTriggerImpl TriggerImpl = (CronTriggerImpl)trigger;
                    Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
                    Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
                    //设置状态
                    jobInfo.setTriggers_state(tState.name());
                    jobInfo.setJob_state(jState.name());
                    //设置专属参数
                    jobInfo.setCronexpression(TriggerImpl.getCronExpression());
                    //设置id
                    jobInfo.setId(i);
                    i += 1;

                    jobInfos.add(jobInfo);
                } else if (trigger.getClass().equals(CalendarIntervalTriggerImpl.class)) {
                    System.out.println("CalendarIntervalTriggerImpl");
                    CalendarIntervalTriggerImpl TriggerImpl = (CalendarIntervalTriggerImpl)trigger;
                    Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
                    Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
                    //设置状态
                    jobInfo.setTriggers_state(tState.name());
                    jobInfo.setJob_state(jState.name());
                    //设置专属参数
                    jobInfo.setCalendartime(TriggerImpl.getRepeatIntervalUnit().name());
                    jobInfo.setCalendarnum(String.valueOf(TriggerImpl.getRepeatInterval()));
                    jobInfo.setPreserveHourOfDayAcrossDaylightSavings(String.valueOf(TriggerImpl.isPreserveHourOfDayAcrossDaylightSavings()));
                    jobInfo.setSkipDayIfHourDoesNotExist(String.valueOf(TriggerImpl.isSkipDayIfHourDoesNotExist()));
                    jobInfo.setTimezone(TriggerImpl.getTimeZone().getID());
                    //设置id
                    jobInfo.setId(i);
                    i += 1;
                    jobInfos.add(jobInfo);

                } else if (trigger.getClass().equals(DailyTimeIntervalTriggerImpl.class)) {
                    System.out.println("DailyTimeIntervalTriggerImpl");
                    DailyTimeIntervalTriggerImpl TriggerImpl = (DailyTimeIntervalTriggerImpl)trigger;
                    Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
                    Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
                    //设置状态
                    jobInfo.setTriggers_state(tState.name());
                    jobInfo.setJob_state(jState.name());
                    //设置专属参数

                    jobInfo.setDailytime(TriggerImpl.getRepeatIntervalUnit().name());
                    jobInfo.setDailynum(String.valueOf(TriggerImpl.getRepeatInterval()));
                    jobInfo.setDailyrepeatcount(String.valueOf(TriggerImpl.getRepeatCount()));
                    // 假设 TriggerImpl.getDaysOfWeek() 返回的是 Set<Integer>
                    Set<Integer> daysOfWeek = TriggerImpl.getDaysOfWeek();

                    // 将 Set<Integer> 转换为 Set<String>
                    Set<String> daysOfWeekString = daysOfWeek.stream()
                            .map(String::valueOf)
                            .collect(Collectors.toSet());
                    jobInfo.setDailyworkday(daysOfWeekString);

//                    jobInfo.setStartime(TriggerImpl.getStartTimeOfDay().toString());
//                    jobInfo.setEndtime(TriggerImpl.getEndTimeOfDay().toString());
                    //设置id
                    jobInfo.setId(i);
                    i += 1;
                    jobInfos.add(jobInfo);

                } else {
                    System.out.println(triggers.getClass());
                }

            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return jobInfos;
    }
    //获取所有的trigger
    @Override
    public List<JobInfo> getAllTriggers(String triggerGroup) {
        List<JobInfo> jobInfos = new ArrayList<>();
        try {
                    Set<TriggerKey> triggerKeys ;
                    if (triggerGroup == null) {
                        triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyGroup());
                    }else{
                        GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.groupEquals(triggerGroup);
                        triggerKeys = scheduler.getTriggerKeys(groupMatcher);
                    }
                    for(TriggerKey triggerKey:triggerKeys) {
                        JobInfo jobInfo = new JobInfo();
                        //获取JobDetail和设置JobInfo
                        Trigger trigger = scheduler.getTrigger(triggerKey);

                        jobInfo.setTriggername(triggerKey.getName());
                        jobInfo.setTriggergroup(triggerKey.getGroup());
                        jobInfo.setTrigger(vScriptTagScervice.getScriptId(triggerKey.getName(),triggerKey.getGroup(),jobInfo.getType()));
                        jobInfo.setType(Arrays.stream(trigger.getClass().getName().split("\\."))
                                .reduce((first, second) -> second)
                                .map(str -> str.length() > 4 ? str.substring(0, str.length() - 4) : str)
                                .orElse("返回的字符串为空"));
                        jobInfo.setStartime(sdf.format(trigger.getStartTime()));
                        if (trigger.getEndTime() == null) {
                            jobInfo.setEndtime(null);
                        } else {
                            jobInfo.setEndtime(sdf.format(trigger.getEndTime()));
                        }
                        jobInfo.setTriggers_state(scheduler.getTriggerState(triggerKey).name());
                        jobInfo.setPriority(String.valueOf(trigger.getPriority()));
                        jobInfos.add(jobInfo);
                    }



        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return jobInfos;

    }


    //获取所有的trigger分组
    @Override
    public List<String> getTriggerGroupAll() {
        List<String> triggerGroups = new ArrayList<>();
        try {
            triggerGroups = scheduler.getTriggerGroupNames();
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return triggerGroups;
    }

    //根据triggergroup来查找trigger
    @Override
    public List<JobInfo> getTriggerByGroup(String triggerGroup) {
        List<JobInfo> jobInfos = new ArrayList<>();
        try {
            GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.groupEquals(triggerGroup);
            Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(groupMatcher);
            for(TriggerKey triggerKey:triggerKeys) {
                JobInfo jobInfo = new JobInfo();
                //获取JobDetail和设置JobInfo
                Trigger trigger = scheduler.getTrigger(triggerKey);
                jobInfo.setTriggername(triggerKey.getName());
                jobInfo.setTriggergroup(triggerKey.getGroup());

                jobInfos.add(jobInfo);
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return jobInfos;
    }
    @Override
    public List<String> getTriggerName(String triggerGroup) throws SchedulerException {
        List<String> triggerNames = new ArrayList<>();
        Set<TriggerKey> triggerKeys ;
        if (triggerGroup == null) {
            triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyGroup());
        }else{
            GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.groupEquals(triggerGroup);
            triggerKeys = scheduler.getTriggerKeys(groupMatcher);
        }
        for(TriggerKey triggerKey:triggerKeys) {
            triggerNames.add(triggerKey.getName());
        }
        return triggerNames;
    }
    //获取trigger的详情
    @Override
    public JobInfo getTriggerInfo(String triggername, String triggergroup) throws SchedulerException {

        JobInfo jobInfo = new JobInfo();
        System.out.println(jobInfo.getStartime());
        JobKey jobKey = JobKey.jobKey(triggername,triggergroup);

        // 获取JobDetail和设置JobInfo
        // 通过调度器根据作业键获取作业详细信息
        Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(triggername,triggergroup));
        if(trigger == null){
            return null;
        }
        JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
//        // 设置作业名称
//        jobInfo.setJobname(jobKey.getName());
//        // 设置作业组名
//        jobInfo.setJobgroup(jobKey.getGroup());
//        // 设置作业类名
//        jobInfo.setJobclassname(Arrays.stream(jobDetail.getJobClass().getName().split("\\.")).reduce((first, second) -> second).orElse("返回的字符串为空"));
//        // 设置作业描述
//        jobInfo.setDescription(jobDetail.getDescription());
//        // 设置触发器类型
//        jobInfo.setType(Arrays.stream(trigger.getClass().getName().split("\\.")).reduce((first, second) -> second).orElse("返回的字符串为空"));
//        // 设置触发器开始时间
//        jobInfo.setStartime(sdf.format(trigger.getStartTime()));
//
//        // 设置触发器结束时间
//        jobInfo.setEndtime(sdf.format(trigger.getEndTime()));
//
//        // 设置触发器名称
//        jobInfo.setTriggername(trigger.getKey().getName());
//        // 设置触发器组名
//        jobInfo.setTriggergroup(trigger.getKey().getGroup());
//        // 设置作业详细信息的类名
//        jobInfo.setJobDetail(jobDetail.getClass().toString());
//        // 设置触发器的类名
//        jobInfo.setTrigger(trigger.getClass().toString());
//        // 设置触发器的优先级
//        jobInfo.setPriority(String.valueOf(trigger.getPriority()));
        //设置触发器上一次触发时间
        if (trigger.getPreviousFireTime() == null) {
            jobInfo.setPreviousFireTime(null);
        } else {
            jobInfo.setPreviousFireTime(sdfsfs.format(trigger.getPreviousFireTime()));
        }
        jobInfo.setTrigger(vScriptTagScervice.getScriptId(triggername,triggergroup,jobInfo.getType()));

        //设置触发器下一次触发时间,如果下一次触发的时间小于当前时间则赋值为null
        if(trigger.getNextFireTime()!=null){
            if (trigger.getNextFireTime().before(new Date(System.currentTimeMillis()))) {
                jobInfo.setNextFireTime(null);
            } else {
                jobInfo.setNextFireTime(sdfsfs.format(trigger.getNextFireTime()));
            }
            //设置触发器还有多久触发 ,用下一次的触发时间,减去当前时间,精确到秒,如果为负数,则赋值为null
            long remainingTime = (trigger.getNextFireTime().getTime()-new Date(System.currentTimeMillis()).getTime())/1000;
            if (remainingTime < 0) {
                jobInfo.setRemainingTime(null);
            } else {

                jobInfo.setRemainingTime(String.valueOf(remainingTime));
            }
        }

        //设置触发器最终触发时间
        if (trigger.getFinalFireTime() == null) {
            System.out.println("没有最终触发时间");
            jobInfo.setFinalFireTime(null);
        } else {

            jobInfo.setFinalFireTime(sdfsfs.format(trigger.getFinalFireTime()));
        }
        //设置触发器的Misfire指令
        jobInfo.setMisfireInstruction(String.valueOf(trigger.getMisfireInstruction()));
        //设置触发器是否禁止并发执行
        jobInfo.setIsConcurrentExectionDisallowed(String.valueOf(jobDetail.isConcurrentExectionDisallowed()));





        //这里是分类,把还没有执行的单次任务和周期任务分开
        if (trigger.getClass().equals(SimpleTriggerImpl.class)) {
            System.out.println("SimpleTrigger");
            SimpleTriggerImpl TriggerImpl = (SimpleTriggerImpl)trigger;

            Trigger jobTrigger = scheduler.getTrigger(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));

            Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
            Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
            //设置状态
            jobInfo.setTriggers_state(tState.name());
            jobInfo.setJob_state(jState.name());
            //设置专属参数
            jobInfo.setSimpletimesecond(String.valueOf(TriggerImpl.getRepeatInterval()));
            jobInfo.setRepeatcount(String.valueOf(TriggerImpl.getRepeatCount()));
            //jobInfo.setNextfiretime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(TriggerImpl.getNextFireTime()));
            //设置描述



        } else if (trigger.getClass().equals(CronTriggerImpl.class)) {
            System.out.println("CronTriggerImpl");
            CronTriggerImpl TriggerImpl = (CronTriggerImpl)trigger;
            Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
            Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
            //设置状态
            jobInfo.setTriggers_state(tState.name());
            jobInfo.setJob_state(jState.name());
            //设置专属参数
            jobInfo.setCronexpression(TriggerImpl.getCronExpression());
            //设置id
        } else if (trigger.getClass().equals(CalendarIntervalTriggerImpl.class)) {
            System.out.println("CalendarIntervalTriggerImpl");
            CalendarIntervalTriggerImpl TriggerImpl = (CalendarIntervalTriggerImpl)trigger;
            Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
            Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
//            //设置状态
//            jobInfo.setTriggers_state(tState.name());
//            jobInfo.setJob_state(jState.name());
            //设置专属参数
            jobInfo.setCalendartime(TriggerImpl.getRepeatIntervalUnit().name());
            jobInfo.setCalendarnum(String.valueOf(TriggerImpl.getRepeatInterval()));
            jobInfo.setPreserveHourOfDayAcrossDaylightSavings(String.valueOf(TriggerImpl.isPreserveHourOfDayAcrossDaylightSavings()));
            jobInfo.setSkipDayIfHourDoesNotExist(String.valueOf(TriggerImpl.isSkipDayIfHourDoesNotExist()));
            jobInfo.setTimezone(TriggerImpl.getTimeZone().getID());

        } else if (trigger.getClass().equals(DailyTimeIntervalTriggerImpl.class)) {
            System.out.println("DailyTimeIntervalTriggerImpl");
            DailyTimeIntervalTriggerImpl TriggerImpl = (DailyTimeIntervalTriggerImpl)trigger;
            Trigger.TriggerState tState = scheduler.getTriggerState(TriggerKey.triggerKey(TriggerImpl.getName(),TriggerImpl.getGroup()));
            Trigger.TriggerState jState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(),jobKey.getGroup()));
//            //设置状态
//            jobInfo.setTriggers_state(tState.name());
//            jobInfo.setJob_state(jState.name());
            //设置专属参数
            jobInfo.setDailystarttime(TriggerImpl.getStartTimeOfDay().getHour() +":"+ TriggerImpl.getStartTimeOfDay().getMinute() +":"+ TriggerImpl.getStartTimeOfDay().getSecond());
            jobInfo.setDailyendtime(TriggerImpl.getEndTimeOfDay().getHour() +":"+ TriggerImpl.getEndTimeOfDay().getMinute() +":"+ TriggerImpl.getEndTimeOfDay().getSecond());
            jobInfo.setDailytime(TriggerImpl.getRepeatIntervalUnit().name());
            jobInfo.setDailynum(String.valueOf(TriggerImpl.getRepeatInterval()));
            jobInfo.setDailyrepeatcount(String.valueOf(TriggerImpl.getRepeatCount()));

            // 假设 TriggerImpl.getDaysOfWeek() 返回的是 Set<Integer>
            Set<Integer> daysOfWeek = TriggerImpl.getDaysOfWeek();

            // 将 Set<Integer> 转换为 Set<String>
            Set<String> daysOfWeekString = daysOfWeek.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
            jobInfo.setDailyworkday(daysOfWeekString);


//            jobInfo.setStartime(TriggerImpl.getStartTimeOfDay().toString());
//            jobInfo.setEndtime(TriggerImpl.getEndTimeOfDay().toString());
            //设置id

        } else {
            System.out.println(trigger.getClass());
        }
        System.out.println(jobInfo.getStartime());
        return jobInfo;
    }

    @Override
    public String getNextFireTime(String triggername, String triggergroup, java.util.Date specifiedTime) throws SchedulerException {
        System.out.println(triggername+" "+triggergroup);
        //鉴定Trigger是否存在
        boolean a= scheduler.checkExists(TriggerKey.triggerKey(triggername,triggergroup));
        if (!a) {
            return "Trigger不存在或者处于暂停状态";
        }
        try {
            Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(triggername,triggergroup));
            //获取trigger的开始时间和结束时间
            java.util.Date startTime = trigger.getStartTime();
            java.util.Date endTime = trigger.getEndTime();

            //如果开始时间大于指定时间,则返回开始时间
            if (startTime.after(specifiedTime)) {
                return "指定时间不能小于开始时间"+"开始时间为"+startTime;
            }
            //如果结束时间小于指定时间,则返回结束时间
            if (endTime.before(specifiedTime)) {
                return "指定时间不能大于结束时间"+"结束时间为"+endTime;
            }
             java.util.Date nextFireTime = trigger.getFireTimeAfter(specifiedTime);
            if (nextFireTime == null) {
                return "没有下一次触发时间";
            }
            var sfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sfm.format(nextFireTime);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }
    @Override
    public boolean isAllPaused() throws SchedulerException {
        Set<String> triggerGroups = scheduler.getPausedTriggerGroups();
        System.out.println( triggerGroups.contains("_$_ALL_GROUPS_PAUSED_$_"));
        return triggerGroups.contains("_$_ALL_GROUPS_PAUSED_$_");
    }



    @Override
    public boolean resumeJob(String jobName, String jobGroup) throws SchedulerException {

        boolean result = true;
        //鉴定job是否存在
        boolean a= scheduler.checkExists(JobKey.jobKey(jobName,jobGroup));
        if (!a) {
            return false;
        }
        try {
            scheduler.resumeJob(JobKey.jobKey(jobName,jobGroup));
        } catch (SchedulerException e) {
            result = false;
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean resumeTri(String triggername, String triggergroup) throws SchedulerException {
        boolean result = true;
        //鉴定job是否存在
        boolean a= scheduler.checkExists(TriggerKey.triggerKey(triggername,triggergroup));
        if (!a) {
            return false;
        }
        try {
            scheduler.resumeTrigger(TriggerKey.triggerKey(triggername,triggergroup));
        } catch (SchedulerException e) {
            result = false;
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean resumeAllJob() {
    try {
        scheduler.resumeAll();
    }catch (SchedulerException e) {
        log.error(e.getMessage());
        return false;
    }
    return true;
    }

    @Override
    public boolean resumeNow(String jobName, String jobGroup) throws SchedulerException {
        boolean result = true;
        //鉴定job是否存在
        boolean a= scheduler.checkExists(JobKey.jobKey(jobName,jobGroup));
        if (!a) {
            return false;
        }
        // 3. 尝试获取该触发器
        TriggerKey triggerKey = new TriggerKey(jobName+"resumeNow", jobGroup+"resumeNow");
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            System.out.println("触发器已自动销毁！");
        } else {
            System.out.println("触发器仍存在！");
            return false;
        }

        JobKey jobKey = JobKey.jobKey(jobName,jobGroup);
        Trigger immediateTrigger = TriggerBuilder.newTrigger()
                .forJob(jobKey)
                .withIdentity(jobName+"resumeNow", jobGroup+"resumeNow")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
                .build();

        scheduler.scheduleJob(immediateTrigger);


        return result;
    }

    @Override
    public boolean pauseJob(String jobName, String jobGroup) throws SchedulerException {
        boolean result = true;
        //鉴定job是否存在
        boolean a= scheduler.checkExists(JobKey.jobKey(jobName,jobGroup));
        if (!a) {
            return false;
        }
        try {

            scheduler.pauseJob(JobKey.jobKey(jobName,jobGroup));

        } catch (SchedulerException e) {
            System.out.println(e.getMessage());
            result = false;
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public  boolean pauseTri(String triggername, String triggergroup) throws SchedulerException {
        boolean result = true;
        //鉴定Trigger是否存在
        boolean a= scheduler.checkExists(TriggerKey.triggerKey(triggername,triggergroup));
        if (!a) {
            return false;
        }
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(triggername,triggergroup));
        } catch (SchedulerException e) {
            System.out.println(e.getMessage());
            result = false;
            log.error(e.getMessage());
        }
        return result;
    }
    //暂停全部
    @Override
    public boolean pauseAllJob() {
        try {
            scheduler.pauseAll();
        }catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean reScheduleJob(String triggername, String triggergroup, String cronExpression) throws SchedulerException {
        //判断当前状态
        boolean result = true;
        boolean a= scheduler.checkExists(TriggerKey.triggerKey(triggername,triggergroup));
        if (!a) {
            return false;
        }

        try {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(TriggerKey.triggerKey(triggername,triggergroup));
            CronTrigger cronTriggerOld = (CronTrigger)scheduler.getTrigger(TriggerKey.triggerKey(triggername,triggergroup));
            if (!cronTriggerOld.getCronExpression().equals(cronExpression)){
                CronTrigger cronTriggerNew = TriggerBuilder.newTrigger().withIdentity(triggername,triggergroup)
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                        .build();
                scheduler.rescheduleJob(TriggerKey.triggerKey(triggername,triggergroup),cronTriggerNew);
                System.out.println(triggerState.name());
                if (triggerState.name().equals("PAUSED")) {
                    this.pauseJob(triggername,triggergroup);
                }
            }

        } catch (SchedulerException e) {
            result = false;
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public boolean deleteJob(String jobName, String jobGroup) throws SchedulerException {
        String triggername=null;
        String triggergroup=null;
        boolean result = true;
        //鉴定job是否存在
        boolean a= scheduler.checkExists(JobKey.jobKey(jobName,jobGroup));
        if (!a) {
            return false;
        }
        try {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(JobKey.jobKey(jobName,jobGroup));
            if (triggers.size() > 0) {
                if (!"PAUSED".equals(scheduler.getTriggerState(TriggerKey.triggerKey(jobName,jobGroup)).name())) {
                    triggername=triggers.get(0).getKey().getName();
                    triggergroup=triggers.get(0).getKey().getGroup();
                    scheduler.pauseTrigger(TriggerKey.triggerKey(jobName,jobGroup));
                }

                scheduler.unscheduleJob(TriggerKey.triggerKey(jobName,jobGroup));
                vScriptTagScervice.deleteScriptTag(triggername,triggergroup);
            }
            scheduler.deleteJob(JobKey.jobKey(jobName,jobGroup));
            //
            vScriptTagScervice.deleteScriptTag(jobName,jobGroup);

        } catch (SchedulerException e) {
            result = false;
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public int addJob(JobInfo jobInfo) throws SchedulerException {
        System.out.println(jobInfo);
//        //默认触发器名称和分组
//        if (jobInfo.getTriggername()==null){
//            jobInfo.setTriggername(jobInfo.getJobname());
//        }
//        if (jobInfo.getTriggergroup()==null){
//            jobInfo.setTriggergroup(jobInfo.getJobgroup());
//        }

        int result = 0;
        //必填字段检查
        if (jobInfo.getJobname()==null || jobInfo.getJobgroup()==null || jobInfo.getJobclassname()==null) {
            log.error("任务名称,任务分组,任务类不能为空");
            return -5;
        }
        int isjobexist=isJobExist(JobKey.jobKey(jobInfo.getJobname(),jobInfo.getJobgroup()));
        if (isjobexist ==1) {
            log.error("任务已存在");
            return -1 ;
        }
        if(jobInfo.getIsCustomTrigger().equals("false")){
             if (scheduler.checkExists(TriggerKey.triggerKey(jobInfo.getTriggername(),jobInfo.getTriggergroup()))) {
                log.error("调度器已存在");
                return -2;
            }
        }

            try {

                //字段检查
                //给isCustomJobDetail设置默认值
                if (jobInfo.getIsCustomJobDetail()==null){
                    jobInfo.setIsCustomJobDetail("false");
                }


                if (jobInfo.getJobDetail()==null&&jobInfo.getIsCustomJobDetail().equals("true")){
                    log.error("开启自动注入jobDetail时,jobDetail不能为空");
                    return -7;
                }

                //默认关闭自定义注入,设置默认的jobDetail
                if ("false".equals(jobInfo.getIsCustomJobDetail())){
                    jobInfo.setJobDetail(TRIGGER_GROUP_NAME);
                }else {
                    vScriptTagScervice.addScriptTag(jobInfo.getJobname(),jobInfo.getJobgroup(),jobInfo.getJobDetail(),"JobDetail");
                }
                System.out.println("当前使用的jobDetail是:"+jobInfo.getJobDetail());
                //检查给startime设置默认值
                if (jobInfo.getStartime()==null){
                    // 获取当前时间
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    // 将当前时间加1秒
                    LocalDateTime newDateTime = currentDateTime.plusSeconds(1);
                    // 定义格式化模式
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    // 将LocalDateTime格式化为字符串
                    String formattedDate = newDateTime.format(formatter);
                    // 设置开始时间
                    jobInfo.setStartime(formattedDate);
                }

                //检查给endtime设置默认值
                if (jobInfo.getEndtime()==null){
                    jobInfo.setEndtime("2099-12-31");
                }
                //检查给priority设置默认值
                if (jobInfo.getPriority()==null){
                    jobInfo.setPriority("5");
                }

                JobDetail jobDetail = null;
//                通过反射获取JobDetail
                Class<? extends CreatJobDetail> c = applicationContext.getType( jobInfo.getJobDetail())
                        .asSubclass(CreatJobDetail.class);
                Constructor<?> constructor = c.getDeclaredConstructor(ApplicationContext.class); // 参数类型列表
                Object instance = constructor.newInstance(applicationContext);
                CreatJobDetail creatJobDetail = (CreatJobDetail) instance;

                jobDetail=creatJobDetail.createdetail(jobInfo.getJobclassname(),jobInfo.getJobname(),jobInfo.getJobgroup(),jobInfo.getDescription());

//                new DefaultJobDetail111(jobInfo.);
                //type字段检查
                if (jobInfo.getType()==null){
                    log.error("类型为空");
                    return -3;
                }

                String type;
                switch (jobInfo.getType()){
                    case "SimpleTrigger":
                        type="SimpleTrigger";
                        break;
                    case "CalendarIntervalTrigger":
                        type="CalendarIntervalTrigger";
                        break;
                    case "DailyTimeIntervalTrigger":
                        type="DailyTimeIntervalTrigger";
                        break;
                    case "CronTrigger":
                        type="CronTrigger";
                        break;
                    default:
                        log.error("类型错误");
                        return -4;
                }
                //配置isCustomTrigger的默认值
                if (jobInfo.getIsCustomTrigger()==null){
                    jobInfo.setIsCustomTrigger("false");
                }
                //默认关闭自定义注入,设置默认的触发器
                if (jobInfo.getTrigger()==null&&jobInfo.getIsCustomTrigger().equals("true")){
                    log.error("开启自动注入trigger时,trigger不能为空");
                    return -6;
                }
                //确定有类型而且开启了自定义的才使用这个配置
                if (jobInfo.getIsCustomTrigger().equals("true")){

                    //用switch匹配jobinfo根据类型创建不同的触发器,注意新加的触发器,必须要接收jobDetail
                    switch (type) {
                        case "SimpleTrigger":
                            TriggerAbstract Simple =  getBean( jobInfo.getTrigger());
                            SimpleTrigger simpleTrigger= Simple.CreateSimpleTrigger(jobDetail);
                            scheduler.scheduleJob(jobDetail,simpleTrigger);


                            vScriptTagScervice.addScriptTag(simpleTrigger.getKey().getName(), simpleTrigger.getKey().getGroup(),jobInfo.getTrigger(),jobInfo.getType());
                            break;
                        case "CalendarIntervalTrigger":
                            TriggerAbstract Calendar =  getBean( jobInfo.getTrigger());
                            CalendarIntervalTrigger CalendarTrigger= Calendar.CreatCalendarIntervalTrigger(jobDetail);
                            scheduler.scheduleJob(jobDetail,CalendarTrigger);
                            vScriptTagScervice.addScriptTag(CalendarTrigger.getKey().getName(),CalendarTrigger.getKey().getGroup(),jobInfo.getTrigger(),jobInfo.getType());
                            break;
                        case "DailyTimeIntervalTrigger":
                            TriggerAbstract Daily =  getBean( jobInfo.getTrigger());
                            DailyTimeIntervalTrigger DailyTrigger=  Daily.CreateDailyTimeIntervalTrigger(jobDetail);
                            scheduler.scheduleJob(jobDetail,DailyTrigger);
                            vScriptTagScervice.addScriptTag(DailyTrigger.getKey().getName(),DailyTrigger.getKey().getGroup(),jobInfo.getTrigger(),jobInfo.getType());
                            break;
                        case "CronTrigger":
                            TriggerAbstract Cron =  getBean( jobInfo.getTrigger());
                            CronTrigger cronTrigger= Cron.CreateCronTrigger(jobDetail);
                            //这里要jobDetail进行一个绑定
                            scheduler.scheduleJob(jobDetail,cronTrigger);
                            vScriptTagScervice.addScriptTag(cronTrigger.getKey().getName(),cronTrigger.getKey().getGroup(),jobInfo.getTrigger(),jobInfo.getType());
                            break;
                        default:
                            log.error("类型错误");
                            return -4;
                    }
                    return result;
                }

                //这里是用默认的触发器
                switch (type) {
                    case "SimpleTrigger":
                        //设置simpletimesecond的默认值
                        if (jobInfo.getSimpletimesecond()==null){
                            jobInfo.setSimpletimesecond("1");
                        }
                        //设置repeatcount的默认值
                        if (jobInfo.getRepeatcount()==null){
                            jobInfo.setRepeatcount("0");
                        }

                        Trigger SimpleTrigger = TriggerBuilder.newTrigger()
                                .withIdentity(jobInfo.getTriggername(),jobInfo.getTriggergroup())
                                //.startAt(sdf.parse(jobInfo.getNextfiretime()))
                                .withSchedule(SimpleScheduleBuilder
                                        .simpleSchedule()
                                        // 设置时间间隔,单位秒
                                        .withIntervalInSeconds(Integer.parseInt(jobInfo.getSimpletimesecond()))
                                        //设置执行次数,底层实现是repeatCount+1,也就是总共执行repeatCount+1次
                                        .withRepeatCount(Integer.parseInt(jobInfo.getRepeatcount()))
                                )
                                .startAt(Date.valueOf(jobInfo.getStartime()))
                                // 设置结束时间,默认无穷大
                                .endAt(Date.valueOf(jobInfo.getEndtime()))
                                //设置优先级
                                .withPriority(Integer.parseInt(jobInfo.getPriority()))

                                .forJob(jobDetail)
                                .startNow()
                                .build();
                        scheduler.scheduleJob(jobDetail,SimpleTrigger);
                        break;
                        //建议这两个触发器用自定义来实现
                    case "CalendarIntervalTrigger":

                        //设置calendartime的默认值
                        if (jobInfo.getCalendartime()==null){
                            jobInfo.setCalendartime("second");
                        }
                        //设置calendarnum的默认值
                        if (jobInfo.getCalendarnum()==null){
                            jobInfo.setCalendarnum("1");
                        }
                        //设置timezone的默认值
                        if (jobInfo.getTimezone()==null){
                            jobInfo.setTimezone("Asia/Shanghai");
                        }
                        //设置preserveHourOfDayAcrossDaylightSavings的默认值
                        if (jobInfo.getPreserveHourOfDayAcrossDaylightSavings()==null){
                            jobInfo.setPreserveHourOfDayAcrossDaylightSavings("true");
                        }
                        //设置skipDayIfHourDoesNotExist的默认值
                        if (jobInfo.getSkipDayIfHourDoesNotExist()==null){
                            jobInfo.setSkipDayIfHourDoesNotExist("true");
                        }


                        DateBuilder.IntervalUnit time = null;
                        switch (jobInfo.getCalendartime()){
                            case "second":
                                    time= DateBuilder.IntervalUnit.SECOND;
                                break;
                            case "minute":
                                    time= DateBuilder.IntervalUnit.MINUTE;
                                break;
                            case "hour":
                                    time= DateBuilder.IntervalUnit.HOUR;
                                break;
                            case "day":
                                    time= DateBuilder.IntervalUnit.DAY;
                                break;
                            case "week":
                                    time= DateBuilder.IntervalUnit.WEEK;
                                break;
                            case "month":
                                    time= DateBuilder.IntervalUnit.MONTH;
                                break;
                            case "year":
                                    time= DateBuilder.IntervalUnit.YEAR;
                                break;
                        }

                        Trigger CalendarIntervalTrigger = TriggerBuilder.newTrigger()
                                .withIdentity(jobInfo.getTriggername(),jobInfo.getTriggergroup())
                                .withSchedule(CalendarIntervalScheduleBuilder
                                                .calendarIntervalSchedule()
                                                // 设置是否跨日光保存时间保留小时
                                                .preserveHourOfDayAcrossDaylightSavings("true".equals(jobInfo.getPreserveHourOfDayAcrossDaylightSavings()))
                                                // 如果小时不存在则跳过
                                                .skipDayIfHourDoesNotExist("true".equals(jobInfo.getSkipDayIfHourDoesNotExist()))
                                                // 设置时区,默认上海
                                                .inTimeZone(TimeZone.getTimeZone(jobInfo.getTimezone()))
                                        // 指定生成触发器的时间单位和间隔,每月执行一次
                                                 .withInterval(Integer.parseInt(jobInfo.getCalendarnum()),time)
                                )
                                .forJob(jobDetail)
                                // 设置开始时间,默认是当前时间后1秒
                                .startAt(Date.valueOf(jobInfo.getStartime()))
                                // 设置结束时间,默认无穷大
                                .endAt(Date.valueOf(jobInfo.getEndtime()))
                                .startNow()
                                //设置优先级
                                .withPriority(Integer.parseInt(jobInfo.getPriority()))
                                .build();
                        scheduler.scheduleJob(jobDetail,CalendarIntervalTrigger);
                        break;

                    case "DailyTimeIntervalTrigger":
                        //设置dailytime的默认值
                        if (jobInfo.getDailytime()==null){
                            jobInfo.setDailytime("second");
                        }
                        //设置dailyworkday的默认值
                        if (jobInfo.getDailyworkday()==null){
                            jobInfo.setDailyworkday(Set.of("all"));
                        }
                        //设置dailynum的默认值
                        if (jobInfo.getDailynum()==null){
                            jobInfo.setDailynum("1");
                        }
                        //设置dailyrepeatcount的默认值
                        if (jobInfo.getDailyrepeatcount()==null){
                            jobInfo.setDailyrepeatcount("0");
                        }
                        int starthour =00;
                        int startminute =00;
                        int startsecond =00;
                        var starttimeOfDay = TimeOfDay.hourMinuteAndSecondOfDay(starthour, startminute, startsecond);

                        if (jobInfo.getDailystarttime()!=null){
                            var splitstarttime = jobInfo.getDailystarttime().split(":");
                            starthour =Integer.parseInt(splitstarttime[0]);
                            startminute =Integer.parseInt(splitstarttime[1]);
                            startsecond =Integer.parseInt(splitstarttime[2]);
                            starttimeOfDay = TimeOfDay.hourMinuteAndSecondOfDay(starthour, startminute, startsecond);
                        }

                        int endhour =23;
                        int endminute =59;
                        int endsecond =59;
                        var endtimeOfDay = TimeOfDay.hourMinuteAndSecondOfDay(endhour, endminute, endsecond);

                        if (jobInfo.getDailyendtime()!=null){
                            var splitendtime = jobInfo.getDailyendtime().split(":");
                            endhour =Integer.parseInt(splitendtime[0]);
                            endminute =Integer.parseInt(splitendtime[1]);
                            endsecond =Integer.parseInt(splitendtime[2]);
                            endtimeOfDay = TimeOfDay.hourMinuteAndSecondOfDay(endhour, endminute, endsecond);
                        }
                        DateBuilder.IntervalUnit Dailytime = switch (jobInfo.getDailytime()) {
                            case "second" -> DateBuilder.IntervalUnit.SECOND;
                            case "minute" -> DateBuilder.IntervalUnit.MINUTE;
                            case "hour" -> DateBuilder.IntervalUnit.HOUR;
                            default -> null;
                        };


                        Set<Integer> daysOfWeek = new HashSet<>();
                        //用for循环遍历jobInfo.getDailyworkday()这个列表的每一个元素,然后用switch"1,2,3,4,5,6,7,all,workday,weekend"依次添加到daysOfWeek,使用java.util.Calendar,用if包围daysOfWeek.add方法,如果为null则返回result -10,同时记录错误日志
                        for (String day:jobInfo.getDailyworkday()) {
                            switch (day){
                                case "1":
                                    daysOfWeek.add(1);
                                    break;
                                case "2":
                                    daysOfWeek.add(2);
                                    break;
                                case "3":
                                    daysOfWeek.add(3);
                                    break;
                                case "4":
                                    daysOfWeek.add(4);
                                    break;
                                case "5":
                                    daysOfWeek.add(5);
                                    break;
                                case "6":
                                    daysOfWeek.add(6);
                                    break;
                                case "7":
                                    daysOfWeek.add(7);
                                    break;
                                case "all":
                                    daysOfWeek.add(1);
                                    daysOfWeek.add(2);
                                    daysOfWeek.add(3);
                                    daysOfWeek.add(4);
                                    daysOfWeek.add(5);
                                    daysOfWeek.add(6);
                                    daysOfWeek.add(7);
                                    break;
                                case "workday":
                                    daysOfWeek.add(2);
                                    daysOfWeek.add(3);
                                    daysOfWeek.add(4);
                                    daysOfWeek.add(5);
                                    daysOfWeek.add(1);
                                    break;
                                case "weekend":
                                    daysOfWeek.add(6);
                                    daysOfWeek.add(7);
                                    break;
                                default:
                                    log.error("日期错误");
                                    return -6;
                            }
                        }

                        Trigger DailyTimeIntervalTrigger = TriggerBuilder.newTrigger()
                                .withIdentity(jobInfo.getTriggername(),jobInfo.getTriggergroup())
                                .withSchedule(DailyTimeIntervalScheduleBuilder
                                                // 使用dailyTimeIntervalSchedule
                                                .dailyTimeIntervalSchedule()
                                                // 设置开始时间,默认是当前时间
                                                .startingDailyAt(starttimeOfDay)
                                                // 设置结束时间，默认是无穷大
                                                .endingDailyAt(endtimeOfDay)
                                                // 这里的Calendar是java.util.Calendar,设置每周第几天执行
                                                .onDaysOfTheWeek( daysOfWeek)
                                                //间隔多少次执行
                                                .withInterval(Integer.parseInt(jobInfo.getDailynum()),Dailytime)
                                                // 一共执行1次（实际执行1+1次）
                                                .withRepeatCount(Integer.parseInt(jobInfo.getDailyrepeatcount()))
                                )
                                .forJob(jobDetail)
                                .startAt(Date.valueOf(jobInfo.getStartime()))
                                .startNow()
                                // 设置结束时间,默认无穷大
                                .endAt(Date.valueOf(jobInfo.getEndtime()))
                                //设置优先级
                                .withPriority(Integer.parseInt(jobInfo.getPriority()))
                                .build();
                        scheduler.scheduleJob(jobDetail,DailyTimeIntervalTrigger);
                        break;

                    case "CronTrigger":
                        //设置cronexpression的默认值
                        if (jobInfo.getCronexpression()==null){
                        jobInfo.setCronexpression("0/1 * * * * ?");
                        }
                        //设置timezone的默认值
                        if (jobInfo.getTimezone()==null){
                            jobInfo.setTimezone("Asia/Shanghai");
                        }


                        System.out.println(jobInfo.getStartime()+" "+jobInfo.getEndtime());
                        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                                .withIdentity(jobInfo.getTriggername(),jobInfo.getTriggergroup())
                                .withSchedule(CronScheduleBuilder
                                        .cronSchedule(jobInfo.getCronexpression())
                                        // 设置时区,默认上海
                                        .inTimeZone(TimeZone.getTimeZone(jobInfo.getTimezone()))

                                )
                                .startAt(Date.valueOf(jobInfo.getStartime()))
                                .startNow()
                                .forJob(jobDetail)
                                // 设置结束时间,默认无穷大
                                .endAt(Date.valueOf(jobInfo.getEndtime()))
                                //设置优先级
                                .withPriority(Integer.parseInt(jobInfo.getPriority()))
                                .build();
                        scheduler.scheduleJob(jobDetail,cronTrigger);
                        break;
                }

            } catch (SchedulerException e) {
                result = 2;
                log.error("任务调度失败");
                log.error(e.getMessage());
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage());
                return 2;
            } catch (InvocationTargetException e) {
                log.error(e.getMessage());
                return 2;
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
                return 2;
            } catch (InstantiationException e) {
                log.error(e.getMessage());
                return 2;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        return result;
    }


    @Override
    public int jobTOtri(JobInfo jobInfo) throws SchedulerException {
//        //默认触发器名称和分组
//        if (jobInfo.getTriggername()==null){
//            jobInfo.setTriggername(jobInfo.getJobname());
//        }
//        if (jobInfo.getTriggergroup()==null){
//            jobInfo.setTriggergroup(jobInfo.getJobgroup());
//        }

        int result = 0;
        //必填字段检查
        if ((jobInfo.getJobname()==null || jobInfo.getJobgroup()==null)&& "true".equals(jobInfo.getIsCustomTrigger()) ) {
            log.error("任务名称,任务分组,不能为空");
            return -5;
        }
        System.out.println("name"+jobInfo.getJobname());
        System.out.println("group"+jobInfo.getJobgroup());
        System.out.println(JobKey.jobKey(jobInfo.getJobname(),jobInfo.getJobgroup()));

        if (("false".equals(jobInfo.getIsCustomTrigger()))&&(scheduler.getTrigger(TriggerKey.triggerKey(jobInfo.getTriggername(),jobInfo.getTriggergroup()))!=null)) {
            log.error("触发器已存在");
            return -2;
        } else {
            try {
                //获取已有的jobDetail
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobInfo.getJobname(),jobInfo.getJobgroup()));
                System.out.println("jobDetail"+jobDetail);

                //字段检查

//                if (jobInfo.getJobDetail()==null&&jobInfo.getIsCustomJobDetail().equals("true")){
//                    log.error("开启自动注入jobDetail时,jobDetail不能为空");
//                    return -7;
//                }
//                //给isCustomJobDetail设置默认值
//                if (jobInfo.getIsCustomJobDetail()==null){
//                    jobInfo.setIsCustomJobDetail("false");
//                }
//                //默认关闭自定义注入,设置默认的jobDetail
//                if ("false".equals(jobInfo.getIsCustomJobDetail())){
//                    jobInfo.setJobDetail("com.atguigu.cloud.quartz.jobDetail.je");
//                }
                //检查给startime设置默认值
                if (jobInfo.getStartime()==null){
                    //给当前时间加上1秒
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    // 将当前时间加1秒
                    LocalDateTime newDateTime = currentDateTime.plusSeconds(1);
                    // 定义格式化模式
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    // 将LocalDateTime格式化为字符串
                    String formattedDate = newDateTime.format(formatter);
                    // 设置开始时间
                    jobInfo.setStartime(formattedDate);
                }

                //检查给endtime设置默认值
                if (jobInfo.getEndtime()==null){
                    jobInfo.setEndtime("2099-12-31");
                }
                //检查给priority设置默认值
                if (jobInfo.getPriority()==null){
                    jobInfo.setPriority("5");
                }


                //type字段检查
                if (jobInfo.getType()==null){
                    log.error("类型为空");
                    return -3;
                }

                String type;
                switch (jobInfo.getType()){
                    case "SimpleTrigger":
                        type="SimpleTrigger";
                        break;
                    case "CalendarIntervalTrigger":
                        type="CalendarIntervalTrigger";
                        break;
                    case "DailyTimeIntervalTrigger":
                        type="DailyTimeIntervalTrigger";
                        break;
                    case "CronTrigger":
                        type="CronTrigger";
                        break;
                    default:
                        log.error("类型错误");
                        return -4;
                }
                //配置isCustomTrigger的默认值
                if (jobInfo.getIsCustomTrigger()==null){
                    jobInfo.setIsCustomTrigger("false");
                }
                //默认关闭自定义注入,设置默认的触发器
                if (jobInfo.getTrigger()==null&&jobInfo.getIsCustomTrigger().equals("true")){
                    log.error("开启自动注入trigger时,trigger不能为空");
                    return -6;
                }
                //确定有类型而且开启了自定义的才使用这个配置
                if (jobInfo.getIsCustomTrigger().equals("true")){

                    //用switch匹配jobinfo根据类型创建不同的触发器,注意新加的触发器,必须要接收jobDetail
                    switch (type) {
                        case "SimpleTrigger":
                            TriggerAbstract Simple =  getBean( jobInfo.getTrigger());
                            SimpleTrigger SimpleTrigger= Simple.CreateSimpleTrigger(jobDetail);
                            scheduler.scheduleJob(SimpleTrigger);
                            vScriptTagScervice.addScriptTag(SimpleTrigger.getKey().getName(), SimpleTrigger.getKey().getGroup(),jobInfo.getTrigger(),jobInfo.getType());
                            break;
                        case "CalendarIntervalTrigger":
                            TriggerAbstract Calendar =  getBean( jobInfo.getTrigger());
                            CalendarIntervalTrigger CalendarTrigger= Calendar.CreatCalendarIntervalTrigger(jobDetail);
                            scheduler.scheduleJob(CalendarTrigger);
                            vScriptTagScervice.addScriptTag(CalendarTrigger.getKey().getName(),CalendarTrigger.getKey().getGroup(),jobInfo.getTrigger(),jobInfo.getType());
                            break;
                        case "DailyTimeIntervalTrigger":
                            TriggerAbstract Daily =  getBean( jobInfo.getTrigger());
                            DailyTimeIntervalTrigger DailyTrigger=  Daily.CreateDailyTimeIntervalTrigger(jobDetail);
                            scheduler.scheduleJob(DailyTrigger);
                            vScriptTagScervice.addScriptTag(DailyTrigger.getKey().getName(),DailyTrigger.getKey().getGroup(),jobInfo.getTrigger(),jobInfo.getType());
                            break;
                        case "CronTrigger":
                            TriggerAbstract cro =  getBean( jobInfo.getTrigger());
                            CronTrigger cronTrigger= cro.CreateCronTrigger(jobDetail);
                            scheduler.scheduleJob(cronTrigger);
                            vScriptTagScervice.addScriptTag(cronTrigger.getKey().getName(),cronTrigger.getKey().getGroup(),jobInfo.getTrigger(),jobInfo.getType());
                            break;
                        default:
                            log.error("类型错误");
                            return -4;
                    }
                    return result;
                }

                //这里是用默认的触发器
                switch (type) {
                    case "SimpleTrigger":
                        //设置simpletimesecond的默认值
                        if (jobInfo.getSimpletimesecond()==null){
                            jobInfo.setSimpletimesecond("1");
                        }
                        //设置repeatcount的默认值
                        if (jobInfo.getRepeatcount()==null){
                            jobInfo.setRepeatcount("0");
                        }

                        Trigger SimpleTrigger = TriggerBuilder.newTrigger()
                                .withIdentity(jobInfo.getTriggername(),jobInfo.getTriggergroup())
                                //.startAt(sdf.parse(jobInfo.getNextfiretime()))
                                .withSchedule(SimpleScheduleBuilder
                                        .simpleSchedule()
                                        // 设置时间间隔,单位秒
                                        .withIntervalInSeconds(Integer.parseInt(jobInfo.getSimpletimesecond()))
                                        //设置执行次数,底层实现是repeatCount+1,也就是总共执行repeatCount+1次
                                        .withRepeatCount(Integer.parseInt(jobInfo.getRepeatcount()))
                                )
                                .startAt(Date.valueOf(jobInfo.getStartime()))
                                .startNow()
                                // 设置结束时间,默认无穷大
                                .endAt(Date.valueOf(jobInfo.getEndtime()))
                                //设置优先级
                                .withPriority(Integer.parseInt(jobInfo.getPriority()))
                                .forJob(jobDetail)
                                .build();
                        scheduler.scheduleJob(SimpleTrigger);
                        break;
                    //建议这两个触发器用自定义来实现
                    case "CalendarIntervalTrigger":

                        //设置calendartime的默认值
                        if (jobInfo.getCalendartime()==null){
                            jobInfo.setCalendartime("second");
                        }
                        //设置calendarnum的默认值
                        if (jobInfo.getCalendarnum()==null){
                            jobInfo.setCalendarnum("1");
                        }
                        //设置timezone的默认值
                        if (jobInfo.getTimezone()==null){
                            jobInfo.setTimezone("Asia/Shanghai");
                        }
                        //设置preserveHourOfDayAcrossDaylightSavings的默认值
                        if (jobInfo.getPreserveHourOfDayAcrossDaylightSavings()==null){
                            jobInfo.setPreserveHourOfDayAcrossDaylightSavings("true");
                        }
                        //设置skipDayIfHourDoesNotExist的默认值
                        if (jobInfo.getSkipDayIfHourDoesNotExist()==null){
                            jobInfo.setSkipDayIfHourDoesNotExist("true");
                        }



                        DateBuilder.IntervalUnit time = null;
                        switch (jobInfo.getCalendartime()){
                            case "second":
                                time= DateBuilder.IntervalUnit.SECOND;
                                break;
                            case "minute":
                                time= DateBuilder.IntervalUnit.MINUTE;
                                break;
                            case "hour":
                                time= DateBuilder.IntervalUnit.HOUR;
                                break;
                            case "day":
                                time= DateBuilder.IntervalUnit.DAY;
                                break;
                            case "week":
                                time= DateBuilder.IntervalUnit.WEEK;
                                break;
                            case "month":
                                time= DateBuilder.IntervalUnit.MONTH;
                                break;
                            case "year":
                                time= DateBuilder.IntervalUnit.YEAR;
                                break;
                        }

                        Trigger CalendarIntervalTrigger = TriggerBuilder.newTrigger()
                                .withIdentity(jobInfo.getTriggername(),jobInfo.getTriggergroup())
                                .withSchedule(CalendarIntervalScheduleBuilder
                                        .calendarIntervalSchedule()

                                        // 设置是否跨日光保存时间保留小时
                                        .preserveHourOfDayAcrossDaylightSavings("true".equals(jobInfo.getPreserveHourOfDayAcrossDaylightSavings()))
                                        // 如果小时不存在则跳过
                                        .skipDayIfHourDoesNotExist("true".equals(jobInfo.getSkipDayIfHourDoesNotExist()))
                                        // 设置时区,默认上海
                                        .inTimeZone(TimeZone.getTimeZone(jobInfo.getTimezone()))
                                        // 指定生成触发器的时间单位和间隔,每月执行一次
                                        .withInterval(Integer.parseInt(jobInfo.getCalendarnum()),time)
                                )
                                .forJob(jobDetail)
                                // 设置开始时间,默认是当前时间后1秒
                                .startAt(Date.valueOf(jobInfo.getStartime()))
                                .startNow()
                                // 设置结束时间,默认无穷大
                                .endAt(Date.valueOf(jobInfo.getEndtime()))
                                //设置优先级
                                .withPriority(Integer.parseInt(jobInfo.getPriority()))
                                .build();
                        scheduler.scheduleJob(CalendarIntervalTrigger);
                        break;

                    case "DailyTimeIntervalTrigger":
                        //设置dailytime的默认值
                        if (jobInfo.getDailytime()==null){
                            jobInfo.setDailytime("second");
                        }
                        //设置dailyworkday的默认值
                        if (jobInfo.getDailyworkday()==null){
                            jobInfo.setDailyworkday(Set.of("all"));
                        }
                        //设置dailynum的默认值
                        if (jobInfo.getDailynum()==null){
                            jobInfo.setDailynum("1");
                        }
                        //设置dailyrepeatcount的默认值
                        if (jobInfo.getDailyrepeatcount()==null){
                            jobInfo.setDailyrepeatcount("0");
                        }
                        int starthour =00;
                        int startminute =00;
                        int startsecond =00;
                        var starttimeOfDay = TimeOfDay.hourMinuteAndSecondOfDay(starthour, startminute, startsecond);


                        if (jobInfo.getDailystarttime()!=null){
                            var splitstarttime = jobInfo.getDailystarttime().split(":");
                            starthour =Integer.parseInt(splitstarttime[0]);
                            startminute =Integer.parseInt(splitstarttime[1]);
                            startsecond =Integer.parseInt(splitstarttime[2]);
                            starttimeOfDay = TimeOfDay.hourMinuteAndSecondOfDay(starthour, startminute, startsecond);
                        }

                        int endhour =23;
                        int endminute =59;
                        int endsecond =59;
                        var endtimeOfDay = TimeOfDay.hourMinuteAndSecondOfDay(endhour, endminute, endsecond);

                        if (jobInfo.getDailyendtime()!=null){
                            var splitendtime = jobInfo.getDailyendtime().split(":");
                            endhour =Integer.parseInt(splitendtime[0]);
                            endminute =Integer.parseInt(splitendtime[1]);
                            endsecond =Integer.parseInt(splitendtime[2]);
                            endtimeOfDay = TimeOfDay.hourMinuteAndSecondOfDay(endhour, endminute, endsecond);
                        }
                        DateBuilder.IntervalUnit Dailytime = null;
                        switch (jobInfo.getDailytime()){
                            case "second":
                                Dailytime= DateBuilder.IntervalUnit.SECOND;
                                break;
                            case "minute":
                                Dailytime= DateBuilder.IntervalUnit.MINUTE;
                                break;
                            case "hour":
                                Dailytime= DateBuilder.IntervalUnit.HOUR;
                                break;
                        }



                        Set<Integer> daysOfWeek = new HashSet<>();
                        //用for循环遍历jobInfo.getDailyworkday()这个列表的每一个元素,然后用switch"1,2,3,4,5,6,7,all,workday,weekend"依次添加到daysOfWeek,使用java.util.Calendar,用if包围daysOfWeek.add方法,如果为null则返回result -10,同时记录错误日志
                        for (String day:jobInfo.getDailyworkday()) {
                            switch (day){
                                case "1":
                                    daysOfWeek.add(1);
                                    break;
                                case "2":
                                    daysOfWeek.add(2);
                                    break;
                                case "3":
                                    daysOfWeek.add(3);
                                    break;
                                case "4":
                                    daysOfWeek.add(4);
                                    break;
                                case "5":
                                    daysOfWeek.add(5);
                                    break;
                                case "6":
                                    daysOfWeek.add(6);
                                    break;
                                case "7":
                                    daysOfWeek.add(7);
                                    break;
                                case "all":
                                    daysOfWeek.add(1);
                                    daysOfWeek.add(2);
                                    daysOfWeek.add(3);
                                    daysOfWeek.add(4);
                                    daysOfWeek.add(5);
                                    daysOfWeek.add(6);
                                    daysOfWeek.add(7);
                                    break;
                                case "workday":
                                    daysOfWeek.add(2);
                                    daysOfWeek.add(3);
                                    daysOfWeek.add(4);
                                    daysOfWeek.add(5);
                                    daysOfWeek.add(1);
                                    break;
                                case "weekend":
                                    daysOfWeek.add(6);
                                    daysOfWeek.add(7);
                                    break;
                                default:
                                    log.error("日期错误");
                                    return -6;
                            }
                        }

                        Trigger DailyTimeIntervalTrigger = TriggerBuilder.newTrigger()
                                .withIdentity(jobInfo.getTriggername(),jobInfo.getTriggergroup())
                                .withSchedule(DailyTimeIntervalScheduleBuilder
                                                // 使用dailyTimeIntervalSchedule
                                                .dailyTimeIntervalSchedule()
                                                // 设置开始时间,默认是当前时间
                                                .startingDailyAt(starttimeOfDay)
                                                // 设置结束时间，默认是无穷大
                                                .endingDailyAt(endtimeOfDay)
                                                // 这里的Calendar是java.util.Calendar,设置每周第几天执行
                                                .onDaysOfTheWeek( daysOfWeek)
                                                //间隔多少次执行
                                                .withInterval(Integer.parseInt(jobInfo.getDailynum()),Dailytime)
                                                // 一共执行1次（实际执行1+1次）
                                                .withRepeatCount(Integer.parseInt(jobInfo.getDailyrepeatcount()))
                                        // .onDaysOfTheWeek(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY) // 周一到周四执行。不写即每天执行
                                )
                                .forJob(jobDetail)
                                .startAt(Date.valueOf(jobInfo.getStartime()))
                                .startNow()
                                // 设置结束时间,默认无穷大
                                .endAt(Date.valueOf(jobInfo.getEndtime()))
                                //设置优先级
                                .withPriority(Integer.parseInt(jobInfo.getPriority()))
                                .build();
                        scheduler.scheduleJob(DailyTimeIntervalTrigger);
                        break;

                    case "CronTrigger":
                        //设置cronexpression的默认值
                        if (jobInfo.getCronexpression()==null){
                            jobInfo.setCronexpression("0/1 * * * * ?");
                        }
                        //设置timezone的默认值
                        if (jobInfo.getTimezone()==null){
                            jobInfo.setTimezone("Asia/Shanghai");
                        }


                        System.out.println(jobInfo.getStartime()+" "+jobInfo.getEndtime());
                        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                                .withIdentity(jobInfo.getTriggername(),jobInfo.getTriggergroup())
                                .withSchedule(CronScheduleBuilder
                                        .cronSchedule(jobInfo.getCronexpression())
                                        // 设置时区,默认上海
                                        .inTimeZone(TimeZone.getTimeZone(jobInfo.getTimezone()))

                                )
                                .startAt(Date.valueOf(jobInfo.getStartime()))
                                .startNow()
                                .forJob(jobDetail)
                                // 设置结束时间,默认无穷大
                                .endAt(Date.valueOf(jobInfo.getEndtime()))
                                //设置优先级
                                .withPriority(Integer.parseInt(jobInfo.getPriority()))
                                .build();
                        scheduler.scheduleJob(cronTrigger);
                        break;
                }

            } catch (SchedulerException e) {
                log.error(e.getMessage());
                result = -2;
                log.error("任务调度失败");
            } catch (NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException | InstantiationException e) {
                log.error(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
    //添加任务实现类
@Override
   public int createdetail(String jobclassname, String jobname, String jobgroup,  String isCustomJobDetail, String jobDetail,String description) throws SchedulerException {
        //默认触发器名称和分组
    int result = 0;
    //必填字段检查
    if (jobname==null || jobgroup==null || jobclassname==null) {
        log.error("任务名称,任务分组,任务类不能为空");
        return -5;
    }

    if (scheduler.getJobDetail(JobKey.jobKey(jobname,jobgroup))!=null) {
        log.error("任务已存在");
        return -1 ;
    }
     else {
        try {
            //字段检查
            //字段检查
            if (jobDetail == null && "true".equals(isCustomJobDetail)) {
                log.error("开启自动注入jobDetail时,jobDetail不能为空");
                return -7;
            }
            //给isCustomJobDetail设置默认值
            if (isCustomJobDetail == null) {
                isCustomJobDetail = "false";
            }
            //默认关闭自定义注入,设置默认的jobDetail
            if ("false".equals(isCustomJobDetail)) {
                jobDetail = (TRIGGER_GROUP_NAME);
            }else {
                vScriptTagScervice.addScriptTag(jobname,jobgroup,jobDetail,"JobDetail");
            }


            JobDetail jobDetailimpl = null;
//                通过反射获取JobDetail
            Class<? extends CreatJobDetail> c = applicationContext.getType( jobDetail)
                    .asSubclass(CreatJobDetail.class);
            Constructor<?> constructor = c.getDeclaredConstructor(ApplicationContext.class); // 参数类型列表
            Object instance = constructor.newInstance(applicationContext);
            CreatJobDetail creatJobDetail = (CreatJobDetail) instance;

            jobDetailimpl = creatJobDetail.createdetail(jobclassname, jobname, jobgroup, description);

            scheduler.addJob(jobDetailimpl, false);
        } catch ( InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | SchedulerException e) {
            log.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    return result;
    }

    //更新触发器
    @Override
    public boolean updateTriggerargument(JobInfo jobInfo) throws SchedulerException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        int result = 0;
        //字段检查
        if (jobInfo.getTriggername()==null || jobInfo.getTriggergroup()==null) {
            log.error("触发器名称,触发器分组不能为空");
            return false;
        }
        //鉴定Trigger是否存在

        if (scheduler.getTrigger(TriggerKey.triggerKey(jobInfo.getTriggername(),jobInfo.getTriggergroup()))==null) {
            log.error("触发器不存在");
            return false;
        }
        try {
        //先获取原有的触发器
        Trigger trigger=scheduler.getTrigger(TriggerKey.triggerKey(jobInfo.getTriggername(),jobInfo.getTriggergroup()));
            System.out.println("获取getTriggername"+jobInfo.getTriggername()+"获取getTriggername"+jobInfo.getTriggergroup());
            System.out.println("第一次获取trigger"+trigger);
        //获取触发器的构造器
        TriggerBuilder triggerBuilder=trigger.getTriggerBuilder();


        //type字段检查
        if (jobInfo.getType()==null){
            log.error("类型为空");
            return false;
        }

        String type;
        switch (jobInfo.getType()){
            case "SimpleTrigger":
                type="SimpleTrigger";
                break;
            case "CalendarIntervalTrigger":
                type="CalendarIntervalTrigger";
                break;
            case "DailyTimeIntervalTrigger":
                type="DailyTimeIntervalTrigger";
                break;
            case "CronTrigger":
                type="CronTrigger";
                break;
            default:
                log.error("类型错误");
                return false;
        }
        //配置isCustomTrigger的默认值
        if (jobInfo.getIsCustomTrigger()==null){
            jobInfo.setIsCustomTrigger("false");
        }
        //默认关闭自定义注入,设置默认的触发器
        if (jobInfo.getTrigger()==null&&jobInfo.getIsCustomTrigger().equals("true")){
            log.error("开启自动注入trigger时,trigger不能为空");
            return false;
        }
        //确定有类型而且开启了自定义的才使用这个配置
        if (jobInfo.getIsCustomTrigger().equals("true")){
            //用switch匹配jobinfo根据类型创建不同的触发器,注意新加的触发器,必须要接收jobDetail
            switch (type) {
                case "SimpleTrigger":
                    TriggerAbstract Simple =  getBean( jobInfo.getTrigger());
                    SimpleTrigger SimpleTrigger= Simple.updateSimpletrigger(triggerBuilder);

                    //先暂停触发器,再重新调度
                    scheduler.pauseTrigger(trigger.getKey());
                    //替换
                    scheduler.rescheduleJob(trigger.getKey(),SimpleTrigger);
                    //恢复触发器
                    scheduler.resumeTrigger(trigger.getKey());
                    vScriptTagScervice.deleteScriptTag(jobInfo.getTriggername(),jobInfo.getTriggergroup());
                    return true;

                case "CalendarIntervalTrigger":
                    TriggerAbstract Calendar =  getBean( jobInfo.getTrigger());
                    CalendarIntervalTrigger CalendarTrigger= Calendar.updateCalendarIntervalTrigger(triggerBuilder);
                    //先暂停触发器,再重新调度
                    scheduler.pauseTrigger(trigger.getKey());
                    //替换
                    scheduler.rescheduleJob(trigger.getKey(),CalendarTrigger);
                    //恢复触发器
                    scheduler.resumeTrigger(trigger.getKey());
                    vScriptTagScervice.deleteScriptTag(jobInfo.getTriggername(),jobInfo.getTriggergroup());
                    return true;


                case "DailyTimeIntervalTrigger":
                    TriggerAbstract Daily =  getBean( jobInfo.getTrigger());
                    DailyTimeIntervalTrigger DailyTrigger= Daily.updateDailyTimeIntervalTrigger(triggerBuilder);
                    //先暂停触发器,再重新调度
                    scheduler.pauseTrigger(trigger.getKey());
                    //替换
                    scheduler.rescheduleJob(trigger.getKey(),DailyTrigger);
                    //恢复触发器
                    scheduler.resumeTrigger(trigger.getKey());
                    vScriptTagScervice.deleteScriptTag(jobInfo.getTriggername(),jobInfo.getTriggergroup());
                    return true;


                case "CronTrigger":
                    TriggerAbstract cron =  getBean( jobInfo.getTrigger());

                    CronTrigger cronTrigger=  cron.updateCrontrigger(triggerBuilder);
                    //先暂停触发器,再重新调度
                    scheduler.pauseTrigger(trigger.getKey());
                    //替换
                    scheduler.rescheduleJob(trigger.getKey(),cronTrigger);
                    //恢复触发器
                    scheduler.resumeTrigger(trigger.getKey());
                    vScriptTagScervice.deleteScriptTag(jobInfo.getTriggername(),jobInfo.getTriggergroup());
                    return true;
                default:
                    log.error("类型错误");
                    return false;
            }
        }

        //这里是用默认的触发器
        switch (type) {
            case "SimpleTrigger":
                SimpleTriggerImpl TriggerImpl = (SimpleTriggerImpl)trigger;
                //字段替换
                if (jobInfo.getSimpletimesecond()==null){
                    jobInfo.setSimpletimesecond(String.valueOf(TriggerImpl.getRepeatInterval()));
                }
                if (jobInfo.getRepeatcount()==null){
                    jobInfo.setRepeatcount(String.valueOf(TriggerImpl.getRepeatCount()));
                }

                if (jobInfo.getStartime()==null){
                    jobInfo.setStartime(sdf.format(trigger.getStartTime()));
                }
                if (jobInfo.getEndtime()==null){
                    jobInfo.setEndtime(sdf.format(trigger.getEndTime()));
                }
                if (jobInfo.getPriority()==null){
                    jobInfo.setPriority(String.valueOf(TriggerImpl.getPriority()));
                }

                Trigger SimpleTrigger = triggerBuilder
                        .withIdentity(jobInfo.getTriggername(),jobInfo.getTriggergroup())
                        //.startAt(sdf.parse(jobInfo.getNextfiretime()))
                        .withSchedule(SimpleScheduleBuilder
                                .simpleSchedule()
                                // 设置时间间隔,单位秒
                                .withIntervalInSeconds(Integer.parseInt(jobInfo.getSimpletimesecond()))
                                //设置执行次数,底层实现是repeatCount+1,也就是总共执行repeatCount+1次
                                .withRepeatCount(Integer.parseInt(jobInfo.getRepeatcount()))
                        )
                        .startAt(Date.valueOf(jobInfo.getStartime()))
                        // 设置结束时间,默认无穷大
                        .endAt(Date.valueOf(jobInfo.getEndtime()))
                        .startNow()
                        //设置优先级
                        .withPriority(Integer.parseInt(jobInfo.getPriority()))
                        .build();
                //先暂停触发器,再重新调度
                scheduler.pauseTrigger(trigger.getKey());
                //替换
                scheduler.rescheduleJob(trigger.getKey(),SimpleTrigger);
                //恢复触发器
                scheduler.resumeTrigger(trigger.getKey());
                vScriptTagScervice.deleteScriptTag(jobInfo.getTriggername(),jobInfo.getTriggergroup());
                break;
            //建议这两个触发器用自定义来实现
            case "CalendarIntervalTrigger":
                CalendarIntervalTriggerImpl CalendarIntervalTriggerImpl = (CalendarIntervalTriggerImpl)trigger;
                //字段替换,把没有填写的字段,用原有的值填写
                if (jobInfo.getCalendartime()==null){
                    jobInfo.setCalendartime(String.valueOf(CalendarIntervalTriggerImpl.getRepeatIntervalUnit()));
                }
                if (jobInfo.getCalendarnum()==null){
                    jobInfo.setCalendarnum(String.valueOf(CalendarIntervalTriggerImpl.getRepeatInterval()));
                }
                if (jobInfo.getTimezone()==null){
                    jobInfo.setTimezone(String.valueOf(CalendarIntervalTriggerImpl.getTimeZone()));
                }
                if (jobInfo.getPreserveHourOfDayAcrossDaylightSavings()==null){
                    jobInfo.setPreserveHourOfDayAcrossDaylightSavings(String.valueOf(CalendarIntervalTriggerImpl.isPreserveHourOfDayAcrossDaylightSavings()));
                }
                if (jobInfo.getSkipDayIfHourDoesNotExist()==null){
                    jobInfo.setSkipDayIfHourDoesNotExist(String.valueOf(CalendarIntervalTriggerImpl.isSkipDayIfHourDoesNotExist()));
                }

                if (jobInfo.getStartime()==null){
                    jobInfo.setStartime(sdf.format(trigger.getStartTime()));
                }
                if (jobInfo.getEndtime()==null){
                    jobInfo.setEndtime(sdf.format(trigger.getEndTime()));
                }
                if (jobInfo.getPriority()==null){
                    jobInfo.setPriority(String.valueOf(CalendarIntervalTriggerImpl.getPriority()));
                }

                DateBuilder.IntervalUnit time = null;
                switch (jobInfo.getCalendartime()){
                    case "second":
                        time= DateBuilder.IntervalUnit.SECOND;
                        break;
                    case "minute":
                        time= DateBuilder.IntervalUnit.MINUTE;
                        break;
                    case "hour":
                        time= DateBuilder.IntervalUnit.HOUR;
                        break;
                    case "day":
                        time= DateBuilder.IntervalUnit.DAY;
                        break;
                    case "week":
                        time= DateBuilder.IntervalUnit.WEEK;
                        break;
                    case "month":
                        time= DateBuilder.IntervalUnit.MONTH;
                        break;
                    case "year":
                        time= DateBuilder.IntervalUnit.YEAR;
                        break;
                }

                Trigger CalendarIntervalTrigger = triggerBuilder.withSchedule(CalendarIntervalScheduleBuilder
                                .calendarIntervalSchedule()
                                // 设置是否跨日光保存时间保留小时
                                .preserveHourOfDayAcrossDaylightSavings("true".equals(jobInfo.getPreserveHourOfDayAcrossDaylightSavings()))
                                // 如果小时不存在则跳过
                                .skipDayIfHourDoesNotExist("true".equals(jobInfo.getSkipDayIfHourDoesNotExist()))
                                // 设置时区,默认上海
                                .inTimeZone(TimeZone.getTimeZone(jobInfo.getTimezone()))
                                // 指定生成触发器的时间单位和间隔,每月执行一次
                                .withInterval(Integer.parseInt(jobInfo.getCalendarnum()),time)

                        )
                        // 设置开始时间,默认是当前时间后1秒
                        .startAt(Date.valueOf(jobInfo.getStartime()))
                        // 设置结束时间,默认无穷大
                        .endAt(Date.valueOf(jobInfo.getEndtime()))
                        .startNow()
                        //设置优先级
                        .withPriority(Integer.parseInt(jobInfo.getPriority()))
                        .build();
                //先暂停触发器,再重新调度
                scheduler.pauseTrigger(trigger.getKey());
                //替换
                scheduler.rescheduleJob(trigger.getKey(),CalendarIntervalTrigger);
                //恢复触发器
                scheduler.resumeTrigger(trigger.getKey());
                vScriptTagScervice.deleteScriptTag(jobInfo.getTriggername(),jobInfo.getTriggergroup());
                break;

            case "DailyTimeIntervalTrigger":
                DailyTimeIntervalTriggerImpl DailyTimeIntervalTriggerImpl = (DailyTimeIntervalTriggerImpl)trigger;
                //字段替换,把没有填写的字段,用原有的值填写
                if (jobInfo.getDailytime()==null){
                    jobInfo.setDailytime(String.valueOf(DailyTimeIntervalTriggerImpl.getRepeatIntervalUnit()));
                }
                if (jobInfo.getDailyworkday()==null){
                    // 假设 TriggerImpl.getDaysOfWeek() 返回的是 Set<Integer>
                    Set<Integer> daysOfWeek = DailyTimeIntervalTriggerImpl.getDaysOfWeek();

                    // 将 Set<Integer> 转换为 Set<String>
                    Set<String> daysOfWeekString = daysOfWeek.stream()
                            .map(String::valueOf)
                            .collect(Collectors.toSet());

                    jobInfo.setDailyworkday(daysOfWeekString);
                }
                if (jobInfo.getDailynum()==null){
                    jobInfo.setDailynum(String.valueOf(DailyTimeIntervalTriggerImpl.getRepeatInterval()));
                }
                if (jobInfo.getDailyrepeatcount()==null){
                    jobInfo.setDailyrepeatcount(String.valueOf(DailyTimeIntervalTriggerImpl.getRepeatCount()));
                }
                if (jobInfo.getStartime()==null){
                    jobInfo.setStartime(sdf.format(trigger.getStartTime()));
                }
                if (jobInfo.getEndtime()==null){
                    jobInfo.setEndtime(sdf.format(trigger.getEndTime()));
                }
                if (jobInfo.getPriority()==null){
                    jobInfo.setPriority(String.valueOf(DailyTimeIntervalTriggerImpl.getPriority()));
                }
                var startTimeOfDay = DailyTimeIntervalTriggerImpl.getStartTimeOfDay();
                var endTimeOfDay = DailyTimeIntervalTriggerImpl.getEndTimeOfDay();



                if (jobInfo.getDailystarttime()!=null){
                    var splitstarttime = jobInfo.getDailystarttime().split(":");
                    int starthour =Integer.parseInt(splitstarttime[0]);
                    int startminute =Integer.parseInt(splitstarttime[1]);
                    int startsecond =Integer.parseInt(splitstarttime[2]);
                    startTimeOfDay=TimeOfDay.hourMinuteAndSecondOfDay(starthour,startminute,startsecond);
                }


                if (jobInfo.getDailyendtime()!=null){
                    var splitendtime = jobInfo.getDailyendtime().split(":");
                    int endhour =Integer.parseInt(splitendtime[0]);
                    int endminute =Integer.parseInt(splitendtime[1]);
                    int endsecond =Integer.parseInt(splitendtime[2]);
                    endTimeOfDay=TimeOfDay.hourMinuteAndSecondOfDay(endhour,endminute,endsecond);
                }


                DateBuilder.IntervalUnit Dailytime = null;
                switch (jobInfo.getDailytime()){
                    case "second":
                        Dailytime= DateBuilder.IntervalUnit.SECOND;
                        break;
                    case "minute":
                        Dailytime= DateBuilder.IntervalUnit.MINUTE;
                        break;
                    case "hour":
                        Dailytime= DateBuilder.IntervalUnit.HOUR;
                        break;
                }

                Set<Integer> daysOfWeek = new HashSet<>();
                //用for循环遍历jobInfo.getDailyworkday()这个列表的每一个元素,然后用switch"1,2,3,4,5,6,7,all,workday,weekend"依次添加到daysOfWeek,使用java.util.Calendar,用if包围daysOfWeek.add方法,如果为null则返回result -10,同时记录错误日志
                for (String day:jobInfo.getDailyworkday()) {
                    switch (day){
                        case "1":
                            daysOfWeek.add(1);
                            break;
                        case "2":
                            daysOfWeek.add(2);
                            break;
                        case "3":
                            daysOfWeek.add(3);
                            break;
                        case "4":
                            daysOfWeek.add(4);
                            break;
                        case "5":
                            daysOfWeek.add(5);
                            break;
                        case "6":
                            daysOfWeek.add(6);
                            break;
                        case "7":
                            daysOfWeek.add(7);
                            break;
                        case "all":
                            daysOfWeek.add(1);
                            daysOfWeek.add(2);
                            daysOfWeek.add(3);
                            daysOfWeek.add(4);
                            daysOfWeek.add(5);
                            daysOfWeek.add(6);
                            daysOfWeek.add(7);
                            break;
                        case "workday":
                            daysOfWeek.add(2);
                            daysOfWeek.add(3);
                            daysOfWeek.add(4);
                            daysOfWeek.add(5);
                            daysOfWeek.add(1);
                            break;
                        case "weekend":
                            daysOfWeek.add(6);
                            daysOfWeek.add(7);
                            break;
                        default:
                            log.error("日期错误");
                            return false;
                    }
                }

                Trigger DailyTimeIntervalTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobInfo.getTriggername(),jobInfo.getTriggergroup())
                        .withSchedule(DailyTimeIntervalScheduleBuilder
                                        // 使用dailyTimeIntervalSchedule
                                        .dailyTimeIntervalSchedule()
                                        // 设置开始时间,默认是当前时间
                                        .startingDailyAt(startTimeOfDay)
                                        // 设置结束时间，默认是无穷大
                                        .endingDailyAt(endTimeOfDay)
                                        // 这里的Calendar是java.util.Calendar,设置每周第几天执行
                                        .onDaysOfTheWeek( daysOfWeek)
                                        //间隔多少次执行
                                        .withInterval(Integer.parseInt(jobInfo.getDailynum()),Dailytime)
                                        // 一共执行1次（实际执行1+1次）
                                        .withRepeatCount(Integer.parseInt(jobInfo.getDailyrepeatcount()))
                                // .onDaysOfTheWeek(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY) // 周一到周四执行。不写即每天执行
                        )

                        .startAt(Date.valueOf(jobInfo.getStartime()))
                        .startNow()
                        // 设置结束时间,默认无穷大
                        .endAt(Date.valueOf(jobInfo.getEndtime()))
                        //设置优先级
                        .withPriority(Integer.parseInt(jobInfo.getPriority()))
                        .build();
                //先暂停触发器,再重新调度
                scheduler.pauseTrigger(trigger.getKey());

                scheduler.rescheduleJob(trigger.getKey(),DailyTimeIntervalTrigger);
                //恢复触发器
                scheduler.resumeTrigger(trigger.getKey());
                vScriptTagScervice.deleteScriptTag(jobInfo.getTriggername(),jobInfo.getTriggergroup());
                break;

            case "CronTrigger":
                CronTriggerImpl CronTriggerImpl = (CronTriggerImpl)trigger;
                //字段替换,把没有填写的字段,用原有的值填写
                if (jobInfo.getCronexpression()==null){
                    jobInfo.setCronexpression(String.valueOf(CronTriggerImpl.getCronExpression()));
                }
                if (jobInfo.getStartime()==null){
                    jobInfo.setStartime(sdf.format(trigger.getStartTime()));
                }
                if (jobInfo.getEndtime()==null){
                    jobInfo.setEndtime(sdf.format(trigger.getEndTime()));
                }
                //设置时区
                if (jobInfo.getTimezone()==null){
                    jobInfo.setTimezone(String.valueOf(CronTriggerImpl.getTimeZone()));
                }
                if (jobInfo.getPriority()==null){
                    jobInfo.setPriority(String.valueOf(CronTriggerImpl.getPriority()));
                }

                System.out.println(jobInfo.getStartime()+" "+jobInfo.getEndtime());
                CronTrigger cronTrigger = (CronTrigger) triggerBuilder
                        .withSchedule(CronScheduleBuilder
                                .cronSchedule(jobInfo.getCronexpression())
                                // 设置时区,默认上海
                                .inTimeZone(TimeZone.getTimeZone(jobInfo.getTimezone()))
                        )
                        .startAt(Date.valueOf(jobInfo.getStartime()))
                        // 设置结束时间,默认无穷大
                        .endAt(Date.valueOf(jobInfo.getEndtime()))
                        //设置优先级
                        .withPriority(Integer.parseInt(jobInfo.getPriority()))
                        .startNow()
                        .build();
                //先暂停触发器,再重新调度
                scheduler.pauseTrigger(trigger.getKey());
                System.out.println(trigger.getKey());
                scheduler.rescheduleJob(trigger.getKey(),cronTrigger);
                //恢复触发器
                scheduler.resumeTrigger(trigger.getKey());
                vScriptTagScervice.deleteScriptTag(jobInfo.getTriggername(),jobInfo.getTriggergroup());
                break;
        }
    } catch (SchedulerException e) {

        log.error("任务调度失败");
        return false;
    }
        return true;
    }

//替换触发器
    @Override
    public boolean updateTrigger(String oldtriggername, String oldtriggergroup, JobInfo jobinfo) throws SchedulerException {
        System.out.println("oldname:"+oldtriggername+' '+"oldgergroup:"+oldtriggergroup);
        //字段检查
        if (scheduler.getTrigger(TriggerKey.triggerKey(oldtriggername,oldtriggergroup))==null) {
            log.error("触发器不存在");
            return false;
        }
        //获取job信息,从原有的触发器
        System.out.println("oldname:"+oldtriggername+' '+"oldgergroup"+oldtriggergroup);
        Trigger trigger=scheduler.getTrigger(TriggerKey.triggerKey(oldtriggername,oldtriggergroup));
        System.out.println("触发器"+trigger);

        JobDetail jobDetail=scheduler.getJobDetail(trigger.getJobKey());

        jobinfo.setJobname(trigger.getJobKey().getName());
        jobinfo.setJobgroup(trigger.getJobKey().getGroup());
        jobinfo.setJobclassname(jobDetail.getJobClass().getName());
        jobinfo.setDescription(jobDetail.getDescription());

        //先停止触发器
        scheduler.pauseTrigger(TriggerKey.triggerKey(oldtriggername,oldtriggergroup));
        //删除要替换的触发器
        scheduler.unscheduleJob(TriggerKey.triggerKey(oldtriggername,oldtriggergroup));
        System.out.println(jobinfo);
        vScriptTagScervice.deleteScriptTag(oldtriggername,oldtriggergroup);

        //添加新的触发器,并判断
        if(jobTOtri(jobinfo)==0){
            return true;
        }else {
            return false;
        }

    }


    //更新现有的任务
    @Override
    public int updateJob(String jobclassname, String jobname, String jobgroup,  String isCustomJobDetail, String jobDetail,String description) throws SchedulerException {

        int result = 0;
        //必填字段检查
        if (jobname==null || jobgroup==null || jobclassname==null) {
            log.error("任务名称,任务分组,任务类不能为空");
            return -5;
        }
        System.out.println("jobname:"+jobname+' '+"jobgroup:"+jobgroup+' '+"jobclassname:"+jobclassname);

        if (scheduler.getJobDetail(JobKey.jobKey(jobname,jobgroup))==null) {
            log.error("任务不存在");
            return -1 ;
        }
        else {
            try {
                //字段检查
                if (jobDetail == null && "true".equals(isCustomJobDetail)) {
                    log.error("开启自动注入jobDetail时,jobDetail不能为空");
                    return -7;
                }
                //给isCustomJobDetail设置默认值
                if (isCustomJobDetail == null) {
                    isCustomJobDetail = "false";
                }
                //默认关闭自定义注入,设置默认的jobDetail
                if ("false".equals(isCustomJobDetail)) {
                    jobDetail = (TRIGGER_GROUP_NAME);
                    vScriptTagScervice.deleteScriptTag(jobname,jobgroup);
                }else {
                    vScriptTagScervice.addScriptTag(jobname,jobgroup,jobDetail,"JobDetail");
                }
                var oldJobDetail = scheduler.getJobDetail(JobKey.jobKey(jobname, jobgroup));
                if (StrUtil.isBlank(description)){
                    description = oldJobDetail.getDescription();
                }
                if (StrUtil.isBlank(jobclassname)){
                    jobclassname = oldJobDetail.getJobClass().getName();
                }

                JobDetail jobDetailimpl = null;
//                通过反射获取JobDetail
                Class<? extends CreatJobDetail> c = applicationContext.getType( jobDetail)
                        .asSubclass(CreatJobDetail.class);
                Constructor<?> constructor = c.getDeclaredConstructor(ApplicationContext.class); // 参数类型列表
                Object instance = constructor.newInstance(applicationContext);
                CreatJobDetail creatJobDetail = (CreatJobDetail) instance;

                jobDetailimpl = creatJobDetail.createdetail(jobclassname, jobname, jobgroup, description);


//                JobDetail jobDetailimpl = null;
//                //通过反射获取JobDetail
//                Class<? extends CreatJobDetail> c = applicationContext.getType( jobDetail)
//                        .asSubclass(CreatJobDetail.class);
////                Constructor<?> constructor = c.getDeclaredConstructor(ApplicationContext.class); // 参数类型列表
////                Method method=c.getMethod("createdetail",String.class,String.class,String.class,String.class);
//                Object instance = c.getDeclaredConstructor().newInstance();
//                CreatJobDetail creatJobDetail = (CreatJobDetail) instance;
//                jobDetailimpl = creatJobDetail.createdetail(jobclassname, jobname, jobgroup, description);

                pauseJob(jobname,jobgroup);
                scheduler.addJob(jobDetailimpl, true);

            } catch (InvocationTargetException e) {
                log.error(e.getMessage());
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage());
            } catch (InstantiationException e) {
                log.error(e.getMessage());
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            } catch (SchedulerException e) {
                log.error(e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
        @Override
    public Boolean deleteAllJob() {
        String jobname=null;
        String jobgroup=null;
        String triggername=null;
        String triggergroup=null;
        try {
            List<String> triigergroups = scheduler.getTriggerGroupNames();
            for (String triigergroup:triigergroups) {
                Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triigergroup));
                for (TriggerKey triggerKey:triggerKeys) {
                    triggername=triggerKey.getName();
                    triggergroup=triggerKey.getGroup();
                    scheduler.unscheduleJob(triggerKey);
                    vScriptTagScervice.deleteScriptTag(triggername,triggergroup);
                }
            }


            List<String> groups = scheduler.getJobGroupNames();
            for (String group:groups) {
                Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group));
                for (JobKey jobKey:jobKeys) {
                    jobname=jobKey.getName();
                    jobgroup=jobKey.getGroup();
                    scheduler.deleteJob(jobKey);
                    vScriptTagScervice.deleteScriptTag(jobname,jobgroup);
                }
            }


            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }
    }
//删除触发器
    @Override
    public boolean jobUNtri(String triggername, String triggergroup) throws SchedulerException {
        boolean result = true;
        //鉴定Trigger是否存在
        boolean a= scheduler.checkExists(TriggerKey.triggerKey(triggername,triggergroup));
        if (!a) {
            return false;
        }

        scheduler.unscheduleJob(TriggerKey.triggerKey(triggername,triggergroup));
        vScriptTagScervice.deleteScriptTag(triggername,triggergroup);
        return result;
    }

//关闭调度器
    @Override
    public boolean shutdown(boolean iswait) {
        try {
            scheduler.shutdown(iswait);
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }
    }
//暂停调度器
    @Override
    public boolean standby() {
        try {
            scheduler.standby();
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }
    }
    //启动调度器
    @Override
    public boolean start() {
        try {
            scheduler.start();
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public <T> void createDelayedQueue(DelayedJob job, T parameter, String ID, Long time) throws SchedulerException {
        // 注入参数
        dynamicBeanOperate.registerBeanByPro(ID, job.getClass(),parameter);
        // 创建定时任务
        Trigger trigger = TriggerBuilder.newTrigger(
                )
                .withIdentity(ID, "DelayedQueue")
                .startAt(new Date(System.currentTimeMillis() + 100000))
                .build();
        scheduler.scheduleJob(JobBuilder.newJob(MyQuartzJobWrapper.class)
                .usingJobData("targetBeanName", ID)
                .withIdentity(ID,"DelayedQueue")
                .storeDurably().build(),trigger);
    }
//使用自定义的trigger
    @Override
    public <T> void createDelayedQueue(DelayedJob job, T parameter, String ID, Trigger trigger) throws SchedulerException {
        // 注入参数
        dynamicBeanOperate.registerBeanByPro(ID, job.getClass(),parameter);

        scheduler.scheduleJob(JobBuilder.newJob(MyQuartzJobWrapper.class)
                .usingJobData("targetBeanName", ID)
                .withIdentity(ID,"DelayedQueue")
                .storeDurably().build(),trigger);
    }

}

