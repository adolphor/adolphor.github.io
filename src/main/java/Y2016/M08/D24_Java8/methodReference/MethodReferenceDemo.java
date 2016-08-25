package Y2016.M08.D24_Java8.methodReference;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bob on 2016/8/24.
 */

public class MethodReferenceDemo {
  public static void main(String[] args) {
    /**
     * 范例1
     */
    // lambda表达式：作为函数式接口实现参考上节范例
    Converter<String, Integer> converter = Integer::valueOf;  // 方法引用：调用静态方法，作为函数式接口实现
    Integer i = converter.convert("123");
    System.out.println(i);

    /**
     * 范例2
     */
    List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");
    Collections.sort(names, (a, b) -> a.compareTo(b)); // lambda表达式进行排序
    names.forEach((name) -> System.out.println(name));    // 前面讲过，lambda表达式方式进行遍历的方式
    names.forEach(System.out::println); // 方法引用：调用系统静态方法进行遍历

    /**
     * 范例3
     */
    PersonFactory<Person> factory1 = (name, age) -> new Person(); // lambda表达式
    Person person1 = factory1.create("Person1", 28);
    person1.eat();

    PersonFactory<Person> factory2 = Person::new;  // 方法引用：构造函数
    Person person2 = factory2.create("Person2", 28);
    person2.eat();

    /**
     * 范例4
     */
    Something something = new Something();
    OperateUtil<String, String> startsUtil = something::startsWith;
    OperateUtil<String, String> endsUtil = something::endsWith;
    TestOperateUtil testOperate = new TestOperateUtil();
    System.out.println("Java starts with: " + testOperate.oerateResult(startsUtil, "Java"));
    System.out.println("Java ends with: " + testOperate.oerateResult(endsUtil, "Java"));
    // 相当于此接口的实现类调用自身的方法用作函数式接口的实现，只是这里的实现类和函数式接口之间并没有继承关系，
    // 只要符合参数类型，任何函数式接口都可以作为此实例方法的引用，而函数式接口实例对此实例方法本身没有任何影响
    // 比如，此实例方法甚至可以使用 Converter 函数式接口进行接收和引用
    Converter<String, String> startsWith = something::startsWith;
    // 只不过换了一个方法名，也就是将一个实例方法抽象化为另一个接口类型的实现了
    System.out.println("Java starts with: " + startsWith.convert("Java"));

  }
}

@FunctionalInterface
interface Converter<V, T> {
  T convert(V v);
}

class Person {
  public String name;
  public int age;

  Person() {
  }

  Person(String name) {
    this.name = name;
  }

  Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public void eat() {
    System.out.println(name + "正在吃饭...");
  }

  public void run(int miles) {
    System.out.println(name + "跑了" + miles + "米");
  }

}

@FunctionalInterface
interface PersonFactory<P extends Person> {
  P create(String name, int age);
}

/**
 * 泛型化的函数式接口
 *
 * @param <T> 参数类型
 * @param <V> 返回值类型
 */
@FunctionalInterface
interface OperateUtil<T, V> {
  V operate(T t);
}

class TestOperateUtil {
  public Object oerateResult(OperateUtil operateUtil, String str) {
    return operateUtil.operate(str);
  }
}

class Something {

  String startsWith(String s) {
    return String.valueOf(s.charAt(0));
  }

  String endsWith(String s) {
    return String.valueOf(s.charAt(s.length() - 1));
  }
}
