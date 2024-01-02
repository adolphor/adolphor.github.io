package y2021.m10.d04.s03;

import java.util.LinkedList;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/10/4 21:32
 * @Email: 0haizhu0@gmail.com
 */
public class PrintThreadBySynchronized implements Runnable {

  private static final Object LOCK = new Object();

  // 计数，同时确定线程是否要加入等待队列，还是可以直接去资源队列里面去获取数据进行打印
  private static int count = 0;
  private LinkedList<Integer> queue;
  private Integer threadNo;

  public PrintThreadBySynchronized(LinkedList<Integer> queue, Integer threadNo) {
    this.queue = queue;
    this.threadNo = threadNo;
  }

  @Override
  public void run() {
    while (true) {
      synchronized (LOCK) {
        while (count % 3 != this.threadNo) {
          if (count >= 101) {
            break;
          }
          try {
            LOCK.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        if (count >= 101) {
          break;
        }
        Integer val = this.queue.poll();
        System.out.println("thread-" + this.threadNo + ":" + val);
        count++;

        LOCK.notifyAll();
      }
    }
  }
}
