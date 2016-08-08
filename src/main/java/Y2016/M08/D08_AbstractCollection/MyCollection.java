package Y2016.M08.D08_AbstractCollection;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Bob on 2016/8/8.
 */
public class MyCollection {
  public static void main(String[] args) {
    ArrayList<String> list = new ArrayList<>();
    list.add("A");
    list.add("B");
    list.add("C");
    System.out.println(list.size());
    Iterator<String> iterator = list.iterator();
    while (iterator.hasNext()){
      if (iterator.next().equals("B"))
        iterator.remove();
    }
    System.out.println(list.size());
  }
}
