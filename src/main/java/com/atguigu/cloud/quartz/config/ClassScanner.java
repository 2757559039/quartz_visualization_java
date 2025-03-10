package com.atguigu.cloud.quartz.config;/**
 * @Auter zzh
 * @Date 2024/12/12
 */

/**
 * @projectName: kehse-springboot02
 * @package: com.atguigu.cloud.quartz
 * @className: util
 * @author: Eric
 * @description: TODO
 * @date: 2024/12/12 13:45
 * @version: 1.0
 */

import com.atguigu.cloud.scriptBuilder.DAO.entity.ScriptTypeEnums;
import com.atguigu.cloud.scriptBuilder.DAO.entity.VQrtzScript;
import com.atguigu.cloud.scriptBuilder.DAO.mapper.VQrtzScriptMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.atguigu.cloud.quartz.config.SpringContextHolder.containsBean;
import static com.atguigu.cloud.quartz.config.SpringContextHolder.getBean;

@Component
public class ClassScanner {
//    @Value("${job.jobPackageName}")
//    public String  jobPackageName;
//    @Value("${job.jobDetailPackageName}")
//    public String jobDetailPackageName;
//    @Value("${job.triggerPackageName}")
//    public String triggerPackageName;
//    @Value("${job.updateTriggerPackageName}")
//    public String updateTriggerPackageName;


    private final VQrtzScriptMapper vQrtzJobScriptMapper;
    @Autowired
    public ClassScanner(VQrtzScriptMapper vQrtzJobScriptMapper){
        this.vQrtzJobScriptMapper=vQrtzJobScriptMapper;
    }



//    public Set<Class<?>> jobclass;
//    public Set<Class<?>> jobDetailclass;
//    public Set<Class<?>> triggerclass;
//    public Set<Class<?>> updateTriggerclass;

    public List<String> getscanJobClass(){
//        jobclass  = ClassUtil.scanPackage(jobPackageName);
        QueryWrapper<VQrtzScript> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("SCRIPT_TYPE", ScriptTypeEnums.JOB.getType());
        List<VQrtzScript> vQrtzScripts= vQrtzJobScriptMapper.selectList(queryWrapper);
        var list = vQrtzScripts
                .stream()
                .map(VQrtzScript::getClassName)
                .filter(SpringContextHolder::containsBean)
                .toList()
                ;

        return list;
    }

    public List<String> getscanJobDetailClass(){
        QueryWrapper<VQrtzScript> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("SCRIPT_TYPE", ScriptTypeEnums.JOB_DETAIL.getType());
        List<VQrtzScript> vQrtzScripts= vQrtzJobScriptMapper.selectList(queryWrapper);
        return vQrtzScripts.stream().map(VQrtzScript::getClassName).filter(SpringContextHolder::containsBean)
                .toList();
    }
    public List<String> getscanTriggerClass(String Type){
        QueryWrapper<VQrtzScript> queryWrapper= new QueryWrapper<>();
        switch (Type){
            case "SimpleTrigger":
                queryWrapper.eq("SCRIPT_TYPE", ScriptTypeEnums.SIMPLE_TRIGGER.getType());
                break;
            case "CalendarIntervalTrigger":
                queryWrapper.eq("SCRIPT_TYPE", ScriptTypeEnums.CALENDAR_INTERVAL_TRIGGER.getType());
                break;
            case "DailyTimeIntervalTrigger":
                queryWrapper.eq("SCRIPT_TYPE", ScriptTypeEnums.DAILY_TIME_INTERVAL_TRIGGER.getType());
                break;
            case "CronTrigger":
                queryWrapper.eq("SCRIPT_TYPE", ScriptTypeEnums.CRON_TRIGGER.getType());
                break;
            default:return new ArrayList<>();
        }
        List<VQrtzScript> vQrtzScripts= vQrtzJobScriptMapper.selectList(queryWrapper);
        return vQrtzScripts.stream().map(VQrtzScript::getClassName).filter(SpringContextHolder::containsBean).toList();
    }
    public List<String> getscanUpdateTriggerClass(String Type){
        QueryWrapper<VQrtzScript> queryWrapper= new QueryWrapper<>();
        switch (Type){
            case "SimpleUpdateTrigger":
                queryWrapper.eq("SCRIPT_TYPE", ScriptTypeEnums.SIMPLE_UPDATE_TRIGGER.getType());
                break;
            case "CalendarUpdateIntervalTrigger":
                queryWrapper.eq("SCRIPT_TYPE", ScriptTypeEnums.CALENDAR_UPDATE_INTERVAL_TRIGGER.getType());
                break;
            case "DailyUpdateTimeIntervalTrigger":
                queryWrapper.eq("SCRIPT_TYPE", ScriptTypeEnums.DAILY_UPDATE_TIME_INTERVAL_TRIGGER.getType());
                break;
            case "CronUpdateTrigger":
                queryWrapper.eq("SCRIPT_TYPE", ScriptTypeEnums.CRON_UPDATE_TRIGGER.getType());
                break;
            default:return new ArrayList<>();
        }

        List<VQrtzScript> vQrtzScripts= vQrtzJobScriptMapper.selectList(queryWrapper);
        return vQrtzScripts.stream().map(VQrtzScript::getClassName).filter(SpringContextHolder::containsBean).toList();
    }
}



