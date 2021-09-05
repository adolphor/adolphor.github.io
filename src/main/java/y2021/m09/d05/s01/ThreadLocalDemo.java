package y2021.m09.d05.s01;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: HongBo.Zhu
 * @Date: 2021/9/5 下午6:38
 */
public class ThreadLocalDemo {

  private static ThreadLocal<StringBuilder> counter = ThreadLocal.withInitial(() -> new StringBuilder());

  public static void main(String[] args) throws InterruptedException {
    int threads = 3;
    CountDownLatch countDownLatch = new CountDownLatch(threads);
    InnerClass innerClass = new InnerClass();

    Runnable runnable = () -> {
      innerClass.add(String.valueOf(RandomUtils.nextInt()));
      innerClass.set("hello world");
      countDownLatch.countDown();
    };

    for (int i = 1; i <= threads; i++) {
      new Thread(runnable, "thread - " + i).start();
    }

    countDownLatch.await();
  }

  private static class InnerClass {

    public void add(String newStr) {
      StringBuilder str = counter.get();
      counter.set(str.append(newStr));

      System.out.printf("Thread name:%s , ThreadLocal hashcode:%s, Instance hashcode:%s, Value:%s\n",
        Thread.currentThread().getName(),
        counter.hashCode(),
        counter.get().hashCode(),
        counter.get().toString());
    }

    public void set(String words) {
      counter.set(new StringBuilder(words));
      System.out.printf("Set, Thread name:%s , ThreadLocal hashcode:%s,  Instance hashcode:%s, Value:%s\n",
        Thread.currentThread().getName(),
        counter.hashCode(),
        counter.get().hashCode(),
        counter.get().toString());
    }
  }

}
