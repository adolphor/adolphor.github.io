package Y2016.m07.d26;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/8/22 19:38
 */
public class FinalFieldExample {
  static FinalFieldExample instance;

  final int x;
  int y;

  public FinalFieldExample() {
    x = 1;
    y = 2;
  }

  public static void writer() {
    instance = new FinalFieldExample();
  }

  public static void reader() {
    final FinalFieldExample theInstance = instance;
    if (theInstance != null) {
      int diff = instance.y - instance.x;
      print(diff);
    }
  }

  public static void print(int val) {
    System.out.println(val);
  }

}
