package y2023.m10.d10.skeleton.base.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * 应用常量封装类
 *
 * @author Bob
 * @created 2015年7月6日 下午1:31:04
 * @since v0.1
 */
public class Constants {

  public static final String PROJECT_NAME = "skeleton";

  /**
   * 默认查询页码
   */
  public static final Integer PAGE_NUM = 1;
  /**
   * 默认每个页面查询数量
   */
  public static final Integer PAGE_SIZE = 10;

  public static final String TEMPLATE_ORDER_BY = "orderBy";

  public static final int SECOND = 1;
  public static final int MINUTE = 60 * SECOND;
  public static final int HOUR = 60 * MINUTE;
  public static final int DAY = 24 * HOUR;
  public static final int WEEK = 7 * DAY;

  public static final HashMap<String, JSONObject> logMap = new HashMap<>();

}
