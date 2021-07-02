package y2021.m06.d11.redisStructure.domain;

import lombok.Data;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/6/11 15:43
 */
@Data
public class TypeSds {
  /** buf 数组中已使用字节的数量，等于 SDS 所保存字符串的长度 */
  private int len;
  /** buf 数组中未使用字节的数量 */
  private int free;
  /** 字节数组，用于保存字符串 */
  private char[] buf;

  /**
   * 在存储之前就需要计算字符串长度的，那这个计算长度的方法是使用的C语言计算的方法吗？
   * 如果是，那么是不是也是有性能问题？(C语言计算字符串程度需要遍历整个字符串)
   **/


  /**
   * 空间预分配
   * 通过空间预分配策略， Redis 可以减少连续执行字符串增长操作所需的内存重分配次数。
   * <p>
   * 1. 如果对 SDS 进行修改之后， SDS 的长度（也即是 len 属性的值）将小于 1 MB ，
   * 那么程序分配和 len 属性同样大小的未使用空间， 这时 SDS len 属性的值将和 free
   * 属性的值相同。 举个例子， 如果进行修改之后， SDS 的 len 将变成 13 字节， 那么
   * 程序也会分配13 字节的未使用空间， SDS 的 buf 数组的实际长度将变成
   * 13 + 13 + 1 = 27 字节（额外的一字节用于保存空字符）。
   * <p>
   * 2. 如果对 SDS 进行修改之后， SDS 的长度将大于等于 1 MB ， 那么程序会分配 1 MB
   * 的未使用空间。 举个例子， 如果进行修改之后， SDS 的 len 将变成 30 MB ， 那么程
   * 序会分配 1 MB 的未使用空间， SDS 的 buf 数组的实际长度将为 30 MB + 1 MB + 1
   * byte 。
   */
  private void beforeAdd() {
  }

  /**
   * 惰性空间释放（使用空间减少，不立即回收多出来的空间）
   * <p>
   * 惰性空间释放用于优化 SDS 的字符串缩短操作： 当 SDS 的 API 需要缩短 SDS 保存的字
   * 符串时， 程序并不立即使用内存重分配来回收缩短后多出来的字节， 而是使用 free 属性将
   * 这些字节的数量记录起来， 并等待将来使用。
   */
  private void afterReduce() {

  }

  /**
   *
   */
  private void resize() {

  }

}
