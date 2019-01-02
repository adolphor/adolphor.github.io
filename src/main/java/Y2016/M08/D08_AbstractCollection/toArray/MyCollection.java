package Y2016.M08.D08_AbstractCollection.toArray;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by Bob on 2016/8/8.
 */
public class MyCollection<E> extends AbstractCollection<E> {

  transient Object[] elementData; // non-private to simplify nested class access

  private int size;

  protected transient int modCount = 0;

  private static final Object[] EMPTY_ELEMENTDATA = {};

  private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

  public MyCollection() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
  }

  public MyCollection(int initialCapacity) {
    if (initialCapacity > 0) {
      this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
      this.elementData = EMPTY_ELEMENTDATA;
    } else {
      throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
    }
  }

  // 不考虑扩容的情况
  @Override
  public boolean add(E e) {
    elementData[size++] = e;
    return true;
  }

  @Override
  public Iterator<E> iterator() {
    return new MyCollection.Itr();
  }

  @Override
  public int size() {
    return size;
  }

  private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned; -1 if no such
    int expectedModCount = modCount;

    public boolean hasNext() {
      return cursor != size;
    }

    @SuppressWarnings("unchecked")
    public E next() {
      checkForComodification();
      int i = cursor;
      if (i >= size)
        throw new NoSuchElementException();
      Object[] elementData = MyCollection.this.elementData;
      if (i >= elementData.length)
        throw new ConcurrentModificationException();
      cursor = i + 1;
      return (E) elementData[lastRet = i];
    }

    public void remove() {
      if (lastRet < 0)
        throw new IllegalStateException();
      checkForComodification();

      try {
        MyCollection.this.remove(lastRet);
        cursor = lastRet;
        lastRet = -1;
        expectedModCount = modCount;
      } catch (IndexOutOfBoundsException ex) {
        throw new ConcurrentModificationException();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void forEachRemaining(Consumer<? super E> consumer) {
      Objects.requireNonNull(consumer);
      final int size = MyCollection.this.size;
      int i = cursor;
      if (i >= size) {
        return;
      }
      final Object[] elementData = MyCollection.this.elementData;
      if (i >= elementData.length) {
        throw new ConcurrentModificationException();
      }
      while (i != size && modCount == expectedModCount) {
        consumer.accept((E) elementData[i++]);
      }
      // update once at end of iteration to reduce heap write traffic
      cursor = i;
      lastRet = i - 1;
      checkForComodification();
    }

    final void checkForComodification() {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
    }
  }


}
