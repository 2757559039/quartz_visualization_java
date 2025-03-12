package com.visualization.cloud.util.exception.exception;/**
 * @Auter 李孝在
 * @Date 2025/1/23
 */


import com.visualization.cloud.util.exception.errCode.JobBuilderExceptionCodeMsg;
import lombok.Getter;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.exception
 * @className: das
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/23 02:33
 * @version: 1.0
 */

@Getter
public class JobBuilderException extends RuntimeException {
    private Integer code;
    private String msg;

    public JobBuilderException(JobBuilderExceptionCodeMsg appExceptionCodeMsg){
        super(appExceptionCodeMsg.getMsg());
        this.code=appExceptionCodeMsg.getCode();
        this.msg=appExceptionCodeMsg.getMsg();
    }

    public JobBuilderException(Integer code, String msg) {
        super(msg);
        this.code=code;
        this.msg=msg;
    }

}
