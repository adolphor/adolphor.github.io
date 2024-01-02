package y2021.m10.d04.s03;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/10/4 21:40
 * @Email: 0haizhu0@gmail.com
 */
public class PrintThreadByReentrantLock implements Runnable{
  private static final ReentrantLock lock = new ReentrantLock();
  private static final Condition c = lock.newCondition();

  //作为计数，同时也作为资源；因为这道题目是自然数作为资源，所以正好可以公用；
  private static int count = 0;
  private Integer threadNo;

  public PrintThreadByReentrantLock(Integer threadNo) {
    this.threadNo = threadNo;
  }

  @Override
  public void run() {
    while (true) {
      try {
        lock.lock();
        while (count % 3 != this.threadNo) {
          if (count >= 101) {
            break;
          }
          try {
            c.await();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        if (count >= 101) {
          break;
        }
        System.out.println("thread-" + this.threadNo + ":" + count);
        count++;
        c.signalAll();
      } finally {
        lock.unlock();
      }
    }
  }
}
