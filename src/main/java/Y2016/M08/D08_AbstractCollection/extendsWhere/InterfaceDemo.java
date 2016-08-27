package Y2016.M08.D08_AbstractCollection.extendsWhere;

/**
 * Created by Bob on 2016/8/27.
 */
public interface InterfaceDemo {

  void method();

  default void printDemo() {
    System.out.println("I'm from InterfaceDemo.");
  }
}
