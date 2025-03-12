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

import java.lang.reflect.InvocationTargetException;

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz.controller
 * @className: UpdateController
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/14 22:08
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Controller
@RequestMapping(value = "/task/Update")
@Slf4j
@Tag(name = "定时任务中的更新操作")
public class  UpdateController {


    @Resource
    private IJobService jobService;

    //更新操作
    //新增一个更换触发器的功能,但可以读取到原本触发器的信息,有三个不同维度的功能,第一个维度,替换整个触发器,第二个维度,替换原本触发器的一些关键参数,第三个维度,更改触发器的优先级
    @PostMapping(value = "/updatetrigeerargument")
    @ResponseBody
    @Operation(summary = "只能更新默认触发器拥有的参数", description = "只能更新默认触发器拥有的参数")
    public ResultData<String> updateTriggerargument(@RequestBody JobInfo jobInfo) throws SchedulerException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (jobService.updateTriggerargument(jobInfo)) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }

    //更换触发器
    @PostMapping(value = "/updateTrigger")
    @ResponseBody
    @Operation(summary = "job更换触发器", description = "更换触发器")
    public ResultData<String> updateTrigger(@RequestBody  JobInfo jobinfo,String oldtriggername, String oldtriggergroup) throws SchedulerException {
        System.out.println(jobinfo);
        System.out.println("oldname:"+oldtriggername+' '+"oldgergroup:"+oldtriggergroup);
        if (jobService.updateTrigger(oldtriggername, oldtriggergroup, jobinfo)) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }
    //更新任务
    @PostMapping(value = "/updatejob")
    @ResponseBody
    @Operation(summary = "更新现有的任务", description = "更新现有的任务")
    public ResultData<String> updateJob( String jobclassname, String jobname, String jobgroup,  String isCustomJobDetail, String jobDetail,String description) throws SchedulerException {
        int result = jobService.updateJob(jobclassname, jobname, jobgroup,  isCustomJobDetail, jobDetail, description);
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
