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

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz.controller
 * @className: Addcontroller
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/14 22:07
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Controller
@RequestMapping(value = "/task/Add")
@Slf4j
@Tag(name = "定时任务中的增加任务")
public class AddController {


    @Resource
    private IJobService jobService;
    //增加操作
    //添加job+trigeer
    @PostMapping(value = "/job")
    @Operation(summary = "添加任务", description = "添加任务")
    @ResponseBody
    public ResultData<String> addJob(@RequestBody JobInfo jobInfo) throws SchedulerException {
        int result = jobService.addJob(jobInfo);
        if (result == -1) {
            return ResultData.fail("500", "任务已存在！");
        } else if (result == 0) {
            return ResultData.success("success");
        } else if (result == 1) {
            return ResultData.fail("500 ", "没有该任务对应的 Java Class 类！");
        } else if (result == 2) {
            return  ResultData.fail("500", "添加任务失败！");
        } else if (result == 3) {
            return  ResultData.fail("500", "时间格式错误！");
        } else if (result==-2) {
            return ResultData.fail("500", "调度器已存在!");
        } else if (result==-3) {
            return ResultData.fail("500", "类型为空！");
        } else if (result==-4) {
            return ResultData.fail("500", "类型错误！");
        }else if (result==-5) {
            return ResultData.fail("500", "任务名称,任务分组,任务类不能为空！");
        } else if (result==-6) {
            return ResultData.fail("500", "日期错误！");
        } else {
            return  ResultData.fail("500", "未知错误！");
        }
    }
    //给已经存在的任务,增加触发器,去调度jobdetial,增加trigger
    @PostMapping(value = "/jobTOtri")
    @ResponseBody
    @Operation(summary = "给job添加某个特定的调度器", description = "给job添加某个特定的调度器")
    public ResultData<String> jobTOtri(@RequestBody JobInfo jobInfo) throws SchedulerException {
        int result = jobService.jobTOtri(jobInfo);
        if (result == -1) {
            return ResultData.fail("500", "任务不存在！");
        } else if (result == 0) {
            return ResultData.success("success");
        } else if (result == 1) {
            return ResultData.fail("500 ", "没有该任务对应的 Java Class 类！");
        } else if (result == 2) {
            return  ResultData.fail("500", "添加任务失败！");
        } else if (result == 3) {
            return  ResultData.fail("500", "时间格式错误！");
        } else if (result==-2) {
            return ResultData.fail("500", "触发器已存在!");
        } else if (result==-3) {
            return ResultData.fail("500", "类型为空！");
        } else if (result==-4) {
            return ResultData.fail("500", "类型错误！");
        }else if (result==-5) {
            return ResultData.fail("500", "任务名称,任务分组,任务类不能为空！");
        } else if (result==-6) {
            return ResultData.fail("500", "日期错误！");
        } else if (result == -8) {
            return ResultData.fail("500", "cron表达式不能为空！");
        } else {
            return  ResultData.fail("500", "未知错误！");
        }
    }
    //添加任务实现类,单独增加jobdetail
    @PostMapping(value = "/jobdetail")
    @ResponseBody
    @Operation(summary = "添加任务实现类", description = "添加任务实现类")
    public ResultData<String> addJobDetail(String jobclassname, String jobname, String jobgroup,  String isCustomJobDetail, String jobDetail,String description) throws ClassNotFoundException, SchedulerException {
        int result = jobService.createdetail(jobclassname, jobname, jobgroup,  isCustomJobDetail, jobDetail, description);
        if (result == -1) {
            return ResultData.fail("500", "任务已存在！");
        } else if (result == 0) {
            return ResultData.success("success");
        } else if (result == 1) {
            return ResultData.fail("500 ", "没有该任务对应的 Java Class 类！");
        } else if (result == 2) {
            return  ResultData.fail("500", "添加任务失败！");
        } else if (result == 3) {
            return  ResultData.fail("500", "时间格式错误！");
        } else if (result==-2) {
            return ResultData.fail("500", "调度器已存在!");
        } else if (result==-3) {
            return ResultData.fail("500", "类型为空！");
        } else if (result==-4) {
            return ResultData.fail("500", "类型错误！");
        }else if (result==-5) {
            return ResultData.fail("500", "任务名称,任务分组,任务类不能为空！");
        } else if (result==-6) {
            return ResultData.fail("500", "日期错误！");
        } else {
            return  ResultData.fail("500", "未知错误！");
        }
    }
}
