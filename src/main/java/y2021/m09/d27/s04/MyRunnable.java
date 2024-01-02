package y2021.m09.d27.s04;

import java.util.Date;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/27 14:08
 * @Email: 0haizhu0@gmail.com
 */
public class MyRunnable implements Runnable {
  private String command;

  public MyRunnable(String s) {
    this.command = s;
  }

  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());
    processCommand();
    System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
  }

  private void processCommand() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return this.command;
  }

}
