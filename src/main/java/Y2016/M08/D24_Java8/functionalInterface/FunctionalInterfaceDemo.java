package Y2016.M08.D24_Java8.functionalInterface;

/**
 * Created by Bob on 2016/1/22.
 */
public class FunctionalInterfaceDemo {

  public static void main(String args[]) {

    /**
     * 范例：
     */
    // 分别实现如下4个函数式接口：
    MathOperation addition = (a, b) -> a + b;
    MathOperation subtraction = (a, b) -> a - b;
    MathOperation multiplication = (a, b) -> a * b;
    MathOperation division = (a, b) -> a / b;

    // 1、调用范例
    System.out.println("10 + 5 = " + addition.operate(10, 5));
    System.out.println("10 - 5 = " + subtraction.operate(10, 5));
    System.out.println("10 x 5 = " + multiplication.operate(10, 5));
    System.out.println("10 / 5 = " + division.operate(10, 5));

    TestMathOperation testOperation = new TestMathOperation();
    // 2、像实现类实例一样，函数式接口实现也可以作为参数传递，之后再进行调用
    System.out.println("10 + 5 = " + testOperation.operateResult(10, 5, addition));
    System.out.println("10 - 5 = " + testOperation.operateResult(10, 5, subtraction));
    System.out.println("10 x 5 = " + testOperation.operateResult(10, 5, multiplication));
    System.out.println("10 / 5 = " + testOperation.operateResult(10, 5, division));

    // 3、也可以使用匿名实现的方式
    System.out.println("10 + 5 = " + testOperation.operateResult(10, 5, (a, b) -> a + b));
    System.out.println("10 - 5 = " + testOperation.operateResult(10, 5, (a, b) -> a - b));
    System.out.println("10 x 5 = " + testOperation.operateResult(10, 5, (a, b) -> a * b));
    System.out.println("10 / 5 = " + testOperation.operateResult(10, 5, (a, b) -> a / b));
  }

}

@FunctionalInterface
interface MathOperation {

  // 只能含有一个抽象方法
  int operate(int a, int b);

  // 但是可以含有默认的实现方法
  default void other(int num) {
    System.out.println("num is: " + num);
  }
}

// 其实这个是回调方法的实现
class TestMathOperation {
  public int operateResult(int a, int b, MathOperation operation) {
    return operation.operate(a, b);
  }
}
