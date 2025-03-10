package com.atguigu.cloud.scriptBuilder.DAO.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 李孝在
 * @since 2025-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("v_sse_send_info")
public class VSseSendInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sse信息发送id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Integer id;

    /**
     * sse信息发送key
     */
    private String sendKey;

    /**
     * sse信息发送data
     */
    private String sendData;

    /**
     * 发送的日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate sendDate;

}
