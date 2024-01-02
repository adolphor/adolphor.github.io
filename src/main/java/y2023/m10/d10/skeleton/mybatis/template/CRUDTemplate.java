package y2023.m10.d10.skeleton.mybatis.template;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class CRUDTemplate {

  public String insert(Object obj) throws Exception {
    EntityUtil.perpareTableEntity(obj);

    SQL sql = new SQL();
    sql.INSERT_INTO(EntityUtil.getTableName(obj));
    sql.VALUES(EntityUtil.getInsertColumnsName(obj), EntityUtil.getInsertColumnsDefine(obj));
    return sql.toString();
  }

  public String update(Object obj) throws Exception {
    EntityUtil.perpareTableEntity(obj);
    String idName = EntityUtil.getPrimaryKey(obj);

    SQL sql = new SQL();
    sql.UPDATE(EntityUtil.getTableName(obj));
    sql.SET(EntityUtil.getSetDefine(obj));
    sql.WHERE(idName + "=#{" + idName + "}");
    return sql.toString();
  }

  public String select(Object obj) throws Exception {
    EntityUtil.perpareTableEntity(obj);

    SQL sql = new SQL();
    sql.SELECT(EntityUtil.getSelectColumnStr(obj));
    sql.FROM(EntityUtil.getTableName(obj));
    String whereDefine = EntityUtil.getWhereDefine(obj);
    if (StringUtils.isNotEmpty(whereDefine)) {
      sql.WHERE(whereDefine);
    }
    String orderBy = EntityUtil.getOrderBy(obj);
    if (StringUtils.isNotEmpty(orderBy)) {
      sql.ORDER_BY(orderBy);
    }
    return sql.toString();
  }

//分页相关的查询一般情况下查询条件都不是相等处理，故暂时不做封装
//    public String count(Object obj) throws Exception {
//        EntityUtil.perpareTableEntity(obj);
//
//        BEGIN();
//        SELECT("count(*)");
//        FROM(EntityUtil.getTableName(obj));
//        String whereDefine = EntityUtil.getWhereDefine(obj);
//        if (StringUtils.isNotEmpty(whereDefine)) {
//            WHERE(whereDefine);
//        }
//        return SQL();
//    }
//
//    public String query(Object obj){
//        return null;
//    }

  public String delete(Object obj) throws Exception {
    EntityUtil.perpareTableEntity(obj);
    String idname = EntityUtil.getPrimaryKey(obj);

    SQL sql = new SQL();
    sql.DELETE_FROM(EntityUtil.getTableName(obj));
    sql.WHERE(idname + "=#{" + idname + "}");
    return sql.toString();
  }

}
