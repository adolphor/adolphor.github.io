package y2023.m10.d10.skeleton.base.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import y2023.m10.d10.skeleton.base.entity.BaseEntity;
import y2023.m10.d10.skeleton.base.mapper.BaseMapper;
import y2023.m10.d10.skeleton.base.service.BaseService;
import y2023.m10.d10.skeleton.base.query.PageBaseQuery;
import y2023.m10.d10.skeleton.mybatis.page.PageInfo;

import java.util.List;

/**
 * Created by Bob on 2016/1/3.
 */
@Service("baseService")
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

  /**
   * 获取实际操作的对象方法，方便操作
   *
   * @return entity
   */
  public abstract T getEntity();

  /**
   * 获取当前操作的mapper的实例对象，方便通用方法中调用，而不会出现类型转换异常的问题。
   *
   * @return mapper
   */
  public abstract BaseMapper<T> getMapper();

  @Override
  public String insert(T entity) {
    Integer cnt = getMapper().insert(entity);
    return entity.getId();
  }

  @Override
  public Integer update(T entity) {
    return getMapper().update(entity);
  }

  @Override
  public String save(T entity) {
    if (StringUtils.isEmpty(entity.getId())) {
      getMapper().insert(entity);
    } else {
      getMapper().update(entity);
    }
    return entity.getId();
  }

  @Override
  public Integer delete(T entity) {
    return getMapper().delete(entity);
  }

  @Override
  public Integer deleteById(String id) {
    T entity = getEntity();
    entity.setId(id);
    return this.delete(entity);
  }

  @Override
  public T select(T entity) {
    return getMapper().select(entity);
  }

  @Override
  public T selectById(String id) {
    T entity = getEntity();
    entity.setId(id);
    return this.select(entity);
  }

  @Override
  public List<T> selectList(T entity) {
    return getMapper().selectList(entity);
  }

  @Override
  public <Q extends PageBaseQuery> PageInfo<T> pageData(Q query) {
    Long count = getMapper().count(query);
    query.setCount(count);
    List<T> list = getMapper().query(query);
    return new PageInfo<T>(query.getPage(),count,list);
  }

}
