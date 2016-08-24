package Y2016.M08.D24_Java8.lambdaExpression;

import java.util.ArrayList;
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

  }
}
