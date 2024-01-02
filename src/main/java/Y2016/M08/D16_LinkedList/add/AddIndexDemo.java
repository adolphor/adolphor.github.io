package Y2016.M08.D16_LinkedList.add;

import java.util.LinkedList;

/**
 * 链表定位index的小技巧：使用一次二分法
 * Created by Bob on 2016/8/17.
 */
public class AddIndexDemo {
  public static void main(String[] args) {
    LinkedList<String> list = new LinkedList<>();
    list.add("A");
    list.add("B");
    list.add("C");
    list.add("D");
    list.add(2, "index");
    System.out.println(list);
  }
}
