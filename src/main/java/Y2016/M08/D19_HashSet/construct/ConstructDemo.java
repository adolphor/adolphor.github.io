package Y2016.M08.D19_HashSet.construct;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Bob on 2016/8/19.
 */
public class ConstructDemo {
  public static void main(String[] args) {
    HashSet<String> hashSet1 = new HashSet<>();
    hashSet1.add("A");
    hashSet1.add("B");

    HashSet<String> hashSet2 = new HashSet<>(hashSet1);
    hashSet2.add("C");

    Iterator<String> iterator = hashSet2.iterator();
    while (iterator.hasNext())
      System.out.println(iterator.next());

  }
}
