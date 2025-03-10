package com.atguigu.cloud.scriptBuilder.datahandling.entity.DTO;/**
 * @Auter 李孝在
 * @Date 2025/2/27
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.scriptBuilder.datahandling.entity.DTO
 * @className: asd
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/27 10:44
 * @version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectScriptInfoConditionDTO {

    @Schema(description = "脚本类型")
    private String type;
    @Schema(description = "类名")
    private String className;
    @Schema(description = "bean存在状态")
    private Boolean beanState;

}
