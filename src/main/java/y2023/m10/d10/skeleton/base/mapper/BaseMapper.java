package y2023.m10.d10.skeleton.base.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import y2023.m10.d10.skeleton.base.entity.BaseEntity;
import y2023.m10.d10.skeleton.mybatis.template.CRUDTemplate;
import y2023.m10.d10.skeleton.base.query.PageBaseQuery;

import java.util.List;

public interface BaseMapper<T extends BaseEntity> {

  @InsertProvider(type = CRUDTemplate.class, method = "insert")
  Integer insert(T entity);

  @UpdateProvider(type = CRUDTemplate.class, method = "update")
  Integer update(T entity);

  @DeleteProvider(type = CRUDTemplate.class, method = "delete")
  Integer delete(T entity);

  @SelectProvider(type = CRUDTemplate.class, method = "select")
  T select(T entity);

  @SelectProvider(type = CRUDTemplate.class, method = "select")
  List<T> selectList(T entity);

  <V extends T> V selectVo(T entity);

  <V extends T> List<V> selectVoList(T entity);

  // 分页查询没有使用Java模板，需要在XML中进行配置，xml中配置不会有类型转换异常的问题，不需要覆写
  <Q extends PageBaseQuery> Long count(Q query);

  <Q extends PageBaseQuery> List<T> query(Q query);

}
