package y2023.m03.d11.s02;

import java.util.concurrent.*;

import static y2023.m03.d11.s02.Utils.*;

public class ExecutorTask {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        var executor = new ExecutorTask();
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
        Credit credit = executor.calculateCreditForPersonWithExecutors(service, 1L);
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) + " => " + credit);
        System.exit(0);
    }

    public Credit calculateCreditForPersonWithExecutors(ExecutorService threadPoll, Long personId)
            throws ExecutionException, InterruptedException {
        var person = getPerson(personId);
        final var assetFuture = threadPoll.submit(() -> getAssets(person));
        final var liabilityFuture = threadPoll.submit(() -> getLiabilities(person));

        threadPoll.submit(() -> doSomethingElseImportant(person));
        return calculateCredit(assetFuture.get(), liabilityFuture.get());
    }
}
