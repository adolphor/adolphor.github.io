package y2020.m02.d09.scheduledExecutorService;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author adolphor
 */
public class ScheduledExecutorServiceDemo {

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    BasicThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build();
    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, threadFactory);

    ScheduledFuture scheduledFuture = executorService.schedule(() -> {
      System.out.println("Executed!");
      return "Called!";
    }, 5, TimeUnit.SECONDS);

    Object result = scheduledFuture.get();
    System.out.println("result = " + result);

  }
}
