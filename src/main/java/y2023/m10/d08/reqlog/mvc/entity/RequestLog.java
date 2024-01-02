package y2023.m10.d08.reqlog.mvc.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    private Long id;
    private String uri;
    private String method;
    private String reqParam;
    private String respParam;
    private String asyncResult;
    private Integer wasSuccess;
    private Date createTime;
    private Date updateTime;


}
