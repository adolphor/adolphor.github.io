package Y2016.M08.D24_Java8.methodReference;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Created by Bob on 2016/8/24.
 */

public class MethodReferenceDemo {
  public static void main(String[] args) {
    /**
     * 范例1
     */
    // lambda表达式：作为函数式接口实现参考上节范例
    Function<String, Integer> converter1 = num -> Integer.valueOf(num);  // lambda表达式方式
    Integer i1 = converter1.apply("123");
    System.out.println(i1);
    Function<String, Integer> converter2 = Integer::valueOf;  // 方法引用：调用静态方法，作为函数式接口实现
    Integer i2 = converter2.apply("123");
    System.out.println(i2);

    /**
     * 范例2
     */
    List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");
    Collections.sort(names, (a, b) -> a.compareTo(b)); // lambda表达式进行排序
    names.forEach((name) -> System.out.println(name));    // lambda表达式方式进行遍历的方式
    names.forEach(System.out::println); // 方法引用：调用系统静态方法进行遍历

    /**
     * 范例3
     */
    BiFunction<String, Integer, Person> biFunction1 = (name, age) -> new Person(name, age); // lambda表达式
    Person person1 = biFunction1.apply("Person1", 28);
    person1.eat();

    BiFunction<String, Integer, Person> biFunction2 = Person::new;  // 方法引用：构造函数
    Person person2 = biFunction2.apply("Person2", 28);
    person2.run(100);

    /**
     * 范例4
     */
    Something something = new Something();
    UnaryOperator<String> starts = something::startsWith;
    UnaryOperator<String> ends = something::endsWith;
    TestFunction testFun = new TestFunction();
    System.out.println("Java starts with: " + testFun.funResult(starts, "Java"));
    System.out.println("Java ends with: " + testFun.funResult(ends, "Java"));

  }
}

class Person {
  public String name;
  public int age;

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

class TestFunction {
  public Object funResult(Function function, String str) {
    return function.apply(str);
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
