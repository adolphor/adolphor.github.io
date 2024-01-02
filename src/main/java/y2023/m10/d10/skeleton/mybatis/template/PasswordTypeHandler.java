package y2023.m10.d10.skeleton.mybatis.template;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import y2023.m10.d10.skeleton.util.EncryptionUtil;

import javax.crypto.NoSuchPaddingException;
import java.security.GeneralSecurityException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordTypeHandler extends BaseTypeHandler<String> {

  private static final Logger LOG = LoggerFactory.getLogger(PasswordTypeHandler.class);

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
      throws SQLException {
    try {
      ps.setBytes(i, EncryptionUtil.encrypt(parameter));
    } catch (NoSuchPaddingException e) {
      LOG.error("密码加密出错：", e);
      throw new SQLException(e);
    } catch (GeneralSecurityException e) {
      LOG.error("密码加密出错：", e);
      throw new SQLException(e);
    }
  }

  @Override
  public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String pswd = null;
    try {
      pswd = EncryptionUtil.decrypt(rs.getBytes(columnName));
    } catch (NoSuchPaddingException e) {
      LOG.error("密码解密出错：", e);
      throw new SQLException(e);
    } catch (GeneralSecurityException e) {
      LOG.error("密码解密出错：", e);
      throw new SQLException(e);
    }
    return pswd;
  }

  @Override
  public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String pswd = null;
    try {
      pswd = EncryptionUtil.decrypt(rs.getBytes(columnIndex));
    } catch (NoSuchPaddingException e) {
      LOG.error("密码解密出错：", e);
      throw new SQLException(e);
    } catch (GeneralSecurityException e) {
      LOG.error("密码解密出错：", e);
      throw new SQLException(e);
    }
    return pswd;
  }

  @Override
  public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String pswd = null;
    try {
      pswd = EncryptionUtil.decrypt(cs.getBytes(columnIndex));
    } catch (NoSuchPaddingException e) {
      LOG.error("密码解密出错：", e);
      throw new SQLException(e);
    } catch (GeneralSecurityException e) {
      LOG.error("密码解密出错：", e);
      throw new SQLException(e);
    }
    return pswd;
  }

}
