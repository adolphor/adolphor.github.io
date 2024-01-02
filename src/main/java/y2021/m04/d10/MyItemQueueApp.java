package y2021.m04.d10;

/**
 * @author adolphor
 */
public class MyItemQueueApp {
  public static void main(String[] args) throws Exception {
    MyItemQueue queue = new MyItemQueue(3);
    queue.insert(10);
    queue.insert(20);
    queue.insert(30);
    while (!queue.isEmpty()) {
      System.out.println(queue.remove());
    }
    queue.insert(40);
    System.out.println(queue.remove());

  }
}
