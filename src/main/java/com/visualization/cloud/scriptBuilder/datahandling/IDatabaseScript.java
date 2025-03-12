package com.visualization.cloud.scriptBuilder.datahandling;

import com.visualization.cloud.scriptBuilder.DAO.entity.ScriptTypeEnums;
import com.visualization.cloud.scriptBuilder.DAO.entity.VQrtzScript;
import com.visualization.cloud.scriptBuilder.datahandling.entity.DTO.SelectScriptInfoConditionDTO;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @Auter 李孝在
 * @Date 2025/1/27
 */
public interface IDatabaseScript {

    void load();
    void recorder(String script, ScriptTypeEnums type);
    void delete(Integer id) throws SchedulerException;
    void update(Integer id,String script) throws SchedulerException;
    List<VQrtzScript> selectAll(SelectScriptInfoConditionDTO selectScriptInfoConditionDTO);
    VQrtzScript select(Integer id);
    List<String> selectFilterCriteria();
}
