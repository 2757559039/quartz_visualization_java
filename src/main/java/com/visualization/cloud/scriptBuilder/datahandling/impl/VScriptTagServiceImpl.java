package com.visualization.cloud.scriptBuilder.datahandling.impl;/**
 * @Auter zzh
 * @Date 2025/2/27
 */

import cn.hutool.core.util.StrUtil;
import com.visualization.cloud.scriptBuilder.DAO.entity.VScriptTag;
import com.visualization.cloud.scriptBuilder.DAO.mapper.VScriptTagMapper;
import com.visualization.cloud.scriptBuilder.datahandling.VScriptTagScervice;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @projectName: quartz_visualization
 * @package: com.atguigu.cloud.scriptBuilder.datahandling.impl
 * @className: VScriptTagServiceImpl
 * @author: Eric
 * @description: TODO
 * @date: 2025/2/27 20:46
 * @version: 1.0
 */
@Slf4j
@Service
public class VScriptTagServiceImpl implements VScriptTagScervice {
    @Resource
    private VScriptTagMapper vScriptTagMapper;

    @Override
    public Boolean addScriptTag(String name, String group, String scriptId, String scriptType) {
        LambdaQueryWrapper<VScriptTag> queryWrapper = new LambdaQueryWrapper<>();
        //首先查询有无重复的标签,以name,group,scriptType共同查询,如果有,则更新,如果没有则插入
        if(vScriptTagMapper
                .selectOne(
                        queryWrapper
                                .eq(VScriptTag::getNameTag,name)
                                .eq(VScriptTag::getGroupTag,group)
                                .eq(VScriptTag::getScriptType,scriptType)
                                )
                !=null
        ){
            //更新
            VScriptTag vScriptTag = new VScriptTag();
            vScriptTag.setScriptId(scriptId);
            return vScriptTagMapper.update(vScriptTag, queryWrapper) == 1;
        }else {
            //插入
            VScriptTag vScriptTag = new VScriptTag();
            vScriptTag.setScriptId(scriptId);
            vScriptTag.setGroupTag(group);
            vScriptTag.setNameTag(name);
            vScriptTag.setScriptType(scriptType);

            return vScriptTagMapper.insert(vScriptTag) == 1;
        }

    }
    @Override
    public Boolean deleteScriptTag(String name, String group) {
        LambdaQueryWrapper<VScriptTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VScriptTag::getNameTag,name).eq(VScriptTag::getGroupTag,group);
        return vScriptTagMapper.delete(queryWrapper) == 1;
    }
    @Override
    public Boolean deleteScriptTag(String scriptId) {
        LambdaQueryWrapper<VScriptTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VScriptTag::getScriptId,scriptId);
        return vScriptTagMapper.delete(queryWrapper) == 1;
    }
    @Override
    public List<VScriptTag> getScriptTag(String name, String group, String scriptId, String scriptType) {
        LambdaQueryWrapper<VScriptTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(name),VScriptTag::getNameTag,name)
                .eq(StrUtil.isNotBlank(group),VScriptTag::getGroupTag,group)
                .eq(Objects.nonNull(scriptId),VScriptTag::getScriptType,scriptType)
                .eq(StrUtil.isNotBlank(scriptType),VScriptTag::getScriptId,scriptId);
        return vScriptTagMapper.selectList(queryWrapper);
    }
    @Override
    public String getScriptId(String name, String group, String scriptType) {
        LambdaQueryWrapper<VScriptTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VScriptTag::getNameTag,name)
                .eq(VScriptTag::getGroupTag,group)
                .eq(VScriptTag::getScriptType,"script");
        ;
        VScriptTag vScriptTag = vScriptTagMapper.selectOne(queryWrapper);
        if (vScriptTag == null) {
            return null;
        }
        return vScriptTag.getScriptId();
    }



}
