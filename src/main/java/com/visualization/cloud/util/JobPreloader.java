package com.visualization.cloud.util;/**
 * @Auter 李孝在
 * @Date 2025/1/24
 */

import com.visualization.cloud.scriptBuilder.DAO.entity.VQrtzScript;
import com.visualization.cloud.scriptBuilder.DAO.mapper.VQrtzScriptMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import groovy.lang.GroovyClassLoader;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @projectName: visualization
 * @package: com.visualization.cloud.util
 * @className: asd
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/24 23:01
 * @version: 1.0
 */
@Slf4j
@DependsOn("groovyClassLoader")
@Component
//初始化加载数据库中的脚本为Bean
public class JobPreloader {

    private final GroovyClassLoader groovyClassLoader;
    private final DynamicBeanOperate dynamicBeanRegistrar;
    private final VQrtzScriptMapper vQrtzJobScriptMapper;

    public JobPreloader(
            GroovyClassLoader groovyClassLoader,
            DynamicBeanOperate dynamicBeanRegistrar,
            VQrtzScriptMapper vQrtzJobScriptMapper) {
        this.groovyClassLoader = groovyClassLoader;
        this.dynamicBeanRegistrar = dynamicBeanRegistrar;
        this.vQrtzJobScriptMapper = vQrtzJobScriptMapper;
    }


    public void preloadDynamicJobs() {
        QueryWrapper<VQrtzScript> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("BEAN_STATE",true);
        List<VQrtzScript> scripts = vQrtzJobScriptMapper.selectList(queryWrapper);
        //写入默认的JobDetail
        VQrtzScript vQrtzScript = new VQrtzScript();
        vQrtzScript.setBeanState(true);
        vQrtzScript.setScript("package com.visualization.cloud.quartz.showpo;/**\n" +
                " * @Auter zzh\n" +
                " * @Date 2025/2/7\n" +
                " */\n" +
                "\n" +
                "import com.visualization.cloud.quartz.po.CreatJobDetail;\n" +
                "import org.quartz.JobBuilder;\n" +
                "import org.quartz.JobDetail;\n" +
                "import org.quartz.JobKey;\n" +
                "import org.springframework.context.ApplicationContext;\n" +
                "import com.visualization.cloud.quartz.po.QuartzJobBeanAbstract;\n" +
                "\n" +
                "/**\n" +
                " * @projectName: quartz_visualization\n" +
                " * @package: com.visualization.cloud.quartz.po\n" +
                " * @className: DefaultJobDetail\n" +
                " * @author: Eric\n" +
                " * @description: TODO\n" +
                " * @date: 2025/2/7 21:12\n" +
                " * @version: 1.0\n" +
                " */\n" +
                "public class DefaultJobDetail extends CreatJobDetail {\n" +
                "    public DefaultJobDetail(ApplicationContext applicationContext) {\n" +
                "        super(applicationContext);\n" +
                "    }\n" +
                "\n" +
                "    public JobDetail createdetail(String Jobclassname, String Jobname, String Jobgroup, String Description) throws ClassNotFoundException {\n" +
                "           System.out.println(Jobclassname) ;\n"+

                "        Class<? extends QuartzJobBeanAbstract> jobClass = this.applicationContext.getType(Jobclassname).asSubclass(QuartzJobBeanAbstract.class);\n" +
                "           System.out.println(jobClass) ;\n"+
                "        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(Jobname, Jobgroup).withDescription(Description).storeDurably().build();\n" +
                "        return jobDetail;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "}"
        );
        vQrtzScript.setScriptType("job_detial");
        vQrtzScript.setClassName("DefaultJobDetail");

        scripts.add(vQrtzScript);



//        System.out.println("scripts:"+scripts);
        scripts.forEach(script -> {
            try {
                Class<?> clazz = groovyClassLoader.parseClass(script.getScript());
                String beanName = clazz.getSimpleName();
//                        .substring(0, 1).toLowerCase()
//                        + clazz.getSimpleName().substring(1); // 类名首字母小写作为 Bean 名称
                System.out.println("beanName:"+beanName);
                dynamicBeanRegistrar.registerBean(beanName, clazz);
            } catch (Exception e) {
                log.error("e: ", e);
            }
        });
    }
    @PostConstruct
    public void init() {
        preloadDynamicJobs();
    }
}
