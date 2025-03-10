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
 * @since 2025-01-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("v_qrtz_script")
@Schema(description = "脚本表")
public class VQrtzScript implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "脚本id")
    @TableId(value = "ID", type = IdType.NONE)
    private Integer id;

    @Schema(description = "脚本类名")
    private String className;

    @Schema(description = "脚本")
    private String script;

    @Schema(description = "脚本类型")
    private String scriptType;

    @Schema(description = "bean状态")
    private Boolean beanState;

}
