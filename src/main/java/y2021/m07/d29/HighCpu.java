package y2021.m07.d29;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/13 下午9:28
 * @Email: 0haizhu0@gmail.com
 */
public class HighCpu {

  private static ExecutorService executorService = Executors.newFixedThreadPool(5);
  public static Object lock = new Object();

  public static void main(String[] args) {
    Task task1 = new Task();
    Task task2 = new Task();
    executorService.execute(task1);
    executorService.execute(task2);
  }

  static class Task implements Runnable {
    public void run() {
      synchronized (lock) {
        long sum = 0L;
        while (true) {
          sum += 1;
        }
      }
    }
  }

}
