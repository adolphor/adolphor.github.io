package Y2016.M08.D24_Java8.functionalInterface;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Java8 自带的函数接口范例
 * TODO 这个应该是Stream方面的内容
 * Created by Bob on 2016/8/25.
 */
public class Java8FunctionalInterfaceDemo {
  public static void main(String[] args) {

    List<Integer> listInt = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    List<String> listStr = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");

    Consumer<Integer> printConsumer = System.out::println;

    Function<Integer, Integer> f = i -> i + 2;
    listInt.stream()
            .map(f)
            .forEach(printConsumer);

    Predicate<Integer> p = i -> i > 5;
    listInt.stream()
            .filter(p)
            .forEach(printConsumer);

    boolean b = listInt.stream().allMatch(p);
    System.out.println(b);

  }

}









