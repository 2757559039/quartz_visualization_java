package com.atguigu.cloud.util.exception.errCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Auter 李孝在
 * @Date 2025/2/21
 */
@AllArgsConstructor
@Getter
public enum GroovyBeanExceptionCodeMsg {
    BEAN_EXIST(2001,"该类名对应的bean已存在"),
    BEAN_NO_EXIST_IN_CONTEXT(2002,"该类名对应的bean不存在"),
    BEAN_NO_EXIST_IN_DB(2003,"该类对应的脚本不存在");

    private final Integer code;
    private final String msg;
}
