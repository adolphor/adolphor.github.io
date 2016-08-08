package Y2016.M08.D04_Collection.retainAll;

import java.util.ArrayList;

public class RetainAllDemo {
  public static void main(String[] args) {
    ArrayList<String> temp1 = new ArrayList<>();
    temp1.add("A");
    temp1.add("B");
    temp1.add("C");
    ArrayList<String> temp2 = new ArrayList<>();
    temp2.add("A");
    temp2.add("B");

    boolean result = temp1.retainAll(temp2);  // true：删除元素C
    System.out.println(result);
    System.out.println("temp1：" + temp1);
    System.out.println("temp2：" + temp2);

    result = temp1.retainAll(temp2);          // false：没有需要移除的元素
    System.out.println(result);
    System.out.println("temp1：" + temp1);
    System.out.println("temp2：" + temp2);

    temp1.remove("A");
    temp1.add("D");
    result = temp1.retainAll(temp2);          // true：删除元素D，temp2中的A对集合中的元素没有影响
    System.out.println(result);
    System.out.println("temp1：" + temp1);
    System.out.println("temp2：" + temp2);
  }
}
