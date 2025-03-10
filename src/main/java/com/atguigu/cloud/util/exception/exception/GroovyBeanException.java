package com.atguigu.cloud.util.exception.exception;


import com.atguigu.cloud.util.exception.errCode.GroovyBeanExceptionCodeMsg;
import lombok.Getter;

/**
 * @Auter 李孝在
 * @Date 2025/2/21
 */
@Getter
public class GroovyBeanException extends RuntimeException {
    private Integer code;
    private String msg;

    public GroovyBeanException(GroovyBeanExceptionCodeMsg appExceptionCodeMsg){
        super(appExceptionCodeMsg.getMsg());
        this.code=appExceptionCodeMsg.getCode();
        this.msg=appExceptionCodeMsg.getMsg();
    }

    public GroovyBeanException(Integer code, String msg) {
        super(msg);
        this.code=code;
        this.msg=msg;
    }

}