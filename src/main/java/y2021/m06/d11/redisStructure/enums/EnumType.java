package y2021.m06.d11.redisStructure.enums;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/6/11 15:42
 */
public enum EnumType {

  REDIS_STRING(0, "字符串"),
  REDIS_LIST(1, "列表"),
  REDIS_SET(2, "集合"),
  REDIS_ZSET(3, "有序集"),
  REDIS_HASH(4, "哈希表");

  private int value;
  private String remark;

  EnumType(int value, String remark) {
    this.value = value;
    this.remark = remark;
  }

  public int getValue() {
    return value;
  }

}
