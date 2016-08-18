package Y2016.M08.D16_LinkedList.toArray;

import java.util.LinkedList;

/**
 * Created by Bob on 2016/8/18.
 */
public class ToArrayDemo {
  public static void main(String[] args) {
    LinkedList<String> linkedList1 = new LinkedList<>();
    linkedList1.add("A");
    linkedList1.add("B");
    linkedList1.add("C");
    linkedList1.add("D");

    String[] array1 = new String[6];
    array1[0] = "array1";
    array1[1] = "array2";
    array1[2] = "array3";
    array1[3] = "array4";
    array1[4] = "array5";
    array1[5] = "array6";

    String[] strings = linkedList1.toArray(array1);
    for (String str : strings) {
      System.out.print(str + "、");
    }
  }
}
