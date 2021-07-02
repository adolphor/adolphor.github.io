package y2021.m06.d11.redisStructure.domain;

import lombok.Data;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/6/11 15:42
 */
@Data
public class AbstractRedisObject {
  /** 类型 */
  private int type = 4;
  /** 编码方式 */
  private int encoding = 4;
  /** 指向对象的值 */
  private AbstractRedisObject ptr;
  /** 对齐位 */
  private int notUsed = 2;
}
