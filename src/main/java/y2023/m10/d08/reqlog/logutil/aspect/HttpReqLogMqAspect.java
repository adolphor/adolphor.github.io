package y2023.m10.d08.reqlog.logutil.aspect;

import com.alibaba.fastjson.JSONObject;
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
import y2023.m10.d08.reqlog.logutil.LogParamUtil;
import y2023.m10.d08.reqlog.mvc.dao.RequestLogDao;
import y2023.m10.d08.reqlog.mvc.entity.RequestLog;
import y2023.m10.d08.reqlog.exception.BaseRuntimeException;

@Component
@Aspect
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class HttpReqLogMqAspect {

    private final RequestLogDao requestLogDao;

    @Pointcut("execution(* y2023.m10.d08.reqlog.mvc.service..*(..))")
    public void mqLog() {
    }

    @Before("mqLog()")
    public void doBefore(JoinPoint joinPoint) {
        LogParamUtil.setStartTime(System.currentTimeMillis());
        log.debug("[MQ请求参数][{}]：{}", LogParamUtil.getTraceId(), joinPoint.getArgs());
        log.debug("[MQ请求类名][{}]：{}=>{}", LogParamUtil.getTraceId(), joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "mqLog()", throwing = "exception")
    public void doAfterThrowing(Exception exception) {
        log.debug("[MQ处理异常耗时][{}]：{}毫秒", LogParamUtil.getTraceId(), (System.currentTimeMillis() - LogParamUtil.getStartTime()));
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
                    .asyncResult(error.toJSONString())
                    .build();
            int cnt = requestLogDao.updateDouyinLogResult(douyinLog);
            log.debug("[MQ处理异常日志记录][{}]：{}={}={}", LogParamUtil.getTraceId(), cnt, logId, error.toJSONString());
            // 清除当前线程缓存
            LogParamUtil.destroy();
        }
    }

    @AfterReturning(pointcut = "mqLog()", returning = "result")
    public void doAfterReturning(Object result) {
        // 处理完请求，返回内容
        Long startTime = LogParamUtil.getStartTime();
        long endTime = System.currentTimeMillis();
        String resultJson = JSONObject.toJSONString(result);
        log.debug("[MQ处理耗时][{}]：{}毫秒，响应结果:{}", LogParamUtil.getTraceId(), endTime - startTime, resultJson);
        Long logId = LogParamUtil.getLogId();
        if (logId != null) {
            RequestLog douyinLog = RequestLog.builder()
                    .id(logId)
                    .asyncResult(resultJson)
                    .build();
            int cnt = requestLogDao.updateDouyinLogResult(douyinLog);
            log.debug("[MQ处理异常日志记录][{}]：{}={}={}", LogParamUtil.getTraceId(), cnt, logId, resultJson);
            // 清除当前线程缓存
            LogParamUtil.destroy();
        }
    }
}
