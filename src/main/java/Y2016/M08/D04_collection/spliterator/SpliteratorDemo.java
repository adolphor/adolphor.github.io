package Y2016.M08.D04_collection.spliterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

/**
 * TODO java8 特性
 * Created by Bob on 2016/8/8.
 */
public class SpliteratorDemo {
  public static void main(String[] args) {
    List<String> list = new ArrayList<>();
    list.add("A");
    list.add("B");
    Spliterator<String> splt = list.spliterator();
    System.out.println(splt.characteristics());
  }
}
