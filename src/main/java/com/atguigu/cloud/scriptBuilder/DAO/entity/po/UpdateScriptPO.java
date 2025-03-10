package com.atguigu.cloud.scriptBuilder.DAO.entity.po;/**
 * @Auter 李孝在
 * @Date 2025/1/27
 */

import com.atguigu.cloud.scriptBuilder.DAO.entity.ScriptTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.quartz.scriptBuilder.DAO.entity.po
 * @className: UpdateScriptPO
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/27 22:57
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "更新脚本PO")
public class UpdateScriptPO {
    @Schema(description = "脚本id")
    private Integer id;
    @Schema(description = "脚本")
    private String script;


}
