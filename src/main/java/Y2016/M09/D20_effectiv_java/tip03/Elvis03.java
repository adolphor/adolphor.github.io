package Y2016.M09.D20_effectiv_java.tip03;

/**
 * Created by Bob on 2017/1/18.
 * 引用地址：
 * http://adolphor.com/blog/2016/09/20/effective-java-second-edition.html#tip03
 */
public enum Elvis03 { // 无需声明实现 Serializable 接口

  // JVM 层级保证的单例
  INSTANCE;

  private String host; // 可以包含属性
  private int port;

  public void doWork() { // 可以包含方法
    System.out.println("Do what ever...");
  }

  public void leaveTheBuilding() {
    System.out.println("leaveTheBuilding...");
  }

  // 基本上和一般的类没什么区别
  public static void main(String[] args) {
    Elvis03 elvis03 = Elvis03.INSTANCE;
    elvis03.doWork();
  }

}
