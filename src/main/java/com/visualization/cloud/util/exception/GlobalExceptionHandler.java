package com.visualization.cloud.util.exception;

import com.visualization.cloud.util.exception.exception.GroovyBeanException;
import com.visualization.cloud.util.exception.exception.JobBuilderException;
import com.visualization.cloud.util.resp.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public <T> Object exceptionHandler(Exception e,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {

        boolean isSseRequest = isSseRequest(request);

        if (isSseRequest) {
            return handleSseException(e, request, response);
        }
        if (e instanceof GroovyBeanException){
            return ResultData.fail((((GroovyBeanException) e).getCode().toString()), e.getMessage());
        }
        else {
            return handleNormalException(e);
        }
    }

    private boolean isSseRequest(HttpServletRequest request) {
        // 方式1：检查 Accept 头
        String acceptHeader = request.getHeader("Accept");
        boolean isAcceptSse = acceptHeader != null && acceptHeader.contains(MediaType.TEXT_EVENT_STREAM_VALUE);

        // 方式2：检查请求路径特征（例如路径包含 /sse/）
        boolean isUrlSse = request.getRequestURI().contains("/sse/");

        return isAcceptSse || isUrlSse;
    }

    private <T> ResultData<T> handleNormalException(Exception e) {
        if (e instanceof JobBuilderException) {
            JobBuilderException appException = (JobBuilderException) e;
            return ResultData.fail(
                    appException.getCode().toString(),
                    appException.getMessage());
        }
        log.error("e: ", e);
        return ResultData.fail("500", "服务器异常:" + e);
    }

    private <T> ResponseEntity<Void> handleSseException(Exception e,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
        // 关键点1：检查响应是否已提交（避免操作已关闭的流）
        if (response.isCommitted()) {
            return null;
        }

        // 关键点2：使用 OutputStream 而非 Writer
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ResultData<?> resultData;
        if (e instanceof JobBuilderException) {
            JobBuilderException appException = (JobBuilderException) e;
            resultData = ResultData.fail(appException.getCode().toString(), appException.getMessage());
        } else {
            log.error("e: ", e);
            resultData = ResultData.fail("500", "服务器异常:" + e);
        }

        // 手动构造 SSE 格式数据
        String sseData = "data: " + new ObjectMapper().writeValueAsString(resultData) + "\n\n";
        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(sseData.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException ex) {
            // 忽略已关闭的流（如客户端提前断开连接）
        }

        return null;
    }
}