package com.atguigu.cloud.quartz.CRUDSPcontroller;/**
 * @Auter zzh
 * @Date 2024/12/14
 */



import com.atguigu.cloud.quartz.jobService.IJobService;//改动2
import com.atguigu.cloud.util.resp.ResultData;
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
 * @className: DeleteController
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/14 22:08
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Controller
@RequestMapping(value = "/task/Delete")
@Slf4j
@Tag(name = "定时任务中的删除操作")
public class DeleteController {


    @Resource
    private IJobService jobService;
    //删除操作
    @PostMapping(value = "/job")
    @ResponseBody
    @Operation(summary = "删除任务", description = "删除任务")
    public ResultData<String> deleteJob(String name, String group) throws SchedulerException {
        if (jobService.deleteJob(name, group)) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }
    //删除调度器
    @PostMapping(value = "/jobUNtri")
    @ResponseBody
    @Operation(summary = "给job取消某个特定的调度器", description = "给job取消某个特定的调度器")
    public ResultData<String> jobUNtri(String triggername, String triggergroup) throws SchedulerException {
        if (jobService.jobUNtri(triggername, triggergroup)) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }

    @PostMapping(value = "/alljob")
    @ResponseBody
    @Operation(summary = "删除所有任务", description = "删除所有任务")
    public ResultData<String> deleteAllJob() throws SchedulerException {
        if (jobService.deleteAllJob()) {
            return ResultData.success("success");
        } else {
            return ResultData.fail("500", "error");
        }
    }
}
