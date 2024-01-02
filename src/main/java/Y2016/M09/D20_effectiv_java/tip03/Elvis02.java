package Y2016.M09.D20_effectiv_java.tip03;

import java.io.Serializable;

/**
 * Created by Bob on 2017/1/18.
 * 引用地址：
 * http://adolphor.com/blog/2016/09/20/effective-java-second-edition.html#tip03
 */
public class Elvis02 implements Serializable { // 实现Serializable，表明可以序列化

  /**
   * 类级内部类 也就是静态的成员式内部类 该内部类的实例与外部类的实例没有依赖
   * 而且只有被调用的时候才会被装载，从而实现延迟加载
   * 参考：
   * http://blog.csdn.net/card361401376/article/details/51340822
   */
  private static class SingletonHolder {
    //静态初始化器 由虚拟机保证线程安全
    private static Elvis02 INSTANCE = new Elvis02();
  }

  private Elvis02() {
    System.out.println("Executing Elvis02 constructor...");
  }

  public static Elvis02 getInstance() {
    return SingletonHolder.INSTANCE;
  }

  private Object readResolve() { // 必须声明此函数，防止序列化的时候产生新的实例
    return SingletonHolder.INSTANCE;
  }

  public void leaveTheBuilding() {
    System.out.println("leaveTheBuilding...");
  }

  public void doWork() {
    System.out.println("Do what ever...");
  }

}
