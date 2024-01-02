package y2021.m08.d22.s01;

import y2021.m08.d22.s03.util.Debug;

/**
 * @Author: Bob.Zhu
 * @Date: 2021/8/22 18:54
 */
public class ClassLayInitDemo {
  public static void main(String[] args) {
    Debug.info(Collaborator.class.hashCode()); // 语句1
    Debug.info(Collaborator.number); // 语句2
    Debug.info(Collaborator.flag);
  }

  static class Collaborator {
    static int number = 1;
    static boolean flag = true;
    static {
      Debug.info("Collaborator initial izing. ..");
    }
  }

}
