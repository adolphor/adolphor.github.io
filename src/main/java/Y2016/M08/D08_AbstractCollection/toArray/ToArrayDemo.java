package Y2016.M08.D08_AbstractCollection.toArray;

/**
 * Test the finishToArray method of toArray
 * Created by Bob on 2016/8/10.
 * 即便finishToArray方法能够将后续变动的元素追加到数组，
 * 但是因为不是线程安装，所以结果并没有保障。如果需要线程
 * 安全，请使用Concurrent Collections。
 */
public class ToArrayDemo {
  public static void main(String[] args) {

    MyCollection<String> list = new MyCollection<>(210);
    list.add("A");
    list.add("B");
    new Thread(() -> {
      for (int i = 0; i < 200; i++) {
        list.add("loop=>" + i);
      }
    }).start();
    new Thread(() -> {
      Object[] array = list.toArray();
      System.out.println(array);
    }).start();
  }
}



