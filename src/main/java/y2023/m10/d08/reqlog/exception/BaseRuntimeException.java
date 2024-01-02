package y2023.m10.d08.reqlog.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/8/11 09:56
 */
@Setter
@Getter
public class BaseRuntimeException extends RuntimeException {

    /** 异常编码 */
    private String code;
    /** 异常提醒消息 */
    private String msg;
    /** 异常详情描述 */
    private String detail;
    /** 异常信息栈 */
    private Throwable cause;

    public BaseRuntimeException(BaseEnumError error) {
        this(error, null);
    }

    public BaseRuntimeException(BaseEnumError error, Throwable cause) {
        super(error.getDetail(), cause);
        this.code = error.getCode();
        this.msg = error.getMsg();
    }

}
