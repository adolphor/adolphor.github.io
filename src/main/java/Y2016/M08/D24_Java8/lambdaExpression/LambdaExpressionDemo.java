package Y2016.M08.D24_Java8.lambdaExpression;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Bob on 2016/8/24.
 */
public class LambdaExpressionDemo {
  public static void main(String[] args) {

    List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");

    /**
     * 原始实现方式
     */
    Collections.sort(names, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return b.compareTo(a);
      }
    });

    /**
     * lambda表达式方式，以下三种方式是等价的
     */
    Collections.sort(names, (String a, String b) -> {  // 带有参数类型声明 和 return 关键字
      return b.compareTo(a);
    });
    Collections.sort(names, (String a, String b) -> b.compareTo(a));  // 省略return关键字
    Collections.sort(names, (a, b) -> b.compareTo(a)); // 省略参数类型声明

    /**
     * 使用lambda表达式进行遍历
     */
    names.forEach(name -> System.out.println(name));


    /**
     * 范例：没有返回值
     */
    GreetingService greetService2 = (message) -> System.out.println("Hello " + message);    // 带有圆括号
    GreetingService greetService1 = message -> System.out.println("Hello " + message);    // 省略圆括号

    greetService1.sayMessage("Mahesh");
    greetService2.sayMessage("Suresh");

    /**
     * 范例：有返回值
     */
    Converter<String, Integer> converter = source -> Integer.valueOf(source); // 只有一个参数，省略圆括号
    Integer integer = converter.convert("123");
    System.out.println(integer);

  }
}

@FunctionalInterface
interface GreetingService {
  void sayMessage(String message);
}

@FunctionalInterface
interface Converter<V, T> {
  T convert(V v);
}


