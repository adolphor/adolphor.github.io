package y2020.m02.d09.scheduledExecutorService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author adolphor
 */
public class ScheduledExecutorServiceDemo {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    ScheduledFuture scheduledFuture = executorService.schedule(() -> {
      System.out.println("Executed!");
      return "Called!";
    }, 5, TimeUnit.SECONDS);
    Object result = scheduledFuture.get();
    System.out.println("result = " + result);
  }
}
