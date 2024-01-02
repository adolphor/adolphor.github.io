package Y2016.M09.D20_effectiv_java.tip06;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * Created by Bob on 2017/1/18.
 * 引用地址：
 * http://adolphor.com/blog/2016/09/20/effective-java-second-edition.html#tip06
 */
public class Stack {
  private Object[] elements;
  private int size = 0;
  private static final int DEFAULT_CAPACITY = 16;

  public Stack() {
    elements = new Object[DEFAULT_CAPACITY];
  }

  public void push(Object e) {
    ensureCapacity();
    elements[size++] = e;
  }

  public Object pop() {
    if (size == 0) {
      throw new EmptyStackException();
    }
    Object element = elements[--size];
    // 清空引用链接，以供垃圾回收
    elements[size] = null;
    return element;
  }

  private void ensureCapacity() {
    if (elements.length == size) {
      elements = Arrays.copyOf(elements, 2 * size + 1);
    }
  }

}
