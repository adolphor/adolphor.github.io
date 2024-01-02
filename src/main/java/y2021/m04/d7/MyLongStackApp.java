package y2021.m04.d7;

/**
 * @author adolphor
 */
public class MyLongStackApp {
  public static void main(String[] args) {
    MyLongStack myStack = new MyLongStack(10);
    myStack.push(20);
    myStack.push(40);
    myStack.push(60);
    myStack.push(80);

    while (!myStack.isEmpty()) {
      long pop = myStack.pop();
      System.out.println(pop);
    }
  }
}
