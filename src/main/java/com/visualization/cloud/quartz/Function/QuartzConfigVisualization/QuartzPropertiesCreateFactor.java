package com.visualization.cloud.quartz.Function.QuartzConfigVisualization;/**
 * @Auter zzh
 * @Date 2025/2/6
 */
import java.util.Locale;
import java.util.Properties;

import static com.visualization.cloud.quartz.config.SpringContextHolder.getBean;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.quartz.Function.QuartzConfigVisualization
 * @className: QuartzPropertiesCreateFactor
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/6 17:01
 * @version: 1.0
 */

public class QuartzPropertiesCreateFactor {

    private static  AllQuartzProperties.SchedulerProperties schedulerProperties=getBean(AllQuartzProperties.SchedulerProperties.class);
    private static  AllQuartzProperties.ThreadPoolProperties threadPoolProperties=getBean(AllQuartzProperties.ThreadPoolProperties.class);
    private static  AllQuartzProperties.JobStoreProperties jobStoreProperties=getBean(AllQuartzProperties.JobStoreProperties.class);
    private static  AllQuartzProperties.DataSourceProperties dataSourceProperties=getBean(AllQuartzProperties.DataSourceProperties.class);
    private static  AllQuartzProperties.PluginProperties pluginProperties=getBean(AllQuartzProperties.PluginProperties.class);
    private static  AllQuartzProperties.TriggerListener triggerListener=getBean(AllQuartzProperties.TriggerListener.class);
    private static  AllQuartzProperties.JobListener jobListener=getBean(AllQuartzProperties.JobListener.class);


    public static Properties createQuartzPropertiesCreate(){
        Properties properties = new Properties();
        //调度器属性

        //如果getInstanceName里有大写,则全改为小写

        if (schedulerProperties.getInstanceName() != null) {
            properties.setProperty("org.quartz.scheduler.instanceName", schedulerProperties.getInstanceName().toLowerCase(Locale.ROOT));
        }
        if (schedulerProperties.getInstanceId() != null) {
            properties.setProperty("org.quartz.scheduler.instanceId", schedulerProperties.getInstanceId());
        }
        if (schedulerProperties.getInstanceIdGeneratorClass() != null) {
            properties.setProperty("org.quartz.scheduler.instanceIdGenerator.class", schedulerProperties.getInstanceIdGeneratorClass());
        }
        if (schedulerProperties.getThreadName() != null) {
            properties.setProperty("org.quartz.scheduler.threadName", schedulerProperties.getThreadName());
        }
        if (schedulerProperties.getMakeSchedulerThreadDaemon() != null) {
            properties.setProperty("org.quartz.scheduler.makeSchedulerThreadDaemon", String.valueOf(schedulerProperties.getMakeSchedulerThreadDaemon()));
        }
        if (schedulerProperties.getThreadsInheritContextClassLoaderOfInitializer() != null) {
            properties.setProperty("org.quartz.scheduler.threadsInheritContextClassLoaderOfInitializer", String.valueOf(schedulerProperties.getThreadsInheritContextClassLoaderOfInitializer()));
        }
        if (schedulerProperties.getIdleWaitTime() != null) {
            properties.setProperty("org.quartz.scheduler.idleWaitTime", String.valueOf(schedulerProperties.getIdleWaitTime()));
        }
        if (schedulerProperties.getDbFailureRetryInterval() != null) {
            properties.setProperty("org.quartz.scheduler.dbFailureRetryInterval", String.valueOf(schedulerProperties.getDbFailureRetryInterval()));
        }
        if (schedulerProperties.getClassLoadHelperClass() != null) {
            properties.setProperty("org.quartz.scheduler.classLoadHelper.class", schedulerProperties.getClassLoadHelperClass());
        }
        if (schedulerProperties.getJobFactoryClass() != null) {
            properties.setProperty("org.quartz.scheduler.jobFactory.class", schedulerProperties.getJobFactoryClass());
        }
        if (schedulerProperties.getUserTransactionURL() != null) {
            properties.setProperty("org.quartz.scheduler.userTransactionURL", schedulerProperties.getUserTransactionURL());
        }
        if (schedulerProperties.getWrapJobExecutionInUserTransaction() != null) {
            properties.setProperty("org.quartz.scheduler.wrapJobExecutionInUserTransaction", String.valueOf(schedulerProperties.getWrapJobExecutionInUserTransaction()));
        }
        if (schedulerProperties.getSkipUpdateCheck() != null) {
            properties.setProperty("org.quartz.scheduler.skipUpdateCheck", String.valueOf(schedulerProperties.getSkipUpdateCheck()));
        }
        if (schedulerProperties.getBatchTriggerAcquisitionMaxCount() != null) {
            properties.setProperty("org.quartz.scheduler.batchTriggerAcquisitionMaxCount", String.valueOf(schedulerProperties.getBatchTriggerAcquisitionMaxCount()));
        }
        if (schedulerProperties.getBatchTriggerAcquisitionFireAheadTimeWindow() != null) {
            properties.setProperty("org.quartz.scheduler.batchTriggerAcquisitionFireAheadTimeWindow", String.valueOf(schedulerProperties.getBatchTriggerAcquisitionFireAheadTimeWindow()));
        }
        if (schedulerProperties.getRmiExport() != null) {
            properties.setProperty("org.quartz.scheduler.rmiExport", String.valueOf(schedulerProperties.getRmiExport()));
        }
        if (schedulerProperties.getRmiProxy() != null) {
            properties.setProperty("org.quartz.scheduler.rmiProxy", String.valueOf(schedulerProperties.getRmiProxy()));
        }
        //线程池属性
        if (threadPoolProperties.getThreadPoolClass() != null) {
            properties.setProperty("org.quartz.threadPool.class", threadPoolProperties.getThreadPoolClass());
        }
        if (threadPoolProperties.getThreadCount() != null) {
            properties.setProperty("org.quartz.threadPool.threadCount", String.valueOf(threadPoolProperties.getThreadCount()));
        }
        if (threadPoolProperties.getThreadPriority() != null) {
            properties.setProperty("org.quartz.threadPool.threadPriority", String.valueOf(threadPoolProperties.getThreadPriority()));
        }
        if (threadPoolProperties.getMakeThreadsDaemons() != null) {
            properties.setProperty("org.quartz.threadPool.makeThreadsDaemons", String.valueOf(threadPoolProperties.getMakeThreadsDaemons()));
        }
        if (threadPoolProperties.getThreadsInheritContextClassLoaderOfInitializingThread() != null) {
            properties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", String.valueOf(threadPoolProperties.getThreadsInheritContextClassLoaderOfInitializingThread()));
        }
        if (threadPoolProperties.getThreadNamePrefix() != null) {
            properties.setProperty("org.quartz.threadPool.threadsinheritgroupofinitializingthread", String.valueOf(threadPoolProperties.getThreadsInheritGroupOfInitializingThread()));
        }

        if (threadPoolProperties.getThreadNamePrefix() != null) {
            properties.setProperty("org.quartz.threadPool.threadNamePrefix", threadPoolProperties.getThreadNamePrefix());
        }
        //JobStore属性
        if (jobStoreProperties.getJobStoreClass() != null) {
            properties.setProperty("org.quartz.jobStore.class", jobStoreProperties.getJobStoreClass());
        }
        if (jobStoreProperties.getMisfireThreshold() != null) {
            properties.setProperty("org.quartz.jobStore.misfireThreshold", String.valueOf(jobStoreProperties.getMisfireThreshold()));
        }
        if (jobStoreProperties.getDriverDelegateClass() != null) {
            properties.setProperty("org.quartz.jobStore.driverDelegateClass", jobStoreProperties.getDriverDelegateClass());
        }
        if (jobStoreProperties.getDataSource() != null) {
            properties.setProperty("org.quartz.jobStore.dataSource", jobStoreProperties.getDataSource());
        }
        if (jobStoreProperties.getTablePrefix() != null) {
            properties.setProperty("org.quartz.jobStore.tablePrefix", jobStoreProperties.getTablePrefix());
        }
        if (jobStoreProperties.getUseProperties() != null) {
            properties.setProperty("org.quartz.jobStore.useProperties", String.valueOf(jobStoreProperties.getUseProperties()));
        }
        if (jobStoreProperties.getIsClustered() != null) {
            properties.setProperty("org.quartz.jobStore.isClustered", String.valueOf(jobStoreProperties.getIsClustered()));
        }
        if (jobStoreProperties.getClusterCheckinInterval() != null) {
            properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", String.valueOf(jobStoreProperties.getClusterCheckinInterval()));
        }
        if (jobStoreProperties.getMaxMisfiresToHandleAtATime() != null) {
            System.out.println("maxMisfiresToHandleAtATime:"+jobStoreProperties.getMaxMisfiresToHandleAtATime());
            properties.setProperty("org.quartz.jobStore.maxMisfiresToHandleAtATime", String.valueOf(jobStoreProperties.getMaxMisfiresToHandleAtATime()));
        }
        if (jobStoreProperties.getDontSetAutoCommitFalse() != null) {
            properties.setProperty("org.quartz.jobStore.dontSetAutoCommitFalse", String.valueOf(jobStoreProperties.getDontSetAutoCommitFalse()));
        }
        if (jobStoreProperties.getSelectWithLockSQL()!=null){
            properties.setProperty("org.quartz.jobStore.selectWithLockSQL",jobStoreProperties.getSelectWithLockSQL());
        }
        if (jobStoreProperties.getTxIsolationLevelSerializable()!=null){
            properties.setProperty("org.quartz.jobStore.txIsolationLevelSerializable", String.valueOf(jobStoreProperties.getTxIsolationLevelSerializable()));
        }
        if (jobStoreProperties.getAcquireTriggersWithinLock()!=null){
            properties.setProperty("org.quartz.jobStore.acquireTriggersWithinLock", String.valueOf(jobStoreProperties.getAcquireTriggersWithinLock()));
        }
        if (jobStoreProperties.getLockHandlerClass()!=null){
            properties.setProperty("org.quartz.jobStore.lockHandlerClass",jobStoreProperties.getLockHandlerClass());
        }
        //数据源属性
        if (dataSourceProperties.getDriver() != null) {
            properties.setProperty("org.quartz.dataSource.quartz.driver", dataSourceProperties.getDriver());
        }
        if (dataSourceProperties.getUrl() != null) {
            properties.setProperty("org.quartz.dataSource.quartz.URL", dataSourceProperties.getUrl());
        }
        if (dataSourceProperties.getUser() != null) {
            properties.setProperty("org.quartz.dataSource.quartz.user", dataSourceProperties.getUser());
        }
        if (dataSourceProperties.getPassword() != null) {
            properties.setProperty("org.quartz.dataSource.quartz.password", dataSourceProperties.getPassword());
        }
        if (dataSourceProperties.getMaxConnections() != null) {
            properties.setProperty("org.quartz.dataSource.quartz.maxConnections", String.valueOf(dataSourceProperties.getMaxConnections()));
        }
        if (dataSourceProperties.getValidationQuery() != null) {
            properties.setProperty("org.quartz.dataSource.quartz.validationQuery", dataSourceProperties.getValidationQuery());
        }
        if (dataSourceProperties.getJndiURL() != null) {
            properties.setProperty("org.quartz.dataSource.quartz.jndiURL", dataSourceProperties.getJndiURL());
        }
        if (dataSourceProperties.getConnectionProviderClass() != null) {
            properties.setProperty("org.quartz.dataSource.quartz.connectionProvider.class", dataSourceProperties.getConnectionProviderClass());
        }
        //插件属性
        if (pluginProperties.getTriggerHistoryClass() != null) {
            properties.setProperty("org.quartz.plugin.triggerHistory.class", pluginProperties.getTriggerHistoryClass());
        }
        if (pluginProperties.getTriggerFiredMessage() != null) {
            properties.setProperty("org.quartz.plugin.triggHistory.triggerFiredMessage", pluginProperties.getTriggerFiredMessage());
        }
        if (pluginProperties.getTriggerCompleteMessage() != null) {
            properties.setProperty("org.quartz.plugin.triggerHistory.triggerCompleteMessage", pluginProperties.getTriggerCompleteMessage());
        }
        if(pluginProperties.getJobInitializerClass()!=null){
            properties.setProperty("org.quartz.plugin.jobInitializer.class",pluginProperties.getJobInitializerClass());
        }
        if(pluginProperties.getJobInitializerFileNames()!=null){
            properties.setProperty("org.quartz.plugin.jobInitializer.fileNames",pluginProperties.getJobInitializerFileNames());
        }
        if(pluginProperties.getJobInitializerFailOnFileNotFound()!=null){
            properties.setProperty("org.quartz.plugin.jobInitializer.failOnFileNotFound",pluginProperties.getJobInitializerFailOnFileNotFound());
        }
        if(pluginProperties.getJobInitializerOverWriteExistingJobs()!=null){
            properties.setProperty("org.quartz.plugin.jobInitializer.overWriteExistingJobs",pluginProperties.getJobInitializerOverWriteExistingJobs());
        }
        if(pluginProperties.getJobInitializerScanInterval()!=null){
            properties.setProperty("org.quartz.plugin.jobInitializer.scanInterval", String.valueOf(pluginProperties.getJobInitializerScanInterval()));
        }
        if(pluginProperties.getJobInitializerWrapInUserTransaction()!=null){
            properties.setProperty("org.quartz.plugin.jobInitializer.wrapInUserTransaction", String.valueOf(pluginProperties.getJobInitializerWrapInUserTransaction()));
        }
        //监听器属性
        if(triggerListener.getTriggerListenerClass()!=null){
            properties.setProperty("org.quartz.triggerListener.triggerListenerClass",triggerListener.getTriggerListenerClass());
        }
        if(jobListener.getJobListenerClass()!=null){
            properties.setProperty("org.quartz.jobListener.jobListenerClass",jobListener.getJobListenerClass());
        }
        System.out.println("properties:"+properties);
        return properties;
    }

}
