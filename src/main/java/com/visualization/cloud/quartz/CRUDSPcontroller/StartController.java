package com.visualization.cloud.quartz.CRUDSPcontroller;/**
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
 * @className: StartController
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/14 22:09
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Controller
@RequestMapping(value = "/task/Start")
@Slf4j
@Tag(name = "定时任务中的启动操作")
public class StartController {

    @Resource
    private IJobService jobService;
    //启动操作
    //恢复任务
    @PostMapping(value = "/resume")
    @ResponseBody
    @Operation(summary = "恢复任务", description = "恢复任务")
    public ResultData<String> resumeJob(String name, String group) throws SchedulerException {
        if (jobService.resumeJob(name, group)) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }
    //恢复触发器
    @PostMapping(value = "/resumetri")
    @ResponseBody
    @Operation(summary = "给job恢复某个特定的trigger", description = "给job恢复某个特定的trigger")
    public ResultData<String> resumeTri( String triname, String trigroup) throws SchedulerException {
        if (jobService.resumeTri(triname, trigroup)) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }
    //恢复所有
    @PostMapping(value = "/resumeall")
    @ResponseBody
    @Operation(summary = "恢复所有任务", description = "恢复所有任务")
    public ResultData<String> resumeAllJob() throws SchedulerException {
        if (jobService.resumeAllJob()) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }
    @PostMapping(value = "/resumeNow")
    @ResponseBody
    @Operation(summary = "立即执行任务", description = "立即执行任务")
    public ResultData<String> resumeNow(String name, String group) throws SchedulerException {
        if (jobService.resumeNow(name, group)) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "触发器出错,或任务还未执行完成,请等任务结束后,再次尝试!");
        }
    }

}
