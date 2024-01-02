package y2023.m10.d08.reqlog.logutil;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.UUID;

public class LogParamUtil {
    private final static TransmittableThreadLocal<LogParam> extraParamLocal = new TransmittableThreadLocal<>();

    private static LogParam getExtraParam() {
        if (extraParamLocal.get() == null) {
            extraParamLocal.set(new LogParam());
        }
        return extraParamLocal.get();
    }

    public static void destroy() {
        extraParamLocal.remove();
    }

    public static Long getStartTime() {
        return getExtraParam().getStartTime();
    }

    public static void setStartTime(Long startTime) {
        getExtraParam().setStartTime(startTime);
    }

    public static void setLogId(Long logId) {
        getExtraParam().setLogId(logId);
    }

    public static Long getLogId() {
        return getExtraParam().getLogId();
    }

    public static String getTraceId() {
        if (StringUtils.isEmpty(getExtraParam().getTraceId())) {
            getExtraParam().setTraceId(UUID.randomUUID().toString().replace("-",""));
        }
        return getExtraParam().getTraceId();
    }

    public static void setReqParam(Map<String, Object[]> reqParam) {
        getExtraParam().setReqParam(reqParam);
    }

    public static Map<String, Object[]> getReqParam() {
        return getExtraParam().getReqParam();
    }

}
