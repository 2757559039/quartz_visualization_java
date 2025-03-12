package com.visualization.cloud.scriptBuilder.beanhandling.entity.DTO;/**
 * @Auter 李孝在
 * @Date 2025/2/18
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.scriptBuilder.beanhandling.entity.DTO
 * @className: SelectGroovyBeanInfoConditionDTO
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/18 22:32
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectGroovyBeanInfoConditionDTO {
    @Schema(description = "关键字")
    private String keywords;
    @Schema(description = "筛选条件")
    private String filterCriteria;
}
