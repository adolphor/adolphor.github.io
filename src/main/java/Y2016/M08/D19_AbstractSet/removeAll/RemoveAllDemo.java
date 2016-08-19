package Y2016.M08.D19_AbstractSet.removeAll;

import Y2016.M08.D19_AbstractSet.MySet;

/**
 * Created by Bob on 2016/8/19.
 */
public class RemoveAllDemo {
  public static void main(String[] args) {
    MySet<String> mySet1 = new MySet<>();
    mySet1.add("A");
    mySet1.add("B");
    mySet1.add("C");
    mySet1.add("D");
    MySet<String> mySet2 = new MySet<>();
    mySet2.add("A");
    mySet2.add("C");

    boolean b = mySet1.removeAll(mySet2);
    System.out.println(b);

  }
}
