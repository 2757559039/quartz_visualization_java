package com.visualization.cloud.scriptBuilder;/**
 * @Auter 李孝在
 * @Date 2025/2/6
 */

import com.visualization.cloud.scriptBuilder.beanhandling.IGroovyBeanService;
import com.visualization.cloud.scriptBuilder.beanhandling.entity.DTO.SelectGroovyBeanInfoConditionDTO;
import com.visualization.cloud.scriptBuilder.beanhandling.entity.GroovyBeanInfo;
import com.visualization.cloud.scriptBuilder.beanhandling.impl.GroovyBeanServiceImpl;
import com.visualization.cloud.util.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.quartz.scriptBuilder
 * @className: QuartzBeanController
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/6 04:42
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Slf4j
@RestController
@RequestMapping(value = "/groovyBean")
public class GroovyBeanController {
    private final IGroovyBeanService groovyBeanService;

    @Autowired
    private GroovyBeanController(GroovyBeanServiceImpl groovyBeanService){
        this.groovyBeanService=groovyBeanService;
    }

    @PostMapping(value = "/loadGroovyBean")
    @Operation(summary = "安装GroovyBean")
    @Tag(name = "安装")
    @ResponseBody
    public ResultData<String> loadGroovyBean(@RequestParam String className) throws SchedulerException, ClassNotFoundException {
        groovyBeanService.loadGroovyBean(className);
        return ResultData.success("安装成功");
    }

    @PostMapping(value = "/unloadGroovyBean")
    @Operation(summary = "卸载GroovyBean")
    @Tag(name = "卸载")
    @ResponseBody
    public ResultData<String> unloadGroovyBean(@RequestParam String className) throws SchedulerException {
        groovyBeanService.unloadGroovyBean(className);
        return ResultData.success("卸载成功");
    }

    @PostMapping(value = "/selectAllLoadGroovyBean")
    @Operation(summary = "查询所有已启动Bean")
    @Tag(name = "查询所有")
    @ResponseBody
    public ResultData<List<GroovyBeanInfo>> selectAllLoadGroovyBean(@RequestBody SelectGroovyBeanInfoConditionDTO selectGroovyBeanInfoConditionDTO) throws SchedulerException {

        return ResultData.success(  groovyBeanService.selectAllLoadedGroovyBean(selectGroovyBeanInfoConditionDTO));
    }

    @PostMapping(value = "/selectAllUnloadGroovyBean")
    @Operation(summary = "查询所有未启动Bean")
    @Tag(name = "查询所有")
    @ResponseBody
    public ResultData<List<GroovyBeanInfo>> selectAllUnloadGroovyBean(@RequestBody SelectGroovyBeanInfoConditionDTO selectGroovyBeanInfoConditionDTO) throws SchedulerException {

        return ResultData.success(  groovyBeanService.selectAllUnLoadGroovyBean(selectGroovyBeanInfoConditionDTO));
    }



    @PostMapping(value = "/selectAllGroovyBean")
    @Operation(summary = "查询所有Bean")
    @Tag(name = "查询所有")
    @ResponseBody
    public ResultData<List<GroovyBeanInfo>> selectAllGroovyBean(@RequestBody SelectGroovyBeanInfoConditionDTO selectGroovyBeanInfoConditionDTO) {
        List<GroovyBeanInfo> list=groovyBeanService.selectAllGroovyBean(selectGroovyBeanInfoConditionDTO);
        return ResultData.success( list );
    }

    //加载
    @PostMapping(value = "/selectGroovyBean")
    @Operation(summary = "查询Bean详情")
    @Tag(name = "查询Bean详情")
    @ResponseBody
    public ResultData<GroovyBeanInfo> selectGroovyBean(@RequestParam String className){
        return ResultData.success( groovyBeanService.selectGroovyBean(className));
    }



}
