package com.visualization.cloud.quartz.po;/**
 * @Auter zzh
 * @Date 2025/1/29
 */

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.po
 * @className: a
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/29 20:17
 * @version: 1.0
 */
/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import com.visualization.cloud.sse.SSEUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;


import java.util.Objects;

/**
 * Simple implementation of the Quartz Job interface, applying the
 * passed-in JobDataMap and also the SchedulerContext as bean property
 * values. This is appropriate because a new Job instance will be created
 * for each execution. JobDataMap entries will override SchedulerContext
 * entries with the same keys.
 *
 * <p>For example, let's assume that the JobDataMap contains a key
 * "myParam" with value "5": The Job implementation can then expose
 * a bean property "myParam" of type int to receive such a value,
 * i.e. a method "setMyParam(int)". This will also work for complex
 * types like business objects etc.
 *
 * <p><b>Note that the preferred way to apply dependency injection
 * to Job instances is via a JobFactory:</b> that is, to specify
 * {@link SpringBeanJobFactory} as Quartz JobFactory (typically via
 * {@link SchedulerFactoryBean#setJobFactory SchedulerFactoryBean's "jobFactory" property}).
 * This allows to implement dependency-injected Quartz Jobs without
 * a dependency on Spring base classes.
 *
 * @author Juergen Hoeller
 * @since 18.02.2004
 * @see org.quartz.JobExecutionContext#getMergedJobDataMap()
 * @see org.quartz.Scheduler#getContext()
 * @see SchedulerFactoryBean#setSchedulerContextAsMap
 * @see SpringBeanJobFactory
 * @see SchedulerFactoryBean#setJobFactory
 */
@Slf4j
public abstract class QuartzJobBeanAbstract implements Job {
    // 使用ThreadLocal实现线程隔离（关键点）
    private static final ThreadLocal<SsePayload> currentPayload = new ThreadLocal<>();
//    private static final ThreadLocal<SsePayload> currentPayload = new ThreadLocal<>();

    /**
     * This implementation applies the passed-in job data map as bean property
     * values, and delegates to {@code executeInternal} afterwards.
     * @see #executeInternal
     */
    @Override
    public  void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
            MutablePropertyValues pvs = new MutablePropertyValues();
            pvs.addPropertyValues(context.getScheduler().getContext());
            pvs.addPropertyValues(context.getMergedJobDataMap());
            bw.setPropertyValues(pvs, true);

        }
        catch (SchedulerException ex) {
            throw new JobExecutionException(ex);
        }executeInternal(context);
        if (Objects.nonNull(currentPayload.get())) {
                SSEUtil.MessageSender.sendMessage(currentPayload.get().key,currentPayload.get().data);
        }
    }

    /**
     * Execute the actual job. The job data map will already have been
     * applied as bean property values by execute. The contract is
     * exactly the same as for the standard Quartz execute method.
     * @see #execute
     */
    protected abstract void executeInternal(JobExecutionContext context) throws JobExecutionException;

    protected  final void setSseData(String key, String data) {

        currentPayload.set(new SsePayload(key,data));
    }

    // 数据传输记录
    private record SsePayload(String key, String data) {

    }


}

