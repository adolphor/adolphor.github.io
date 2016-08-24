package Y2016.M08.D24_Java8.functionalInterface;

/**
 * Created by Bob on 2016/1/22.
 */
@FunctionalInterface
interface MathOperation {

  // 只能含有一个抽象方法
  int operation(int a, int b);

  // 但是可以含有默认的实现方法
  default void other(int num) {
    System.out.println("num is: " + num);
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

public class LambdaExpressionDemo {

  public static void main(String args[]) {

    /**
     * 范例1：有返回值
     */
    Converter<String, Integer> converter = source -> Integer.valueOf(source); // 只有一个参数，省略圆括号
    Integer integer = converter.convert("123");
    System.out.println(integer);

    /**
     * 范例2：没有返回值
     */
    GreetingService greetService2 = (message) -> System.out.println("Hello " + message);    // 带有圆括号
    GreetingService greetService1 = message -> System.out.println("Hello " + message);    // 省略圆括号

    greetService1.sayMessage("Mahesh");
    greetService2.sayMessage("Suresh");

    /**
     * 范例3：（这个应该是服务注册的实现吧）
     */
    // 分别实现如下4个函数式接口：
    MathOperation addition = (a, b) -> a + b;
    MathOperation subtraction = (a, b) -> a - b;
    MathOperation multiplication = (a, b) -> a * b;
    MathOperation division = (a, b) -> a / b;

    // 3.1、调用范例
    System.out.println("10 + 5 = " + addition.operation(10, 5));
    System.out.println("10 - 5 = " + subtraction.operation(10, 5));
    System.out.println("10 x 5 = " + multiplication.operation(10, 5));
    System.out.println("10 / 5 = " + division.operation(10, 5));

    // 3.2、像实现类实例一样，函数式接口实现也可以作为参数传递，之后再进行调用
    System.out.println("10 + 5 = " + LambdaExpressionDemo.execute(10, 5, addition));
    System.out.println("10 - 5 = " + LambdaExpressionDemo.execute(10, 5, subtraction));
    System.out.println("10 x 5 = " + LambdaExpressionDemo.execute(10, 5, multiplication));
    System.out.println("10 / 5 = " + LambdaExpressionDemo.execute(10, 5, division));

    // 3.3、也可以使用匿名实现的方式
    System.out.println("10 + 5 = " + LambdaExpressionDemo.execute(10, 5, (a, b) -> a + b));
    System.out.println("10 - 5 = " + LambdaExpressionDemo.execute(10, 5, (a, b) -> a - b));
    System.out.println("10 x 5 = " + LambdaExpressionDemo.execute(10, 5, (a, b) -> a * b));
    System.out.println("10 / 5 = " + LambdaExpressionDemo.execute(10, 5, (a, b) -> a / b));
  }

  public static int execute(int a, int b, MathOperation mathOperation) {
    return mathOperation.operation(a, b);
  }

}