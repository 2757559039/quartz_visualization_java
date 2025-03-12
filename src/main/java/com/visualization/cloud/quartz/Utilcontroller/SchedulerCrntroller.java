package com.visualization.cloud.quartz.Utilcontroller;/**
 * @Auter zzh
 * @Date 2024/12/14
 */



import com.visualization.cloud.quartz.jobService.IJobService;
import com.visualization.cloud.util.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz.controller
 * @className: SchedulerCrntroller
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/14 22:42
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Controller
@RequestMapping(value = "/task/Scheduler")
@Slf4j
@Tag(name = "定时任务中的调度器操作")
public class SchedulerCrntroller {
    @Resource
    private IJobService jobService;
    //调度器的操作
    //关闭调度器
    @PostMapping(value = "/shutdown")
    @ResponseBody
    @Operation(summary = "关闭调度器", description = "关闭调度器,iswait为true时等待任务执行完毕")
    public ResultData<String> shutdown(boolean iswait) throws SchedulerException {
        if (jobService.shutdown(iswait)) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }

    //待机调度器
    @PostMapping(value = "/standby")
    @ResponseBody
    @Operation(summary = "待机调度器", description = "待机调度器")
    public ResultData<String> standby() throws SchedulerException {
        if (jobService.standby()) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }
    //启动调度器
    @PostMapping(value = "/start")
    @ResponseBody
    @Operation(summary = "启动调度器", description = "启动调度器")
    public ResultData<String> start() throws SchedulerException {
        if (jobService.start()) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }
}
