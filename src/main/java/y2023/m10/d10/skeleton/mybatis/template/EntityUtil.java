package y2023.m10.d10.skeleton.mybatis.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import y2023.m10.d10.skeleton.base.entity.Constants;
import y2023.m10.d10.skeleton.util.StringUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析POJO的类
 *
 * @author henliqi
 */
public class EntityUtil {

  public static final Logger logger = LoggerFactory.getLogger(EntityUtil.class);

  private static Map<Class, TableEntity> tableMap = new HashMap<>();

  public static void perpareTableEntity(Object obj) {
    if (null == obj) {
      logger.error("To get tableName, POJO instance can not be null ! ");
      return;
    }
    if (tableMap.containsKey(obj.getClass())) {
      return;
    }
    TableEntity tableEntity = new TableEntity();
    // 解析表名称
    tableEntity.setTableName(prepareTableName(obj));
    // 解析出所有有效的field
    tableEntity.setFieldList(prepareFields(obj.getClass(), null));
    // 根据有效Field，拼装全量字段字符串
    prepareFieldInfo(tableEntity);
    // 放入缓存
    tableMap.put(obj.getClass(), tableEntity);
  }

  /**
   * @param tableEntity
   * @Descreption 根据实体的有效Field，整理字段
   * @Author Bob
   * @Date 2016-01-01 16:01:23
   */
  private static void prepareFieldInfo(TableEntity tableEntity) {
    List<Field> fields = tableEntity.getFieldList();
    HashMap<String, String> columnMap = new HashMap<>();
    StringBuffer selectColumnStr = new StringBuffer();
    for (Field field : fields) {
      // 实体字段
      String propertyName = field.getName();
      // 数据库字段
      Column colAnno = field.getAnnotation(Column.class);
      String columnName = "";
      if (null != colAnno) {
        columnName = colAnno.name();
      } else {
        columnName = camelCase2UnderScore(propertyName);
      }
      // 查询语句拼接
      selectColumnStr.append(columnName).append(" as ").append(propertyName).append(",");

      columnMap.put(propertyName, columnName);
      // 主键字段
      Id idColumn = field.getAnnotation(Id.class);
      if (null != idColumn) {
        tableEntity.setPrimaryKey(field.getName());
      }
    }
    tableEntity.setSelectColumnStr(StringUtil.subLastComma(selectColumnStr));
    tableEntity.setColumnMap(columnMap);
  }


  /**
   * @param entityClass POJO实例class
   * @return map
   * key1 -> fieldList
   * key2 -> keyList
   * @Descreption 获取POJO中有效的Field字段名称（没有使用@Transient注解的字段）
   * @Author Bob
   * @Date 2016-01-01 14:20:00
   */
  private static List<Field> prepareFields(Class<?> entityClass, List<Field> fieldList) {
    if (null == fieldList) {
      fieldList = new ArrayList<>();
    }
    if (entityClass.equals(Object.class)) {
      return fieldList;
    }
    for (Field field : entityClass.getDeclaredFields()) {
      // 如果没有使用@Transient注解，且不是静态方法，则默认增加
      if (null == field.getAnnotation(Transient.class)) {
        fieldList.add(field);
      }
    }
    Class<?> superClass = entityClass.getSuperclass();
    if (superClass != null && !superClass.equals(Object.class)) {
      // 递归调用，获取父类Field，将父类的Field放在子类Field前面
      prepareFields(superClass, fieldList);
    }
    return fieldList;
  }


  /**
   * @param obj POJO实例
   * @Descreption 获取POJO对应的数据库表名
   * 1. 如果 POJO中定义了@Table(name)，获取定义的名称
   * 2. 如果没有指定，则默认按照驼峰类名转换 为 下划线表名
   * @Author Bob
   * @Date 2015-12-31 12:57:00
   */
  public static String prepareTableName(Object obj) {
    String tableName;
    Class<?> clazz = obj.getClass();
    Table table = clazz.getAnnotation(Table.class);
    if (table != null) {
      tableName = table.name();
    } else {
      String pojoName = clazz.getSimpleName();
      tableName = camelCase2UnderScore(pojoName);
    }
    logger.info(tableName);
    return tableName;
  }

  //=======================================静态信息获取===============================================

  public static String getTableName(Object obj) {
    TableEntity tableEntity = tableMap.get(obj.getClass());
    return tableEntity.getTableName();
  }

  public static String getPrimaryKey(Object obj) {
    TableEntity tableEntity = tableMap.get(obj.getClass());
    return tableEntity.getPrimaryKey();
  }

  public static String getSelectColumnStr(Object obj) {
    TableEntity tableEntity = tableMap.get(obj.getClass());
    return tableEntity.getSelectColumnStr();
  }

  //========================================动态信息获取==========================================

  /**
   * @param obj POJO实例
   * @Descreption 用于获取Insert的字段累加
   * @Author Bob
   * @Date 2015-12-31 14:28:00
   */
  public static String getInsertColumnsName(Object obj) {
    StringBuffer sb = new StringBuffer();
    TableEntity tableEntity = tableMap.get(obj.getClass());
    Map<String, String> columnMap = tableEntity.getColumnMap();
    List<PropertyDescriptor> propertyDescriptorList = getDescriptorList(obj);
    for (PropertyDescriptor p : propertyDescriptorList) {
      sb.append(columnMap.get(p.getName())).append(",");
    }
    logger.info(sb.toString());
    return StringUtil.subLastComma(sb);
  }


  /**
   * @param obj POJO实例
   * @Descreption 用于获取Insert的字段累加
   * @Author Bob
   * @Date 2015-12-31 14:28:00
   */
  public static String getInsertColumnsDefine(Object obj) {
    StringBuffer sb = new StringBuffer();
    List<PropertyDescriptor> propertyDescriptorList = getDescriptorList(obj);
    for (PropertyDescriptor p : propertyDescriptorList) {
      String column = p.getName();
      sb.append("#{").append(column).append("},");
    }
    logger.info(sb.toString());
    return StringUtil.subLastComma(sb);
  }


  /**
   * 用于获取where的条件累加
   *
   * @return
   */
  public static String getWhereDefine(Object obj) {
    StringBuffer whereDefine = new StringBuffer();
    TableEntity tableEntity = tableMap.get(obj.getClass());
    Map<String, String> columnMap = tableEntity.getColumnMap();
    List<PropertyDescriptor> propertyDescriptorList = getDescriptorList(obj);
    int i = 0;
    for (PropertyDescriptor p : propertyDescriptorList) {
      String propertyName = p.getName();
      String columnName = columnMap.get(propertyName);
      if (null != columnName) {
        if (i++ != 0) {
          whereDefine.append(" and ");
        }
        whereDefine.append(columnName);
        whereDefine.append("=#{").append(propertyName).append("}");
      }
    }
    return whereDefine.toString();
  }

  /**
   * 用于获取排序字段值
   *
   * @return
   */
  public static String getOrderBy(Object obj) {
    BeanInfo intro = null;
    try {
      intro = Introspector.getBeanInfo(obj.getClass());
    } catch (IntrospectionException e) {
      logger.error("Parse propertyDescriptors error: ", e);
    }
    PropertyDescriptor[] propertyDescriptors = intro.getPropertyDescriptors();
    for (PropertyDescriptor p : propertyDescriptors) {
      if (Constants.TEMPLATE_ORDER_BY.equals(p.getName())) {
        try {
          Object invoke = p.getReadMethod().invoke(obj);
          if (null != invoke) {
            return invoke.toString();
          } else {
            return null;// 主要作用是为了跳出循环
          }
        } catch (Exception e) {
          logger.error("Parse order by", e);
        }
      }
    }
    return null;
  }

  public static String getSetDefine(Object obj) {
    StringBuffer sb = new StringBuffer();
    TableEntity tableEntity = tableMap.get(obj.getClass());
    Map<String, String> columnMap = tableEntity.getColumnMap();
    List<PropertyDescriptor> propertyDescriptorList = getDescriptorList(obj);

    for (PropertyDescriptor p : propertyDescriptorList) {
      String column = p.getName();
      if (isNull(obj, p) || column.equals(getPrimaryKey(obj))) {
        continue;
      }
      sb.append(columnMap.get(p.getName()));
      sb.append("=#{").append(column).append("},");
    }
    logger.info(sb.toString());
    return StringUtil.subLastComma(sb);
  }

  //========================================工具类==========================================

  private static List<PropertyDescriptor> getDescriptorList(Object obj) {
    List<PropertyDescriptor> columnList = new ArrayList<>();

    TableEntity tableEntity = tableMap.get(obj.getClass());
    if (null == tableEntity.getColumnMap() || tableEntity.getColumnMap().size() == 0) {
      return columnList;
    }

    BeanInfo intro = null;
    try {
      intro = Introspector.getBeanInfo(obj.getClass());
    } catch (IntrospectionException e) {
      logger.error("Parse propertyDescriptors error: ", e);
    }
    PropertyDescriptor[] propertyDescriptors = intro.getPropertyDescriptors();
    Map<String, String> columnMap = tableEntity.getColumnMap();
    for (PropertyDescriptor p : propertyDescriptors) {
      if (null != columnMap.get(p.getName()) && isNotNull(obj, p)) {
        columnList.add(p);
      }
    }
    return columnList;
  }

  private static boolean isNull(Object obj, PropertyDescriptor propertyDescriptor) {
    try {
      return null == propertyDescriptor.getReadMethod().invoke(obj);
    } catch (IllegalArgumentException e) {
      logger.error(e.getMessage(), e);
    } catch (IllegalAccessException e) {
      logger.error(e.getMessage(), e);
    } catch (InvocationTargetException e) {
      logger.error(e.getMessage(), e);
    }
    return true;
  }

  private static boolean isNotNull(Object obj, PropertyDescriptor propertyDescriptor) {
    return !isNull(obj, propertyDescriptor);
  }


  /**
   * @param name 待转化字符串
   * @description 下划线格式字符串转换成驼峰格式字符串
   * eg: player_id -> playerId;
   * player_name -> playerName;
   * @author Bob
   * @Date 2015-12-31 12:57:00
   */
  public static String underScore2CamelCase(String name) {
    if (null == name || name.equals("")) {
      return "";
    }
    String[] elems = name.split("_");
    for (int i = 0; i < elems.length; i++) {
      elems[i] = elems[i].toLowerCase();
      if (i != 0) {
        String elem = elems[i];
        char first = elem.toCharArray()[0];
        elems[i] = "" + (char) (first - 32) + elem.substring(1);
      }
    }
    for (String e : elems) {
      System.out.print(e);
    }
    return elems.toString();
  }

  /**
   * @param name 待转换字符串
   * @description 驼峰格式字符串 转换成 下划线格式字符串
   * eg: playerId -> player_id;
   * playerName -> player_name;
   * @author Bob
   * @Date 2015-12-31 12:57:00
   */
  public static String camelCase2UnderScore(String name) {
    if (null == name || name.equals("")) {
      return "";
    }
    Pattern p = Pattern.compile("[A-Z]");
    StringBuilder builder = new StringBuilder(name);
    Matcher mc = p.matcher(name);
    int i = 0;
    while (mc.find()) {
      builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
      i++;
    }
    if ('_' == builder.charAt(0)) {
      builder.deleteCharAt(0);
    }
    return builder.toString();
  }

}
