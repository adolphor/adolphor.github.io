package Y2016.M08.D19_AbstractSet;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 本类主要用来测试 AbstractSet 抽象类中实现的 equals 方法
 * Created by Bob on 2016/8/19.
 */
public class MySet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable {

  static final long serialVersionUID = -5024744406713321676L;

  private transient HashMap<E, Object> map;

  private static final Object PRESENT = new Object();

  public MySet() {
    map = new HashMap<>();
  }

  public MySet(Collection<? extends E> c) {
    map = new HashMap<>(Math.max((int) (c.size() / .75f) + 1, 16));
    addAll(c);
  }

  @Override
  public boolean add(E e) {
    return map.put(e, PRESENT) == null;
  }

  @Override
  public Iterator iterator() {
    return map.keySet().iterator();
  }

  @Override
  public int size() {
    return map.size();
  }

}
