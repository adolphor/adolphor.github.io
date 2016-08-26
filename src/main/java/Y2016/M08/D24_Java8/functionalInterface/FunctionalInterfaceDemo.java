package Y2016.M08.D24_Java8.functionalInterface;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntToLongFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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

    // Java8 函数接口
    // Function 范例
    Function<String, Integer> function = (a) -> a.length();
    System.out.println("Function<String, String> => " + function.apply("Bob"));

    BiFunction<String, String, Integer> biFunction = (a, b) -> (a + b).length();
    System.out.println("BiFunction<String, String, Integer> => " + biFunction.apply("Hello", "Bob"));

    DoubleFunction<String> doubleFunction1 = (a) -> "Double arg is " + a;
    System.out.println("DoubleFunction<String> => " + doubleFunction1.apply(11.11));

    IntToLongFunction intToLongFunction = (a) -> a;
    System.out.println("IntToLongFunction => " + intToLongFunction.applyAsLong(11));

    // Operator 范例（operator就是Function的特例情况）
    BinaryOperator<String> binaryOperator = (a, b) -> a + b;
    System.out.println("BinaryOperator<String> => " + binaryOperator.apply("Hello", "Bob"));

    UnaryOperator<String> unaryOperator = (a) -> "unary msg: " + a;
    System.out.println("UnaryOperator<String> => " + unaryOperator.apply("Hello"));

    IntBinaryOperator intBinaryOperator = (a, b) -> a + b;
    System.out.println("IntBinaryOperator => " + intBinaryOperator.applyAsInt(13, 14));

    // Consumer 范例
    Consumer<String> consumer = (a) -> System.out.println(a);
    System.out.println("Consumer<String> => ");
    consumer.accept("Hello Bob");

    ObjIntConsumer<String> objIntConsumer = (a, b) -> System.out.println(a + b);
    objIntConsumer.accept("Hello Num: ", 11);

    // Supplier 范例：Supplier用于方法引用
    Supplier<String> supplier = () -> "Supplier test";
    System.out.println("Supplier<String> => " + supplier.get());

    DoubleSupplier doubleSupplier = () -> 11.11;
    System.out.println("DoubleSupplier => " + doubleSupplier.getAsDouble());

    // Suppliers may reference constructor methods:
    Supplier<Person> personSupplier1 = Person::new;
    Person person1 = personSupplier1.get();
    System.out.println(person1.name);
    // Supplier referencing a static method
    Supplier<Person> personSupplier2 = PersonFactory::producePerson;
    Person person2 = personSupplier2.get();
    System.out.println(person2.name);
    // Supplier referencing an instance method
    Person person3 = new FunctionalInterfaceDemo().getPerson();
    System.out.println(person3.name);

    // Predicate 范例
    Predicate<Integer> predicate = (a) -> a > 18;
    System.out.println("Predicate<String> => " + predicate.test(10));

    BiPredicate<Integer, String> biPredicate = (a, b) -> a > 18 && b.equals("Male");
    System.out.println("BiPredicate<Integer, String> => " + biPredicate.test(20, "Male"));

  }

  private Person getPerson() {
    // Supplier referencing an instance method
    Supplier<Person> userSupplier = this::producePerson;
    Person person = userSupplier.get();
    return person;
  }

  private Person producePerson() {
    Person person = new Person();
    person.name = "Person by instance method";
    return person;
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

class Person {
  public String name;

  Person() {
    name = "Person by constructor methods";
  }
}

class PersonFactory {
  public static Person producePerson() {
    Person person = new Person();
    person.name = "Person by static method";
    return person;
  }
}
