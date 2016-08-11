package Y2016.M08.D08_AbstractCollection.toArray;

/**
 * Test the finishToArray method of toArray
 * Created by Bob on 2016/8/10.
 */
public class ToArrayDemo {
  public static void main(String[] args) {

    MyCollection<String> list = new MyCollection<>(10);
    list.add("A");
    list.add("B");
    new Thread(() -> {
      Object[] array = list.toArray();
      System.out.println();
    }).start();
    new Thread(() -> {
      System.out.println("");
    }).start();
  }
}



