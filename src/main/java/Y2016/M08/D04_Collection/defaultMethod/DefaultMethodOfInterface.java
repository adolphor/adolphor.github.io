package Y2016.M08.D04_Collection.defaultMethod;

/**
 * Created by Bob on 2016/8/8.
 */
public interface DefaultMethodOfInterface {
  void method1();
  default void method2() {
    System.out.println("I'm method2 from interface.");
  }
}
