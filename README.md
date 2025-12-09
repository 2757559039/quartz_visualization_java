**Read this in other languages: [English](README.md), [中文](README_zh.md).**


# Quartiz Visualization Project User Manual

---

## Table of Contents

1. [Project Overview](#1-project-overview)  
2. [Environment Setup](#2-environment-setup)  
3. [Quick Start](#3-quick-start)  
4. [Feature Details](#4-feature-details)
5. [Advanced Features](#5-advanced-features)  
6. [FAQ](#6-faq)  
7. [Important Notes](#7-important-notes)  

## 1. Project Overview
This project is an upgraded solution based on the traditional Quartz scheduling framework, providing three core features:  
- **Visual Task Orchestration**  
- **Dynamic Script Management**  
- **Real-time Monitoring**  

Supported scenarios:  
- Dynamically adjust scheduled task logic (without restarting)  
- Flexible mounting/modification of multiple triggers or tasks  
- Real-time monitoring of task execution status and historical record tracing  
Introduces innovative solutions for latency issues.

**Technology Stack**:  
- Frontend: Vue.js  
- Dynamic Scripts: Groovy  
- Monitoring: SSE (real-time push), MySQL (data storage)  
- Backend: Spring Boot 3.2.8 (Note: Compatibility issues exist with 3.4 version!)

## 2. Environment Setup
### 2.1 Runtime Environment
- JDK 17 
- MySQL 8.0.11
- Maven 3.9.5 (Backend dependency management)
- Spring Boot 

### 2.2 Initial Configuration
#### Step 1: Add Dependencies
Add core library in `pom.xml`:
```xml
<dependency>
    <groupId>io.github.2757559039</groupId>
    <artifactId>quartz_visualization</artifactId>
    <version>0.0.12</version>
</dependency>
```

#### Step 2: Configure Main Class
Add package scan annotation in Spring Boot main class:
```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.visualization.cloud"}) // Critical annotation
@Import(visualization.class)// Critical annotation
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

#### Step 3: application.yml Configuration
```yml
server :
  port : 8002
spring:
  application:
    name: test
  jackson:
    default-property-inclusion: non_null
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/visualization?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true
    username: root
    password: 123456
mybatis-plus:
  global-config:
    db-config:
      id-type: auto
org:
  quartz:
    scheduler:
      instance-name: cluster_scheduler # Recommended unique name per service
      instance-id: AUTO
      rmi-export: false
      rmi-proxy: false
      wrapJobExecutionInUserTransaction: false
      batch-trigger-acquisition-max-count: 1
      class-load-helper-class: com.visualization.cloud.quartz.config.CascadingClassLoadHelper
      make-scheduler-thread-daemon: true

    threadpool:
      thread-pool-class: org.quartz.simpl.SimpleThreadPool
      thread-count: 10
      thread-priority: 5
      threads-inherit-context-class-loader-of-initializing-thread: true
      make-threads-daemons: true

    jobstore:
      isClustered: true
      useProperties: false
      tablePrefix: QRTZ_
      clusterCheckinInterval: 5000
      driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
      dataSource: quartz
      misfireThreshold: 60000
      maxMisfiresToHandleAtATime: 20
      dontSetAutoCommitFalse: false
      selectWithLockSQL: SELECT * FROM {0}LOCKS WHERE LOCK_NAME = ? FOR UPDATE
      txIsolationLevelSerializable: false
      job-store-class: org.quartz.impl.jdbcjobstore.JobStoreTX
    datasource:
      driver: com.mysql.cj.jdbc.Driver
      URL: jdbc:mysql://localhost:3306/visualization?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true
      user: root
      password: 123456
      maxConnections: 5
```

#### Step 4: Database Initialization
```sql
-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: visualization
-- --------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `QRTZ_BLOB_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名',
  `TRIGGER_NAME` varchar(200) NOT NULL COMMENT '     触发器名称',
  `TRIGGER_GROUP` varchar(200) NOT NULL COMMENT '触发器组',
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='以Blob 类型存储的触发器';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_CALENDARS`
--

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名称',
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='日历信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_CRON_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名称',
  `TRIGGER_NAME` varchar(200) NOT NULL COMMENT '     触发器名称',
  `TRIGGER_GROUP` varchar(200) NOT NULL COMMENT '触发器组',
  `CRON_EXPRESSION` varchar(120) NOT NULL COMMENT '时间表达式',
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL COMMENT '时区ID     nvarchar     80',
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='定时触发器';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_FIRED_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名称',
  `ENTRY_ID` varchar(95) NOT NULL COMMENT '组标识',
  `TRIGGER_NAME` varchar(200) NOT NULL COMMENT '触发器名称',
  `TRIGGER_GROUP` varchar(200) NOT NULL COMMENT '触发器组',
  `INSTANCE_NAME` varchar(200) NOT NULL COMMENT '当前实例的名称',
  `FIRED_TIME` bigint NOT NULL COMMENT '当前执行时间',
  `SCHED_TIME` bigint NOT NULL COMMENT '     计划时间',
  `PRIORITY` int NOT NULL COMMENT '权重',
  `STATE` varchar(16) NOT NULL COMMENT '状态',
  `JOB_NAME` varchar(200) DEFAULT NULL COMMENT '作业名称',
  `JOB_GROUP` varchar(200) DEFAULT NULL COMMENT '作业组',
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL COMMENT '是否并行',
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL COMMENT '是否要求唤醒',
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`) USING BTREE,
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE,
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='保存已经触发的触发器状态信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_JOB_DETAILS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名称',
  `JOB_NAME` varchar(200) NOT NULL COMMENT '作业名称',
  `JOB_GROUP` varchar(200) NOT NULL COMMENT '作业组',
  `DESCRIPTION` varchar(250) DEFAULT NULL COMMENT '描述',
  `JOB_CLASS_NAME` varchar(250) NOT NULL COMMENT '作业程序类名',
  `IS_DURABLE` varchar(1) NOT NULL COMMENT '是否持久',
  `IS_NONCONCURRENT` varchar(1) NOT NULL COMMENT '是否并行',
  `IS_UPDATE_DATA` varchar(1) NOT NULL COMMENT '是否更新',
  `REQUESTS_RECOVERY` varchar(1) NOT NULL COMMENT '是否要求唤醒',
  `JOB_DATA` blob COMMENT '作业名称',
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='job 详细信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_LOCKS`
--

DROP TABLE IF EXISTS `QRTZ_LOCKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名称',
  `LOCK_NAME` varchar(40) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='存储程序的悲观锁的信息(假如使用了悲观锁) ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名称',
  `TRIGGER_GROUP` varchar(200) NOT NULL COMMENT '触发器组',
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='存放暂停掉的触发器';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_SCHEDULER_STATE`
--

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名称',
  `INSTANCE_NAME` varchar(200) NOT NULL COMMENT '实例名称',
  `LAST_CHECKIN_TIME` bigint NOT NULL COMMENT '最后的检查时间',
  `CHECKIN_INTERVAL` bigint NOT NULL COMMENT '检查间隔',
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='调度器状态';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_SIMPLE_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名称',
  `TRIGGER_NAME` varchar(200) NOT NULL COMMENT '触发器名称',
  `TRIGGER_GROUP` varchar(200) NOT NULL COMMENT '触发器组',
  `REPEAT_COUNT` bigint NOT NULL COMMENT '重复次数',
  `REPEAT_INTERVAL` bigint NOT NULL COMMENT '重复间隔',
  `TIMES_TRIGGERED` bigint NOT NULL COMMENT '触发次数',
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='简单的触发器';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_SIMPROP_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名称',
  `TRIGGER_NAME` varchar(200) NOT NULL COMMENT '触发器名称',
  `TRIGGER_GROUP` varchar(200) NOT NULL COMMENT '触发器组',
  `STR_PROP_1` varchar(512) DEFAULT NULL COMMENT '计划名称',
  `STR_PROP_2` varchar(512) DEFAULT NULL COMMENT '计划名称',
  `STR_PROP_3` varchar(512) DEFAULT NULL COMMENT '计划名称',
  `INT_PROP_1` int DEFAULT NULL,
  `INT_PROP_2` int DEFAULT NULL,
  `LONG_PROP_1` bigint DEFAULT NULL,
  `LONG_PROP_2` bigint DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='存储CalendarIntervalTrigger和DailyTimeIntervalTrigger两种类型的触发器';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QRTZ_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL COMMENT '计划名称',
  `TRIGGER_NAME` varchar(200) NOT NULL COMMENT '触发器名称',
  `TRIGGER_GROUP` varchar(200) NOT NULL COMMENT '触发器组',
  `JOB_NAME` varchar(200) NOT NULL COMMENT '作业名称',
  `JOB_GROUP` varchar(200) NOT NULL COMMENT '作业组',
  `DESCRIPTION` varchar(250) DEFAULT NULL COMMENT '描述',
  `NEXT_FIRE_TIME` bigint DEFAULT NULL COMMENT '下次执行时间',
  `PREV_FIRE_TIME` bigint DEFAULT NULL COMMENT '前一次',
  `PRIORITY` int DEFAULT NULL COMMENT '优先权',
  `TRIGGER_STATE` varchar(16) NOT NULL COMMENT '触发器状态',
  `TRIGGER_TYPE` varchar(8) NOT NULL COMMENT '触发器类型',
  `START_TIME` bigint NOT NULL COMMENT '开始时间',
  `END_TIME` bigint DEFAULT NULL COMMENT '结束时间',
  `CALENDAR_NAME` varchar(200) DEFAULT NULL COMMENT '日历名称',
  `MISFIRE_INSTR` smallint DEFAULT NULL COMMENT '失败次数',
  `JOB_DATA` blob COMMENT '作业数据',
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE,
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='触发器';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `v_qrtz_script`
--

DROP TABLE IF EXISTS `v_qrtz_script`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `v_qrtz_script` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT '脚本ID',
  `CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '脚本类名',
  `SCRIPT` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '脚本',
  `SCRIPT_TYPE` varchar(200) DEFAULT NULL COMMENT '脚本类型',
  `BEAN_STATE` tinyint(1) DEFAULT NULL COMMENT 'bean状态',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `v_script_tag`
--

DROP TABLE IF EXISTS `v_script_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `v_script_tag` (
  `TAG_ID` int DEFAULT NULL,
  `script_id` varchar(255) DEFAULT NULL,
  `group_tag` varchar(255) DEFAULT NULL,
  `name_tag` varchar(255) DEFAULT NULL,
  `script_type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `v_sse_send_info`
--

DROP TABLE IF EXISTS `v_sse_send_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `v_sse_send_info` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'sse信息发送id',
  `send_key` varchar(255) DEFAULT NULL COMMENT 'sse信息发送key',
  `send_data` text COMMENT 'sse信息发送data',
  `send_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `v_sse_send_info_send_key_send_date_index` (`send_key`,`send_date`)
) ENGINE=InnoDB AUTO_INCREMENT=18248 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-10 20:18:44


```

#### Step 5: Frontend Setup
Use companion frontend repository:  
https://github.com/2757559039/quartz_visualization_vue  
Multiple microservices can share one frontend instance.

## 3. Quick Start: Custom Classes
### Custom Job Class
Must inherit `QuartzJobBeanAbstract`:
```java
public class TestJob extends QuartzJobBeanAbstract {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        System.out.println("TestJob executed at: " + 
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
```

### Custom Trigger Class
Must inherit `TriggerAbstract`:
```java
public class CustomTrigger extends TriggerAbstract {
    @Override
    public CronTrigger CreateCronTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
            .withIdentity("trigger1", "group1")
            .withSchedule(CronScheduleBuilder.cronSchedule("0/1 * * * * ?"))
            .forJob(jobDetail)
            .build();
    }
}
```

### Custom JobDetail Class
Must inherit `CreatJobDetail`:
```java
public class CustomJobDetail extends CreatJobDetail {
    public CustomJobDetail(ApplicationContext ctx) { super(ctx); }
    
    @Override
    public JobDetail createdetail(String className, String name, 
                                  String group, String desc) {
        // Implementation
    }
}
```

### Custom Trigger Update Class
Must inherit `TriggerAbstract`:
```java
public class UpdateTrigger extends TriggerAbstract {
    @Override
    public CronTrigger updateCronTrigger(TriggerBuilder builder) {
        // Update logic
    }
}
```

## 4. Feature Details
### 4.1 Visual Module
#### 4.1.1 Add Operations
- **New Task**: Supports default mode (direct parameters) or custom mode (Groovy scripts)
- **Mount Trigger**: Similar to task creation but selects existing jobs
- **Add Idle Task**: Create tasks without immediate triggers

#### 4.1.2 Delete Operations
- Delete Task (with all triggers)
- Delete Single Trigger
- Delete All (Dangerous!)

#### 4.1.3 Modify Operations
- Update Trigger Parameters
- Replace Trigger Type
- Modify Task Class

#### 4.1.4 Query Operations
- Group-based filtering
- Fuzzy search across multiple fields

### 4.2 Dynamic Script Module
#### 4.2.1 Upload Scripts
```groovy
class DynamicJob {
    void execute() {
        println "Dynamic execution at ${new Date()}"
    }
}
```
#### 4.2.2 Script Management Features

**Core Functions**  
   • **Install**: Register scripts as Spring Beans through Groovy compilation, making them discoverable by the scheduling framework  
   • **Delete**: Permanently remove scripts from the database  
   • **View**: Inspect script content  

**Updates**  
Script content can be modified. For job classes:  
• Changes immediately affect the scheduling framework  
• For other components (tasks/triggers):  
  • Updates only modify future constructor configurations  
  • Existing JobDetails and Triggers remain unaffected  

#### 4.2.3 Virtual Class Management  
**Uninstallation**  
Completely remove virtual classes from both the JVM and Spring Bean container

### 4.3 Monitoring Module
#### 4.3.1 Implementation
In job classes:
```java
protected final void setSseData(String key, String data) {
    // Send monitoring data
}
```

#### 4.3.2 Usage
By selecting a key and date, you can enable the following two functionalities
- Real-time monitoring via SSE
- Historical log filtering/cleaning

## 5. Advanced Features
### Latency Solution Implementation
1. Create delayed job class:
Create a logic class that extends DelayedJob and implement your custom logic. Unlike traditional Quartz job classes which have limitations on dependency injection, this approach allows you to freely use and inject objects or classes required by your logic.

Note: The injected object is passed as an Object type. While you can use this parameter, you must manually cast it to the correct type within your logic. Only one parameter object can be injected.
```java
public class PaymentTimeoutJob extends DelayedJob {
    @Override
    protected void executeInternal(JobExecutionContext context) {
        // Order timeout logic
    }
}
```

2. Usage:
Use the static method createDelayedQueue anywhere you need to dynamically construct a scheduled task. You can manage the unexpected termination of its lifecycle via killDelayedQueue (e.g., in a payment scenario where the user pays for the order before the countdown ends). Normal completion of the task will be automatically cleaned up by the framework.

To use createDelayedQueue, you need to pass in: the logic class you wrote, the object/parameter used in your logic class, a unique identifier (ID), and the desired duration for the task (in milliseconds).
```java
// Create delayed task
QuartzManager.createDelayedQueue(new PaymentTimeoutJob(), orderId, "ORDER_123", 900_000);

// Cancel task
QuartzManager.killDelayedQueue("ORDER_123");
```
3.PS (Supported Methods)
```java
public static void createDelayedQueue(DelayedJob job, Object parameter, String iD, String triggerName) throws SchedulerException {
        Trigger trigger = SpringContextHolder.getBean(triggerName);
        jobService.createDelayedQueue(job, parameter, iD, trigger);
    }

    public static void createDelayedQueue(DelayedJob job, Object parameter, String ID, Long time) throws SchedulerException {
        jobService.createDelayedQueue(job, parameter, ID, time);
    }

    public static void createDelayedQueue(DelayedJob job, Object parameter, String iD, LocalDateTime time) throws SchedulerException {
        jobService.createDelayedQueue(job, parameter, iD, time);
    }
```

Currently, we support three types of construction methods:

(DelayedJob job, Object parameter, String iD, String triggerName): Pass in the job class, the parameter to inject, a unique ID, and the name of an existing trigger.

(DelayedJob job, Object parameter, String ID, Long time): Same parameters as above; specifies the delay duration before execution (in milliseconds).

(DelayedJob job, Object parameter, String ID, LocalDateTime time): Same parameters as above; specifies the exact start time for execution (precise to the second, supports yyyy-MM-dd HH:mm:ss format).

## 6. FAQ
**Q1: Task shows "Stopped" but trigger still fires**  
Check for other active triggers attached to the task.

**Q2: Groovy script not taking effect**  
Confirm script installation and class name consistency.

**Q3: Monitoring data not updating**  
Verify SSE connection and clear browser cache.

## 7. Important Notes
• **Data Security**: Regular database backups recommended
• **Script Standards**: Groovy scripts must implement parameterless `execute()`
• **Configuration Keys**: Do not modify Quartz configuration except instance names
• **Trigger Conflicts**: Avoid excessive short-interval triggers for same job

---

**Support Contact**  
• Email: 2757559039@qq.com  
• WeChat: Loves_Philosophy  
• Docs: Project GitHub Repository  

---

**Version: 0.0.12 | Updated: 2025-03-13**
