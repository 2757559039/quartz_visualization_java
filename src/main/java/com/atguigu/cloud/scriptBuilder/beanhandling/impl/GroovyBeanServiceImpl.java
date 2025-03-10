package com.atguigu.cloud.scriptBuilder.beanhandling.impl;/**
 * @Auter 李孝在
 * @Date 2025/2/6
 */


import cn.hutool.core.util.StrUtil;
import com.atguigu.cloud.quartz.config.GroovyClassLoaderKillClassEnhance;
import com.atguigu.cloud.scriptBuilder.DAO.entity.VQrtzScript;
import com.atguigu.cloud.scriptBuilder.DAO.mapper.VQrtzScriptMapper;
import com.atguigu.cloud.scriptBuilder.beanhandling.IGroovyBeanService;
import com.atguigu.cloud.scriptBuilder.beanhandling.entity.DTO.SelectGroovyBeanInfoConditionDTO;
import com.atguigu.cloud.scriptBuilder.beanhandling.entity.GroovyBeanInfo;
import com.atguigu.cloud.util.exception.errCode.GroovyBeanExceptionCodeMsg;
import com.atguigu.cloud.util.exception.exception.GroovyBeanException;
import com.atguigu.cloud.util.DynamicBeanOperate;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.quartz.scriptBuilder.beanhandling.impl
 * @className: GroovyBeanServiceImpl
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/6 05:22
 * @version: 1.0
 */

@Service
public class GroovyBeanServiceImpl implements IGroovyBeanService {

    private final DynamicBeanOperate dynamicBeanRegistrar;
    private final GroovyClassLoaderKillClassEnhance classLoader;
    private final VQrtzScriptMapper vQrtzScriptMapper;
    private final ApplicationContext applicationContext;
    private final Scheduler scheduler;

    @Autowired
    public GroovyBeanServiceImpl(
            DynamicBeanOperate dynamicBeanRegistrar,
            GroovyClassLoaderKillClassEnhance classLoader,
            VQrtzScriptMapper vQrtzScriptMapper,
            ApplicationContext applicationContext,
            Scheduler scheduler
    ){
        this.dynamicBeanRegistrar=dynamicBeanRegistrar;
        this.classLoader=classLoader;
        this.vQrtzScriptMapper=vQrtzScriptMapper;
        this.applicationContext=applicationContext;
        this.scheduler=scheduler;
    }

    @Override
//    @Transactional
    public void loadGroovyBean(String className)  {
        QueryWrapper<VQrtzScript> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("CLASS_NAME",className);
        VQrtzScript vQrtzScript = vQrtzScriptMapper.selectOne(queryWrapper);
        if (Objects.isNull(vQrtzScript)){
            throw new GroovyBeanException(GroovyBeanExceptionCodeMsg.BEAN_NO_EXIST_IN_DB);
        }
        if (applicationContext.containsBean(getBeanName(className))){
            throw new GroovyBeanException(GroovyBeanExceptionCodeMsg.BEAN_EXIST);
        }
        Class<?> clazz = classLoader.parseClass(vQrtzScript.getScript());

        dynamicBeanRegistrar.registerBean(getBeanName(className),clazz);
        UpdateWrapper<VQrtzScript> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("CLASS_NAME",className).set("BEAN_STATE", true);
        vQrtzScriptMapper.update(updateWrapper);
    }


    @Override
    public void unloadGroovyBean(String className) throws SchedulerException {
        String beanName =getBeanName(className);
        boolean exists = applicationContext.containsBean(beanName);
        if (!exists){
            throw new GroovyBeanException(GroovyBeanExceptionCodeMsg.BEAN_NO_EXIST_IN_CONTEXT);
        }

        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
        for (JobKey jobKey : jobKeys) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            // 删除指定job的jobDetail
            if (jobDetail.getJobClass().getSimpleName().equals(beanName)) {
                scheduler.deleteJob(jobKey);
            }
        }


        classLoader.KillClass(getFullClassNameByBeanName(beanName));
        dynamicBeanRegistrar.unregisterBean(beanName);
        UpdateWrapper<VQrtzScript> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("CLASS_NAME",className).set("BEAN_STATE",false);
        vQrtzScriptMapper.update(updateWrapper);

    }

    @Override
    public List<GroovyBeanInfo> selectAllLoadedGroovyBean(SelectGroovyBeanInfoConditionDTO selectGroovyBeanInfoConditionDTO) {

        List<VQrtzScript> vQrtzScriptList= vQrtzScriptMapper.selectList(getQueryWrapper(selectGroovyBeanInfoConditionDTO,true));
        List<GroovyBeanInfo> groovyBeanInfoList =new ArrayList<>();
        for (var vQrtzScript : vQrtzScriptList) {
            String className= vQrtzScript.getClassName();
            String beanName=getBeanName(className);
            GroovyBeanInfo groovyBeanInfo=new GroovyBeanInfo();
            boolean exists = applicationContext.containsBean(beanName);
            if (exists) {
                groovyBeanInfo.setExists(exists);
                groovyBeanInfo.setScriptType(vQrtzScript.getScriptType());
                groovyBeanInfo.setClassName(vQrtzScript.getClassName());
                groovyBeanInfoList.add(groovyBeanInfo);
            }
        }

        return groovyBeanInfoList;
    }

    @Override
    public List<GroovyBeanInfo> selectAllUnLoadGroovyBean(SelectGroovyBeanInfoConditionDTO selectGroovyBeanInfoConditionDTO) {

        List<VQrtzScript> vQrtzScriptList= vQrtzScriptMapper.selectList(getQueryWrapper(selectGroovyBeanInfoConditionDTO,false));
        List<GroovyBeanInfo> groovyBeanInfoList = new ArrayList<>();
        for (var vQrtzScript : vQrtzScriptList) {
            String className = vQrtzScript.getClassName();
            String beanName=getBeanName(className);
            GroovyBeanInfo groovyBeanInfo = new GroovyBeanInfo();
            boolean exists = applicationContext.containsBean(beanName);
            if (!exists) {
                groovyBeanInfo.setExists(exists);
                groovyBeanInfo.setScriptType(vQrtzScript.getScriptType());
                groovyBeanInfo.setClassName(vQrtzScript.getClassName());
                groovyBeanInfoList.add(groovyBeanInfo);
            }

        }

        return groovyBeanInfoList;
    }



    @Override
    //查询数据库中的脚本,判断是否存在其bean,若存在输出额外的信息
    public List<GroovyBeanInfo> selectAllGroovyBean(SelectGroovyBeanInfoConditionDTO selectGroovyBeanInfoConditionDTO) {
        List<VQrtzScript> vQrtzScriptList= vQrtzScriptMapper.selectList(getQueryWrapper(selectGroovyBeanInfoConditionDTO,null));
        List<GroovyBeanInfo> groovyBeanInfoList =new ArrayList<>();
        for (var vQrtzScript : vQrtzScriptList) {
            String className= vQrtzScript.getClassName();

            GroovyBeanInfo groovyBeanInfo=new GroovyBeanInfo();
            String beanName=getBeanName(className);
            boolean exists = applicationContext.containsBean(beanName);
            groovyBeanInfo.setExists(exists);
            groovyBeanInfo.setScriptType(vQrtzScript.getScriptType());
            groovyBeanInfo.setClassName(vQrtzScript.getClassName());


            groovyBeanInfoList.add(groovyBeanInfo);
        }

        return groovyBeanInfoList;
    }


    @Override
    public GroovyBeanInfo selectGroovyBean(String className) {
        String beanName=getBeanName(className);
        GroovyBeanInfo groovyBeanInfo=new GroovyBeanInfo();

        boolean exists = applicationContext.containsBean(beanName);
        groovyBeanInfo.setExists(exists);
        groovyBeanInfo.setClassName(className);

        return groovyBeanInfo;

    }



    private String getBeanName(String className){
        return className;
//                .substring(0, 1).toLowerCase()
//                + className.substring(1); // 类名首字母小写作为 Bean 名称
    }

    private String getFullClassNameByBeanName(String beanName){
        Object o= applicationContext.getBean(beanName);
        return o.getClass().getName();
    }
    private QueryWrapper<VQrtzScript> getQueryWrapper(SelectGroovyBeanInfoConditionDTO selectGroovyBeanInfoConditionDTO,Boolean flag){
        String getKeywords = selectGroovyBeanInfoConditionDTO.getKeywords();
        String filterCriteria = selectGroovyBeanInfoConditionDTO.getFilterCriteria();
        QueryWrapper<VQrtzScript>  queryWrapper =new QueryWrapper<>();
        if (Objects.nonNull(flag)){
            queryWrapper.eq("BEAN_STATE",flag);
        }
        if (StrUtil.isNotBlank(getKeywords)){
            queryWrapper.like("CLASS_NAME",getKeywords);
        }
        if (StrUtil.isNotBlank(filterCriteria)){
            queryWrapper.eq("SCRIPT_TYPE",filterCriteria);
        }

        return queryWrapper;
    }

}
