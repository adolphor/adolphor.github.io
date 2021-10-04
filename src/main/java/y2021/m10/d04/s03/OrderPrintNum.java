package y2021.m10.d04.s03;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/10/4 21:42
 * @Email: 0haizhu0@gmail.com
 */
public class OrderPrintNum {
  private static final LinkedList<Integer> QUEUE = new LinkedList();
  private static ExecutorService POOL_SERVICE = Executors.newFixedThreadPool(3);
  public static void main(String[] args) {
    initData();
    runPrint1();

    initData();
    runPrint2();
  }

  private static void initData() {
    for (int i = 0; i <= 100; i++) {
      QUEUE.add(i);
    }
    if (POOL_SERVICE.isTerminated()) {
      POOL_SERVICE = Executors.newFixedThreadPool(3);
    }
  }

  private static void runPrint1() {
    POOL_SERVICE.execute(new PrintThreadBySynchronized(QUEUE, 2));
    POOL_SERVICE.execute(new PrintThreadBySynchronized(QUEUE, 1));
    POOL_SERVICE.execute(new PrintThreadBySynchronized(QUEUE, 0));

    POOL_SERVICE.shutdown();

    while (true) {
      if (POOL_SERVICE.isTerminated()) {
        System.out.println("finished!!");
        break;
      }
    }
  }

  private static void runPrint2() {
    POOL_SERVICE.execute(new PrintThreadByReentrantLock(0));
    POOL_SERVICE.execute(new PrintThreadByReentrantLock(1));
    POOL_SERVICE.execute(new PrintThreadByReentrantLock(2));

    POOL_SERVICE.shutdown();

    while (true) {
      if (POOL_SERVICE.isTerminated()) {
        System.out.println("finished!!");
        break;
      }
    }
  }
}
