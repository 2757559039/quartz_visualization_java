package com.visualization.cloud.quartz.Utilcontroller;/**
 * @Auter zzh
 * @Date 2024/12/14
 */



import com.visualization.cloud.quartz.config.ClassScanner;
import com.visualization.cloud.util.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz.controller
 * @className: ReflectController
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/14 22:39
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Controller
@RequestMapping(value = "/task/Reflect")
@Slf4j
@Tag(name = "定时任务中的反射类的操作")
public class ReflectController {
    @Resource
    ClassScanner classScanner;
    //通过反射获取类的列表
    @PostMapping(value = "/jobclass")
    @ResponseBody
    @Operation(summary = "获取所有job类", description = "获取所有job类")
    public ResultData<List<String>> getAllJobClass() {
        return ResultData.success(classScanner.getscanJobClass()) ;
    }

    @PostMapping(value = "/jobdetailclass")
    @ResponseBody
    @Operation(summary = "获取所有jobdetail类", description = "获取所有jobdetail类")
    public ResultData<List<String>> getAllJobDetailClass() {
        return ResultData.success(classScanner.getscanJobDetailClass()) ;
    }

    @PostMapping(value = "/triggerclass")
    @ResponseBody
    @Operation(summary = "获取所有trigger类", description = "获取所有trigger类")
    public ResultData<List<String>> getAllTriggerClass(String type) {
        return ResultData.success(classScanner.getscanTriggerClass(type)) ;
    }

    @PostMapping(value = "/updatetriggerclass")
    @ResponseBody
    @Operation(summary = "获取所有updatetrigger类", description = "获取所有updatetrigger类")
    public ResultData<List<String>> getAllUpdateTriggerClass(String type ) {
        return ResultData.success(classScanner.getscanUpdateTriggerClass(type)) ;
    }
}
