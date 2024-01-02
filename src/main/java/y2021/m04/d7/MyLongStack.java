package y2021.m04.d7;

/**
 * @author adolphor
 */
public class MyLongStack {

  private int maxSize;
  private long[] stackArray;
  private int top;

  public MyLongStack(int size) {
    maxSize = size;
    stackArray = new long[size];
    top = -1;
  }

  public void push(long j) {
    stackArray[++top] = j;
  }

  public long pop() {
    return stackArray[top--];
  }

  public long peek() {
    return stackArray[top];
  }

  public boolean isEmpty() {
    return top == -1;
  }

  public boolean isFull() {
    return top == maxSize - 1;
  }

}
