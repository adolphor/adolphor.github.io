package y2023.m10.d08.reqlog.logutil.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import y2023.m10.d08.reqlog.logutil.LogParamUtil;
import y2023.m10.d08.reqlog.mvc.dao.RequestLogDao;
import y2023.m10.d08.reqlog.mvc.entity.RequestLog;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * webmvc5以下版本，需要继承 HandlerInterceptorAdapter
 */
@RestControllerAdvice
@Order(2)
@Slf4j
public class LogInterceptor extends WebRequestHandlerInterceptorAdapter implements ResponseBodyAdvice, RequestBodyAdvice {

    private final RequestLogDao requestLogDao;

    public LogInterceptor(RequestLogDao requestLogDao, WebRequestInterceptor requestInterceptor) {
        super(requestInterceptor);
        this.requestLogDao = requestLogDao;
    }

    /**
     * 必须注册到 WebMvcConfigurer.addInterceptors(registry) 才会生效，参考：LogInterceptorRegister.java
     */
    // HandlerInterceptor start
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();

        log.debug("[请求IP][{}]: {}", LogParamUtil.getTraceId(), request.getRemoteAddr());
        log.debug("[请求URI][{}]: {}", LogParamUtil.getTraceId(), request.getRequestURI());
        log.debug("[请求方式][{}]: {}", LogParamUtil.getTraceId(), request.getMethod());

        // reqParam
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object[]> parameterLogMap = new HashMap<>();
        BeanUtil.copyProperties(parameterMap, parameterLogMap);
        String reqParams = JSONObject.toJSONString(parameterLogMap);
        // 插入数据库
        RequestLog douyinLog = RequestLog.builder()
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .wasSuccess(1)
                .reqParam(reqParams)
                .build();
        int cnt = requestLogDao.insertDouyinLog(douyinLog);
        log.debug("[请求日志记录][{}]-插入：{}={}={}", LogParamUtil.getTraceId(), cnt, douyinLog.getId(), douyinLog);
        // 缓存当前日志ID
        LogParamUtil.setStartTime(startTime);
        LogParamUtil.setLogId(douyinLog.getId());
        LogParamUtil.setReqParam(parameterLogMap);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
        log.debug("[请求IP ][{}]: {}", LogParamUtil.getTraceId(), request.getRequestURI());
        log.debug("[请求耗时][{}]: {}", LogParamUtil.getTraceId(), (System.currentTimeMillis() - LogParamUtil.getStartTime()));
        log.debug("[运行内存][{}]: 最大内存:{}m, 已分配内存:{}m, 已分配内存中的剩余空间:{}m, 最大可用内存:{}m",
                LogParamUtil.getTraceId(),
                Runtime.getRuntime().maxMemory() / 1024 / 1024,
                Runtime.getRuntime().totalMemory() / 1024 / 1024,
                Runtime.getRuntime().freeMemory() / 1024 / 1024,
                (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);
    }
    // HandlerInterceptor end

    /**
     * RequestBodyAdvice：只有包含@RequestBody注解的Controller才会进入 supports 方法
     */
    // RequestBodyAdvice start
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("[RequestBodyAdvice-supports][{}]", LogParamUtil.getTraceId());
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        log.debug("[RequestBodyAdvice-beforeBodyRead][{}]", LogParamUtil.getTraceId());
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("[RequestBodyAdvice-afterBodyRead][{}]", LogParamUtil.getTraceId());
        logBodyParam(body);
        return body;
    }

    private void logBodyParam(Object body) {
        if (LogParamUtil.getLogId() == null) {
            return;
        }
        Map<String, Object[]> reqParam = LogParamUtil.getReqParam();
        if (Objects.isNull(body)) {
            return;
        }
        // 尝试转换body数据为json
        try {
            JSONObject jsonObject = JSONObject.parseObject(body.toString());
            body = jsonObject;
        } catch (Exception e) {
            log.debug("[请求日志记录][{}]-非json数据：{}", LogParamUtil.getTraceId(), body);
        }

        // 拼装
        Object[] bodyArr = {body};
        reqParam.put("body", bodyArr);
        RequestLog logBody = RequestLog.builder()
                .id(LogParamUtil.getLogId())
                .reqParam(JSONObject.toJSONString(reqParam))
                .build();
        int cnt = requestLogDao.updateDouyinLogResult(logBody);
        log.debug("[请求日志记录][{}]-Body：{}={}={}", LogParamUtil.getTraceId(), cnt, LogParamUtil.getLogId(), logBody);
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("[RequestBodyAdvice-handleEmptyBody][{}]", LogParamUtil.getTraceId());
        logBodyParam(body);
        return body;
    }
    // RequestBodyAdvice end

    /**
     * ResponseBodyAdvice：都会进入supports方法，只要 supports为true 都会生效
     */
    // ResponseBodyAdvice start
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Long logId = LogParamUtil.getLogId();
        if (logId != null) {
            RequestLog.RequestLogBuilder builder = RequestLog.builder().id(logId);
            if (!Objects.isNull(body)) {
                JSONObject jsonObject = JSONObject.parseObject(body.toString());
                if (!jsonObject.getString("code").equals("0")) {
                    builder.wasSuccess(0);
                }
            }
            builder.respParam(JSONObject.toJSONString(body));
            int cnt = requestLogDao.updateDouyinLogResult(builder.build());
            log.debug("[请求日志记录][{}]-Resp：{}={}={}", LogParamUtil.getTraceId(), cnt, logId, JSONObject.toJSONString(body));
        }
        return body;
    }
    // ResponseBodyAdvice end

}