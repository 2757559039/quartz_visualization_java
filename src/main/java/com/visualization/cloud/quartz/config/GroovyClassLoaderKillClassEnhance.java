package com.visualization.cloud.quartz.config;/**
 * @Auter 李孝在
 * @Date 2025/2/18
 */

import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.runtime.InvokerHelper;

public class GroovyClassLoaderKillClassEnhance extends GroovyClassLoader {

    public GroovyClassLoaderKillClassEnhance(ClassLoader systemclassLoader) {
        super(systemclassLoader);
    }
    public void KillClass (String name){
        Class<?> clearedClasses = this.classCache.remove(name);
        this.sourceCache.remove(name);
        InvokerHelper.removeClass(name.getClass());
    }
}
