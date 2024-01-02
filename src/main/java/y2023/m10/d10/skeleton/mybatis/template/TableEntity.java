package y2023.m10.d10.skeleton.mybatis.template;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by Bob on 2016/1/1.
 */
public class TableEntity {
  // 数据库名称
  private String tableName;
  // 主键字段
  private String primaryKey;
  // 有效属性字段拼接
  private String selectColumnStr;
  // 属性和数据库字段映射Map
  private Map<String, String> columnMap;
  // 有效Field字段
  private List<Field> fieldList;
  // 有效PropertyDescriptor
  private List<PropertyDescriptor> descriptorList;

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getPrimaryKey() {
    return primaryKey;
  }

  public void setPrimaryKey(String primaryKey) {
    this.primaryKey = primaryKey;
  }

  public String getSelectColumnStr() {
    return selectColumnStr;
  }

  public void setSelectColumnStr(String selectColumnStr) {
    this.selectColumnStr = selectColumnStr;
  }

  public Map<String, String> getColumnMap() {
    return columnMap;
  }

  public void setColumnMap(Map<String, String> columnMap) {
    this.columnMap = columnMap;
  }

  public List<Field> getFieldList() {
    return fieldList;
  }

  public void setFieldList(List<Field> fieldList) {
    this.fieldList = fieldList;
  }

  public List<PropertyDescriptor> getDescriptorList() {
    return descriptorList;
  }

  public void setDescriptorList(List<PropertyDescriptor> descriptorList) {
    this.descriptorList = descriptorList;
  }
}
