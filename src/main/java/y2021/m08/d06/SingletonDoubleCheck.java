package y2021.m08.d06;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/8/20 01:08
 */
public class SingletonDoubleCheck {
  /** 保存该类的唯一-实例，使用volatile关键字修饰instance */
  private static volatile SingletonDoubleCheck instance;

  /** 私有构造器使其他类无法直接通过new创建该类的实例 */
  private SingletonDoubleCheck() {
    //什么也不做
  }

  public static SingletonDoubleCheck getInstance() {
    if (null == instance) {// 操作①：第1次检查
      synchronized (SingletonDoubleCheck.class) {
        if (null == instance) {// 操作②：第2次检查
          instance = new SingletonDoubleCheck();//操作③
        }
      }
    }
    return instance;
  }
}
