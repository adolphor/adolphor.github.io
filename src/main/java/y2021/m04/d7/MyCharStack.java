package y2021.m04.d7;

/**
 * @author adolphor
 */
public class MyCharStack {

  private int maxSize;
  private char[] stackArray;
  private int top;

  public MyCharStack(int size) {
    maxSize = size;
    stackArray = new char[size];
    top = -1;
  }

  public void push(char j) {
    stackArray[++top] = j;
  }

  public char pop() {
    return stackArray[top--];
  }

  public char peek() {
    return stackArray[top];
  }

  public boolean isEmpty() {
    return top == -1;
  }

  public boolean isFull() {
    return top == maxSize - 1;
  }

}
