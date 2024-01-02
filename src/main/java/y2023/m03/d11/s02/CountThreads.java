package y2023.m03.d11.s02;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class CountThreads {
    public static void main(String[] args) {
        var counter = new AtomicInteger();
        while (true) {
            var thread = new Thread(() -> {
                int threadsCount = counter.incrementAndGet();
                System.out.printf(
                        Locale.CHINA,
                        "started %d\t threads %d%n",
                        threadsCount,
                        Thread.currentThread().getId()
                );
                LockSupport.park();
            });
            thread.start();
        }
    }
}
