package com.visualization.cloud.quartz.CRUDSPcontroller;/**
 * @Auter zzh
 * @Date 2024/12/14
 */



import com.visualization.cloud.quartz.po.JobInfo;
import com.visualization.cloud.quartz.jobService.IJobService;
import com.visualization.cloud.util.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz.controller
 * @className: SelectController
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/14 22:09
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Controller
@RequestMapping(value = "/task/Select")
@Slf4j
@Tag(name = "定时任务中的查询操作")
public class SelectController {


    @Resource
    private IJobService jobService;
    //问题2:如果自定义的没有写自动结束时间,就会报错data not null,所以最好做一个异常处理
    //查询操作
    //查询job
    @PostMapping(value = "/jobs")
    @ResponseBody
    @Operation(summary = "获取所有任务", description = "获取所有任务")
    public ResultData<List<JobInfo>> getAllJobs() throws SchedulerException {
        List<JobInfo> jobInfos = jobService.getAllJobs();
        System.out.println( jobInfos);
        if ( !jobInfos.isEmpty()) {
            return  ResultData.success(jobInfos);
        } else {
            return ResultData.fail("500", "No job");
        }
    }

    //查询所有没有触发器的任务
    @PostMapping(value = "/NoUseJob")
    @ResponseBody
    @Operation(summary = "获取所有未使用的job类", description = "获取所有未使用的job类")
    public ResultData<List<JobInfo>> getAllNoUseJobs() {
        ResultData<List<JobInfo>> response = new ResultData<>();
        List<JobInfo> jobInfos = jobService.getAllNoUseJobs();
        System.out.println(jobInfos);
        if ( !jobInfos.isEmpty()) {
            return  ResultData.success(jobInfos);
        } else {
            return ResultData.fail("500", "No job");
        }
    }
    //按照小组搜索job
    @PostMapping(value = "/FINDjobBYgroup")
    @ResponseBody
    @Operation(summary = "根据分组获取job类", description = "根据分组获取job类")
    public ResultData<List<JobInfo>> getJobByGroup(String group) {
        return ResultData.success(jobService.getJobByGroup(group)) ;
    }
    @PostMapping(value = "/jobDetailname")
    @ResponseBody
    @Operation(summary = "获取job名称", description = "获取job名称")
    public ResultData<List<String>> getJobDetail( String jobgroup) throws SchedulerException {
        return ResultData.success(jobService.getJobName(jobgroup)) ;
    }
    //获取所有的job分组
    @PostMapping(value = "/jobgroupall")
    @ResponseBody
    @Operation(summary = "获取所有的job分组", description = "获取所有的job分组")
    public ResultData<List<String>> getJobGroupAll() {
        return ResultData.success(jobService.getJobGroupAll()) ;
    }

    //获取所有的trigger
    @PostMapping(value = "/triggers")
    @ResponseBody
    @Operation(summary = "获取trigger", description = "获取所有的trigger")
    public ResultData<List<JobInfo>> getAllTriggers(String triggergroup) {
        return ResultData.success(jobService.getAllTriggers(triggergroup)) ;
    }
    //根据job获取所有相关的trigger
    @PostMapping(value = "/FINDtriBYjob")
    @ResponseBody
    @Operation(summary = "根据job获取trigger类", description = "根据job获取trigger类")
    public ResultData<List<JobInfo>> getTriByJob(String jobname, String jobgroup) {
        return ResultData.success(jobService.getTriByJob(jobname, jobgroup)) ;
    }
    //获取所有的trigger分组
    @PostMapping(value = "/triggergroupall")
    @ResponseBody
    @Operation(summary = "获取所有的trigger分组", description = "获取所有的trigger分组")
    public ResultData<List<String>> getTriggerGroupAll() {
        return ResultData.success(jobService.getTriggerGroupAll()) ;
    }
    //根据triggergroup来搜索
//    @PostMapping(value = "/FINDtriggerBYgroup")
//    @ResponseBody
//    @Operation(summary = "根据分组获取trigger类", description = "根据分组获取trigger类")
//    public ResultData<List<JobInfo>> getTriggerByGroup(String triggergroup) {
//        return ResultData.success(jobService.getTriggerByGroup(triggergroup)) ;
//    }
    @PostMapping(value = "/Triggername")
    @ResponseBody
    @Operation(summary = "获取Trigger名称", description = "获取Trigger名称")
    public ResultData<List<String>> getTrigger( String triggergroup) throws SchedulerException {
        return ResultData.success(jobService.getTriggerName(triggergroup)) ;
    }
    //获取triggergroup详情
    @PostMapping(value = "/triggerDetail")
    @ResponseBody
    @Operation(summary = "获取trigger详情", description = "获取trigger详情")
    public ResultData<JobInfo> getTriggerDetail(String triggername, String triggergroup) throws SchedulerException {
        return ResultData.success(jobService.getTriggerInfo(triggername, triggergroup)) ;
    }
    //获取所有trigger

    //获取该触发器在指定时间为开始时间的下次触发时间
    @PostMapping(value = "/nextFireTime")
    @ResponseBody
    @Operation(summary = "获取指定时间开始的下次触发时间", description = "获取指定时间开始的下次触发时间")
    public ResultData<String> getNextFireTime(String triggername, String triggergroup, String specifiedtime) throws SchedulerException {
        System.out.println(specifiedtime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 转换为 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(specifiedtime, formatter);
        // 转换为 Date（如需兼容旧代码）
        Date specifiedTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(specifiedTime);
        return ResultData.success(jobService.getNextFireTime(triggername, triggergroup,specifiedTime)) ;
    }
    @PostMapping(value = "/isAllPaused")
    @ResponseBody
    @Operation(summary = "判断是否全部暂停", description = "判断是否全部暂停")
    public ResultData<Boolean> isAllPaused() throws SchedulerException {
        return ResultData.success(jobService.isAllPaused()) ;
    }

}
