package com.atguigu.cloud.quartz.Function.DelayedQueue;/**
 * @Auter zzh
 * @Date 2025/2/4
 */

import com.atguigu.cloud.quartz.po.QuartzJobBeanAbstract;
import lombok.Data;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.scriptBuilder.DelayedQueue.test
 * @className: jobdad
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/4 12:30
 * @version: 1.0
 */
@Data
public abstract class DelayedJob<T> extends QuartzJobBeanAbstract {
    public T parameter= (T) "注入失败";

    /**
     * This implementation applies the passed-in job data map as bean property
     * values, and delegates to {@code executeInternal} afterwards.
     * @see #executeInternal
     */
    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
            MutablePropertyValues pvs = new MutablePropertyValues();
            pvs.addPropertyValues(context.getScheduler().getContext());
            pvs.addPropertyValues(context.getMergedJobDataMap());
            bw.setPropertyValues(pvs, true);
        }
        catch (SchedulerException ex) {
            throw new JobExecutionException(ex);
        }
        executeInternal(context);
    }

    /**
     * Execute the actual job. The job data map will already have been
     * applied as bean property values by execute. The contract is
     * exactly the same as for the standard Quartz execute method.
     * @see #execute
     */
    protected abstract void executeInternal(JobExecutionContext context) throws JobExecutionException;



}