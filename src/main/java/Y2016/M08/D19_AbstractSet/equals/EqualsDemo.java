package Y2016.M08.D19_AbstractSet.equals;

import Y2016.M08.D19_AbstractSet.MySet;

/**
 * Created by Bob on 2016/8/19.
 */
public class EqualsDemo {
  public static void main(String[] args) {
    MySet<String> mySet1 = new MySet<>();
    mySet1.add("A");
    mySet1.add("B");
    mySet1.add("C");
    MySet<String> mySet2 = new MySet<>();
    mySet2.add("A");
    mySet2.add("B");

    boolean equals = mySet1.equals(mySet2);
    System.out.println(equals);
  }
}
