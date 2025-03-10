package com.atguigu.cloud.quartz.po;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 *  @Auter zzh
 *  @Date 2024/12/12
 * </p>
 */

public abstract class CreatJobDetail {

    protected final ApplicationContext applicationContext ;

    // 构造器注入
    @Autowired
    public CreatJobDetail(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

    }

    public abstract JobDetail createdetail(String Jobclassname, String Jobname, String Jobgroup, String Description) throws ClassNotFoundException;
}

