package Y2016.M09.D20_effectiv_java.tip03;

import java.io.Serializable;

/**
 * Created by Bob on 2017/1/18.
 * 引用地址：
 * http://adolphor.com/blog/2016/09/20/effective-java-second-edition.html#tip03
 */
public class Elvis01 implements Serializable { // 实现Serializable，表明可以序列化

  // 公有域方式
  private transient static final Elvis01 INSTANCE = new Elvis01(); // 为了序列化，所有实例域都必须是瞬时（transient）的

  private Elvis01() { // 要想解决利用反射创建新实例的问题，需要改写构造器
    System.out.println("Executing Elvis01 constructor...");
  }

  public static Elvis01 getInstance() {
    return INSTANCE;
  }

  private Object readResolve(){ // 必须声明此函数，防止序列化的时候产生新的实例
    return INSTANCE;
  }

  public void doWork() {
    System.out.println("Do what ever...");
  }

  // 测试
  public static void main(String[] args) {
    // 正常使用情况
    Elvis01 elvis01 = Elvis01.getInstance();
    elvis01.doWork();

    // 使用反射，获取新的实例
    Elvis01 elvis02 = null;
    try {
      // TODO 好像不是这么玩的…… 上面的实例是使用getInstance方法获取的，这里是使用newInstance获取的……
      Class<?> aClass = Class.forName(Elvis01.class.getName());
      Object o = aClass.newInstance();
      elvis02 = (Elvis01) o;
      elvis02.doWork();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // 序列化实例测试
    // TODO 以后再看吧

    // 比较两个实例的内存地址：
    System.out.println(elvis01);
    System.out.println(elvis02);
    System.out.println(elvis01 == elvis02);
  }
}
