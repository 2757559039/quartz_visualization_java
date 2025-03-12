package com.visualization.cloud.scriptBuilder.datahandling;

import com.visualization.cloud.scriptBuilder.DAO.entity.VScriptTag;

import java.util.List;

/**
 * @Auter zzh
 * @Date 2025/2/27
 */
public interface VScriptTagScervice {




    Boolean addScriptTag(String name, String group, String scriptId, String scriptType);

    Boolean deleteScriptTag(String name, String group);



    Boolean deleteScriptTag(String scriptId);

    List<VScriptTag> getScriptTag(String name, String group, String scriptId, String scriptType);




    String getScriptId(String name, String group, String scriptType);
}
