package com.visualization.cloud.quartz.jobService;/**
 * @Auter zzh
 * @Date 2024/12/10
 */




import com.visualization.cloud.quartz.Function.DelayedQueue.DelayedJob;
import com.visualization.cloud.quartz.po.JobInfo;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz
 * @className: IJobService
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/10 19:46
 * @version: 1.0
 */
public interface IJobService {
    //查询所有任务
    public List<JobInfo> getAllJobs() throws SchedulerException;

    List<JobInfo> getAllNoUseJobs();

    List<String> getTriggerName(String triggerGroup) throws SchedulerException;

    //获取trigger的详情
    JobInfo getTriggerInfo(String triggername, String triggergroup) throws SchedulerException;




    String getNextFireTime(String triggername, String triggergroup, java.util.Date specifiedTime) throws SchedulerException;

    boolean isAllPaused() throws SchedulerException;

    //恢复任务
    public boolean resumeJob(String jobName,String jobGroup) throws SchedulerException;

    boolean resumeTri(String triggername, String triggergroup) throws SchedulerException;

    boolean resumeAllJob();

    boolean resumeNow(String jobName, String jobGroup) throws SchedulerException;

    //停止任务
    public boolean pauseJob(String jobName,String jobGroup) throws SchedulerException;

    boolean pauseTri(String triggername, String triggergroup) throws SchedulerException;

    //暂停全部
    boolean pauseAllJob();

    //修改任务执行周期表达式
    public boolean reScheduleJob(String triggername,String triggergroup,String cronExpression) throws SchedulerException;
    //删除任务
    public boolean deleteJob(String jobName,String jobGroup) throws SchedulerException;
    //新增任务
    public int addJob(JobInfo jobInfo) throws SchedulerException;
    //判断任务是否存在
    public int isJobExist(JobKey jobKey);

    //添加任务实现类
    int createdetail(String jobclassname, String jobname, String jobgroup, String isCustomJobDetail, String jobDetail, String description) throws ClassNotFoundException, SchedulerException;

    //更新触发器
    boolean updateTriggerargument(JobInfo jobInfo) throws SchedulerException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;

    //替换触发器
    boolean updateTrigger(String oldtriggername, String oldtriggergroup, JobInfo jobinfo) throws SchedulerException;

    //更新现有的任务
    int updateJob(String jobclassname, String jobname, String jobgroup, String isCustomJobDetail, String jobDetail, String description) throws SchedulerException;

    Boolean deleteAllJob();
    boolean jobUNtri(String triggername, String triggergroup) throws SchedulerException;

    int jobTOtri(JobInfo jobInfo) throws SchedulerException;

    //通过jobgroup来查找job
    List<JobInfo> getJobByGroup(String jobGroup);


    List<String> getJobName(String jobGroup) throws SchedulerException;

    //获取所有的jobgroup分组
    List<String> getJobGroupAll();

    //根据job获取所有相关的trigger
    List<JobInfo> getTriByJob(String jobName, String jobGroup);

    //获取所有的trigger
    List<JobInfo> getAllTriggers(String triggerGroup);

    //获取所有的trigger分组
    List<String> getTriggerGroupAll();

    //根据triggergroup来查找trigger
    List<JobInfo> getTriggerByGroup(String triggerGroup);

    //关闭调度器
    boolean shutdown(boolean iswait);

    //暂停调度器
    boolean standby();

    //启动调度器
    boolean start();

    <T> void createDelayedQueue(DelayedJob job, T parameter, String ID, Long time) throws SchedulerException;

    <T> void createDelayedQueue(DelayedJob job, T parameter, String ID, Trigger trigger) throws SchedulerException;
}
