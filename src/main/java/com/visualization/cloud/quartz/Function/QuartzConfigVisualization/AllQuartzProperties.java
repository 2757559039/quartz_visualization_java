package com.visualization.cloud.quartz.Function.QuartzConfigVisualization;/**
 * @Auter zzh
 * @Date 2025/2/6
 */

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.Function.QuartzConfigVisualization
 * @className: QuaertzProperties
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/6 17:47
 * @version: 1.0
 */
@Configuration
@RefreshScope
@EnableConfigurationProperties({
        AllQuartzProperties.SchedulerProperties.class,
        AllQuartzProperties.ThreadPoolProperties.class,
        AllQuartzProperties.JobStoreProperties.class,
        AllQuartzProperties.DataSourceProperties.class,
        AllQuartzProperties.PluginProperties.class,
        AllQuartzProperties.TriggerListener.class,
        AllQuartzProperties.JobListener.class
})
public class AllQuartzProperties {
    @Data
    @ConfigurationProperties(prefix = "org.quartz.scheduler")
    public static class SchedulerProperties {

        private String instanceName;
        private String instanceId;
        // 其他调度器属性（完整列表）
        private String instanceIdGeneratorClass;
        private String threadName;
        private Boolean makeSchedulerThreadDaemon;
        private Boolean threadsInheritContextClassLoaderOfInitializer;
        private Integer idleWaitTime;
        private Integer dbFailureRetryInterval;
        private String classLoadHelperClass;
        private String jobFactoryClass;
        private String userTransactionURL;
        private Boolean wrapJobExecutionInUserTransaction;
        private Boolean skipUpdateCheck;
        private Integer batchTriggerAcquisitionMaxCount;
        private Integer batchTriggerAcquisitionFireAheadTimeWindow;
        private Boolean rmiExport;
        private Boolean rmiProxy;
    }

    @Data
    @ConfigurationProperties(prefix = "org.quartz.threadpool")
    public static class ThreadPoolProperties {
        private String threadPoolClass;
        // 完整线程池属性
        private Integer threadCount;
        private Integer threadPriority;
        private Boolean makeThreadsDaemons;
        private Boolean threadsInheritContextClassLoaderOfInitializingThread;
        private Boolean threadsInheritGroupOfInitializingThread;
        private String threadNamePrefix;
    }

    @Data
    @ConfigurationProperties(prefix = "org.quartz.jobstore")
    public static class JobStoreProperties {

        private String jobStoreClass;
        // 完整JobStore属性
        private Integer misfireThreshold;
        private String driverDelegateClass;
        private String dataSource;
        private String tablePrefix;
        private Boolean useProperties;
        private Boolean isClustered;
        private Integer clusterCheckinInterval;
        private Integer maxMisfiresToHandleAtATime;
        private Boolean dontSetAutoCommitFalse;
        private String selectWithLockSQL;
        private Boolean txIsolationLevelSerializable;
        private Boolean acquireTriggersWithinLock;
        private String lockHandlerClass;

    }

    @Data
    @ConfigurationProperties(prefix = "org.quartz.datasource")
    public static class DataSourceProperties {

        private String driver;

        // 完整数据源属性
        private String url;
        private String user;
        private String password;
        private Integer maxConnections;
        private String validationQuery;
        private String jndiURL;
        private String connectionProviderClass;

    }

    @Data
    @ConfigurationProperties(prefix = "org.quartz.plugin")
    public static class PluginProperties {

        private String triggerHistoryClass;
        private String triggerFiredMessage;
        private String triggerCompleteMessage;

        private String jobInitializerClass;
        private String jobInitializerFileNames;
        private String jobInitializerFailOnFileNotFound;
        private String jobInitializerOverWriteExistingJobs;

        private Integer jobInitializerScanInterval;
        private Boolean jobInitializerWrapInUserTransaction;
    }
    @Data
    @ConfigurationProperties(prefix = "org.quartz.triggerlistener")
    public static class TriggerListener {
        private String triggerListenerClass;
    }
    @Data
    @ConfigurationProperties(prefix = "org.quartz.joblistener")
    public static class JobListener
    {
        private String jobListenerClass;
    }
}