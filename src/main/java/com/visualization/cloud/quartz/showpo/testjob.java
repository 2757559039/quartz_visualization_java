package com.visualization.cloud.quartz.showpo;/**
 * @Auter zzh
 * @Date 2025/2/27
 */

import com.visualization.cloud.quartz.po.QuartzJobBeanAbstract;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.showpo
 * @className: testjob
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/27 18:42
 * @version: 1.0
 */
public class testjob extends QuartzJobBeanAbstract {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("testjob");
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateformat.format(System.currentTimeMillis());


        System.out.println("执行时间："  );
    }
}
