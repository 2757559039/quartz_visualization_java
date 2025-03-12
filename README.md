
# Quartiz Visualization 项目使用手册

---

## **目录**
1. [项目概述](#1-项目概述)  
2. [环境准备](#2-环境准备)  
3. [快速入门](#3-快速入门)  
4. [功能详解](#4-功能详解)  
5. [高级功能](#5-高级功能)  
6. [常见问题（FAQ）](#6-常见问题faq)  
7. [注意事项](#7-注意事项)  

---

## **1. 项目概述**
本项目是基于传统定时框架的升级解决方案，提供**可视化任务编排**、**动态化脚本管理**和**实时监控**三大核心功能，支持以下场景：  
- 动态调整定时任务逻辑（无需重启）  
- 多触发器灵活挂载与参数修改  
- 任务执行状态实时监控与历史记录追溯  

**技术栈**：  
- 前端：Vue.js  
- 动态脚本：Groovy  
- 监控：SSE（实时推送）、MySQL（数据存储）  
- 后端：Spring Boot  

---

## **2. 环境准备**
### **2.1 运行环境**
- JDK 1.8+  
- MySQL 5.7+  
- Node.js 12+（前端构建）  
- Maven 3.6+（后端依赖管理）  

### **2.2 初始化配置**
#### **Step 1：添加项目依赖**
在 `pom.xml` 中引入核心库：
```xml
<dependency>
    <groupId>io.github.2757559039</groupId>
    <artifactId>quartz_visualization</artifactId>
    <version>0.0.12</version>
</dependency>
```

#### **Step 2：配置主启动类**
在 Spring Boot 启动类添加包扫描注解：
```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.visualization.cloud"}) // 关键注解
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

#### **Step 3：设置 application.yml**
创建或修改 `src/main/resources/application.yml`：
```yaml
server:
  port: 8002  # 服务端口
spring:
  application:
    name: test
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/visualization?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true
    username: root
    password: 123456
  jackson:
    default-property-inclusion: non_null  # 忽略JSON空值

# Quartz 集群配置（需与MySQL配合）
org:
  quartz:
    scheduler:
      instance-name: cluster_scheduler
      instance-id: AUTO
    threadpool:
      thread-count: 10  # 线程池大小
    jobstore:
      isClustered: true  # 启用集群模式
      dataSource: quartz
    datasource:
      driver: com.mysql.cj.jdbc.Driver
      URL: jdbc:mysql://localhost:3306/visualization?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true
      user: root
      password: 123456
```

#### **Step 4：数据库初始化**
1. 创建数据库并执行项目内 `sql/init.sql` 脚本  
2. 确保 `visualization` 数据库已存在  

#### **Step 5：前端构建**
```bash
cd frontend && npm install && npm run build
```

### **2.3 启动服务**
```bash
# 后端启动
mvn spring-boot:run

# 前端访问
http://localhost:8080
```

---

## **3. 快速入门**
**目标**：5分钟内创建一个每秒执行一次的定时任务  

1. **进入任务管理页面**  
   - 导航栏点击 **任务管理** > **新增任务**  

2. **填写任务信息**  
   - **任务类名**：`com.example.TestJob`（默认类示例）  
   - **任务分组**：`default`  
   - **触发器类型**：选择 `SimpleTrigger`，设置间隔为 `1秒`  
   - 点击 **提交**  

3. **启动任务**  
   - 在任务列表中，找到新增任务，点击 **恢复**  
   - 任务状态变为“运行中”，每秒执行一次  

---

## **4. 功能详解**
### **4.1 任务管理**
- **新增任务**  
  支持两种模式：  
  - **默认模式**：直接填写类名、分组、触发器参数  
  - **自定义模式**：需先在 **脚本管理** 中上传 Groovy 脚本并注册为 Bean  

- **任务操作**  
  - **启动/停止**：单个任务或一键启停所有任务  
  - **立即执行**：手动触发任务（仅限运行中的任务）  
  - **模糊搜索**：通过类名或分组快速定位任务  

### **4.2 触发器管理**
- **挂载触发器**  
  - 单个 Job 可挂载多个触发器，支持 `CronTrigger` 和 `SimpleTrigger`  
  - 示例：为同一个 Job 添加每日 0 点和每小时执行一次的触发器  

### **4.3 脚本管理**
1. **上传脚本**  
   - 进入 **脚本管理** > **上传脚本**，填写 Groovy 脚本信息  
   - 示例脚本：  
     ```groovy
     class CustomJob {
         void execute() {
             println "Dynamic Job Executed!"
         }
     }
     ```

2. **注册与卸载**  
   - **安装**：将脚本注册为 Spring Bean，供任务调用  
   - **卸载**：从 JVM 和 Bean 容器中移除脚本  

### **4.4 监控平台**
- **实时监控**  
  - 查看任务执行状态、成功率、耗时等指标（SSE 推送）  
- **历史记录**  
  - 按日期筛选任务执行日志，支持清空过期数据（保留 30 天）  

---

## **5. 高级功能**
1. **热更新 Job 类**  
   - 修改已上传的 Groovy 脚本后，点击 **更新**，任务逻辑立即生效  

2. **触发器优先级**  
   - 同一 Job 的多个触发器可通过 `priority` 字段设置执行顺序  

3. **参数传递**  
   - 在 Job 详情中配置 `JobDataMap`，向任务传递动态参数  

---

## **6. 常见问题（FAQ）**
**Q1：任务状态为“已停止”，但触发器仍在执行**  
- 检查是否有其他触发器挂载到该任务，需单独停止触发器  

**Q2：Groovy 脚本上传后未生效**  
- 确认脚本已点击 **安装**，且类名与任务配置中的类名一致  

**Q3：监控数据未更新**  
- 检查 SSE 连接是否正常，或清空浏览器缓存后重试  

---

## **7. 注意事项**
- **数据安全**：清空历史记录操作不可逆，建议定期备份数据库  
- **脚本规范**：Groovy 脚本需实现无参 `execute()` 方法  
- **配置关键点**：  
  - 生产环境需修改 `url` 中的数据库 IP 和端口  
  - 调整 `thread-count` 根据服务器 CPU 核心数优化性能  
- **触发器冲突**：避免为同一任务设置过多短间隔触发器，防止资源竞争  

---

**获取支持**  
- 问题反馈：2757559039@qq.com  
- 文档更新：访问项目 Git 仓库查看最新版本  

--- 

**版本：1.0.0 | 更新日期：2025-03-10**
```

---

### 关键更新说明：
1. **依赖配置**：在 `环境准备` 章节中明确添加了 Maven 依赖和启动类注解  
2. **YAML 配置**：以代码块形式完整呈现 `application.yml` 参数  
3. **操作流程**：优化了从依赖配置到启动服务的步骤顺序  
4. **注意事项**：补充了生产环境配置和性能调优建议
