package y2023.m10.d08.reqlog.mvc.dao;

import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import y2023.m10.d08.reqlog.mvc.entity.RequestLog;

@Repository
public interface RequestLogDao extends Mapper<RequestLog> {

    int insertDouyinLog(RequestLog requestLog);

    int updateDouyinLogResult(RequestLog requestLog);

}
