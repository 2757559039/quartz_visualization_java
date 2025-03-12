package com.visualization.cloud.scriptBuilder.beanhandling.entity;/**
 * @Auter 李孝在
 * @Date 2025/2/6
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.quartz.scriptBuilder.beanhandling
 * @className: QuarztBeanInfo
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/6 05:57
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroovyBeanInfo {

    private Boolean exists;

    private String className;

    private String ScriptType;

}
