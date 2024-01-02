package y2021.m07.d29.synchronize;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/27 09:23
 * @Email: 0haizhu0@gmail.com
 */
public class SynchronizedDemo {

  public void f1() {
    synchronized (SynchronizedDemo.class) {
      System.out.println("Hello World.");
    }
  }

  public synchronized void f2() {
    System.out.println("Hello World.");
  }

}
