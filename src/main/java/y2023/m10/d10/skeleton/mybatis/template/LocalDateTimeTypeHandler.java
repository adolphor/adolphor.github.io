package y2023.m10.d10.skeleton.mybatis.template;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 支持将DB中的datetime日期字段转换为Java8的LocalDateTime.
 *
 * @author Bob
 * @created 2015年7月4日 下午5:55:25
 * @since v0.1
 */
@MappedTypes(LocalDateTime.class)
public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setTimestamp(i, Timestamp.valueOf(parameter));
  }

  @Override
  public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
    Timestamp timestamp = rs.getTimestamp(columnName);
    if (timestamp == null) {
      return null;
    } else {
      return timestamp.toLocalDateTime();
    }
  }

  @Override
  public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    Timestamp timestamp = rs.getTimestamp(columnIndex);
    if (timestamp == null) {
      return null;
    } else {
      return timestamp.toLocalDateTime();
    }
  }

  @Override
  public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    Timestamp timestamp = cs.getTimestamp(columnIndex);
    if (timestamp == null) {
      return null;
    } else {
      return timestamp.toLocalDateTime();
    }
  }
}
