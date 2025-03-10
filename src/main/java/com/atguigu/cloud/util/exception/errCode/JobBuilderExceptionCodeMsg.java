package com.atguigu.cloud.util.exception.errCode;/**
 * @Auter 李孝在
 * @Date 2025/1/23
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.exception
 * @className: asd
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/23 02:26
 * @version: 1.0
 */
@AllArgsConstructor
@Getter
public enum JobBuilderExceptionCodeMsg {
    JOB_SCRIPT_SAVE_TO_DB_ERROR(1001,"任务脚本保存到数据库失败"),
    JOB_SCRIPT_SAVE_TO_FILE_ERROR(1002,"任务脚本保存到文件失败"),
    JOB_SCRIPT_LOAD_TO_DB_ERROR(1003,"任务脚本从数据库中加载失败"),
    JOB_SCRIPT_LOAD_TO_FILE_ERROR(1004,"任务脚本从文件中加载失败"),
    JOB_SCRIPT_UNLAWFUL(1005,"任务脚本不合法,请检查是否实现了Job接口"),
    JOB_SCRIPT_EXISTS(1006,"该任务脚本已存在");
    private final Integer code;
    private final String msg;

}