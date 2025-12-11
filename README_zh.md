**其他语言版本: [English](README.md), [中文](README_zh.md).**

# Quartiz Visualization 项目使用手册

---

##  目录

1. [项目概述](#1-项目概述)  
2. [环境准备](#2-环境准备)  
3. [快速入门](#3-快速入门)  
4. [功能详解](#4-功能详解)
6. [高级功能](#5-高级功能)  
7. [常见问题（FAQ）](#6-常见问题faq)  
8. [注意事项](#7-注意事项)  

## 1. 项目概述
本项目是基于传统定时框架quartz的升级解决方案，提供**可视化任务编排**、**动态化脚本管理**和**实时监控**三大核心功能，支持以下场景：  
- 动态调整定时任务逻辑（无需重启）  
- 多触发器或任务灵活挂载与参数修改  
- 任务执行状态实时监控与历史记录追溯
同时为延迟问题,提出全新的解决方案
版本已经适配mybatis-flex


**技术栈**：  
- 前端：Vue.js  
- 动态脚本：Groovy  
- 监控：SSE（实时推送）、MySQL（数据存储）  
- 后端：Spring Boot  3.2.8 (如果是3.4版本存在适配性问题!)
- DAO: mybatis-plus

## 2. 环境准备
### 2.1 运行环境
- JDK 17 
- MySQL 8.0.11
- Maven 3.9.5（后端依赖管理）
- springboot 

### 2.2 初始化配置
#### Step 1：添加项目依赖
在 `pom.xml` 中引入核心库：
```xml
<dependency>
    <groupId>io.github.2757559039</groupId>
    <artifactId>quartz_visualization</artifactId>
    <version>0.1.2</version>
</dependency>
```

#### Step 2：配置主启动类
可能会产生冲突的配置文件名称

CascadingClassLoadHelper
ClassScanner
GroovyClassLoaderKilIClassEnhance
GroovyConfig
SchedulerConfig
ScriptBuilderMyBatisPlusConfig
SpringContextHolder
SSECacheConfig

在 Spring Boot 启动类添加包扫描注解：
```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.visualization.cloud","你自己的包的根路径地址,例如org.example"}) // 关键注解
@Import(visualization.class)// 关键注解
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

#### Step 3：设置 application.yml
```yml
server :
  port : 8002
spring:
  application:
    name: test
  jackson:
    # json 序列化排除值为 null 的属性
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
      instance-name: cluster_scheduler #建议每一个服务拥有不同的名称,来隔绝定时任务框架的影响
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
#### Step 4：数据库初始化
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
#### Step 5：设置 配置类(mybatis-plus使用者须看)
为了保证兼容性,qv底层使用了独立的一套mybatis-plus,这会导致主项目的mybatis-plus的自动配置,yml配置失效,需要使用配置类才可以解决,这里给出可运行的参考模板
其他更多功能请自行添加
```java
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.logging.stdout.StdOutImpl; // 1. 导入日志实现类
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
// 扫描主项目的 Mapper
@MapperScan(
        basePackages = "org.example.DAL.mapper",
        sqlSessionFactoryRef = "mainSqlSessionFactory"
)
public class MyBatisPlusConfig {

    @Bean
    @Primary
    public MybatisPlusInterceptor mainInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean("mainSqlSessionFactory")
    @Primary
    public SqlSessionFactory mainSqlSessionFactory(DataSource dataSource,
                                                   MybatisPlusInterceptor interceptor) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPlugins(interceptor);

        // 1. 基础配置 & SQL 打印
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        // 【关键】开启控制台 SQL 打印
        configuration.setLogImpl(StdOutImpl.class);
        bean.setConfiguration(configuration);

        // 2. 全局配置 (解决 YML 配置失效问题)
        // 因为手动接管了 Factory，YML 中的 id-type: auto 会失效，必须在这里重新配置
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();

        // 如果你的数据库主键是自增的，请保留这行；如果是雪花算法，请注释掉
        dbConfig.setIdType(IdType.AUTO);

        globalConfig.setDbConfig(dbConfig);
        bean.setGlobalConfig(globalConfig);

        return bean.getObject();
    }
}
```


#### Step 6：前端构建
[使用配套前端]访问前端仓库,拉取前端组件:https://github.com/2757559039/quartz_visualization_vue 
并选择其端口号,即可完成配置,多个微服务可以共用一个前端框架

## 3. 自定义类快速入门
**目标**：创建四大自定义类

### 创建自定义job类
创建job逻辑类必须要继承QuartzJobBeanAbstract并实现executeInternal方法
```java
public class testjob extends QuartzJobBeanAbstract {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("testjob");
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateformat.format(System.currentTimeMillis());
        
        System.out.println("执行时间："dateStr  );
    }
}

```
### 创建自定义trigger类
必须要继承TriggerAbstract,并且如果是创建触发器,则重写对应类别的create前缀方法
```java
public class triiger extends TriggerAbstract {

@Override
    public CronTrigger CreateCronTrigger(JobDetail jobDetail) throws ClassNotFoundException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        // 将当前时间加1秒
        LocalDateTime newDateTime = currentDateTime.plusSeconds(1);
        // 定义格式化模式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 将LocalDateTime格式化为字符串
        String formattedDate = newDateTime.format(formatter);
    System.out.println("执行了子类的");
    CronTrigger a=
            TriggerBuilder.newTrigger()
            .withIdentity("1234","1234")
            .withSchedule(CronScheduleBuilder
                    .cronSchedule("0/1 * * * * ?")
            )
            .startAt(Date.valueOf(formattedDate))
            .startNow()
            .forJob(jobDetail)
            // 设置结束时间,默认无穷大
            .endAt(Date.valueOf("2099-12-31"))
            //设置优先级
            .withPriority(5)
            .build();
    System.out.println(a);
        return a;

    }


}
```
### 创建自定义jobdetial类
继承CreatJobDetail并实现对应的方法,同时必须构造器注入applicationContext
```java
public class DefaultJobDetail111 extends CreatJobDetail {
    public DefaultJobDetail111(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public JobDetail createdetail(String Jobclassname, String Jobname, String Jobgroup, String Description) throws ClassNotFoundException {
        Class<? extends QuartzJobBeanAbstract> jobClass = this.applicationContext.getType(Jobclassname).asSubclass(QuartzJobBeanAbstract.class);
        System.out.println(jobClass);
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(Jobname, Jobgroup).withDescription(Description).storeDurably().build();
        return jobDetail;
    }


}
```
### 创建自定义更新触发器类
必须要继承TriggerAbstract,并且如果是更新触发器,则重写对应类别的update前缀方法
```java
public class update_trigger111 extends TriggerAbstract {
    @Override
    public CronTrigger updateCrontrigger (TriggerBuilder triggerBuilder){
        CronTrigger build =(CronTrigger)  triggerBuilder.withSchedule(
                        CronScheduleBuilder
                                .cronSchedule("2/10 * * * * ?")
                                // 设置时区,默认上海
                                .inTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                )
                .startAt(Date.valueOf("2024-12-13"))
                // 设置结束时间,默认无穷大
                .endAt(Date.valueOf("9999-12-31"))
                //设置优先级
                .withPriority(Integer.parseInt("5"))
                .build();

        return build;

    }
}

```


   
## 4. 功能详解
### 4.1 可视化模块
#### 4.1.1 增加策略
- **新增任务**  
  支持两种模式：  
  - **默认模式**：直接填写类名、分组、触发器参数  
  - **自定义模式**：需先在 **脚本管理** 中上传 Groovy 脚本并注册为 Bean  
  
- **挂载触发器**  
  同样支持两种模式：  
  和新增任务类似,但是是通过选择任务分组,任务名的方法来设置任务名,且无法设置任务的详细属性,只能设置触发器
  - **默认模式**：直接填写类名、分组、触发器参数  
  - **自定义模式**：需先在 **脚本管理** 中上传 Groovy 脚本并注册为 Bean  
  
- **添加空闲任务**  
添加一个没有触发器等待调度的任务
  同样支持两种模式：  
  - **默认模式**：直接填写类名、分组、
  - **自定义模式**：需先在 **脚本管理** 中上传 Groovy 脚本并注册为 Bean  

#### 4.1.2 删除策略
- **删除任务**  
删除一个任务连同这个任务所挂载的所有触发器也一并删除

- **删除触发器**  
删除一个触发器

- **删除所有任务和所有触发器**  
慎用!!!

#### 4.1.3 修改策略
- **更改触发器参数**  
  同样支持两种模式：  
  - **默认模式**：重新填写需要更新的参数
  - **自定义模式**：需先在 **脚本管理** 中上传 Groovy 脚本并注册为 Bean,并且要注意,上传的类的类别必须是带有update前缀的,同时在编写脚本时候,要实现对应触发器的update方法,而不是普通的createtrigger方法

- **更换触发器**  
选择旧的触发器,并选择创建新的触发器的参数,这个操作可以更改触发器的类别

- **更改任务**  
可以更改任务的job类,和任务的描述


#### 4.1.4 查询策略
- **根据分组查询**  
直接选完事了

- **输入框模糊搜索**  
同时直接匹配四个值,任务名,任务组,任务类名,描述

#### 4.1.5 启动策略
- **恢复任务**  
暂停这个任务下的所有触发器

- **恢复触发器**  
单独恢复某个触发器

- **恢复所有任务和所有触发器**  
恢复所有任务和触发器,同时开启`立即执行一次`的功能

#### 4.1.6 停止策略
- **暂停任务**  
暂停这个任务下的所有触发器,但是触发器可以单独恢复

- **暂停触发器**  
单独暂停某个触发器

- **暂停所有任务和所有触发器**  
同时所有新增的触发器或任务都默认是暂停状态,且禁用`立即执行一次`的功能


### 4.2 动态化模块
#### 4.2.1 上传功能
**上传脚本**  
   - 进入 **脚本管理** > **上传脚本**，填写 Groovy 脚本信息  
   - 示例脚本：  
     ```groovy
     class CustomJob {
         void execute() {
             println "Dynamic Job Executed!"
         }
     }
     ```
	 
#### 4.2.2 脚本管理功能

**基本功能**  
   - **安装**：将脚本注册为 Spring Bean，并被groovy编译,供定时框架发现和使用  
   - **删除**：从数据库直接删除
   - **查看**: 查看脚本内容
   
   
**更新**
可以更新脚本内容,除了job在更改后,会直接影响定时框架,其他的更新,只是更新了任务和触发器的构造器,已经构造出的jobdetial和trigger不会有影响

#### 4.2.3 虚拟类管理功能
**卸载**
从 JVM 和 Bean 容器中移除虚拟类

### 4.3 监控平台模块
#### 4.3.1 监控模块的使用前提
首先需要在继承了QuartzJobBeanAbstract中使用setSseData
```java
protected  final void setSseData(String key, String data)
```
setSseData存在两个参数,一个是唯一标识key,一个是数据,可以传输任意你需要的数据,将其转成字符串形式即可
key的来源是持久化在数据库中的日志的key的所有种类列表,所以无法额外删除和创建空key,只能在job类中创建
#### 4.3.2 日志管理的使用
选择key,和日期既可以实现以下两个功能

- **实时监控**  
  - 查看任务日志（SSE实时推送） 
- **历史记录**  
  - 按日期筛选任务执行日志，支持清空过期数据（保留 30 天）  

## 5. 高级功能
### 5.1延迟队列
提供延迟任务的另一种解决方案
使用方法:
### 第一步
编写一个继承了DelayedJob的逻辑类,并实现自己的逻辑,在逻辑类中使用的一些需要额外注入的对象或类,传统的quartz的job类是不支持的,但在这里,你可以随意的使用,并在后面注入
请注意,注入的是Object parameter,你可以使用parameter,但必须要自行强制转换类型,且只能注入一个
```java
public class test extends  DelayedJob {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    }
}
```
### 第二步
在任何你需要的地方,使用createDelayedQueue静态方法,动态的构造一个定时任务,并通过killDelayedQueue来管理其生命周期的意外结束(例如在支付场景中,用户在倒计时结束前支付了订单),正常的执行结束会由框架自动清理
使用createDelayedQueue你需要传入一个你编写的逻辑类,一个你在逻辑类中所使用的其他类,一个唯一标识,一个你希望的定时任务的时长(毫秒级别)
```java
    public static void createDelayedQueue(DelayedJob job, Object parameter, String ID,Long time ) throws SchedulerException {
        jobService.createDelayedQueue(job, parameter,ID,time);
    }

    //编程式管理手动删除延时队列
    public static void killDelayedQueue(String ID) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(ID,"DelayedQueue");
        scheduler.deleteJob(jobKey);
        dynamicBeanOperate.unregisterBean(jobKey.getName());
    }
```
### PS
```java
    public static void createDelayedQueue(DelayedJob job, Object parameter, String iD, String triggerName) throws SchedulerException {

       Trigger trigger= SpringContextHolder.getBean(triggerName);
        jobService.createDelayedQueue(job, parameter,iD,trigger);
    }

    public static void createDelayedQueue(DelayedJob job, Object parameter, String ID,Long time ) throws SchedulerException {
        jobService.createDelayedQueue(job, parameter,ID,time);
    }

    public static void createDelayedQueue(DelayedJob job, Object parameter, String iD, LocalDateTime time) throws SchedulerException {
        jobService.createDelayedQueue(job, parameter,iD,time);
    }
```
目前阶段我们一共支持三种构造方法
1.(DelayedJob job, Object parameter, String iD, String triggerName) 传入job类,注入参数,唯一性id,现有的触发器名称
2.(DelayedJob job, Object parameter, String ID,Long time ) 同上参数,多久后开始执行(毫秒级别)
3.(DelayedJob job, Object parameter, String ID,Long time ) 同上参数,什么时间开始执行(精确到秒级别,支持年月日时分秒的格式)

### 5.2 编程式管理
参照这个方法,即可直接管理任务的创建,关键在于使用@Lazy注解
```java
@Component
public class test {

     @Resource
     @Lazy
     private IJobService iJobService;

      public void test() throws SchedulerException {
          System.out.println(iJobService);
          System.out.println(iJobService.getAllJobs());
      }
}
```

## 6. 常见问题（FAQ）
**Q1：任务状态为“已停止”，但触发器仍在执行**  
- 检查是否有其他触发器挂载到该任务，需单独停止触发器  

**Q2：Groovy 脚本上传后未生效**  
- 确认脚本已点击 **安装**，且类名与任务配置中的类名一致  

**Q3：监控数据未更新**  
- 检查 SSE 连接是否正常，或清空浏览器缓存后重试

**Q4：监控数据卡顿延迟**  
- 可以根据需要修改数据库中的事务里的删除时间,默认设置为一个月清除一次
- 同时尽量减少秒级别的监控需求

## 7. 注意事项
- **数据安全**：清空历史记录操作不可逆，建议定期备份数据库  
- **脚本规范**：Groovy 脚本需实现无参 `execute()` 方法,遵循Groovy规范,经过检验,无法如下的使用内联写法.在绝大部分情况下,Groovy是与java语法一致的
```
return new Object();
```
- **配置关键点**：  
  - application中关于quartz的配置,除名字外,不要随意更改
  - 调整 `thread-count` 根据服务器 CPU 核心数优化性能  
- **触发器冲突**：避免为同一任务设置过多短间隔触发器，防止资源竞争  

---

**获取支持**  
- 问题反馈：2757559039@qq.com
- 微信号: Loves_Philosophy
- 文档更新：访问项目 Git 仓库查看最新版本  

--- 

**版本：0.1.12 | 更新日期：2025-12-10**


---





