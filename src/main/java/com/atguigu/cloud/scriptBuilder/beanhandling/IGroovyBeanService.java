package com.atguigu.cloud.scriptBuilder.beanhandling;



import com.atguigu.cloud.scriptBuilder.beanhandling.entity.DTO.SelectGroovyBeanInfoConditionDTO;
import com.atguigu.cloud.scriptBuilder.beanhandling.entity.GroovyBeanInfo;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @Auter 李孝在
 * @Date 2025/2/6
 */
public interface IGroovyBeanService {
    void loadGroovyBean(String className) throws ClassNotFoundException;

    GroovyBeanInfo selectGroovyBean(String className);

    List<GroovyBeanInfo> selectAllGroovyBean(SelectGroovyBeanInfoConditionDTO selectGroovyBeanInfoConditionDTO);

    void unloadGroovyBean(String className) throws SchedulerException;

    List<GroovyBeanInfo> selectAllLoadedGroovyBean(SelectGroovyBeanInfoConditionDTO selectGroovyBeanInfoConditionDTO);
    List<GroovyBeanInfo> selectAllUnLoadGroovyBean(SelectGroovyBeanInfoConditionDTO selectGroovyBeanInfoConditionDTO);
}
