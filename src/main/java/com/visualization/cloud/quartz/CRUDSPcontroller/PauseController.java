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
import org.springframework.web.bind.annotation.*;

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz.controller
 * @className: PauseController
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/14 22:12
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Controller
@RequestMapping(value = "/task/Pause")
@Slf4j
@Tag(name = "定时任务中的暂停操作")
public class PauseController {

    @Resource
    private IJobService jobService;

    //暂停操作
    @PostMapping(value = "/job")
    @ResponseBody
    @Operation(summary = "暂停任务", description = "暂停任务")
    public ResultData<String> pauseJob(String jobname, String jobgroup) throws SchedulerException {

        if (jobService.pauseJob(jobname, jobgroup)) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }

    @PostMapping(value = "/trigger")
    @ResponseBody
    @Operation(summary = "给job暂停某个特定的调度器", description = "给job暂停某个特定的调度器")
    public ResultData<String> pauseTri(String triname, String trigroup) throws SchedulerException {

        if (jobService.pauseTri(triname, trigroup)) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }
    //暂停所有
    @PostMapping(value = "/alljob")
    @ResponseBody
    @Operation(summary = "暂停所有任务", description = "暂停所有任务")
    public ResultData<String> pauseAllJob() throws SchedulerException {
        if (jobService.pauseAllJob()) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }
}
