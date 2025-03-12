package com.visualization.cloud.scriptBuilder.datahandling.impl;/**
 * @Auter 李孝在
 * @Date 2025/1/27
 */

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.visualization.cloud.quartz.config.GroovyClassLoaderKillClassEnhance;

//import com.atguigu.cloud.quartz.scriptBuilder.DAO.entity.QrtzJobDetails;
import com.visualization.cloud.quartz.jobService.JobServiceImp;
import com.visualization.cloud.quartz.po.JobInfo;
import com.visualization.cloud.scriptBuilder.DAO.entity.ScriptTypeEnums;
import com.visualization.cloud.scriptBuilder.DAO.entity.VQrtzScript;
//import com.atguigu.cloud.quartz.scriptBuilder.DAO.mapper.QrtzJobDetailsMapper;
import com.visualization.cloud.scriptBuilder.DAO.entity.VScriptTag;
import com.visualization.cloud.scriptBuilder.DAO.mapper.VQrtzScriptMapper;
import com.visualization.cloud.scriptBuilder.datahandling.IDatabaseScript;
import com.visualization.cloud.scriptBuilder.datahandling.VScriptTagScervice;
import com.visualization.cloud.scriptBuilder.datahandling.entity.DTO.SelectScriptInfoConditionDTO;
import com.visualization.cloud.util.exception.errCode.JobBuilderExceptionCodeMsg;
import com.visualization.cloud.util.exception.exception.JobBuilderException;
import com.visualization.cloud.util.DynamicBeanOperate;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.visualization.cloud.scriptBuilder.DAO.mapper.VScriptTagMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.Phases;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static cn.hutool.core.lang.Console.log;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.quartz.scriptBuilder.datahandling.impl
 * @className: DatabaseScriptImpl
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/27 07:27
 * @version: 1.0
 */
@Slf4j
@Service
public class DatabaseScriptImpl implements IDatabaseScript {
    private final VQrtzScriptMapper vQrtzJobScriptMapper;
    private final GroovyClassLoaderKillClassEnhance classLoader;
    private final DynamicBeanOperate dynamicBeanRegistrar;
    private final ApplicationContext applicationContext;
    private final Scheduler scheduler;
    private final VScriptTagScervice vScriptTagScervice;
    private final JobServiceImp jobServiceImp;
    public DatabaseScriptImpl(VQrtzScriptMapper vQrtzJobScriptMapper,
                              GroovyClassLoaderKillClassEnhance classLoader,
                                  DynamicBeanOperate dynamicBeanRegistrar,
                                  ApplicationContext applicationContext,
                                  VScriptTagMapper vScriptTagMapper,
                                  JobServiceImp jobServiceImp,
                                  VScriptTagScervice vScriptTagScervice,
                                  Scheduler scheduler) {
        this.vQrtzJobScriptMapper = vQrtzJobScriptMapper;
        this.classLoader=classLoader;
        this.dynamicBeanRegistrar=dynamicBeanRegistrar;
//        this.qrtzJobDetailsMapper=qrtzJobDetailsMapper;
        this.applicationContext=applicationContext;
        this.scheduler=scheduler;
        this.vScriptTagScervice=vScriptTagScervice;
        this.jobServiceImp=jobServiceImp;

    }

    private static String classPattern = "(class\\s+)(\\w+)(\\s+implements\\s+Job\\s*\\{)";

    /**
     * <h1>
     *     加载任务:不确定是否有用(
     * </h1>
     */
    public void load() {
        //加载所有任务的脚本
        List<VQrtzScript> vQrtzJobScriptList=vQrtzJobScriptMapper.selectList(null);
        for (VQrtzScript script : vQrtzJobScriptList){
            loadJobClass(script.getScript());
        }
    }

    private void loadJobClass(String code) {
        Class<?> clazz = classLoader.parseClass(code);
    }



    @Override
//    @Transactional
    public void recorder(String script, ScriptTypeEnums type) {

        Class<?> clazz = classLoader.parseClass(script);

        String className = clazz.getSimpleName(); // 获取类名
        String beanName = getBeanName(className);

        //唯一性验证
        QueryWrapper<VQrtzScript> queryWrapper=new QueryWrapper<>();
        if(CollUtil.isNotEmpty(vQrtzJobScriptMapper.selectList(queryWrapper.eq("CLASS_NAME",className)))){
            throw new JobBuilderException(JobBuilderExceptionCodeMsg.JOB_SCRIPT_EXISTS);
        }
        //添加至bean
        dynamicBeanRegistrar.registerBean(beanName, clazz);
        //添加至数据库
        VQrtzScript vQrtzScript = new VQrtzScript()
                .setScript(script)
                .setClassName(className)
                .setScriptType(type.getType())
                .setBeanState(true);
        vQrtzJobScriptMapper.insert(vQrtzScript);
    }

    @Override
//    @Transactional
    public void delete(Integer scriptId) throws SchedulerException {
       VQrtzScript vQrtzScript = vQrtzJobScriptMapper.selectById(scriptId);
       String scriptType = vQrtzScript.getScriptType();
       String jobClassName = vQrtzScript.getClassName();
       String beanName = getBeanName(jobClassName);
       boolean bean_exists = applicationContext.containsBean(beanName);
       if(bean_exists){
           //job删除->jobDetail删除->Trigger删除
           if (scriptType.equals(ScriptTypeEnums.JOB.getType())){
               //获得所有的jobKeys
               Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyJobGroup());
               for (JobKey jobKey : jobKeys) {
                   JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                   // 删除指定job的jobDetail
                   if (jobDetail.getJobClass().getSimpleName().equals(jobClassName)) {
                       scheduler.deleteJob(jobKey);
                   }
               }
           }
           System.out.println("before"+Arrays.toString(classLoader.getLoadedClasses()));
           classLoader.KillClass(getFullClassNameByBeanName(beanName));
           System.out.println("after"+Arrays.toString(classLoader.getLoadedClasses()));

           dynamicBeanRegistrar.unregisterBean(beanName);

       }
        vQrtzJobScriptMapper.deleteById(scriptId);
        vScriptTagScervice.deleteScriptTag(jobClassName);
    }

    @Override
//    @Transactional
    public void update(Integer id, String script) throws SchedulerException {
        VQrtzScript vQrtzScript= vQrtzJobScriptMapper.selectById(id);
        var className = vQrtzScript.getClassName();

        String newScriptClassName = extractClassName(script);
        if (!className.equals(newScriptClassName)){
            throw new RuntimeException("脚本类名必须相同");
        }
        UpdateWrapper<VQrtzScript> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("ID",id).set("SCRIPT",script);
        vQrtzJobScriptMapper.update(updateWrapper);

        Class<?> clazz = classLoader.parseClass(script);

        boolean bean_exists = applicationContext.containsBean(getBeanName(className));
        if(bean_exists){
            dynamicBeanRegistrar.unregisterBean(getBeanName(className));
        }
        dynamicBeanRegistrar.registerBean(newScriptClassName,clazz);
        var scriptType = vQrtzScript.getScriptType();
        if (!Objects.equals(scriptType, "job")){
            if ("job_detail".equals(scriptType)){
                List<VScriptTag> vScriptTags = vScriptTagScervice.getScriptTag(null, null, className, "job_detail");
                for (VScriptTag vScriptTag : vScriptTags) {
                    jobServiceImp.updateJob(null, vScriptTag.getNameTag(), vScriptTag.getGroupTag(), "true", className, null);
                }

            }else {
                List<VScriptTag> vScriptTags = vScriptTagScervice.getScriptTag(null, null, className, vQrtzScript.getScriptType());
                JobInfo jobInfo = new JobInfo();
                for (VScriptTag vScriptTag : vScriptTags) {
                    jobInfo.setTriggername(vScriptTag.getNameTag());
                    jobInfo.setTriggergroup(vScriptTag.getGroupTag());
                    jobInfo.setTrigger(className);
                    jobInfo.setIsCustomTrigger("true");
                    jobServiceImp.jobTOtri(jobInfo);
                }
            }
        }

    }




    @Override
    public List<VQrtzScript> selectAll(SelectScriptInfoConditionDTO selectScriptInfoConditionDTO) {
        String type= selectScriptInfoConditionDTO.getType();
        String className = selectScriptInfoConditionDTO.getClassName();
        Boolean beanState=selectScriptInfoConditionDTO.getBeanState();


        LambdaQueryWrapper<VQrtzScript> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(type),VQrtzScript::getScriptType,type)
                .like(StrUtil.isNotBlank(className),VQrtzScript::getClassName,className)
                .eq(Objects.nonNull(beanState),VQrtzScript::getBeanState,beanState);

       return vQrtzJobScriptMapper.selectList(queryWrapper);
    }

    @Override
    public VQrtzScript select(Integer id) {
        return vQrtzJobScriptMapper.selectById(id);
    }

    @Override
    public List<String> selectFilterCriteria() {
        return ScriptTypeEnums.getAllTypes();
    }


    private String getBeanName(String className){
        return className;
//                .substring(0, 1).toLowerCase()
//                + className.substring(1); // 类名首字母小写作为 Bean 名称
    }

    private String extractClassName(String script) {
        CompilationUnit unit = new CompilationUnit();
        unit.addSource("script.groovy", script);
        // 仅解析语法树，不生成字节码
        unit.compile(Phases.CONVERSION);

        // 遍历 AST 查找类定义
        List<ClassNode> classes = unit.getAST().getClasses();
        if (classes.isEmpty()) {
            throw new RuntimeException("脚本中没有定义类");
        }
        return classes.get(0).getNameWithoutPackage();
    }

    private String getFullClassNameByBeanName(String beanName){
        Object o= applicationContext.getBean(beanName);
        return o.getClass().getName();
    }
}
