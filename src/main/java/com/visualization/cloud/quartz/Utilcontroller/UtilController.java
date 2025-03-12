package com.visualization.cloud.quartz.Utilcontroller;/**
 * @Auter zzh
 * @Date 2024/12/14
 */



import com.visualization.cloud.util.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz.controller
 * @className: UtilController
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/14 22:43
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Controller
@RequestMapping(value = "/task/Util")
@Slf4j
@Tag(name = "定时任务中的工具方法")
public class UtilController {
    // 校验是否是合法cron表达式
    @PostMapping(value = "/cron-check")
    @ResponseBody
    @Operation(summary = "校验cron表达式", description = "校验cron表达式")
    public ResultData<Boolean> checkCron(String cron) {
        boolean valid = false;
        try {
            valid= CronExpression.isValidExpression(cron);
             ;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultData.fail("500", "cron表达式格式错误！");
        }
        if (valid) {
            return ResultData.success(true);
        } else {
            return ResultData.fail("500", "cron表达式格式错误！");
        }
    }
    @PostMapping(value = "/getType")
    @ResponseBody
    @Operation(summary = "获取类型", description = "获取类型")
    public ResultData<String> getType() {
        return ResultData.success("SimpleTrigger, CronTrigger, DailyTimeIntervalTrigger, CalendarIntervalTrigger");
    }


}
