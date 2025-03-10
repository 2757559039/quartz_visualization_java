package com.atguigu.cloud.scriptBuilder;/**
 * @Auter 李孝在
 * @Date 2025/1/23
 */

import com.atguigu.cloud.scriptBuilder.DAO.entity.JobCodeEntity;
import com.atguigu.cloud.scriptBuilder.DAO.entity.ScriptTypeEnums;
import com.atguigu.cloud.scriptBuilder.DAO.entity.VQrtzScript;
import com.atguigu.cloud.scriptBuilder.DAO.entity.po.UpdateScriptPO;
import com.atguigu.cloud.scriptBuilder.datahandling.IDatabaseScript;
import com.atguigu.cloud.scriptBuilder.datahandling.entity.DTO.SelectScriptInfoConditionDTO;
import com.atguigu.cloud.util.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.quartz.scriptBuilder
 * @className: Controller
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/23 00:36
 * @version: 1.0
 */
@CrossOrigin(origins ="${server.port}" )
@Slf4j
@RestController
@RequestMapping(value = "/scriptBuilder")
public class JobBuilderController {



//    private final IDatabaseScript databaseScript;
//
//    @Autowired
//    private JobBuilderController(IDatabaseScript databaseScript){
//        this.databaseScript=databaseScript;
//    }

    @Resource
    private IDatabaseScript databaseScript;
    //保存
    @PostMapping(value = "/saveJobToDB")
    @Operation(summary = "保存job到数据库")
    @Tag(name = "保存")
    @ResponseBody
    public ResultData<String> saveJobToDB(@RequestBody JobCodeEntity jobCodeEntity){
        databaseScript.recorder(jobCodeEntity.getCode(), ScriptTypeEnums.JOB);
        return ResultData.success("保存成功");
    }
    @PostMapping(value = "/saveJobDetailToDB")
    @Operation(summary = "保存JobDetail到数据库")
    @Tag(name = "保存")
    @ResponseBody
    public ResultData<String> saveJobDetailToDB(@RequestBody JobCodeEntity jobCodeEntity){
        databaseScript.recorder(jobCodeEntity.getCode(), ScriptTypeEnums.JOB_DETAIL);
        return ResultData.success("保存成功");
    }

    @PostMapping(value = "/saveTriggerToDB")
    @Operation(summary = "保存trigger到数据库")
    @Tag(name = "保存")
    @ResponseBody
    public ResultData<String> saveTriggerToDB(@RequestBody JobCodeEntity jobCodeEntity,String Type){
        switch (Type){
            case "SimpleTrigger":
                databaseScript.recorder(jobCodeEntity.getCode(), ScriptTypeEnums.SIMPLE_TRIGGER);
                break;
            case "CalendarIntervalTrigger":
                databaseScript.recorder(jobCodeEntity.getCode(), ScriptTypeEnums.CALENDAR_INTERVAL_TRIGGER);
                break;
            case "DailyTimeIntervalTrigger":
                databaseScript.recorder(jobCodeEntity.getCode(), ScriptTypeEnums.DAILY_TIME_INTERVAL_TRIGGER);
                break;
            case "CronTrigger":
                databaseScript.recorder(jobCodeEntity.getCode(), ScriptTypeEnums.CRON_TRIGGER);
                break;
            default:
                return ResultData.fail("500","类型错误");
        }
        return ResultData.success("保存成功");
    }

    @PostMapping(value = "/saveUpdateTriggerToDB")
    @Operation(summary = "保存updateTrigger到数据库")
    @Tag(name = "保存")
    @ResponseBody
    public ResultData<String> saveUpdateTriggerToDB(@RequestBody JobCodeEntity jobCodeEntity,String Type){
        switch (Type){
            case "SimpleUpdateTrigger":
                databaseScript.recorder(jobCodeEntity.getCode(), ScriptTypeEnums.SIMPLE_UPDATE_TRIGGER);
                break;
            case "CalendarUpdateIntervalTrigger":
                databaseScript.recorder(jobCodeEntity.getCode(), ScriptTypeEnums.CALENDAR_UPDATE_INTERVAL_TRIGGER);
                break;
            case "DailyUpdateTimeIntervalTrigger":
                databaseScript.recorder(jobCodeEntity.getCode(), ScriptTypeEnums.DAILY_UPDATE_TIME_INTERVAL_TRIGGER);
                break;
            case "CronUpdateTrigger":
                databaseScript.recorder(jobCodeEntity.getCode(), ScriptTypeEnums.CRON_UPDATE_TRIGGER);
                break;
            default:
                return ResultData.fail("500","类型错误");
        }
        return ResultData.success("保存成功");
    }

    @PostMapping(value = "/deleteScript")
    @Operation(summary = "删除脚本")
    @Tag(name = "删除")
    @ResponseBody
    public ResultData<String> deleteScript(@RequestParam Integer id) throws SchedulerException {
        databaseScript.delete(id);
        return ResultData.success("删除成功");
    }

    @PostMapping(value = "/updateScript")
    @Operation(summary = "更新脚本:仅更新脚本内容而不更新脚本关系")
    @Tag(name = "更新")
    @ResponseBody
    public ResultData<String> updateScript(@RequestBody UpdateScriptPO updateScriptPO) throws SchedulerException {
        databaseScript.update(updateScriptPO.getId(),updateScriptPO.getScript());
        return ResultData.success("更新成功");
    }

    @PostMapping(value = "/selectAllScript")
    @Operation(summary = "查询所有脚本:传入类型,或者不传-->查所有")
    @Tag(name = "查询")
    @ResponseBody
    public ResultData<List<VQrtzScript>> selectAllScript(SelectScriptInfoConditionDTO selectScriptInfoConditionDTO) throws SchedulerException {
        return ResultData.success(  databaseScript.selectAll(selectScriptInfoConditionDTO));
    }

    @PostMapping(value = "/selectScript")
    @Operation(summary = "查询单个脚本")
    @Tag(name = "查询")
    @ResponseBody
    public ResultData<VQrtzScript> selectScript(@RequestParam Integer id) throws SchedulerException {
        return ResultData.success(  databaseScript.select(id));
    }
    @PostMapping(value = "/selectFilterCriteria")
    @Operation(summary = "查询脚本的所有类型:数据字典")
    @Tag(name = "查询")
    @ResponseBody
    public ResultData<List<String>> selectFilterCriteria(){
        return ResultData.success( databaseScript.selectFilterCriteria());
    }

    //加载
    @PostMapping(value = "/loadFromDB")
    @Operation(summary = "加载脚本从数据库中")
    @Tag(name = "加载")
    @ResponseBody
    public ResultData<String> loadJobFromDB(){
        databaseScript.load();
        return ResultData.success("加载成功");
    }


}
