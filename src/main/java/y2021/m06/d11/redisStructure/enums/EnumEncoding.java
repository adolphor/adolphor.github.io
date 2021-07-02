package y2021.m06.d11.redisStructure.enums;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/6/11 15:42
 */
public enum EnumEncoding {

  ENCODING_RAW(0, "字符串"),
  ENCODING_INT(1, "整数"),
  ENCODING_HT(2, "哈希表"),
  ENCODING_ZIPMAP(3, "zipmap"),
  ENCODING_LINKEDLIST(4, "双端链表"),
  ENCODING_ZIPLIST(5, "压缩列表"),
  ENCODING_INTSET(6, "整数集合"),
  ENCODING_SKIPLIST(7, "跳跃表"),
  ;

  private int value;
  private String remark;

  EnumEncoding(int value, String remark) {
    this.value = value;
    this.remark = remark;
  }

  public int getValue() {
    return value;
  }

}
