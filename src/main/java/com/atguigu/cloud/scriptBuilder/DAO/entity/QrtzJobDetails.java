//package com.atguigu.cloud.quartz.scriptBuilder.DAO.entity;
//
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import java.sql.Blob;
//import java.io.Serializable;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.experimental.Accessors;
//
///**
// * <p>
// *
// * </p>
// *
// * @author 李孝在
// * @since 2025-01-27
// */
//@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
//@TableName("qrtz_job_details")
//public class QrtzJobDetails implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @TableId(value = "SCHED_NAME", type = IdType.NONE)
//    private String schedName;
//
//    private String jobName;
//
//    private String jobGroup;
//
//    private String description;
//
//    private String jobClassName;
//
//    private String isDurable;
//
//    private String isNonconcurrent;
//
//    private String isUpdateData;
//
//    private String requestsRecovery;
//
//    private Blob jobData;
//
//
//}
