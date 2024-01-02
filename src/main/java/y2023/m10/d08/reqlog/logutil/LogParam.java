package y2023.m10.d08.reqlog.logutil;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class LogParam implements Serializable {

    // 修改(异步)调用后的返回结果
    private Long logId;
    // 串联打印的日志信息
    private String traceId;
    // 用于记录完整请求时间
    private Long startTime = 0L;
    // 用户扩展记录POST请求方式下的Body数据
    private Map<String, Object[]> reqParam;

}
