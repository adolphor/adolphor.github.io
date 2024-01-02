package y2023.m10.d10.skeleton.base.service;

import y2023.m10.d10.skeleton.base.entity.BaseEntity;
import y2023.m10.d10.skeleton.base.query.PageBaseQuery;
import y2023.m10.d10.skeleton.mybatis.page.PageInfo;

import java.util.List;

/**
 * Created by Bob on 2015/12/30.
 */
public interface BaseService<T extends BaseEntity> {

  String insert(T entity);

  Integer update(T entity);

  String save(T entity);

  Integer delete(T entity);

  Integer deleteById(String id);

  T select(T entity);

  T selectById(String id);

  List<T> selectList(T entity);

  <Q extends PageBaseQuery> PageInfo<T> pageData(Q query);

}
