package y2021.m08.d06;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/8/20 01:17
 */
public class SingletonStaticHolder {

  /** 私有构造器 */
  private SingletonStaticHolder() {
  }

  private static class InstanceHolder {
    /** 保存外部类的唯一实例 */
    final static SingletonStaticHolder INSTANCE = new SingletonStaticHolder();
  }

  public static SingletonStaticHolder getInstance() {
    return InstanceHolder.INSTANCE;
  }

}
