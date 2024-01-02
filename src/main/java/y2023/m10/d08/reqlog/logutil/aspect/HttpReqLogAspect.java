package y2023.m10.d08.reqlog.logutil.aspect;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import y2023.m10.d08.reqlog.logutil.LogParamUtil;
import y2023.m10.d08.reqlog.mvc.dao.RequestLogDao;
import y2023.m10.d08.reqlog.mvc.entity.RequestLog;
import y2023.m10.d08.reqlog.exception.BaseRuntimeException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class HttpReqLogAspect {

    private final RequestLogDao requestLogDao;

    @Pointcut("execution(* y2023.m10.d08.reqlog.mvc.controller..*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        LogParamUtil.setStartTime(System.currentTimeMillis());
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        if (request != null) {
            log.debug("[请求IP][{}]：{}", LogParamUtil.getTraceId(), request.getRemoteAddr());
            log.debug("[请求URI][{}]：{}", LogParamUtil.getTraceId(), request.getRequestURI());
            log.debug("[请求方式][{}]：{}", LogParamUtil.getTraceId(), request.getMethod());
            log.debug("[请求类名][{}]：{}=>{}", LogParamUtil.getTraceId(), joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            // reqParam
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, Object[]> parameterLogMap = new HashMap<>();
            BeanUtil.copyProperties(parameterMap, parameterLogMap);

            // 解析是否传递了Body参数
            ArrayList<Object> list = new ArrayList<>();
            Collection<Object[]> values = parameterLogMap.values();
            for (Object[] val : values) {
                for (Object v : val) {
                    list.add(v);
                }
            }
            Object[] args = joinPoint.getArgs();
            for (Object obj : args) {
                if (obj != null && !list.contains(obj)) {
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(obj));
                        obj = jsonObject;
                    } catch (Exception e) {
                        log.debug("[请求日志记录][{}]-非json数据：{}", LogParamUtil.getTraceId(), obj);
                    }
                    Object[] bodyArr = {obj};
                    parameterLogMap.put("body", bodyArr);
                }
            }
            String reqParams = JSONObject.toJSONString(parameterLogMap);
            log.debug("[请求参数][{}]：{}", LogParamUtil.getTraceId(), reqParams);
            // 插入数据库
            RequestLog douyinLog = RequestLog.builder()
                    .uri(request.getRequestURI())
                    .method(request.getMethod())
                    .wasSuccess(1)
                    .reqParam(reqParams)
                    .build();
            requestLogDao.insertDouyinLog(douyinLog);
            // 缓存当前日志ID
            LogParamUtil.setLogId(douyinLog.getId());
        }
    }

    @AfterThrowing(pointcut = "webLog()", throwing = "exception")
    public void doAfterThrowing(Exception exception) {
        log.debug("[请求异常耗时][{}]：{}毫秒", (System.currentTimeMillis() - LogParamUtil.getStartTime()));
        JSONObject error = new JSONObject();
        if (exception instanceof BaseRuntimeException) {
            BaseRuntimeException ex = (BaseRuntimeException) exception;
            error.put("code", ex.getCode());
            error.put("msg", ex.getMsg());
        } else {
            error.put("code", "999");
            error.put("msg", exception.getMessage());
        }
        // 记录日志
        Long logId = LogParamUtil.getLogId();
        if (logId != null) {
            RequestLog douyinLog = RequestLog.builder()
                    .id(logId)
                    .wasSuccess(0)
                    .respParam(error.toJSONString())
                    .build();
            int cnt = requestLogDao.updateDouyinLogResult(douyinLog);
            log.debug("[请求异常日志记录][{}]：{}={}={}", LogParamUtil.getTraceId(), cnt, logId, error.toJSONString());
        }
        // 清除当前线程缓存
        LogParamUtil.destroy();
    }

    @AfterReturning(pointcut = "webLog()", returning = "result")
    public void doAfterReturning(Object result) {
        // 处理完请求，返回内容
        Long startTime = LogParamUtil.getStartTime();
        long endTime = System.currentTimeMillis();
        String resultJson = JSONObject.toJSONString(result);
        log.debug("[请求耗时][{}]：{}毫秒，响应结果:{}", LogParamUtil.getTraceId(), endTime - startTime, resultJson);
        Long logId = LogParamUtil.getLogId();
        if (logId != null) {
            RequestLog douyinLog = RequestLog.builder()
                    .id(logId)
                    .respParam(resultJson)
                    .build();
            int cnt = requestLogDao.updateDouyinLogResult(douyinLog);
            log.debug("[请求日志记录][{}]：{}={}={}", LogParamUtil.getTraceId(), cnt, logId, resultJson);
            // 清除当前线程缓存
            LogParamUtil.destroy();
        }
    }
}
