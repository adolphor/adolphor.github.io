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
    Converter<String, Integer> converter = Integer::valueOf;  // 方法引用静态方法，作为函数式接口实现
    Integer integer = converter.convert("123");
    System.out.println(integer);

    /**
     * 范例2
     */
    List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");
    Collections.sort(names, (a, b) -> a.compareTo(b)); // 排序
    names.forEach((name) -> System.out.println(name));    // lambda表达式方式
    names.forEach(System.out::println); // 方法引用系统静态方法

    /**
     * 范例3
     */
    PersonFactory<Person> factory = Person::new;  // 方法引用：构造函数
    Person adolphor = factory.create("adolphor", 28);

    adolphor.eat();

    Runnable eat = adolphor::eat;
    eat.run();

    Something something = new Something();
    Converter<String, String> startsWith = something::startsWith;
    String java = startsWith.convert("Java");
    System.out.println(java);    // "J"
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

class Something {
  String startsWith(String s) {
    return String.valueOf(s.charAt(0));
  }
}
