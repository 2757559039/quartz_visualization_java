package com.atguigu.cloud.scriptBuilder.DAO.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 李孝在
 * @since 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("v_script_tag")
@Schema(description = "脚本标记表")
public class VScriptTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标记id
     */
    @TableId(value = "TAG_ID", type = IdType.AUTO)
    private Integer tagId;

    /**
     * 脚本id
     */

    private String scriptId;

    /**
     * 组标记
     */

    private String groupTag;

    /**
     * 名标记
     */

    private String nameTag;

    private String scriptType;


}
