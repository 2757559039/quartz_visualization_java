package com.visualization.cloud.sse;/**
 * @Auter 李孝在
 * @Date 2025/1/29
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.visualization.cloud.scriptBuilder.DAO.entity.po.VSseSendInfo;
import com.visualization.cloud.scriptBuilder.DAO.mapper.VSseSendInfoMapper;
import com.visualization.cloud.util.resp.ResultData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import static cn.hutool.core.lang.Console.log;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.sse
 * @className: SSEUtil
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/29 14:26
 * @version: 1.0
 */
@Component
@Slf4j
public class SSEUtil {


    private static VSseSendInfoMapper vSseSendInfoMapper;

    @Autowired
    private SSEUtil(VSseSendInfoMapper vSseSendInfoMapper){
        SSEUtil.vSseSendInfoMapper =vSseSendInfoMapper;
    }


    @Slf4j
    //连接管理器
    public static class SSEEmitterManager {

        //key:唯一标识,List多播支持
        private static final Map<String, List<SseEmitter>> sseCache = new ConcurrentHashMap<>();


        //创建连接
        public static SseEmitter sseEmitterBuilder(String key, Long timeout , Long reLinkTime )  {

            // 创建一个新的SseEmitter，设置超时时间
            SseEmitter emitter = new SseEmitter(timeout);

            // 获取该uid对应的连接列表，如果不存在则创建新列表
            List<SseEmitter> emitters = sseCache.computeIfAbsent(key, k -> new ArrayList<>());

            // 添加新的连接到列表中
            emitters.add(emitter);

            // 设置超时回调
            emitter.onTimeout(() -> {
                log.info("连接超时: {}", key);
                removeEmitter(key, emitter);
            });

            // 设置完成回调
            emitter.onCompletion(() -> {
                log.info("连接完成: {}", key);
                removeEmitter(key, emitter);
            });

            // 设置错误回调
            emitter.onError(ex -> {
                log.error("连接错误: {}, 错误: {}", key, ex.getMessage());
                removeEmitter(key, emitter);
            });

            try {
                // 发送初始连接成功消息
                emitter.send(SseEmitter.event()
                        .name("connect")
                        .data("连接成功")
                        .reconnectTime(reLinkTime)); // 设置重连时间为3秒

                log.info("创建新连接: {}", key);
            } catch (IOException e) {
                log.error("发送初始消息失败: {}", e.getMessage());
                removeEmitter(key, emitter);
            }

            return emitter;
        }


        /**
         * 移除连接
         * @param key 主题key
         * @param emitter 要移除的SseEmitter
         */
        public static void removeEmitter(String key, SseEmitter emitter) {
            List<SseEmitter> emitters = sseCache.get(key);
            if (emitters != null) {
                emitters.remove(emitter);
                // 如果该uid下没有连接了，则移除整个uid
                if (emitters.isEmpty()) {
                    sseCache.remove(key);
                    log.info("移除key: {}", key);
                }
            }
        }


        /**
         * 获取指定key的所有连接
         * @param key KEY
         * @return 连接列表，如果不存在则返回空列表
         */
        public static List<SseEmitter> getConnections(String key) {
            return sseCache.getOrDefault(key, new ArrayList<>());
        }
    }


    //专门发送消息的方法
    public static class MessageSender {

        private SSEEmitterManager sseEmitterManager;


        /**
         * 发送消息
         * @param key 主题key
         * @param message 消息内容
         * @return 是否发送成功
         */

        public static void sendMessage(String key, String message) {
            // 将消息添加到缓存和数据库
            MessageBuffer.addMessage(key,message);
            log.info("已将消息转发至缓存处理: {}, 内容: {}", key, message);
        }


    }


    //消息缓存
    @Component
    public static class MessageBuffer {

        // SSE消息缓存队列
        private static final Map<String, LinkedBlockingQueue<String>> messageBuffers = new ConcurrentHashMap<>();

        // 数据库缓存队列
        private static final Map<String, LinkedBlockingQueue<String>> dbBuffers = new ConcurrentHashMap<>();

        // 最大缓存消息数
        private static final int MAX_SSE_BUFFER_SIZE = 200;

        // 最大缓存消息数
        private static final int MAX_DB_BUFFER_SIZE = 200;


        // 信息发送间隔
        private static final int SSE_BRUSH_INTERVAL = 1000;

        // 刷盘间隔
        private static final int DB_BRUSH_INTERVAL = 10*1000;




        /**
         * 添加消息到缓存
         * @param message 消息
         */

        public static void  addMessage(String key,String message) {
            // 获取该KEY的消息缓存队列，如果不存在则创建
            LinkedBlockingQueue<String> sseBuffer = messageBuffers.computeIfAbsent(
                    key, k -> new LinkedBlockingQueue<>(MAX_SSE_BUFFER_SIZE));

            // 如果缓存已满，移除最旧的消息
            if (sseBuffer.size() >= MAX_SSE_BUFFER_SIZE) {
                sseBuffer.poll();
                log.warn("消息发送缓存已满，移除最旧消息: {}", key);
            }

            // 添加新消息到缓存
            sseBuffer.offer(message);
            log.info("消息已添加到 消息发送缓存中: {}, 内容: {}", key, message);



            LinkedBlockingQueue<String> dbBuffer = dbBuffers.computeIfAbsent(
                    key, k -> new LinkedBlockingQueue<>(MAX_DB_BUFFER_SIZE));

            // 如果缓存已满，移除最旧的消息
            if (dbBuffer.size() >= MAX_DB_BUFFER_SIZE) {
                dbBuffer.poll();
                log.warn("数据库发送缓存已满，移除最旧消息: {}", key);
            }
            // 添加消息到数据库缓存
            dbBuffer.offer("\""+message+"\"");
            log.info("消息已添加到 数据库发送缓存中: {}, 内容: {}", key, message);


        }

        /**
         * 定时任务：发送缓存的消息到客户端
         * 每500毫秒执行一次
         */
        @Scheduled(fixedRate =SSE_BRUSH_INTERVAL )
        @Async
        public void sendBufferedMessages() {

            // 遍历所有key
            for (String key : new ArrayList<>(messageBuffers.keySet())) {
                LinkedBlockingQueue<String> buffer = messageBuffers.get(key);

                // 如果缓存为空，跳过
                if (buffer == null || buffer.isEmpty()) {
                    continue;
                }

                // 获取该KEY的所有连接
                List<SseEmitter> emitters =SSEEmitterManager.getConnections(key);

                // 如果没有活跃连接，跳过
                if (emitters.isEmpty()) {
                    continue;
                }

                // 从缓存中取出所有消息
                List<String> messages = new ArrayList<>();
                buffer.drainTo(messages);

                //无消息,跳过
                if (messages.isEmpty()) {
                    continue;
                }


                // 发送批量消息到所有连接
                for (SseEmitter emitter : new ArrayList<>(emitters)) {
                    try {

                        emitter.send(SseEmitter.event()
                                .name("message")
                                .data(ResultData.success(messages)));
                    } catch (IOException e) {
                        log.error("发送消息失败: {}", e.getMessage());
                        // 发送失败时移除连接
                        SSEEmitterManager.removeEmitter(key, emitter);
                    }
                }

                log.info("发送批量消息到 {}: {} 条消息", key, messages.size());
            }

        }

        /**
         * 定时任务：将缓存的消息保存到数据库
         * 每2秒执行一次
         */
        @Scheduled(fixedRate = DB_BRUSH_INTERVAL)
        @Async
        public void saveBufferedMessagesToDb() {


            for (String key : new ArrayList<>(dbBuffers.keySet())) {
                //获取缓存
                LinkedBlockingQueue<String> buffer = dbBuffers.get(key);
                // 从缓存中取出所有消息
                List<String> messages = new ArrayList<>();
                buffer.drainTo(messages);

                //无消息,跳过
                if (messages.isEmpty()) {
                    continue;
                }
                VSseSendInfo vSseSendInfo=new VSseSendInfo();
                vSseSendInfo.setSendKey(key);
                vSseSendInfo.setSendData(messages.toString());
                vSseSendInfo.setSendDate(LocalDate.now());
                //插入数据
                vSseSendInfoMapper.insert(vSseSendInfo);


                //查询
//                QueryWrapper<VSseSendInfo> vSseSendInfoQueryWrapper =new QueryWrapper<>();
//                vSseSendInfoQueryWrapper.eq("send_key",key).eq("send_date", LocalDate.now());
//                VSseSendInfo  vSseSendInfo = vSseSendInfoMapper.selectOne(vSseSendInfoQueryWrapper);


//                //若该key在今天的记录不存在--->插入数据
//                if (Objects.isNull(vSseSendInfo)){
//                    vSseSendInfo=new VSseSendInfo();
//                    vSseSendInfo.setSendKey(key);
//                    vSseSendInfo.setSendData(batchContent.toString());
//                    vSseSendInfo.setSendDate(LocalDate.now());
//                    //插入数据
//                    vSseSendInfoMapper.insert(vSseSendInfo);
//                }
//                //否则--->更新数据
//                else {
//                    //重设(追加data)
//                    String historyData = vSseSendInfo.getSendData();
//
//                    //更新数据
//                    UpdateWrapper<VSseSendInfo> updateWrapper=new UpdateWrapper<>();
//                    updateWrapper.eq("send_date", LocalDate.now()).eq("send_key",key).set("send_data",historyData+historyData);
//                    vSseSendInfoMapper.update(updateWrapper);
//
//                }
                log.info("保存消息{},到数据库: {} 条消息", key,messages.size());
            }

        }



    }


    //数据恢复
    public static List<String> SSEHistoryDataRecovery(String key, LocalDate queryDate){
        QueryWrapper<VSseSendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("send_key", key);
        queryWrapper.eq("send_date", queryDate);
        List<VSseSendInfo> vSseSendInfoList = vSseSendInfoMapper.selectList(queryWrapper);
        List<String> responseList = vSseSendInfoList.stream()
                .map(VSseSendInfo::getSendData)
                .flatMap(json -> {

                    try {
                        // 解析JSON数组
                        return JSON.parseArray(json, String.class).stream();
                    } catch (JSONException e) {
                        return Stream.empty(); // 跳过无效数据
                    }
                })
                .toList();
        return responseList;

    }

    //发送简单的信息
    public static void simpleBroadcastEvent(String key, String data) {
        List<SseEmitter> sseEmitterList = SSEEmitterManager.sseCache.get(key);
        for (SseEmitter sseEmitter : sseEmitterList) {
            try {
                sseEmitter.send(SseEmitter.event().name("simple").data(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }





    //获取缓存的连接
    public static List<String> getLinkingFromDB() {
        QueryWrapper<VSseSendInfo> queryWrapper = new QueryWrapper<>();
        //去重
        queryWrapper.select("DISTINCT send_key");
        List<VSseSendInfo> vSseSendInfoList = vSseSendInfoMapper.selectList(queryWrapper);
        List<String> distinctSeeds = vSseSendInfoList.stream()
                .map(VSseSendInfo::getSendKey)
                .toList();
        return distinctSeeds;
    }
//
    public static List<LocalDate> getLinkRecordDateFromDB(String key) {
        QueryWrapper<VSseSendInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("send_key",key);
        List<VSseSendInfo> vSseSendInfoList = vSseSendInfoMapper.selectList(queryWrapper);
        List<LocalDate> linkRecordDateList = vSseSendInfoList.stream()
                .map(VSseSendInfo::getSendDate)
                .toList();
        return linkRecordDateList;
    }





}
