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
    class MyComparator implements Comparator<String> {
      @Override
      public int compare(String a, String b) {
        return a.compareTo(b);
      }
    }
    // 初始化实例
    Comparator<String> myComparator = new MyComparator();
    Collections.sort(names, myComparator);
    // 匿名方式
    Collections.sort(names, new MyComparator());


    Collections.sort(names, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return a.compareTo(b);
      }
    });

    Collections.sort(names, (String a, String b) -> {  // 带有参数类型声明 和 return 关键字
      return a.compareTo(b);
    });
    Collections.sort(names, (String a, String b) -> a.compareTo(b));  // 省略return关键字
    Collections.sort(names, (a, b) -> a.compareTo(b)); // 省略参数类型声明

    // target type
    Comparator<String> comparator1 = new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        return a.compareTo(b);
      }
    };
    Collections.sort(names, comparator1);

    Comparator<String> comparator2 = (a, b) -> a.compareTo(b);
    Collections.sort(names, comparator2);

    // scope
    String scopeTestStr = "Bob";
    Collections.sort(names, (a, b) -> {
      System.out.println(scopeTestStr);   // 可以访问非final变量
      // scopeTestStr = "change";         // 但不能进行修改
      return a.compareTo(b);
    });

  }
}
