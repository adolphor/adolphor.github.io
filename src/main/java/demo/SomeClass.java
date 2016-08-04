package demo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Doc comment here for <code>SomeClass</code>
 *
 * @param T type parameter
 * @see Math#sin(double)
 */
@Deprecated
public class SomeClass<T extends Runnable> { // some comment
  private T field = null;
  private double unusedField = 12345.67890;
  private String anotherString = "Another\nString";
  public static int staticField = 0;
  public final int instanceFinalField = 0;

  public SomeClass(AnInterface param, int[] reassignedParam) {
    int localVar = 1;
    System.out.println(anotherString + toString() + localVar);
    long time = Date.parse("1.2.3"); // Method is deprecated
    int reassignedValue = this.staticField;
    reassignedValue++;
    field.run();
    new SomeAbstractClass() {
      {
        int a = 2;
      }
    };
  }
}


enum AnEnum {CONST1, CONST2}

interface AnInterface {
  int CONSTANT = 2;

  void method();
}

abstract class SomeAbstractClass {
}