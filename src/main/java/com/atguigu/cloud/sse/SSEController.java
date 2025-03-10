package com.atguigu.cloud.sse;/**
 * @Auter 李孝在
 * @Date 2025/1/25
 */

import com.atguigu.cloud.scriptBuilder.DAO.mapper.VSseSendInfoMapper;
import com.atguigu.cloud.util.resp.ResultData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @projectName: visualization
 * @package: com.atguigu.cloud.sse
 * @className: SSEController
 * @author: Eric
 * @description: TODO
 * @date: 2025/1/25 02:53
 * @version: 1.0
 */
@RestController
@RequestMapping(path = "sse/")
@Tag(name = "sse连接")
public class SSEController {

    @Resource
    private SSEUtil sseUtil;
    @Resource
    private VSseSendInfoMapper vSseSendInfoMapper;

//    @Operation(description = "创建全局连接")
////    @GetMapping(path = "/globalSubscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter globalSubscribe(@RequestParam @Schema(description = "全局类型,job,trigger或者scheduler") String cacheKey) throws IOException {
//        return sseEmitterBuilder(null,cacheKey);
//    }
//
//    @Operation(description = "创建触发器连接")
////    @GetMapping(path = "/triggerSubscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter triggerSubscribe(@RequestParam @Schema(description ="触发器的key或者group") String cacheKey) throws IOException {
//        return sseEmitterBuilder("trigger:",cacheKey);
//    }
//
//    @Operation(description = "创建任务连接")
////    @GetMapping(path = "/jobSubscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter jobSubscribe(@RequestParam @Schema(description ="任务的key或者group") String cacheKey) throws IOException {
//        return sseEmitterBuilder("job:",cacheKey);
//    }

    @Operation(description = "创建自定义任务连接")
    @GetMapping(path = "/definedJobSubscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter definedJobSubscribe(@RequestParam @Schema(description ="连接的key") String cacheKey) throws IOException {
        return SSEUtil.SSEEmitterManager.sseEmitterBuilder(cacheKey,
                30_000L,
                30_000L);
    }


    @Operation(description = "历史记录恢复")
    @GetMapping(path = "/restoreSSEInfoHistory")
    public ResultData<List<String>> restoreSSEInfoHistory(@RequestParam @Schema(description ="连接的key") String cacheKey,@RequestParam @Schema(description ="查询时间") LocalDate recordDate) throws IOException {
        return  ResultData.success(SSEUtil.SSEHistoryDataRecovery(cacheKey, recordDate));
    }


    @Operation(description = "获取当前的缓存连接")
    @PostMapping(path = "/getLinkingCache")
    public ResultData<List<String>> getLinkingCache() {
        return  ResultData.success(SSEUtil.getLinkingFromDB());
    }

    @Operation(description = "获取的缓存连接的记录日期")
    @PostMapping(path = "/getLinkRecordDateFromDB")
    public ResultData<List<LocalDate>> getLinkRecordDateFromDB(@RequestParam @Schema(description = "待查询的key") String key) {
        return  ResultData.success(SSEUtil.getLinkRecordDateFromDB(key));
    }




    @PostMapping(path = "/send_test")
    public void send_test(){
        Map<String, LinkedBlockingQueue<String>> messageBuffers = new ConcurrentHashMap<>();
        int a=messageBuffers.keySet().size();
        System.out.println(a);
    }


}
