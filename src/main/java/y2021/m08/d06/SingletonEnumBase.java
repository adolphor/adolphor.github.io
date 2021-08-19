package y2021.m08.d06;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/8/20 01:22
 */
public class SingletonEnumBase {
  public static void main(String[] args) {
    Singleton.INSTANCE.doSomething();
  }

  public enum Singleton {
    INSTANCE;

    Singleton() {
    }

    public void doSomething() {
    }
  }

}
